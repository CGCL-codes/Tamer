package org.jmsrmi.test;

import java.io.IOException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

    public static void main(String[] args) {
        try {
            System.out.println("---- BEGIN TEST (SPRING) ---");
            AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
            MathService helper = (MathService) ctx.getBean("client");
            System.out.println(helper.multiply(3, 4));
            try {
                helper.badMethod();
            } catch (IOException ex) {
                System.out.println("Exception caught: " + ex.getMessage());
            }
            Accumulator accu = new Accumulator(5);
            helper.setValueListener(accu);
            helper.substract(10, 5);
            helper.substract(10, 2);
            helper.substract(7, 6);
            helper.substract(3, 1);
            helper.substract(15, 5);
            System.out.println(accu.getSum(10000));
            ctx.destroy();
            System.out.println("---- END TEST (SPRING) ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
