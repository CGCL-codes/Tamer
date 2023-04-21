package org.nightlabs.ejb3.stateless.bean;

public interface TestEJB {

    public int add(int x, int y);

    public String sayHello();

    public String getTime();

    public double[] compute(int number);

    public int subtract(int x, int y);
}
