package com.daffodilwoods.daffodildb.utils;

import java.util.ArrayList;

public class DBStack extends ArrayList {

    public DBStack() {
    }

    public Object push(Object item) {
        add(item);
        return item;
    }

    public Object pop() {
        Object obj;
        int len = size();
        obj = peek();
        remove(len - 1);
        return obj;
    }

    public Object peek() {
        int len = size();
        if (len == 0) throw new java.util.EmptyStackException();
        return get(len - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    public int search(Object o) {
        int i = lastIndexOf(o);
        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

    private static final long serialVersionUID = 1976092019760920L;
}
