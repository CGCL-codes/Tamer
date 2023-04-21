package com.pragprog.dhnako.carserv.dom.payment;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.When;

@Bounded
@Immutable(When.ONCE_PERSISTED)
public class PaymentMethodType extends AbstractDomainObject {

    public String title() {
        return paymentMethodSubclass().getSimpleName();
    }

    public String iconName() {
        return title();
    }

    private String fullyQualifiedClassName;

    @MemberOrder(sequence = "1")
    public String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    public void setFullyQualifiedClassName(final String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    @Hidden
    public PaymentMethod create() {
        try {
            PaymentMethod paymentMethod = newTransientInstance(paymentMethodSubclass());
            paymentMethod.setType(this);
            return paymentMethod;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Hidden
    public Class<? extends PaymentMethod> paymentMethodSubclass() {
        try {
            return (Class<? extends PaymentMethod>) Class.forName(getFullyQualifiedClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No such payment method type");
        }
    }
}
