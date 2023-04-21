package faultcache;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The Fault-Cache is part of the FaultManager. It simply stores the list of activated
 * faults inside memory for faster access.
 *
 * @author Peter Schwenkenberg
 *
 */
public class Fault {

    /**
     * the "fault cache" to avoid db-calls
     */
    private static Vector<Integer> faultCache = new Vector<Integer>();

    public Fault() {
        faultCache.add(0);
    }

    /**
     * Is a fault activated, i.e. stored in the "fault cache"? 
     */
    public static boolean isActivated(int fault) {
        if (faultCache.contains(fault)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the fault cache with a String of faults
     */
    public static void setFaultCache(String faultList) {
        faultCache = parseInts(faultList);
        System.out.println("Setting FaultCache to: " + faultCache);
    }

    /**
     * Translates a String of an integer-list ("1,2,3") to a vector of integers
     * ([1,2,3]). 
     */
    public static Vector<Integer> parseInts(String str) {
        Vector<Integer> result = new Vector<Integer>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',' || i == 0) {
                int j = i + 1;
                if (i == 0) {
                    j = 0;
                }
                String theInt = "";
                while (true) {
                    if (j >= str.length() || str.charAt(j) == ',') {
                        break;
                    }
                    theInt += str.charAt(j);
                    j++;
                }
                try {
                    result.add(Integer.parseInt(theInt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
