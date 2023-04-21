package org.aoplib4j.uml;

/**
 * @author Adrian Citu
 *
 */
public class Class2 {

    public void method1Class2() {
        System.out.println("method 1, class 2");
        this.method2Class2();
        Class3 cls3 = new Class3();
        cls3.method1Class3();
    }

    private Integer method2Class2() {
        return null;
    }
}
