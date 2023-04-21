package org.googlecode.struts2jxls.example;

/**
 * Date: May 3, 2008
 * Time: 10:49:51 AM
 *
 * @author Dmitry Lisin
 */
public class Employee {

    private String name;

    private int age;

    private double payment;

    public Employee(String name, int age, double payment) {
        this.name = name;
        this.age = age;
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getPayment() {
        return payment;
    }
}
