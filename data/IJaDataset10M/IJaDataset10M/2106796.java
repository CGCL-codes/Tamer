package org.bodega;

import java.util.Iterator;
import java.util.logging.*;
import org.json.*;

/** Test to demonstrate random order of JSON Object members.
*
* @author Paul Copeland
*/
public class TestJsonArray {

    public static Logger logger = Logger.getLogger(TestDriver.class.getPackage().getName());

    private static java.io.PrintStream out = System.err;

    public static void main(String args[]) throws Throwable {
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        String testArgs[] = new String[] { "A", "D", "B", "C" };
        for (int i = 0; i < testArgs.length; i++) {
            ja.put(new JSONObject().put(testArgs[i], new Integer(i)));
            jo.put(testArgs[i], new Integer(i));
        }
        out.println();
        out.print("JSONArray members: ");
        for (int i = 0; i < testArgs.length; i++) out.print("-" + ja.get(i));
        out.println();
        out.print("JSONObject iterator: ");
        for (Iterator it = jo.keys(); it.hasNext(); ) out.print("-" + jo.get((String) it.next()));
        out.println();
        out.println("JSONObject toString: " + jo);
    }
}
