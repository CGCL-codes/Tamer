package com.tsadom.db;

import java.util.LinkedList;

/**
 * @author uriel
 */
public class DbLinkedListInt extends LinkedList {

    static final long serialVersionUID = -6185024900451324140L;

    public int value;

    public DbLinkedListInt(int key) {
        super();
        value = key;
    }
}
