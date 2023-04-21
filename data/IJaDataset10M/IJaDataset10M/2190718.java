package com.doxological.doxquery;

import com.doxological.doxquery.context.*;
import com.doxological.doxquery.values.*;

/**
 * <p>The main method for the command line interface.</p>
 *
 * @author John Snelson
 */
public class Main {

    public static void main(String args[]) {
        try {
            long start = System.nanoTime();
            XQueryEnvironment env = new XQueryEnvironment();
            XQueryExpression expr = env.parse("stdin", System.in);
            Sequence result = expr.execute();
            System.out.println(result.toQuery());
            System.out.println();
            Item item;
            while ((item = result.next()) != null) {
                System.out.println(item.toString());
            }
            System.out.println();
            long end = System.nanoTime();
            System.out.println("Time taken: " + ((double) (end - start) / (1000000.0)) + " ms");
        } catch (XQueryException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
