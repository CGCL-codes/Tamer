package com.gtt.pattern.structural.facade;

/**
 * Facade
 * 
 * @author 高甜甜(gao12581@sina.com)
 * @date 2011-3-31
 * 
 */
public class Facade {

    private Class1 c1 = new Class1();

    private Class2 c2 = new Class2();

    private Class3 c3 = new Class3();

    public void operation() {
        System.out.println("The facade operation carries out complex decision-making");
        System.out.println("which in turn results in calls to the subsystem classes");
        c3.operation3();
        c1.operation1();
        c2.operation2();
    }
}
