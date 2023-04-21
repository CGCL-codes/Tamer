package com.enerjy.analyzer.java.rules.testfiles.T0270;

/**
 * foreach in array
 * @author Peter Carr
 */
public class PTest02 {

    private PTest02() {
    }

    /**
     * Test normal usage of for-each.
     * @param strs
     */
    public static void test(String[] strs) {
        for (String str : strs) {
            System.out.println(str);
        }
    }

    /**
     * Test nested for-each loops.
     * @param outers
     * @param inners
     */
    public static void test2(String[] outers, String[] inners) {
        for (String outer : outers) {
            for (String inner : inners) {
                System.out.println("\t" + outer + "." + inner);
            }
        }
    }
}
