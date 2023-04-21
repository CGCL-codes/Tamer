package tools;

/**
 * This is a class to just test new code or java features.
 * Try to keep each thing you are testing contained within a series of static
 * functions with one frontend function.
 * This way if either of us discovers some cool new java feature, it is right
 * here for either of us to see.
 * @author sicklybeans
 */
public class TestClass {

    public static void testStackTraceStuff(String toparg1) {
        testStackTraceStuffSubFunc("Test sub arg", 513);
    }

    private static void testStackTraceStuffSubFunc(String subarg1, int subarg2) {
        System.out.printf("In sub function");
        StackTraceElement[] stack = (new Throwable("Throwable message fsadjlasdjf")).getStackTrace();
        System.out.printf("stack size %d\n", stack.length);
        for (int i = 0; i < stack.length; i++) {
            System.out.printf("Stack Element %d toString: %s\n", i, stack[i].toString());
            System.out.printf("Stack Element %d getFileName: %s\n", i, stack[i].getFileName());
            System.out.printf("Stack Element %d getLineNumber: %s\n", i, stack[i].getLineNumber());
            System.out.printf("Stack Element %d getMethodName: %s\n", i, stack[i].getMethodName());
            System.out.printf("Stack Element %d getClassName: %s\n", i, stack[i].getClassName());
        }
    }

    public static void main(String args[]) {
        testStackTraceStuff("Test top arg");
    }
}
