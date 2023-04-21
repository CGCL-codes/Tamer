package jaxlib.swing.binding;

import java.awt.Component;
import java.awt.Window;
import javax.annotation.CheckForNull;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import jaxlib.beans.Property;
import jaxlib.beans.PropertyBinding;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ComponentPropertyBinding.java 2928 2011-06-06 02:39:57Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public abstract class ComponentPropertyBinding<A, B extends Component> extends PropertyBinding<A, B> {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    protected ComponentPropertyBinding(final A leftBean, final Property<? super A, ?> leftProperty, final B component, final Property<? super B, ?> componentProperty) {
        super(leftBean, leftProperty, component, componentProperty);
    }

    protected ComponentPropertyBinding(final A leftBean, final String leftPropertyName, final B component, final String componentPropertyName) {
        super(leftBean, leftPropertyName, component, componentPropertyName);
    }

    protected <R> ComponentPropertyBinding(final A leftBean, final String leftPropertyName, final B component, final Property<? super B, ?> componentProperty) {
        super(leftBean, leftPropertyName, component, componentProperty);
    }

    boolean isDisablingComponentIfLeftNotWritable() {
        return false;
    }

    @Override
    protected void callLeftSetter(final Object v) throws Throwable {
        ComponentBindingValidator validator = getComponentBindingValidator();
        if ((validator == null) || validator.validate(this, v)) {
            validator = null;
            super.callLeftSetter(v);
        }
    }

    @Override
    protected Object convertRightToLeft(final Object rightValue) throws Exception {
        try {
            return super.convertRightToLeft(rightValue);
        } catch (final Exception ex) {
            final ComponentBindingValidator validator = getComponentBindingValidator();
            if (validator != null) validator.conversionFailed(this, rightValue, ex);
            return validator;
        }
    }

    /**
   * @since JaXLib 1.0
   */
    @CheckForNull
    public ComponentBindingValidator getComponentBindingValidator() {
        for (Component c = getRightBean(); c != null; c = c.getParent()) {
            if (c instanceof ComponentBindingValidator) {
                return (ComponentBindingValidator) c;
            } else if (c instanceof JComponent) {
                final Object v = ((JComponent) c).getClientProperty(ComponentBindingValidator.class);
                if (v instanceof ComponentBindingValidator) return (ComponentBindingValidator) v;
                if (c instanceof RootPaneContainer) return null;
            } else if ((c instanceof Window) || (c instanceof RootPaneContainer)) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void initDefaults(final PropertyBinding.UpdateMode autoUpdate) {
        final Component component = getRightBean();
        final Property<A, ?> p = getLeftProperty();
        if (isDisablingComponentIfLeftNotWritable() && !p.isWritable()) component.setEnabled(false);
        updateRight();
        setAutoUpdate(autoUpdate);
    }
}
