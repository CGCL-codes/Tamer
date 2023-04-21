package com.k_int.z3950.server.demo;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Observer;
import java.util.Collection;
import java.util.Iterator;
import com.k_int.IR.*;
import org.apache.log4j.Category;
import com.k_int.util.RPNQueryRep.RootNode;

/**
 * A sample implementation of searchable that returns random numbers of hits
 * and random result records
 */
public class DemoSearchable implements com.k_int.IR.Searchable {

    private Hashtable properties = null;

    private static Category cat = Category.getInstance("com.k-int.z3950.server.demo");

    public DemoSearchable() {
        cat.debug("New DemoSearchable");
    }

    public void init(Properties p) {
        this.properties = p;
    }

    public void destroy() {
    }

    public int getManagerType() {
        return com.k_int.IR.Searchable.SPECIFIC_SOURCE;
    }

    public SearchTask createTask(IRQuery q, Object user_data) {
        return this.createTask(q, user_data, null);
    }

    public SearchTask createTask(IRQuery q, Object user_data, Observer[] observers) {
        DemoSearchTask retval = new DemoSearchTask(this, null);
        return retval;
    }
}
