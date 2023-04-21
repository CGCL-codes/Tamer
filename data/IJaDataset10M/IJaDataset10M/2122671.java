package net.sf.beanform;

import java.util.Iterator;
import java.util.Map;
import net.sf.beanform.prop.BeanProperty;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.form.IFormComponent;

/**
 * A low level BeanForm component that renders labels for all the properties.
 * This component must be wrapped by a {@link BeanFormRows} component.
 *
 * @author Daniel Gredler
 */
public abstract class BeanFormLabel extends BeanFormRowComponent {

    static final String PROPERTYSELECTION_BLOCK_ID = "propertySelectionBlock";

    static final String CUSTOM_BLOCK_ID = "customBlock";

    static final String TEXTFIELD_BLOCK_ID = "textFieldBlock";

    static final String TEXTAREA_BLOCK_ID = "textAreaBlock";

    static final String CHECKBOX_BLOCK_ID = "checkboxBlock";

    static final String DATEPICKER_BLOCK_ID = "datePickerBlock";

    static final String UPLOAD_BLOCK_ID = "uploadBlock";

    static final String INSERT_BLOCK_ID = "insertBlock";

    public IComponent getLabelBlock() {
        BeanProperty property = this.getProperty();
        if (this.hasPropertySelectionModel(property, true)) return this.getComponent(PROPERTYSELECTION_BLOCK_ID); else if (this.hasCustomField(property)) return this.getComponent(CUSTOM_BLOCK_ID); else if (property.usesTextField()) return this.getComponent(TEXTFIELD_BLOCK_ID); else if (property.usesTextArea()) return this.getComponent(TEXTAREA_BLOCK_ID); else if (property.usesCheckbox()) return this.getComponent(CHECKBOX_BLOCK_ID); else if (property.usesDatePicker()) return this.getComponent(DATEPICKER_BLOCK_ID); else if (property.usesUpload()) return this.getComponent(UPLOAD_BLOCK_ID); else if (property.usesInsert()) return this.getComponent(INSERT_BLOCK_ID); else {
            String msg = BeanFormMessages.cantFindFieldForProperty(property);
            throw new ApplicationRuntimeException(msg);
        }
    }

    public IFormComponent getPropertySelection() {
        return this.getField(BeanFormField.PROPERTYSELECTION_FIELD_ID);
    }

    public IFormComponent getTextField() {
        return this.getField(BeanFormField.TEXTFIELD_FIELD_ID);
    }

    public IFormComponent getTextArea() {
        return this.getField(BeanFormField.TEXTAREA_FIELD_ID);
    }

    public IFormComponent getCheckbox() {
        return this.getField(BeanFormField.CHECKBOX_FIELD_ID);
    }

    public IFormComponent getDatePicker() {
        return this.getField(BeanFormField.DATEPICKER_FIELD_ID);
    }

    public IFormComponent getUpload() {
        return this.getField(BeanFormField.UPLOAD_FIELD_ID);
    }

    /**
     * Called whenever a label needs its field. This method expects the label's "prerender"
     * attribute to be set to <tt>true</tt> (the default), as it adds any extra user-specified
     * informal bindings to the field before returning it, in anticipation of a prerender.
     */
    private IFormComponent getField(String id) {
        IFormComponent field = null;
        Map siblings = this.getContainer().getComponents();
        for (Iterator i = siblings.values().iterator(); i.hasNext(); ) {
            IComponent sibling = (IComponent) i.next();
            if (sibling instanceof BeanFormField) {
                field = (IFormComponent) sibling.getComponent(id);
                this.addExtraBindings(field);
                break;
            }
        }
        return field;
    }
}
