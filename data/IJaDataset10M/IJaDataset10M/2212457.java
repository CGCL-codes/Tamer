package com.javabi.htmlbuilder.css.unit;

/**
 * A Percent Unit.
 */
public class Percent extends CSSUnitNumber {

    /**
	 * Creates a new percent.
	 * @param number the number.
	 */
    public Percent(int number) {
        super(number);
    }

    @Override
    public CSSUnit getUnit() {
        return CSSUnit.PERCENT;
    }
}
