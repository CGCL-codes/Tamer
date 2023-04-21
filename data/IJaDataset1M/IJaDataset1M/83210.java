package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class OrderDuration extends FIXMLChoice {

    private static final Class[] s_types = { TimeInForce.class, GTD_TimeInForce.class };

    private FIXMLObject _Choice;

    public void addChoiceType(Class c) {
    }

    public Class[] getChoiceTypes() {
        return s_types;
    }

    public void choose(Object obj) throws Exception {
        if (obj == null) return;
        boolean match = false;
        Class c = obj.getClass();
        for (int i = 0; i < s_types.length; i++) {
            if (s_types[i].equals(c)) {
                match = true;
                break;
            }
        }
        if (!match) {
            throw new Exception("Type " + c.getName() + " is not allowed.");
        } else {
            _Choice = (FIXMLObject) obj;
        }
    }

    public Object getChoice() {
        return _Choice;
    }

    public void initChoice(Object obj) throws Exception {
        if (_Choice != null) {
            throw new Exception("Value of OrderDuration has been initialized.");
        }
        choose(obj);
    }

    public void initTimeInForce(Object obj) throws Exception {
        initChoice(obj);
    }

    public void initGTD_TimeInForce(Object obj) throws Exception {
        initChoice(obj);
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_Choice != null) sb.append(_Choice.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<OrderDuration>\n");
        if (_Choice != null) sb.append(_Choice.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</OrderDuration>");
        return sb.toString();
    }
}
