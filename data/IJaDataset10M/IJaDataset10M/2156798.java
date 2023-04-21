package org.happy.commons.patterns.decorator.beans;

import org.happy.commons.patterns.decorator.LayerDecorator_1x0Impl;

/**
 * decorator for MyBean Interface
 * @author Andreas Hollmann
 *
 */
public class MyBeanDecorator extends LayerDecorator_1x0Impl<MyBean> implements MyBean {

    public MyBeanDecorator(MyBean decorated) {
        super(decorated);
    }

    public Integer getValue() {
        return decorated.getValue();
    }

    public void setValue(Integer value) {
        decorated.setValue(value);
    }
}
