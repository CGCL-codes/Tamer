package com.ezsoft.ezpersistence;

/**
 * <p>Title:        Persistence</p>
 * <p>  Description:</p>
 * <p> Copyright:    Copyright (c) 2001</p>
 * <p>  Company:      http://www.ez-softinc.com</p>
 * @author Michael Lee
 * @version 0.9
 */
public class PersistenceField {

    protected String NAME;

    protected int TYPE;

    public PersistenceField() {
    }

    public PersistenceField(String name, int type) {
        NAME = name;
        TYPE = type;
    }
}
