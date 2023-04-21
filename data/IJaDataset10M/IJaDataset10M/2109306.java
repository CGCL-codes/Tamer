package net.sourceforge.freejava.type.traits;

import net.sourceforge.freejava.lang.INegotiation;
import net.sourceforge.freejava.lang.NegotiationException;

public abstract class AbstractValidator<T> implements IValidator<T> {

    @Override
    public void validate(T o, INegotiation negotiation) throws ValidateException, NegotiationException {
        if (negotiation != null) negotiation.bypass();
        validate(o);
    }
}
