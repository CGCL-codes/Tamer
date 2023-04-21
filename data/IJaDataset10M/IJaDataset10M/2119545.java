package com.appropel.kaylee.binding;

import com.appropel.kaylee.util.SwingComponentFinder;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Component;
import java.awt.Container;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import org.apache.log4j.Logger;

/**
 * Encapsulates the information about a particular binding.
 *
 * @author Kevin A. Roll
 */
public final class BindingInfo {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(BindingInfo.class);

    /** Business object which is being bound to. */
    private Object object;

    /** Name of property. */
    private String propertyName;

    /** Form that holds Swing components. */
    private Container form;

    /** Individual component being bound. */
    private Component component;

    /** JGoodies presentation model for entire bean. */
    private PresentationModel presentationModel;

    /** JGoodies value model for particular property. */
    private ValueModel valueModel;

    /**
     * Creates a new BindingInfo.
     *
     * @param object object that is being bound to.
     * @param propertyName name of property.
     * @param form form that holds Swing Components.
     * @param presentationModel JGoodies presentation model.
     */
    public BindingInfo(final Object object, final String propertyName, final Container form, final PresentationModel presentationModel) {
        this.object = object;
        this.propertyName = propertyName;
        this.form = form;
        this.component = SwingComponentFinder.locateComponent(form, propertyName);
        if (component == null) {
            throw new RuntimeException("No component with name '" + propertyName + "' found on form!");
        }
        this.presentationModel = presentationModel;
    }

    public Component getComponent() {
        return component;
    }

    public Component getForm() {
        return form;
    }

    public Object getObject() {
        return object;
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns an appropriate PresentationModel.
     *
     * @return A PresentationModel.
     */
    public PresentationModel getPresentationModel() {
        return presentationModel;
    }

    /**
     * Returns an appropriate ValueModel.
     *
     * @return A ValueModel.
     */
    public ValueModel getValueModel() {
        if (valueModel == null) {
            valueModel = getPresentationModel().getModel(propertyName);
        }
        return valueModel;
    }

    /**
     * Returns an appropriate PropertyDescriptor.
     *
     * @return A PropertyDescriptor.
     */
    public PropertyDescriptor getPropertyDescriptor() {
        try {
            return new PropertyDescriptor(propertyName, object.getClass());
        } catch (final IntrospectionException ex) {
            LOGGER.error("Unable to obtain property descriptor for " + propertyName, ex);
            return null;
        }
    }
}
