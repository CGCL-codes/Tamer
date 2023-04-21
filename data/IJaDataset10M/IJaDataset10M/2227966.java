package org.jjoblist.core.test;

import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

public class T1 {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Throwable {
        JSONObject obj = new JSONObject();
        obj.put("qwe", "123");
        obj.put("q", "1");
        LinkedList<JSONObject> o = new LinkedList<JSONObject>();
        o.add(obj);
        JSONArray array = new JSONArray(o);
        System.out.println(array);
    }
}
