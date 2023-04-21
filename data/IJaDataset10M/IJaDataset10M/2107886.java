package net.sf.betterj.model.util;

/**
 * @author Revenge
 * 
 */
public abstract class AbstractEnumValue implements IEnum {

    protected int _enumValue;

    @Override
    public void addValue(final int value) {
        _enumValue |= value;
    }

    @Override
    public void resetValue() {
        _enumValue = 0;
    }
}
