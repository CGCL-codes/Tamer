package com.fdaoud.rayures.action;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * @author Frederic Daoud
 */
public class SomeActionBean extends TestActionBean {

    @Validate(required = true, on = "validated")
    public String parameter;

    public Resolution validated() {
        return new ForwardToDefaultView(this);
    }
}
