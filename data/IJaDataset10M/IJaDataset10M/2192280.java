package com.enerjy.analyzer.java.rules.testfiles.T0061;

import java.util.Enumeration;

@SuppressWarnings("all")
public class PTest4 {

    public Enumeration<Object> entries() {
        final Enumeration enum_ = null;
        return new Enumeration<Object>() {

            public boolean hasMoreElements() {
                return enum_.hasMoreElements();
            }

            public MyObject nextElement() {
                Object ze = (Object) enum_.nextElement();
                return new MyObject(ze);
            }
        };
    }

    static class MyObject extends Object {

        public MyObject(Object o) {
        }
    }
}
