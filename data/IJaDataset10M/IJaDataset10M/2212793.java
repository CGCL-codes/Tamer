package jvmTestCases;

/**
 * 
 * Description:
 * <p>
 * This file should generate following opcodes
 * from opcode=03 to 17
 * and from opcode=21 to 86
 * and from opcode=96 to 132
 * and from opcode=172 to 176
 * In case of error in any opcode the output of this class should be changed.
 * This class does not depends upon any other class
 * 
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class LoadAndStoreConstantMathOpcodes_1 implements ToCheckInvokeInterface {

    protected long[] longArray = new long[5];

    protected String[] strArray = new String[15];

    protected int[] intArray = new int[5];

    protected char[] charArray = { 'a', 'b' };

    protected int invokeSpecialTestInt = 0;

    private static final int mySuccessValue = 298982923;

    static int testgetStaticInheritance = 70;

    private static LoadAndStoreConstantMathOpcodes_1 myObject = new LoadAndStoreConstantMathOpcodes_1();

    protected int testGetInstanceOf = 55;

    protected int testSuperFields1;

    protected int testSuperFields2 = 10;

    protected LoadAndStoreConstantMathOpcodes_1() {
        invokeSpecialTestInt = 5;
    }

    public static LoadAndStoreConstantMathOpcodes_1 getInstanceOf() {
        return myObject;
    }

    public int testInvokeInterface(int i) {
        return i + 5;
    }

    /**
     * here for ConstantPoolOpCodes class testing/usage. It has nothing to do with this class
     */
    public static int testInvokeStatic() {
        return 500;
    }

    public int checkLoadStoreAndMathInstr() {
        Printer.startTest("LoadAndStoreConstantMathOpcodes");
        int result = 0;
        result += checkIConsts();
        Printer.println(result);
        Printer.println(result);
        result += checkRem();
        Printer.println(result);
        result += checkShift();
        Printer.println(result);
        result += checkAloadsAStore();
        Printer.println(result);
        result += checkArrayloads();
        Printer.println(result);
        result += checkXOR();
        Printer.println(result);
        result += checkAToB(500);
        Printer.println(result);
        result += checkAndOr(1, 2, 3, 4);
        Printer.println(result);
        if (result == mySuccessValue) {
        } else {
            System.out.println("******** ERROR! Some load, store or Math instruction has not worked correctly...");
            System.out.print(result);
        }
        return result;
    }

    private long checkAToB(int inputInt) {
        return (byte) inputInt + (char) inputInt + (long) inputInt + (short) inputInt;
    }

    /**
     * following program test all iconst<>
     * also test iadd, imul, isub, idiv, ireturn
     *  
     */
    private int checkIConsts() {
        int iconst5 = 5;
        int iconstm1 = -1;
        int iconst2 = 2;
        int iconst3 = 3;
        int iconst4 = 4;
        int iconst0 = 0;
        int iconst1 = 1;
        return (iconst5 + iconst0) * (iconst1 / iconst2) + (iconst3 * iconst4) + (iconst5 - iconstm1);
    }

    /**
     * checks lload, ladd, lmul, lsub, 
     * @return
     */
    long checkLConsts() {
        long lconst5 = 512;
        long lconstm1 = -1212;
        long lconst2 = 2;
        long lconst3 = 3;
        long lconst4 = 4;
        long lconst0 = 0;
        long lconst1 = 1;
        return (lconst5 + lconst0) * (lconst1 / lconst2) + (lconst3 * lconst4) + (lconst5 - lconstm1);
    }

    /**
     * check Math rem and neg instructions
     * @return
     */
    private long checkRem() {
        int b = 2;
        int iLocal = -(11 % b);
        long lLocal = -((long) 10 % (long) iLocal + 2);
        return lLocal;
    }

    private long checkShift() {
        int i = 23;
        i = i << 2;
        i = i >> 3;
        i = i >>> 1;
        long l = (long) i;
        l = l << 6;
        l = l >> 2;
        l = l >>> 5;
        return l;
    }

    private long checkAndOr(int input1, int input2, long input3, long input4) {
        return (input1 | input2) & input2 & ((input3 & input4) | input4);
    }

    private long checkXOR() {
        int i = 70;
        i ^= 255;
        int l = 90;
        l ^= (long) i;
        return l;
    }

    /**
     * checks all aload and areturn
     * @return
     */
    private static int checkAloadsAStore() {
        LoadAndStoreConstantMathOpcodes_1 aload_0 = new LoadAndStoreConstantMathOpcodes_1();
        LoadAndStoreConstantMathOpcodes_1 aload_1 = aload_0;
        LoadAndStoreConstantMathOpcodes_1 aload_2 = aload_1;
        LoadAndStoreConstantMathOpcodes_1 aload_3 = aload_2;
        LoadAndStoreConstantMathOpcodes_1 aload = aload_3;
        return aload.invokeSpecialTestInt;
    }

    private long checkArrayloads() {
        byte[] byteArray = { 95, 99 };
        short[] shortArray = { 123, 9 };
        longArray[longArray.length - 1] = 52;
        intArray[1] = 298978978;
        strArray[1] = "1";
        byteArray[1] = 100;
        shortArray[1] = 1;
        return (byteArray[0] + byteArray[1] + longArray[longArray.length - 1] + intArray[1] + byteArray[1] + shortArray[0] + shortArray[1]);
    }

    public static void main(String args[]) throws Exception {
        LoadAndStoreConstantMathOpcodes_1 lscm = new LoadAndStoreConstantMathOpcodes_1();
        System.out.print("-----> ");
        System.out.println(lscm.checkLoadStoreAndMathInstr());
    }
}
