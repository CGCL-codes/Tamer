package gov.nasa.jpf.symbc;

import java.util.Arrays;

public class TestIntStatic1 {

    private static String I1_PC1 = "# = 1\nx_1_SYMINT > CONST_1";

    private static String I1_PC2 = "x_1_SYMINT <= CONST_1";

    private static String I1_PC3 = "(y_2_SYMINT + x_1_SYMINT) > CONST_30";

    private static String I1_PC4 = "y_2_SYMINT > CONST_30";

    private static String I1_PC5 = "(y_2_SYMINT + x_1_SYMINT) <= CONST_30";

    private static String I1_PC6 = "y_2_SYMINT <= CONST_30";

    private static String makePCAssertString(String location, String goodPC, String badPC) {
        return String.format("Bad Path condition in %s:\nEXPECTED:\n%s\nACTUAL:\n%s\n", location, goodPC, badPC);
    }

    private static String trimPC(String pc) {
        return pc.substring(pc.indexOf("\n") + 1);
    }

    private static boolean pcMatches(String newPC, String oldPC) {
        String currentPC = TestUtils.getPathCondition();
        currentPC = trimPC(currentPC);
        newPC = trimPC(newPC);
        oldPC = trimPC(oldPC);
        if (oldPC.equals("")) return newPC.equals(currentPC); else return (newPC + " && " + oldPC).equals(currentPC);
    }

    public static void testInt1(int x, int y) {
        String pc = "";
        int z = x + y;
        if (x > 1) {
            assert pcMatches(I1_PC1, pc) : makePCAssertString("TestIntStatic1.testInt1 if x > 1", I1_PC1, TestUtils.getPathCondition());
            z = y;
        } else {
            assert pcMatches(I1_PC2, pc) : makePCAssertString("TestIntStatic1.testInt1 x <= 1", I1_PC2, TestUtils.getPathCondition());
        }
        pc = trimPC(TestUtils.getPathCondition());
        if (z > 30) {
            assert (pcMatches(I1_PC3, pc) || pcMatches(I1_PC4, pc)) : makePCAssertString("TestIntStatic1.testInt1 z <= 30", "one of\n" + (I1_PC3 + " && " + pc) + "\nor\n" + (I1_PC4 + " && " + pc), TestUtils.getPathCondition());
            z = 91;
        } else {
            assert (pcMatches(I1_PC5, pc) || pcMatches(I1_PC6, pc)) : makePCAssertString("TestIntStatic1.testInt1 z <= 30", "one of\n" + (I1_PC5 + " && " + pc) + "\nor\n" + (I1_PC6 + " && " + pc), TestUtils.getPathCondition());
        }
        pc = trimPC(TestUtils.getPathCondition());
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("MAIN: " + Arrays.asList(args));
        testInt1(11, 21);
    }
}
