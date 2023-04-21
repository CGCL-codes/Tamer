package org.springframework.richclient.form.builder.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.binding.form.Binding;
import org.springframework.richclient.binding.form.BindingFactory;
import org.springframework.richclient.form.binding.swt.SWTBindingUtils;
import org.springframework.util.Assert;

/**
 * @author Oliver Hutchison
 */
public abstract class AbstractFormBuilder {

    private final BindingFactory bindingFactory;

    private final Composite parentComposite;

    protected AbstractFormBuilder(Composite parentComposite, BindingFactory bindingFactory) {
        Assert.notNull(bindingFactory);
        this.bindingFactory = bindingFactory;
        this.parentComposite = parentComposite;
    }

    protected BindingFactory getBindingFactory() {
        return bindingFactory;
    }

    protected FormModel getFormModel() {
        return bindingFactory.getFormModel();
    }

    protected Binding getDefaultBinding(String field) {
        try {
            SWTBindingUtils.pushParent(parentComposite);
            return getBindingFactory().createBinding(field);
        } finally {
            SWTBindingUtils.popParent();
        }
    }

    protected Binding getBinding(String field, Control control) {
        try {
            SWTBindingUtils.pushParent(parentComposite);
            return getBindingFactory().bindControl(control, field);
        } finally {
            SWTBindingUtils.popParent();
        }
    }

    protected Label getLabelFor(String field, Control control) {
        Label label = new Label(parentComposite, SWT.NONE);
        label.setText(field);
        return label;
    }
}
