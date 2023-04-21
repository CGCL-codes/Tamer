package com.ixora.rms.logging.data;

import com.ixora.rms.data.CounterValue;
import com.ixora.rms.data.CounterValueObject;
import com.ixora.rms.data.ValueObject;

/**
 * @author Daniel Moraru
 */
public class AggCounterValueObject implements AggCounterValue {

    /** Value */
    private ValueObject fValue;

    /**
	 * @see com.ixora.rms.logging.data.AggCounterValue#addCounterValue(com.ixora.rms.data.CounterValue)
	 */
    public void addCounterValue(CounterValue cv) {
        if (fValue == null) {
            fValue = ((CounterValueObject) cv).getValue();
        } else {
            fValue.aggregate(((CounterValueObject) cv).getValue());
        }
    }

    /**
	 * @see com.ixora.rms.logging.data.AggCounterValue#getCounterValue()
	 */
    public CounterValue getCounterValue() {
        return new CounterValueObject(fValue);
    }
}
