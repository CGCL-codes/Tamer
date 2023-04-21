package org.xactor.test.ws.usertx.interfaces;

import javax.ejb.EJBLocalObject;

/**
 * Counter bean local interface.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public interface CounterLocal extends EJBLocalObject {

    Integer getId();

    int getCounter();

    void setCounter(int counter);
}
