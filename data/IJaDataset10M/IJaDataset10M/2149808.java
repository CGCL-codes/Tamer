package pl.xperios.rdk.client.widgets.Panels;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import pl.xperios.rdk.client.widgets.Fields.XField;
import pl.xperios.rdk.shared.validators.ValidatorCondition;

/**
 *
 * @author Praca
 */
public class XLabeledPanel extends LayoutContainer {

    /**
     *
     */
    public XLabeledPanel() {
        super(new FlowLayout());
    }

    @Override
    protected boolean add(Component component) {
        XField field = (XField) component;
        return add(field, field.getFieldWidth());
    }

    public boolean add(XField field, int width) {
        field.setFieldWidth(width);
        String fieldLabel = ((Field) field).getFieldLabel();
        if (fieldLabel == null || fieldLabel.equals("")) {
            return add((Field) field);
        }
        LayoutContainer wrapper = new LayoutContainer();
        Label label = new Label(fieldLabel + ":");
        FlowData style;
        if (field.getValidationConditionsCount() > 0) {
            if (field.containsCondition(ValidatorCondition.ValidIsNotNull)) {
                wrapper.setStyleAttribute("color", "red");
                wrapper.setStyleAttribute("font-weight", "bold");
                label.setToolTip("Pole obowiązkowe");
            } else {
                wrapper.setStyleAttribute("color", "blue");
                label.setToolTip("Pole walidowane");
            }
            style = new FlowData(0, 25, 5, 0);
        } else {
            style = new FlowData(0, 8, 5, 0);
        }
        label.setStyleAttribute("float", "left");
        wrapper.add(label, new FlowData(3, 2, 0, 0));
        ((Field) field).setStyleAttribute("float", "left");
        wrapper.add(((Field) field));
        wrapper.setStyleAttribute("float", "left");
        return super.add(wrapper, style);
    }
}
