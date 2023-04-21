package net.sf.jbob.swing;

import javax.swing.JComponent;
import net.sf.jbob.core.BindingDescription;
import net.sf.jbob.core.ViewBob;
import net.sf.jbob.core.ViewPojo;

public class ViewSwing extends ViewPojo {

    public ViewSwing() {
        super();
    }

    public ViewSwing(BindingDescription bindingDescription) {
        super(bindingDescription);
    }

    public JComponent getComponent() {
        return (JComponent) getBindingDescription().getView();
    }

    @Override
    public boolean isEnabled() {
        return getComponent().isEnabled();
    }

    @Override
    public boolean isVisible() {
        return getComponent().isVisible();
    }

    @Override
    public void setEnabled(boolean value) {
        getComponent().setEnabled(value);
    }

    @Override
    public void setVisible(boolean value) {
        getComponent().setVisible(value);
    }
}
