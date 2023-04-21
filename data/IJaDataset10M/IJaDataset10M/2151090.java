package net.sf.betterj.model.value;

import net.sf.betterj.model.util.AbstractEnumValue;

/**
 * @author Oleh Sklyarenko
 */
public abstract class AbstractValueAttributes extends AbstractEnumValue implements IValueAttributes {

    /***
     * 
     * @param value
     *            - one of the net.sf.betterj.model.value.IValueAttribute
     */
    protected AbstractValueAttributes(final int value) {
        _enumValue = value;
    }

    @Override
    public boolean isLiteral() {
        return _enumValue == IValueAttribute.LITERAL;
    }

    @Override
    public boolean isNull() {
        return _enumValue == IValueAttribute.NULL;
    }

    @Override
    public boolean isVariable() {
        return _enumValue == IValueAttribute.VARIABLE;
    }
}
