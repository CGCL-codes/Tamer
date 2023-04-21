package com.justin.ioc.picocontainer;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.NamedMethodInjection;
import org.springframework.util.Assert;

public class NamedMethodInjectionSample {

    public static void main(String[] args) {
        MutablePicoContainer pico = new DefaultPicoContainer(new NamedMethodInjection());
        pico.addConfig("aaa", new Orange());
        pico.addConfig("bbb", new Pear());
        pico.addConfig("ccc", new Banana());
        pico.addComponent(Apple8.class);
        Apple8 apple8 = pico.getComponent(Apple8.class);
        Assert.notNull(apple8);
        Assert.notNull(apple8.getBanana());
        Assert.notNull(apple8.getOrange());
        Assert.notNull(apple8.getPear());
    }
}
