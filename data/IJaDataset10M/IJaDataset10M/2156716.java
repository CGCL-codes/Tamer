package org.slasoi.slamodel.sla;

import org.slasoi.slamodel.sla.tools.Validator;
import org.slasoi.slamodel.sla.tools.ExprValidator;

public class CustomAction extends Guaranteed.Action.Defn {

    private static final long serialVersionUID = 1L;

    public Validator.Warning[] validate(ExprValidator ev) throws Validator.Exception {
        return new Validator.Warning[0];
    }
}
