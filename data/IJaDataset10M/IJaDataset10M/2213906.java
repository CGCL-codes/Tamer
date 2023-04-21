package net.sf.warpcore.ejb;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class EntityContext {

    public EntityContext() {
    }

    public static Object lookup(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    public static UniquePK getPrimaryKey() {
        return null;
    }

    public static String getPrincipal() {
        return "";
    }
}
