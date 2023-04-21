package tests;

public class sysclasses extends TCBase {

    public static String testspecpath2 = "$A" + z + "$B";

    @Override
    public void setUp() throws Exception {
        testspecpath = testspecpath2;
        super.setUp();
    }

    /** Tests using JMLDataGroup*/
    public void testDataGroup() {
        helpTCF("A.java", " class A { //@ public model JMLDataGroup streamState;\n}");
    }
}
