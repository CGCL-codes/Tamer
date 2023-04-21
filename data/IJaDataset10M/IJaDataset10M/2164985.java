package com.pragprog.dhnako.carserv.fixture.payment;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import com.pragprog.dhnako.carserv.dom.payment.PaymentMethod;
import com.pragprog.dhnako.carserv.dom.payment.PaymentMethodType;

public abstract class AbstractPaymentMethodTypesFixture extends AbstractFixture {

    protected PaymentMethodType createPaymentMethodType(Class<? extends PaymentMethod> subclass) {
        PaymentMethodType paymentMethodType = newTransientInstance(PaymentMethodType.class);
        paymentMethodType.setFullyQualifiedClassName(subclass.getName());
        persist(paymentMethodType);
        return paymentMethodType;
    }
}
