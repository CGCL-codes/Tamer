package org.verus.newgenlib.uc.util;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author siddartha
 */
public class Utility {

    private static Utility thisInstance;

    private Utility() {
    }

    public static Utility getInstance() {
        if (thisInstance == null) thisInstance = new Utility();
        return thisInstance;
    }

    public String getTestedString(Object obj) {
        if (obj == null) return ""; else return obj.toString();
    }

    public String buildSolrQuery(String xmlForm, String formName, Hashtable enteredparam, ArrayList andparams) {
        String solrQuery = "";
        return solrQuery;
    }
}
