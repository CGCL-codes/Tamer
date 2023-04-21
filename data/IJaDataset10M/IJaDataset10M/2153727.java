package com.pragprog.dhnako.carserv.dom.payment;

import org.nakedobjects.applib.annotation.Hidden;

@Hidden
public interface PaymentMethodTypeRepository {

    public PaymentMethodType findByClassName(final String fullyQualifiedClassName);
}
