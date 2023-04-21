package com.kokesoft.easywebdav.ui;

import java.util.Hashtable;
import org.eclipse.swt.graphics.Image;

public class VersionsData extends OutlineViewGroup {

    Hashtable versions;

    public VersionsData() {
        versions = new Hashtable();
    }

    public void addVersion(String name, String url) {
        versions.put(url, name);
    }

    public void removeVersion(String url) {
        versions.remove(url);
    }

    public Object[] getChildren(Object parentElement) {
        Object[] empty = {};
        return empty;
    }

    public boolean hasChildren(Object parent) {
        return versions != null && versions.size() != 0;
    }

    public Image getImage(Object obj) {
        return null;
    }

    public String getText(Object obj) {
        return "TODO";
    }
}
