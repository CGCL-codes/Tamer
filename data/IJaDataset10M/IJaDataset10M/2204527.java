package com.cascadelayout.test;

import com.cascadelayout.layout.TestPainel;

public class Tests3 {

    private static int GAP = AbstractTest.GAP;

    public static void test1(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(100, 60 + (2 * GAP));
        at.setUserSize(200, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(0, 20 + GAP, 100, 20);
        at.addCommonBound(25, 40 + (2 * GAP), 50, 20);
        at.execute();
    }

    public static void test2(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(100, 60 + (2 * GAP));
        at.setUserSize(200, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(0, 20 + GAP, 100, 20);
        at.addCommonBound(0, 40 + (2 * GAP), 50, 20);
        at.execute();
    }

    public static void test3(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(100, 60 + (2 * GAP));
        at.setUserSize(200, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(0, 20 + GAP, 100, 20);
        at.addCommonBound(50, 40 + (2 * GAP), 50, 20);
        at.execute();
    }

    public static void test4(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(0, 20 + GAP, 50, 20);
        at.execute();
    }

    public static void test5(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(25, 20 + GAP, 50, 20);
        at.execute();
    }

    public static void test6(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(50, 20 + GAP, 50, 20);
        at.execute();
    }

    public static void test7(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(100 + GAP, 20 + GAP, 50, 20);
        at.execute();
    }

    public static void test8(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(125 + GAP, 20 + GAP, 50, 20);
        at.execute();
    }

    public static void test9(TestPainel tp) {
        AbstractTest at = AbstractTest.getInstance();
        at.setTestPainel(tp);
        at.setPackedSize(200 + GAP, 40 + GAP);
        at.setUserSize(300, 300);
        at.addCommonBound(0, 0, 100, 20);
        at.addCommonBound(100 + GAP, 0, 100, 20);
        at.addCommonBound(150 + GAP, 20 + GAP, 50, 20);
        at.execute();
    }
}
