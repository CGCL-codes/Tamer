package org.procol.framework.logging.util;

import java.sql.Timestamp;

public class TempTester {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String separator = "<|>";
        String testee = "<unit>" + "Operations per second" + "</unit>";
        String[] result = testee.split(separator);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("<timestamp>" + timestamp.toString() + "</timestamp>");
        String separator2 = "\\[|\\]|,";
        String testee2 = "[-1.0,1.0]";
        String[] result2 = testee2.split(separator2);
        for (int i = 0; i < result2.length; i++) {
            System.out.println(result2[i]);
        }
    }
}
