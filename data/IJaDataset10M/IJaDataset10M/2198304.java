package com.anothergtdapp.db;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author adolfo
 */
public class Context extends DBReference {

    private static List<Context> contexts = null;

    private static HashMap<Integer, Context> contextMap = null;

    public static List<Context> getDefaultContexts() {
        if (contexts == null) {
            contexts = DBController.getDefaultController().getContexts();
            contextMap = new HashMap<Integer, Context>();
            for (Context c : contexts) {
                contextMap.put(c.getId(), c);
            }
        }
        return contexts;
    }

    public static void saveContexts() {
        if (contexts == null) return;
        DBController.getDefaultController().setContexts(contexts);
        contexts = null;
        getDefaultContexts();
    }

    public static Context getContextById(int id) {
        getDefaultContexts();
        return contextMap.get(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
