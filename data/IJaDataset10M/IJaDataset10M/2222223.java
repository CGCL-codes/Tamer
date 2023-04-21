package com.doculibre.intelligid.wicket.components.form.multiModes;

import wicket.Component;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.TextField;
import wicket.model.IModel;

/**
 * 
 * @author francisbaril
 *
 */
@SuppressWarnings("serial")
public class MultiModesTextFieldPanel extends AbstractMultiModesFormComponentPanel {

    private static final String COMPONENT_ID = "component";

    public MultiModesTextFieldPanel(String id, IModel valueModel, boolean isEdition) {
        super(id, valueModel, isEdition);
        applyMaxLength(getMaxLength());
    }

    public MultiModesTextFieldPanel(String id, IModel valueModel, boolean isEdition, boolean isEnabled) {
        super(id, valueModel, isEdition, isEnabled);
        applyMaxLength(getMaxLength());
    }

    public MultiModesTextFieldPanel(String id, IModel valueModel, ModeFormulaire mode) {
        super(id, valueModel, mode);
        applyMaxLength(getMaxLength());
    }

    public MultiModesTextFieldPanel(String id, IModel valueModel, ModeFormulaire mode, boolean required, String label) {
        super(id, valueModel, mode);
        setRequiredIfEnabled(required);
        setLabel(label);
        applyMaxLength(getMaxLength());
    }

    public MultiModesTextFieldPanel(String id, IModel valueModel, ModeFormulaire mode, boolean isEnabled) {
        super(id, valueModel, mode, isEnabled);
        applyMaxLength(getMaxLength());
    }

    public MultiModesTextFieldPanel(String id, IModel valueModel, ModeFormulaire mode, boolean isEnabled, boolean required, String label) {
        super(id, valueModel, mode, isEnabled);
        setRequiredIfEnabled(required);
        setLabel(label);
        applyMaxLength(getMaxLength());
    }

    protected int getMaxLength() {
        return 255;
    }

    @Override
    protected Component newEditionComponent(WebMarkupContainer componentHolder, IModel valueModel) {
        return new TextField(COMPONENT_ID, valueModel);
    }

    public MultiModesTextFieldPanel applyMaxLength(int maxLength) {
        TextField textField = (TextField) getEditionComponent();
        if (textField != null) {
            textField.add(new SimpleAttributeModifier("maxlength", "" + maxLength));
        }
        return this;
    }

    public MultiModesTextFieldPanel applySize(int size) {
        TextField textField = (TextField) getEditionComponent();
        if (textField != null) {
            textField.add(new SimpleAttributeModifier("size", "" + size));
        }
        return this;
    }
}
