package org.criticalfailure.anp.core.domain.factory.impl;

import org.criticalfailure.anp.core.domain.entity.Address;
import org.criticalfailure.anp.core.domain.factory.IAddressFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author pauly
 * 
 */
@Component("addressFactory")
public class AddressFactoryImpl implements IAddressFactory, ApplicationContextAware {

    private Logger logger;

    private ApplicationContext appContext;

    public AddressFactoryImpl() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public Address createAddress() {
        Address addr = (Address) appContext.getBean("address");
        return addr;
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.appContext = context;
    }
}
