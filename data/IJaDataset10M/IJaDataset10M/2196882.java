package Test3Junit;

public class rb extends java.lang.Object {

    public static void main(java.lang.String[] argv) {
        Test3Junit.rb.main();
    }

    public static java.io.PrintStream main() {
        junit.framework.TestSuite suite = null;
        MyTest test = null;
        java.io.PrintStream temp$1 = java.lang.System.out;
        temp$1.println("Running");
        suite = new junit.framework.TestSuite("Selected tests");
        test = new MyTest();
        MyTest temp$2 = test;
        temp$2.setName("test1");
        junit.framework.TestSuite temp$3 = suite;
        temp$3.addTest(test);
        junit.textui.TestRunner.run(suite);
        java.io.PrintStream temp$4 = java.lang.System.out;
        temp$4.println("Done");
        return temp$4;
    }
}
