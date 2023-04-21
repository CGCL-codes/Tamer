package ru.arriah.common.ejb;

import javax.ejb.*;

public interface SequenceServiceLocalObject extends EJBLocalObject {

    public Integer getNextKeyFor(String entityName);
}
