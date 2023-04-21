package com.cliftonsnyder.clutch.mr.example.wc;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import com.cliftonsnyder.clutch.mr.task.ReduceTask;
import com.cliftonsnyder.util.Pair;

/**
 * a word count reduce task
 * 
 * @author Clifton L. Snyder
 * @created August 10, 2006
 * 
 */
public class WCReduceTask extends ReduceTask {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8466265225785520415L;

    public WCReduceTask() {
    }

    @SuppressWarnings("unchecked")
    public void reduce() {
        Hashtable<String, Integer> table = new Hashtable<String, Integer>();
        String key = null;
        while (!keyValuePairs.isEmpty()) {
            Pair<Comparable, Object> kvp = keyValuePairs.remove(0);
            key = (String) kvp.getKey();
            Integer value = (Integer) kvp.getValue();
            Integer v2 = table.remove(key);
            if (v2 != null) {
                value = new Integer(value.intValue() + v2.intValue());
            }
            table.put(key, value);
        }
        Enumeration<String> e = table.keys();
        while (e.hasMoreElements()) {
            key = e.nextElement();
            keyValuePairs.add(new Pair<Comparable, Object>(key, table.get(key)));
        }
    }

    @SuppressWarnings("unchecked")
    public List getResult() {
        return this.keyValuePairs;
    }
}
