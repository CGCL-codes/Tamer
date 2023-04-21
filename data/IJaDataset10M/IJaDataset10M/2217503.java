package com.neurogrid.gui.http.om;

import org.apache.velocity.context.Context;

/**
 * generic data for an html form
 * 
 * @author <a href="mailto:sam@neurogrid.com">Sam Joseph</a>
 */
public class AddTripleForm {

    public static final String OBJECT = "OBJECT";

    public static final String ACTION = "ACTION";

    public static final String ADD_TRIPLE = "ADD_TRIPLE";

    public static final String SUBMIT = "SUBMIT";

    public static final String PREDICATE = "PREDICATE";

    public static final String SUBJECT = "SUBJECT";

    /**
   * add all the form inputs to the context
   *
   * @param p_context     a context to add relevant data to
   */
    public static void populateContext(Context p_context) throws Exception {
        p_context.put("OBJECT", OBJECT);
        p_context.put("ACTION", ACTION);
        p_context.put("ADD_TRIPLE", ADD_TRIPLE);
        p_context.put("SUBMIT", SUBMIT);
        p_context.put("PREDICATE", PREDICATE);
        p_context.put("SUBJECT", SUBJECT);
    }
}
