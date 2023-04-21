package edu.java.texbooks.scjp.test02;

public abstract class SpecificAbstractClass extends AbstractMyClass {

    public abstract void methodF(int x);

    public void methodG(int x, int y) {
        System.out.println(super.member);
        System.out.println("SpecificAbstractClass.methodG():  " + x + ", " + y);
    }
}
