package com.kapil.framework.test.concurrent;

import com.kapil.framework.concurrent.IBroker;
import com.kapil.framework.concurrent.impl.AbstractConsumer;

public class PrintIntegersConsumer extends AbstractConsumer<Integer> {

    public PrintIntegersConsumer(String name, IBroker<Integer> broker) {
        super(name, broker);
    }

    @Override
    public void doConsume(Integer data) {
        System.out.println("Consumer " + super.name + " consumed: " + data);
    }
}
