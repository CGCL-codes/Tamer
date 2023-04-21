package org.nomicron.suber.model.bean;

import com.dreamlizard.miles.spring.AbstractBeanNameable;
import com.dreamlizard.miles.interfaces.Named;

/**
 * Implementation of a BeanNameable bean that also implements Named.
 */
public class NamedBean extends AbstractBeanNameable implements Named {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
