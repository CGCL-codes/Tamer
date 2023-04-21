package com.threerings.antidote.field.text;

import com.threerings.antidote.property.EnumProperty;
import static com.threerings.antidote.MutabilityHelper.requiresValidation;

public abstract class EnumTextField<T extends Enum<T>> extends SingleLineTextField {

    public EnumTextField(Class<T> enumClass) {
        _enumClass = enumClass;
    }

    /**
     * Returns the user data converted into an enum. Cannot be called before validate().
     */
    public T getEnum() {
        requiresValidation(_enum);
        return _enum;
    }

    @Override
    protected final void validateTextField() {
        final EnumProperty<T> property = new EnumProperty<T>("text field", this, _enumClass);
        property.setValue(getText());
        switch(validateProperties(property)) {
            case ALL_INVALID:
            case SOME_INVALID:
                return;
            case ALL_VALID:
                _enum = property.getValue();
                return;
        }
    }

    /** The enum after it has been validated. */
    private T _enum;

    /** The class for the enum expected in this text field. */
    private final Class<T> _enumClass;
}
