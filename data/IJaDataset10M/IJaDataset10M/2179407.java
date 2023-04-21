package com.sts.webmeet.content.common;

import com.sts.webmeet.common.PackageUtil;
import java.util.Hashtable;

public class ScriptItemImpl implements java.io.Serializable {

    static final long serialVersionUID = -5767100073033809999L;

    private static final String ID = "id";

    private static final String CLASS = "class";

    private static final String NAME = "name";

    private static final String ITEM = "item";

    public Integer getItemID() {
        return (Integer) hash.get(ID);
    }

    public void setItemID(Integer id) {
        hash.put(ID, id);
    }

    public String getIconPath() {
        return "images/" + PackageUtil.getShortPackage(getClassName()) + ".gif";
    }

    public String getShortPackageName() {
        return PackageUtil.getShortPackage(getClassName());
    }

    public String getClassName() {
        return (String) hash.get(CLASS);
    }

    public void setClassName(String strName) {
        hash.put(CLASS, strName);
    }

    public String getName() {
        return (String) hash.get(NAME);
    }

    public void setName(String strName) {
        hash.put(NAME, strName);
    }

    public Object getItem() {
        return hash.get(ITEM);
    }

    public void setItem(Object objItem) {
        hash.put(ITEM, objItem);
    }

    private Hashtable hash = new Hashtable();
}
