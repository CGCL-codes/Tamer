package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.value.PasswordValue;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.abstracts.AbstractFieldSpecification;
import org.nakedobjects.viewer.skylark.value.PasswordField;

public class PasswordFieldSpecification extends AbstractFieldSpecification {

    public boolean canDisplay(final Content content) {
        return content.isValue() && content.getNaked() instanceof PasswordValue;
    }

    public View createView(final Content content, final ViewAxis axis) {
        return new PasswordField(content, this, axis, TEXT_WIDTH);
    }

    public String getName() {
        return "Password Field";
    }
}
