package com.vlee.util;

import java.util.HashSet;

public class TestGUID {

    HashSet newSet = new HashSet();

    /** Creates new TestGUID */
    public TestGUID() {
    }

    public void test() {
        GUIDGenerator tester = null;
        try {
            tester = new GUIDGenerator();
        } catch (Exception e) {
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            System.out.println(tester.getUUID());
            tester.getUUID();
        }
        System.out.println("time =" + (System.currentTimeMillis() - start));
        System.out.println(newSet.size());
    }

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String args[]) {
        TestGUID test = new TestGUID();
        test.test();
    }
}
