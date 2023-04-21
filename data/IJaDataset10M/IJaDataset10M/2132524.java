package org.gecko.jee.community.mobidick.validator;

import org.gecko.jee.community.mobidick.bean.Behavior;
import org.gecko.jee.community.mobidick.validator.error.Validation;

public interface Rule<VALIDABLE extends Object & Validable<VALIDABLE>> extends Behavior {

    public abstract void validate(final VALIDABLE bean, final Validation<VALIDABLE> validation);
}
