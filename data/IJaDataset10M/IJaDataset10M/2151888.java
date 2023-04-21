package com.anzsoft.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class JabberXData {

    public static native String genJabberXDataTable(Node x);

    public static String genJabberXDataReply(Element form) {
        String xml = "<x xmlns='jabber:x:data' type='submit'>";
        NodeList<Element> nodes = form.getElementsByTagName("*");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = nodes.getItem(i);
            try {
                String name = element.getAttribute("name");
                if (name == null || name.isEmpty() || name.equals("jwchat_form_type")) continue;
                InputElement ie = (InputElement) element;
                String value = ie.getValue();
                if (value == null) value = "";
                String type = ie.getType();
                if (type == null) type = "";
                xml += "<field var='" + name + "'><value>";
                if (type.equals("checkbox")) {
                    xml += ie.isChecked() ? "1" : "0";
                } else xml += value;
                xml += "</value></field>";
            } catch (Exception ce) {
            }
        }
        xml += "</x>";
        return xml;
    }
}
