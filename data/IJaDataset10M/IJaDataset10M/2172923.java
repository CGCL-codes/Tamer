package com.apelon.common.util;

import java.io.*;
import java.util.*;

public class ConsIterator implements Enumeration {

    private int iter = 0;

    private int end = 0;

    private Object[] list;

    public ConsIterator(Object[] l, int current) {
        iter = 0;
        end = current;
        list = l;
    }

    public boolean hasMoreElements() {
        return (iter < end);
    }

    public Object nextElement() {
        return list[iter++];
    }

    public Object current() {
        if (iter < end) {
            return list[iter];
        } else {
            return null;
        }
    }
}
