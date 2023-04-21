package com.gtt.pattern.creational.abstractfactory;

/**
 * Concrete Factory 1
 * 
 * @author Michael(gao12581@sina.com)
 * @date 2011-2-23 10:57:00
 * 
 */
public class ConcreteFactory1 extends AbstractFactory {

    @Override
    public AbstractProductA createProductA() {
        return new ProductA1();
    }

    @Override
    public AbstractProductB createProductB() {
        return new ProductB1();
    }
}
