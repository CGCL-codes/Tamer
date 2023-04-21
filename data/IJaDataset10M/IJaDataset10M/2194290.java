package com.webztek.utils;

import java.util.StringTokenizer;

/**
 * 
 * @author Rick
 * @date 05-10-2008
 * @descr 
 *
 */
public class Utils {

    /**
	 * 
	 * @param pStringToClean
	 * @param pType
	 * @return
	 */
    public static String constructCleanQuery(String pStringToClean, String pType) {
        String wCleanQuery = "";
        if (pStringToClean.indexOf(".") > 0) {
            StringTokenizer wQueryTk = new StringTokenizer(pStringToClean, ".");
            while (wQueryTk.hasMoreTokens()) {
                wCleanQuery += capitalizeFirstLetter(wQueryTk.nextToken());
            }
        } else {
            wCleanQuery = capitalizeFirstLetter(pStringToClean);
        }
        if (pType.equals("controller")) {
            wCleanQuery += "Controller";
        } else if (pType.equals("action")) {
            wCleanQuery = wCleanQuery.substring(0, 1).toLowerCase() + wCleanQuery.substring(1);
        }
        System.out.println("################# " + wCleanQuery + " #############");
        return wCleanQuery;
    }

    /**
	 * 
	 * @param pStringToCapitalize
	 * @return The same string but with the first letter capitalized
	 */
    public static String capitalizeFirstLetter(String pStringToCapitalize) {
        return pStringToCapitalize.substring(0, 1).toUpperCase() + pStringToCapitalize.substring(1);
    }
}
