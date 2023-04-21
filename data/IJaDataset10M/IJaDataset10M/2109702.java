package com.billsix.examples.spring;

import com.billsix.examples.spring.dynamicproxies.TransactionInterceptor;
import com.billsix.examples.spring.dynamicproxies.CalculatorService;
import com.billsix.examples.spring.dynamicproxies.CalculatorServiceImplementation;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.aop.framework.ProxyFactoryBean;

public class CalculatorServiceTest extends TestCase {

    public CalculatorServiceTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(CalculatorServiceTest.class);
    }

    public void setUp() throws Exception {
        CalculatorServiceImplementation target = new CalculatorServiceImplementation();
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.addAdvice(new TransactionInterceptor());
        factory.setTarget(target);
        factory.setProxyInterfaces(new Class[] { CalculatorService.class });
        calculatorService = (CalculatorService) factory.getObject();
    }

    public void testValidAccess() {
        assertTrue(calculatorService.add(4, 5) == 9);
    }

    CalculatorService calculatorService;
}
