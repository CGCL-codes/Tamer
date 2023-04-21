package org.in4ama.editor.project.cfg;

import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocumentElement extends BaseItemElement {

    Hashtable conditions, types;

    public DocumentElement() {
        itemRootName = "Fragments";
        itemTagName = "fragment";
        conditions = new Hashtable();
        types = new Hashtable();
    }

    public void setElement(Element ele) {
        super.setElement(ele);
        NodeList list = ele.getElementsByTagName(itemTagName);
        for (int i = 0; i < list.getLength(); i++) {
            Element eleItem = (Element) list.item(i);
            String name = eleItem.getAttribute("name");
            String condition = eleItem.getAttribute("condition");
            if (condition != null) conditions.put(name, condition);
            String type = eleItem.getAttribute("type");
            if (type != null) types.put(name, type);
        }
    }

    public String[] getItems() {
        String[] itemNames = super.getItems();
        for (int i = 0; i < itemNames.length; i++) {
            Element ele = getItemElement(itemNames[i]);
            String condition = ele.getAttribute("condition");
            if (condition != null && condition.length() > 0) {
                conditions.put(itemNames[i], condition);
            }
        }
        return itemNames;
    }

    public void setBackground(String value) {
        rootEle.setAttribute("background", String.valueOf(value));
    }

    public String getBackground() {
        return rootEle.getAttribute("background");
    }

    public void setBackgroundType(String value) {
        rootEle.setAttribute("backgroundtype", String.valueOf(value));
    }

    public String getBackgroundType() {
        return rootEle.getAttribute("backgroundtype");
    }

    public void setEmailfile(String value) {
        rootEle.setAttribute("emailfile", String.valueOf(value));
    }

    public String getEmailSubject() {
        return rootEle.getAttribute("emailsubject");
    }

    public void setEmailSubject(String value) {
        rootEle.setAttribute("emailsubject", String.valueOf(value));
    }

    public String getEmailfile() {
        return rootEle.getAttribute("emailfile");
    }

    public void setUseFragments(boolean value) {
        rootEle.setAttribute("usefragments", String.valueOf(value));
    }

    public boolean getUseFragments() {
        String value = rootEle.getAttribute("usefragments");
        return value == null ? false : value.equals("true");
    }

    public void setFragmentCondition(String fragmentName, String condition) {
        conditions.put(fragmentName, condition);
    }

    public void setFragmentType(String fragmentName, String type) {
        types.put(fragmentName, type);
    }

    public String getFragmentCondition(String fragmentName) {
        return (String) conditions.get(fragmentName);
    }

    public Hashtable getFragmentsConditions() {
        return conditions;
    }

    public String getFragmentType(String fragmentName) {
        return (String) types.get(fragmentName);
    }

    public void saveConditions() {
        Enumeration keys = conditions.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String cond = (String) conditions.get(key);
            if (itemsEle != null) {
                Element itemEle = getItemElement(key);
                itemEle.setAttribute("condition", cond);
            }
        }
    }

    public void saveTypes() {
        Enumeration keys = types.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String type = (String) types.get(key);
            if (itemsEle != null) {
                Element itemEle = getItemElement(key);
                itemEle.setAttribute("type", type);
            }
        }
    }
}
