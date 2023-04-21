package com.hummer.service.data.base;

/**
 * @author gernot.hummer
 *
 */
public class BaseDataFilterValue {

    private Object showValue;

    private Object value;

    /**
	 * The frontend show value will be used in views and dialogs for
	 * item labels.
	 * 
	 * @param showValue
	 * @param value
	 */
    public BaseDataFilterValue(Object showValue, Object value) {
        this.showValue = showValue;
        this.value = value;
    }

    /**
	 * @return the showValue
	 */
    public Object getShowValue() {
        return showValue;
    }

    /**
	 * @return the value
	 */
    public Object getValue() {
        return value;
    }
}
