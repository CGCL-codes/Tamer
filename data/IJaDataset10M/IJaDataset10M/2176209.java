package org.xactor.test.perf.contact.interfaces;

import javax.ejb.EJBLocalObject;

/**
 * An entity bean that holds a contact.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public interface ContactLocal extends EJBLocalObject {

    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);
}
