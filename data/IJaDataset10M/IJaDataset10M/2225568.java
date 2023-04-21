package com.pinae.simba.beanfactory.xml.test;

public class A {

    private B b;

    private String create;

    public A() {
        create = " successful";
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public void show() {
        System.out.println(b.getName());
        System.out.println(b.getPassword());
    }

    public void create() {
        System.out.println("create :" + create);
    }

    public void run() {
        System.out.print("run:");
        System.out.print("name : " + b.getName());
        System.out.println(" password : " + b.getPassword());
    }
}
