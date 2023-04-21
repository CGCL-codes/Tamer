package uk.ac.lkl.migen.system.expresser.model.event;

import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;

/**
 * Represents the creation of a tied number.
 * 
 * @author Ken Kahn
 * 
 */
public class TiedNumberCreationEvent extends CreationEvent<Object> {

    protected TiedNumberExpression<IntegerValue> tiedNumber;

    public TiedNumberCreationEvent(TiedNumberExpression<IntegerValue> tiedNumber, Object source) {
        super(source);
        this.tiedNumber = tiedNumber;
    }

    @Override
    public String logMessage() {
        return "Created tied number " + getTiedNumber().getId() + " (" + getTiedNumber().getIdString() + ")";
    }

    public TiedNumberExpression<IntegerValue> getTiedNumber() {
        return tiedNumber;
    }
}
