package com.qlusters.jlmenu;

import java.util.HashMap;
import java.util.TreeSet;

public class MenuPage {

    private TreeSet headerPrintSet = new TreeSet();

    private TreeSet footerPrintSet = new TreeSet();

    private HashMap hashMap = new HashMap();

    public MenuPage() {
    }

    protected boolean getHeaderPrint(java.lang.Class headerClass) {
        return headerPrintSet.contains(headerClass.getName());
    }

    protected void setHeaderPrint(Class headerClass) {
        headerPrintSet.add(headerClass.getName());
    }

    protected boolean getFooterPrint(java.lang.Class footerClass) {
        return footerPrintSet.contains(footerClass.getName());
    }

    protected void setFooterPrint(Class footerClass) {
        footerPrintSet.add(footerClass.getName());
    }

    protected void setAttribute(String key, Object value) {
        hashMap.put(key, value);
    }

    protected Object getAttribute(String key) {
        return hashMap.get(key);
    }
}
