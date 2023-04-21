package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class CombinedIssueStringProcessor {

    private static CombinedIssueStringProcessor thisInstance;

    /** Creates a new instance of CombinedIssueStringProcessor */
    public CombinedIssueStringProcessor() {
    }

    public static CombinedIssueStringProcessor getInstance() {
        if (thisInstance == null) thisInstance = new CombinedIssueStringProcessor();
        return thisInstance;
    }

    public String getTheGreaterInteger(String str) {
        if (str.indexOf('/') != -1) {
            StringTokenizer stkz = new StringTokenizer(str, "/");
            int[] intarray = new int[stkz.countTokens()];
            int i = 0;
            while (stkz.hasMoreTokens()) {
                intarray[i] = Integer.parseInt(stkz.nextToken());
                i++;
            }
            Arrays.sort(intarray);
            return String.valueOf(String.valueOf(intarray[intarray.length - 1]));
        } else {
            return str;
        }
    }

    public ArrayList getListOfCombined(String str) {
        ArrayList ar = new ArrayList();
        StringTokenizer stkz = new StringTokenizer(str, "/");
        while (stkz.hasMoreTokens()) {
            ar.add(stkz.nextToken());
        }
        return ar;
    }

    public String getTheLesserInteger(String str) {
        if (str.indexOf('/') != -1) {
            StringTokenizer stkz = new StringTokenizer(str, "/");
            int[] intarray = new int[stkz.countTokens()];
            int i = 0;
            while (stkz.hasMoreTokens()) {
                intarray[i] = Integer.parseInt(stkz.nextToken());
                i++;
            }
            Arrays.sort(intarray);
            return String.valueOf(String.valueOf(intarray[0]));
        } else {
            return str;
        }
    }

    public String getFirstNumber(String str) {
        if (str.indexOf('/') != -1) {
            StringTokenizer stkz = new StringTokenizer(str, "/");
            String retstr = stkz.nextToken();
            return retstr;
        } else {
            return str;
        }
    }

    public String getLastNumber(String str) {
        if (str.indexOf('/') != -1) {
            StringTokenizer stkz = new StringTokenizer(str, "/");
            String retstr = "";
            while (stkz.hasMoreTokens()) {
                retstr = stkz.nextToken();
            }
            return retstr;
        } else {
            return str;
        }
    }
}
