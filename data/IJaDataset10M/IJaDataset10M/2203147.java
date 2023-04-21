package com.mapbased.sfw.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NavItem implements java.lang.Comparable<NavItem> {

    private final List<NavItem> children = new CopyOnWriteArrayList<NavItem>();

    public final String name;

    private String caption;

    private int order;

    public List<NavItem> getChildren() {
        return children;
    }

    public String getCaption() {
        return caption;
    }

    public String getLink(String parentPath) {
        if (this.children.size() == 0) {
            return parentPath + this.name;
        } else {
            return parentPath + this.name + "/";
        }
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public NavItem(String name) {
        this.name = name;
        this.caption = name;
    }

    public NavItem getChild(String childName) {
        return getItem(childName, this.children);
    }

    public boolean hasChild() {
        return this.children.size() > 0;
    }

    /**
	 * Only one thread can do add
	 * 
	 * @param item
	 * @return
	 */
    public synchronized NavItem add(NavItem item) {
        NavItem na = this.getChild(item.name);
        if (na == null) {
            this.children.add(item);
            na = item;
        } else {
            if (item.caption != null) {
                na.caption = item.caption;
                na.order = item.order;
            }
        }
        java.util.Collections.sort(this.children);
        return na;
    }

    public static NavItem getItem(String inname, List<NavItem> list) {
        for (NavItem i : list) {
            if (i.name.equals(inname)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NavItem other = (NavItem) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public int compareTo(NavItem o) {
        if (this.order == o.order) {
            return this.name.compareTo(o.name);
        }
        return this.order - o.order;
    }
}
