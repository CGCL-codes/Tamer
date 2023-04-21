package net.sf.RecordEditor.testcode;

import net.sf.RecordEditor.examples.USdate8;

public class TestCreate1 {

    static Class<USdate8> c = USdate8.class;

    public static void main(String[] args) {
        runTesy();
        long t = System.currentTimeMillis();
        runTesy();
        System.out.print((System.currentTimeMillis() - t));
    }

    public static void runTesy() {
        for (int i = 0; i < 500000; i++) {
            try {
                c.newInstance();
            } catch (Exception e) {
            }
        }
    }
}
