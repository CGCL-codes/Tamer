package org.ucdetector.example;

import java.util.ArrayList;

public class AnonymousClass {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>() {

            private static final long serialVersionUID = 1L;

            public int size() {
                changeToPrivate();
                return 2;
            }

            public void changeToPrivate() {
            }
        };
        System.out.println("size=" + list);
        ArrayList<String> list2 = new ArrayList<String>() {

            private static final long serialVersionUID = 1L;

            public int size() {
                return 2;
            }

            @SuppressWarnings("unused")
            public void unused() {
            }
        };
        System.out.println("size=" + list2);
    }
}
