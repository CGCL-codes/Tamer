package com.justin.ioc.picocontainer;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.MethodInjection;
import org.springframework.util.Assert;

public class MethodInjectionSample {

    public static void main(String[] args) {
        MutablePicoContainer pico = new DefaultPicoContainer(new MethodInjection());
        pico.addComponent(Apple7.class);
        pico.addComponent(Orange.class);
        pico.addComponent(Pear.class);
        pico.addComponent(Banana.class);
        Apple7 apple7 = pico.getComponent(Apple7.class);
        Assert.notNull(apple7);
        Assert.notNull(apple7.getBanana());
        Assert.notNull(apple7.getOrange());
        Assert.notNull(apple7.getPear());
        MutablePicoContainer pico1 = new DefaultPicoContainer(new MethodInjection("myInjectMethod"));
        pico1.addComponent(Apple7.class);
        pico1.addComponent(Orange.class);
        pico1.addComponent(Pear.class);
        pico1.addComponent(Banana.class);
        Apple7 apple71 = pico1.getComponent(Apple7.class);
        Assert.notNull(apple71);
        Assert.notNull(apple71.getBanana());
        Assert.notNull(apple71.getOrange());
        Assert.notNull(apple71.getPear());
    }
}
