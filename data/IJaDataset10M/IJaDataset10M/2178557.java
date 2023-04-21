package com.yilan.java.instance.test;

import com.yilan.component.runtime.ExternalReference;
import com.yilan.component.runtime.ExternalService;
import com.yilan.java.instance.JavaInstanceRuntimeComponent;
import calculator.AddService;
import calculator.AddServiceImpl;
import calculator.CalculatorService;
import calculator.CalculatorServiceImpl;
import calculator.DivideService;
import calculator.DivideServiceImpl;
import calculator.MultiplyService;
import calculator.MultiplyServiceImpl;
import calculator.SubtractService;
import calculator.SubtractServiceImpl;
import junit.framework.TestCase;

public class TestInstanceInitilaze extends TestCase {

    public void testLoad() {
        CalculatorService service = new CalculatorServiceImpl();
        AddService add = new AddServiceImpl();
        DivideService divide = new DivideServiceImpl();
        MultiplyService multiply = new MultiplyServiceImpl();
        SubtractService subtract = new SubtractServiceImpl();
        JavaInstanceRuntimeComponent calculatorService = new JavaInstanceRuntimeComponent("calculator.service", service);
        JavaInstanceRuntimeComponent addService = new JavaInstanceRuntimeComponent("calculator.add", add);
        JavaInstanceRuntimeComponent divideService = new JavaInstanceRuntimeComponent("calculator.divide", divide);
        JavaInstanceRuntimeComponent multiplyService = new JavaInstanceRuntimeComponent("calculator.multiply", multiply);
        JavaInstanceRuntimeComponent subtractService = new JavaInstanceRuntimeComponent("calculator.subtract", subtract);
        try {
            calculatorService.initialize();
            addService.initialize();
            divideService.initialize();
            multiplyService.initialize();
            subtractService.initialize();
            ExternalService[] es = calculatorService.getExternalServices();
            ExternalReference[] er = calculatorService.getExternalReferences();
            assertEquals(1, es.length);
            assertEquals(4, er.length);
            assertEquals(0, addService.getExternalReferences().length);
            assertEquals(1, divideService.getExternalServices().length);
            assertEquals("calculator.CalculatorService", es[0].getContract().getContractName());
            calculatorService.connect(addService);
            assertEquals(2.0, service.add(1, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
