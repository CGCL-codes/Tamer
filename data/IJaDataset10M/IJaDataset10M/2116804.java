package org.silentsquare.wiiaccess;

import java.security.Permission;

/**
 * ALL implies PLAY and WATCH.
 * PLAY implies WATCH.
 * @author wjfang
 *
 */
public class WiiPermission extends Permission {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final WiiPermission ALL = new WiiPermission("*");

    public static final WiiPermission PLAY = new WiiPermission("play");

    public static final WiiPermission WATCH = new WiiPermission("watch");

    public WiiPermission(String name) {
        super(name);
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof WiiPermission)) return false;
        if (!p.equals(ALL) && !p.equals(PLAY) && !p.equals(WATCH)) return false;
        if (this.equals(ALL) || this.equals(PLAY) || this.equals(p)) return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WiiPermission)) return false;
        WiiPermission wp = (WiiPermission) obj;
        return wp.getName() == null ? getName() == null : wp.getName().equals(getName());
    }

    @Override
    public String getActions() {
        return null;
    }

    @Override
    public int hashCode() {
        return getName() == null ? 0 : getName().hashCode();
    }

    @Override
    public String toString() {
        return "WiiPermission " + getName();
    }
}
