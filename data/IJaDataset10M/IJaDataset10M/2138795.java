package org.openemed.workflow;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.io.Serializable;

/**
 *  Simple workflow component to be used for activities in workflow
 *
 *@author     dwf
 *@created    May 6, 2004
 */
public class MyWorklet implements Worklet, Serializable, Cloneable {

    private ResourceBundle props;

    private String property = "worklet";

    public MyWorklet() {
        init();
    }

    /**
	 *  initialize the worklet
	 *
	 *@param  prop location of properties
	 */
    public void init(String prop) {
        try {
            props = ResourceBundle.getBundle(prop);
        } catch (java.util.MissingResourceException e) {
            System.err.println("no resource: " + prop);
        }
        if (props != null) System.out.println("input: " + props.getString("input"));
    }

    /**
     * initialize class
     */
    public void init() {
        try {
            props = ResourceBundle.getBundle(property);
        } catch (java.util.MissingResourceException e) {
            System.err.println("no resource: " + property);
        }
    }

    /**
     * check to see if worklet has been initialized
     * @return
     */
    public boolean initialized() {
        if (props != null) return true; else return false;
    }

    /**
	 *  execute the class
	 *
	 *@param  inout   Description of the Parameter
	 */
    public void execute(Properties inout) {
        System.out.println("test: " + inout.get("test"));
        inout.put("result", "actual result");
    }

    /**
	 *  The main program for the Worklet class
	 *
	 *@param  args  The command line arguments
	 */
    public static void main(String[] args) {
        Worklet worklet = new MyWorklet();
        worklet.init("/test");
        Properties p = new Properties();
        p.put("test", "input");
        worklet.execute(p);
        System.out.println("result: " + p.getProperty("result"));
    }
}
