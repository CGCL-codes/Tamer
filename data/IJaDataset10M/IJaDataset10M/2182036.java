package org.imivic.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Class reprezents one action parameter. Action parameter may contains name, value and any number of parameters. <br/>
 * For example: <pre><parameter name="onSuccess">mainPage</parameter></pre><br/>
 * In above examle parameter has name=<code><b>onSuccess></b></code>, value=<code><b>mainPage></b></code> and one attribute: <code><b>name="onSuccess"</b></code>.
 * If action has more than one <code>actionClassName</code> we can set, for witch actionClassName parameter is dedicate.<br/>
 * To do it we must to set attribute with name <code>instanceAccess</code> and value - semicolon separated number of actionClassName.<br/>
 * For example <code>actionClassName=1;3</code> means, that to parameter has access actionClass 1 and 3 on actionClassName.<br/>
 * It is possible to set few parameters with the same name, other values and other numbers of actionCass on instanceAccess list.<br/>
 * For example: <pre><parameter name="type" instanceAccess="1;3">String</parameter>
 * <parameter name="type" instanceAccess="2">Integer</parameter></pre>
 * 
 * @author zbigniewk
 */
public class ActionParameter {

    /**
        * table with numbers of accessClasses
        */
    int[] instanceAccess;

    /**
         * parameter name
         */
    String name;

    /**
         * parameter value
         */
    String value;

    /**
         * Map contains parameter attributes
         */
    Map<String, String> otherAttributes = new HashMap<String, String>();

    /**
         * Default Constructor
         */
    public ActionParameter() {
    }

    /**
         * Constructor construct new Parameter
         * @param name parameter name
         * @param value parameter value
         * @param classOrder list of numbers to access actionClass
         */
    public ActionParameter(String name, String value, String classOrder) {
        this.name = name;
        this.value = value;
        setInstanceAccess(classOrder);
    }

    /**
         * Method check, if parameter has access to actionClass with number <code>i</code>
         * @param i number of actionClass
         * @return true if parameter has access to actionClass with number <code>i</code>
         */
    public boolean isInInstanceAccess(int i) {
        if (instanceAccess == null) return true;
        for (int k : instanceAccess) if (k == i) return true;
        return false;
    }

    public void setInstanceAccess(String o) {
        if (o == null) return;
        String[] tab = o.split(",");
        instanceAccess = new int[tab.length];
        int i = 0;
        for (String t : tab) try {
            instanceAccess[i++] = Integer.parseInt(t);
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getOtherAttributes() {
        return otherAttributes;
    }

    public void setOtherAttributes(Map<String, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addOtherAttribute(String k, String v) {
        otherAttributes.put(k, v);
    }
}
