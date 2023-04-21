package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <pre>
 * Option controls can have group and value attribute.
 * If the value is not null, it is assumed, that there are other options with ALL other possible 
 * values for the same property!
 * if value attribute is not set, it is assumed, that the property is of type boolean.
 * </pre>
 * 
 * @author Arno Schatz
 * 
 */
public abstract class OptionControl extends FormControl implements IOptionControl {

    protected String label;

    protected boolean managedByGroup = false;

    /**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
    public OptionControl() {
        super();
    }

    /**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id
	 *            unique HTML id
	 */
    public OptionControl(String id) {
        super(id);
    }

    protected String optionValue;

    public void loadFrom(Object data) {
        if (managedByGroup) return;
        boolean val;
        if (data == null) {
            val = false;
        } else {
            Object fromModel = PropertyAccessor.retrieveProperty(data, getProperty());
            if (fromModel == null) {
                val = false;
            } else {
                if (getOptionValue() != null) {
                    val = getOptionValue().equals(fromModel.toString());
                } else {
                    val = ((Boolean) fromModel).booleanValue();
                }
            }
        }
        setSelected(val);
    }

    /**
	 * set the state of this radio
	 */
    public void setSelected(boolean newState) {
        getContext().add(getId(), Boolean.toString(newState), IContext.TYPE_SEL, IContext.STATUS_EDITABLE);
    }

    /**
	 * read the state of the radio, where no state is like turned off
	 * 
	 * @return true if radio is selected
	 */
    public boolean isSelected() {
        return getContext().getValueAsBool(getId(), false);
    }

    public void saveTo(Object data) {
        if (managedByGroup) return;
        try {
            if (getOptionValue() != null) {
                if (isSelected()) {
                    PropertyAccessor.storeProperty(data, getProperty(), getOptionValue());
                }
            } else {
                PropertyAccessor.storeProperty(data, getProperty(), Boolean.valueOf(isSelected()));
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    protected void init() {
        if (getContext().getValue(getId()) == null) setSelected(false);
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optValue) {
        this.optionValue = optValue;
    }

    protected void makeLabel(PrintWriter out) {
        if (StringUtils.isNotBlank(getLabel())) {
            Span span = new Span();
            span.setClass("wgtLabel");
            span.setID(getId() + "_label");
            span.addElement(getLabel());
            span.output(out);
        }
    }

    public void setVisible(boolean vis) {
        super.setVisible(vis);
        if (StringUtils.isNotBlank(getLabel())) getContext().makeVisible(getId() + "_label", vis);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        if (isDrawn()) getContext().innerHtml(getId() + "_label", label);
    }

    public void setLabelKey(String labelKey) {
        setLabel(TextService.getString(labelKey));
    }

    /**
	 * @param managedByGroup true if a OptionControlGroup is responsible for handling save and load functions
	 */
    public void setManagedByGroup(boolean managedByGroup) {
        this.managedByGroup = managedByGroup;
    }
}
