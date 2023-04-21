package net.xmlmiddleware.aaf.plugin;

import net.xmlmiddleware.props.*;
import java.util.Vector;

public class Test implements net.xmlmiddleware.aaf.AafPlugin {

    /**
 * Test constructor comment.
 */
    public Test() {
        super();
    }

    /**
 * Insert the method's description here.
 * Creation date: (23/07/2002 16:36:39)
 * @return java.lang.String
 * @param param java.util.Properties
 */
    public String init(java.util.Properties props) {
        String OK = "OK";
        System.out.println("Test.init called, props = " + props);
        return OK;
    }

    /**
 * Insert the method's description here.
 * Creation date: (23/07/2002 16:36:39)
 * @return java.util.Vector
 */
    public String[] req_props() {
        String[] Arg = new String[2];
        Arg[0] = "test1";
        Arg[1] = "test2";
        return Arg;
    }

    public java.lang.String[] opt_props() {
        String[] Arg = new String[0];
        return Arg;
    }

    /**
 * Insert the method's description here.
 * Creation date: (23/07/2002 16:36:39)
 * @return java.lang.String
 * @param param java.util.Hashtable
 */
    public java.util.Hashtable run(java.util.Hashtable var) {
        String Data = (String) var.get(AAFProps.DATA);
        System.out.println("TEST PRINTING OUT DATA " + Data);
        return var;
    }
}
