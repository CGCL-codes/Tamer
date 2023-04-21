package org.incava.doctorj;

import junit.framework.TestCase;

public class TestMethodDocAnalyzer extends Tester {

    public TestMethodDocAnalyzer(String name) {
        super(name);
    }

    public void testReturnOK() {
        evaluate("/** This is a description. */\n" + "class Test {\n" + "    /** This is a description.\n" + "      * @return What this method returns.\n" + "      */\n" + "    int f() { return 1; }\n" + "}\n", new Object[][] {});
    }

    public void testReturnFromVoid() {
        evaluate("/** This is a description. */\n" + "class Test {\n" + "    /** This is a description.\n" + "      * @return \n" + "      */\n" + "    void f() {}\n" + "}\n", new Object[][] { { MethodDocAnalyzer.MSG_RETURN_FOR_VOID_METHOD, new Integer(4), new Integer(9), new Integer(4), new Integer(15) } });
    }

    public void testReturnUndescribed() {
        evaluate("/** This is a description. */\n" + "class Test {\n" + "    /** This is a description.\n" + "      * @return \n" + "      */\n" + "    int f() { return 1; }\n" + "}\n", new Object[][] { { MethodDocAnalyzer.MSG_RETURN_WITHOUT_DESCRIPTION, new Integer(4), new Integer(9), new Integer(4), new Integer(15) } });
    }

    public void testReturnTypeUsed() {
        evaluate("/** This is a description. */\n" + "class Test {\n" + "    /** This is a description.\n" + "      * @return int This returns an integer.\n" + "      */\n" + "    int f() { return 1; }\n" + "}\n", new Object[][] { { MethodDocAnalyzer.MSG_RETURN_TYPE_USED, new Integer(4), new Integer(17), new Integer(4), new Integer(19) } });
    }
}
