package com.entelience.esis;

import com.entelience.sql.Db;
import com.entelience.util.Arrays;
import com.entelience.util.HttpQuick;

/**
 * ESIS HttpTest utility.
 * 
 * Allows an administrative user to test the end-to-end
 * http connection through the libraries that we use.
 */
public class HttpTest {

    private HttpTest() {
    }

    /**
     * Entry point.
     */
    public static void main(Db db, String argsIn[]) throws Exception {
        if (argsIn.length == 0) {
            usage();
            return;
        }
        String command = Arrays.head(argsIn);
        String args[] = Arrays.shift(argsIn);
        if ("get".equals(command) && args.length > 0) {
            try {
                for (int i = 0; i < args.length; ++i) {
                    String url = args[i];
                    System.out.println("### BEGIN " + url);
                    System.out.println(HttpQuick.downloadUrl(db, url));
                    System.out.println("### END " + url);
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace(System.out);
                throw e;
            }
        } else {
            usage();
            return;
        }
    }

    /**
     * Show usage information.
     */
    public static void usage() {
        System.out.println("Usage");
        System.out.println("");
        System.out.println("http get url - test http connection.");
        System.out.println("");
    }
}
