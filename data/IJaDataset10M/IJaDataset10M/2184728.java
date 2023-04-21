package com.msli.graphic.form.shape;

import com.msli.core.exception.NotYetImplementedException;
import com.msli.core.util.CoreUtils;
import com.msli.graphic.form.Form;
import com.msli.graphic.form.FormMediator;

/**
 * A default ShapeMediator that recognizes the standard shapes provided in this
 * package.
 * <P>
 * Can be extended to recognize custom shapes by overriding methods, catching
 * UnsupportedOperandException and UnsupportedOperationException, and invoking a
 * custom implementation accordingly.
 * @author jonb
 */
public class ShapeMediator implements FormMediator {

    @Override
    public <R extends Form<R>> R combine(Form<?> form, R retVal) {
        CoreUtils.assertNonNullArg(form);
        CoreUtils.assertNonNullArg(retVal);
        NotYetImplementedException.doThrow(this);
        return null;
    }

    @Override
    public <R extends Form<R>> R intersect(Form<?> form, R retVal) {
        NotYetImplementedException.doThrow(this);
        return null;
    }

    @Override
    public <R extends Form<R>> R setForm(Form<?> form, R retVal) {
        NotYetImplementedException.doThrow(this);
        return null;
    }

    @Override
    public boolean isContaining(Form<?> form, Form<?> target) {
        NotYetImplementedException.doThrow(this);
        return false;
    }

    @Override
    public boolean isIntersecting(Form<?> form, Form<?> target) {
        NotYetImplementedException.doThrow(this);
        return false;
    }

    public static ShapeMediator getInstance() {
        return INSTANCE;
    }

    private static ShapeMediator INSTANCE = new ShapeMediator();
}
