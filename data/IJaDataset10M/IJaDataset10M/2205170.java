package sis.report;

class SuperClass {

    static boolean constructorWasCalled = false;

    SuperClass(String parm) {
        constructorWasCalled = true;
    }
}
