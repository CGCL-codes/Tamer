package com.ivis.xprocess.ui.validation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class WidgetVerifyValidator extends Validator implements VerifyListener {

    private Validator validator;

    public WidgetVerifyValidator(Validator validator) {
        super(validator);
        this.validator = validator;
    }

    @Override
    public boolean val(String string) {
        return true;
    }

    public void verifyText(VerifyEvent e) {
        if ((e.keyCode == SWT.DEL) || (e.keyCode == SWT.BS)) {
            e.doit = true;
            return;
        }
        if (!validator.validate("" + e.character)) {
            e.doit = false;
        }
    }
}
