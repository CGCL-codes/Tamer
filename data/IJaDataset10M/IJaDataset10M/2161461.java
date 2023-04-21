package org.jboss.tutorial.injection.bean;

import javax.ejb.Stateless;

@Stateless
public class CalculatorBean implements Calculator {

    public int add(int x, int y) {
        return x + y;
    }

    public int subtract(int x, int y) {
        return x - y;
    }
}
