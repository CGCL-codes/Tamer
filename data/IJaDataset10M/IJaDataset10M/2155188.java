package org.zkoss.gwt.client;

import org.zkoss.gwt.client.zk.Widget;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A utility class for ZK GWT
 * 
 * @author Ian Tsai(Zanyking)
 */
public class Utils {

    /**
	 * 
	 * @param obj
	 */
    public static void alert(Object obj) {
        alert("" + obj);
    }

    /**
	 * 
	 * @param str
	 */
    public static native void alert(String str);

    /**
	 * 
	 * @param mesg
	 */
    public static native void log(String mesg);

    /**
	 * 
	 * @param jsObj
	 */
    public static native void log(JavaScriptObject jsObj);

    /**
	 * this method is same as this Javascript code:
	 * jsObj[attr] = value;
	 * 
	 * @param jsObj Javascript Object instance   
	 * @param attr a dynamic value that you want to assigned.
	 * @param value value of this attribute, could be anything(from javascript).
	 */
    public static native void setAttribute(JavaScriptObject jsObj, String attr, Object value);

    /**
     * 
     * @param zkWidget
     * @return
     */
    public static native Widget getGwtWidget(JavaScriptObject zkWidget);
}
