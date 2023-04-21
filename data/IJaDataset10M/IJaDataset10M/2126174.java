package com.orientechnologies.jdo.interfaces;

import java.util.ArrayList;

public class oDomain {

    public oDomain(String iName, String iUrl, int iId, int iStartNumbering) {
        name = iName;
        url = iUrl;
        id = iId;
        startNumbering = iStartNumbering;
        classes = new ArrayList();
    }

    public void addInterface(oInterface iIfc) {
        iIfc.setDomain(this);
        classes.add(iIfc);
    }

    public oInterface getInterface(int iClassId) {
        if (iClassId > classes.size()) return null;
        return (oInterface) classes.get(iClassId);
    }

    public oInterface getInterface(String iClassName) {
        oInterface ifc;
        int tot = classes.size();
        for (int i = 0; i < tot; ++i) {
            ifc = (oInterface) classes.get(i);
            if (ifc.name.equals(iClassName)) return ifc;
        }
        return null;
    }

    public int id;

    public int startNumbering;

    public String name;

    public String url;

    public ArrayList classes;
}
