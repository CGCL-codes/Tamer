package net.sf.snver.gui.riena.ui;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.ui.ridgets.nls.Messages;
import org.eclipse.riena.ui.ridgets.validation.Utils;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

public class NotEmptyWithMessage implements IValidator {

    String message = "Should not be empty!";

    public NotEmptyWithMessage() {
    }

    public NotEmptyWithMessage(String message) {
        this.message = message;
    }

    @Override
    public IStatus validate(Object value) {
        if (value instanceof String && !Utils.isEmpty((String) value)) {
            return ValidationRuleStatus.ok();
        }
        return ValidationRuleStatus.error(false, this.message);
    }
}
