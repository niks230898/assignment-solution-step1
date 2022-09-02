package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
 *
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 *
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 *
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 *
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 *
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DataMunger {

    /*
     * This method will split the query string based on space into an array of words
     * and display it on console
     */
//string tokenizer also can be used
    public String[] getSplitStrings(String queryString) {
        String[] splittedArray = queryString.split(" ");
        for (int i = 0; i < splittedArray.length; i++) {
            splittedArray[i].toLowerCase();
        }
        return splittedArray;

    }

    /*
     * Extract the name of the file from the query. File name can be found after a
     * space after "from" clause. Note: ----- CSV file can contain a field that
     * contains from as a part of the column name. For eg: from_date,from_hrs etc.
     *
     * Please consider this while extracting the file name in this method.
     */

    public String getFileName(String queryString) {
        String[] splitted = getSplitStrings(queryString);
        for (int i = 0; i < splitted.length; i++) {
            if (splitted[i].equalsIgnoreCase("from")) {
                return splitted[i + 1];
            }
        }

        return null;
    }

    /*
     * This method is used to extract the baseQuery from the query string. BaseQuery
     * contains from the beginning of the query till the where clause
     *
     * Note: ------- 1. The query might not contain where clause but contain order
     * by or group by clause 2. The query might not contain where, order by or group
     * by clause 3. The query might not contain where, but can contain both group by
     * and order by clause
     */

    public String getBaseQuery(String queryString) {
        String[] splitted = getSplitStrings(queryString);
        String resString = "";
        int i = 0;
        while (i < splitted.length) {
            if (splitted[i].equalsIgnoreCase("where") || splitted[i].equalsIgnoreCase("order") || splitted[i].equalsIgnoreCase("group")) {
                return resString;
            }
            if (i == 0) {
                resString = resString + splitted[i];
                i++;
                continue;
            }
            resString = resString + " " + splitted[i];
            i++;
        }

        return resString;

    }

    /*
     * This method will extract the fields to be selected from the query string. The
     * query string can have multiple fields separated by comma. The extracted
     * fields will be stored in a String array which is to be printed in console as
     * well as to be returned by the method
     *
     * Note: 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
     * name can contain '*'
     *
     */

    public String[] getFields(String queryString) {
        String[] stringsArray = queryString.split(" ");
        String resString = "";
        for (int i = 1; i < stringsArray.length; i++) {
            if (stringsArray[i].equalsIgnoreCase("from")) {
                break;
            }
            resString=resString+stringsArray[i]+" ";
        }
		resString = resString.trim();
        return resString.split(",");

//		StringTokenizer stokenizer = new StringTokenizer(queryString," ");
//		List<String> stringArray = new ArrayList<>();
//		while (!stokenizer.nextToken().equalsIgnoreCase("from")){
//			stringArray.add(stokenizer.nextToken());
//
//
//		}
//		return (String[]) stringArray.toArray();
    }

    /*
     * This method is used to extract the conditions part from the query string. The
     * conditions part contains starting from where keyword till the next keyword,
     * which is either group by or order by clause. In case of absence of both group
     * by and order by clause, it will contain till the end of the query string.
     * Note:  1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    public String getConditionsPartQuery(String queryString) {
        String[] splitted = getSplitStrings(queryString);
        String resstring = "";
        boolean flag = true;
        for (int i = 0; i < splitted.length; i++) {
            if (flag) {
                if (splitted[i].equalsIgnoreCase("where")) {
                    flag = false;
                }
                continue;
            }
            if(splitted[i].equalsIgnoreCase("order") || splitted[i].equalsIgnoreCase("group")) {
            	break;
            }
            resstring = resstring + splitted[i] + " ";
        }
        resstring = resstring.trim();


        return resstring.length()==0?null:resstring;
    }

    /*
     * This method will extract condition(s) from the query string. The query can
     * contain one or multiple conditions. In case of multiple conditions, the
     * conditions will be separated by AND/OR keywords. for eg: Input: select
     * city,winner,player_match from ipl.csv where season > 2014 and city
     * ='Bangalore'
     *
     * This method will return a string array ["season > 2014","city ='bangalore'"]
     * and print the array
     *
     * Note: ----- 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    public String[] getConditions(String queryString) {
        String[] splittedString = queryString.split(" ");
        List<String> stringList = new ArrayList<>();
        boolean flag=true;
        int i = 0;
        String sample ="";
        while(i < splittedString.length){
            if(splittedString[i].equalsIgnoreCase("order") || splittedString[i].equalsIgnoreCase("group")){
            	
                break;
            }
            if(flag) {
                if (splittedString[i].equalsIgnoreCase("where")) {
                    flag = false;
                }
                i++;
                continue;
            }
            if(!splittedString[i].equalsIgnoreCase("AND") && !splittedString[i].equalsIgnoreCase("OR")){
                sample = sample+splittedString[i]+" ";
            }else {
            	sample = sample.trim();
                stringList.add(sample);
                sample="";
            }
            i++;
        }
        if(!sample.equalsIgnoreCase("")) {
        	sample = sample.trim();
    		stringList.add(sample);
    	}
        
        //tried toArray but not working in eclipse
        String sArray[] = new String[stringList.size()];
        for(int k = 0;k < stringList.size();k++) {
        	sArray[k] = stringList.get(k);
        }
        return sArray.length==0?null:sArray;

    }

    /*
     * This method will extract logical operators(AND/OR) from the query string. The
     * extracted logical operators will be stored in a String array which will be
     * returned by the method and the same will be printed Note:  1. AND/OR
     * keyword will exist in the query only if where conditions exists and it
     * contains multiple conditions. 2. AND/OR can exist as a substring in the
     * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
     * these as well when extracting the logical operators.
     *
     */

    public String[] getLogicalOperators(String queryString) {
        String[] splitted = getSplitStrings(queryString);
        int i = 0;
        List<String> list = new ArrayList<>();
        while (i < splitted.length) {
            if (splitted[i].equalsIgnoreCase("AND") || splitted[i].equalsIgnoreCase("or")) {
                list.add(splitted[i]);
            }
            i++;
        }
        String result[] = new String[list.size()];
        for (int k = 0;k < list.size();k++){
            result[k] = list.get(k);
        }
        return result.length==0?null:result;
    }

    /*
     * This method extracts the order by fields from the query string. Note:
     * 1. The query string can contain more than one order by fields. 2. The query
     * string might not contain order by clause at all. 3. The field names,condition
     * values might contain "order" as a substring. For eg:order_number,job_order
     * Consider this while extracting the order by fields
     */

    public String[] getOrderByFields(String queryString) {
        String[] strina = getSplitStrings(queryString);
        int i = 0;
        boolean flag = true;
        while (i < strina.length) {
        	if(flag) {
        		if(strina[i].equalsIgnoreCase("order")) {
        			i=i+2;
        			flag=false;
        			break;
        		}
        		i++;
        		continue;
        	}

        	
        	
        	        	
        	
        }
        if(flag) {
        	return null;
        }
       String[] result = strina[i].split(",");

        return result.length==0?null:result;
    }

    /*
     * This method extracts the group by fields from the query string. Note:
     * 1. The query string can contain more than one group by fields. 2. The query
     * string might not contain group by clause at all. 3. The field names,condition
     * values might contain "group" as a substring. For eg: newsgroup_name
     *
     * Consider this while extracting the group by fields
     */

    public String[] getGroupByFields(String queryString) {
    	String[] strina = getSplitStrings(queryString);
        int i = 0;
        boolean flag = true;
        while (i < strina.length) {
        	if(flag) {
        		if(strina[i].equalsIgnoreCase("group")) {
        			i=i+2;
        			
        			flag=false;
        			break;
        		}
        		i++;
        		continue;
        	}

        	
        	
        	        	
        	
        }
        if(flag) {
        	return null;
        }
       String[] result = strina[i].split(",");

        return result.length==0?null:result;

    }

    /*
     * This method extracts the aggregate functions from the query string. Note:
     *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
     * followed by "(" 2. The field names might
     * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
     * account_number,consumed_qty,nominee_name
     *
     * Consider this while extracting the aggregate functions
     */

    public String[] getAggregateFunctions(String queryString) {
    	String baseQuery = getBaseQuery(queryString);
    	String[] splittedbase = baseQuery.split(" ");
    	String[] actualQuery = splittedbase[1].split(",");
    	List<String> list = new ArrayList<String>();
    	int i = 0;
    	while(i < actualQuery.length) {
    		if(actualQuery[i].contains("sum") || actualQuery[i].contains("count") || actualQuery[i].contains("min") || actualQuery[i].contains("max") || actualQuery[i].contains("avg")) {
    			list.add(actualQuery[i]);
    		}
    		i++;
    	}
    	String result[] = new String[list.size()];
        for (int k = 0;k < list.size();k++){
            result[k] = list.get(k);
        }
        return result.length==0?null:result;
    }

}
