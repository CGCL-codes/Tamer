package wheel.components;

import java.util.LinkedList;
import java.util.List;

/**
 * A form element that makes it possible to choose a single object or multiple objects (using multiselect mode) from a collection of objects using the &lt;select&gt; element.
 * This component can be build either using an ISelectModel, DynamicSelectModel (by providing el-expressions for the collection, label and value) or
 * a custom IEnhancingSelectModel that lets you customize how the options are rendered.
 *
 * @author Henri Frilund
 */
public class Select extends SelectModelFormElement {

    private boolean multiSelect;

    Select(Component parent, String elementName, String componentId, ISelectModel model, CharSequence binding) {
        super(parent, elementName, componentId, model, binding);
    }

    Select(Component parent, String elementName, String componentId, CharSequence binding, ISelectModel model, boolean multiSelect) {
        super(parent, elementName, componentId, model, binding);
        this.multiSelect = multiSelect;
    }

    public final String defaultTagName() {
        return "select";
    }

    @Override
    public void _setSubmitValue(String[] formValues) {
        this.value = formValues;
        validate();
        if (isValid()) {
            if (getBinding() instanceof ElExpression) {
                ElExpression el = (ElExpression) getBinding();
                if (!multiSelect) el.store(_getTopLevelComponent(true), this, model.translateValue(formValues[0])); else {
                    List boundObjects = new LinkedList();
                    for (int i = 0; i < formValues.length; i++) {
                        String formValue = formValues[i];
                        boundObjects.add(model.translateValue(formValue));
                    }
                    el.store(_getTopLevelComponent(true), this, boundObjects);
                }
            }
        }
    }

    public void buildComponent() {
        _getChildren().clear();
        if (multiSelect) attribute("multiple", "multiple");
        Object boundObject = getBinding();
        if (getBinding() instanceof ElExpression) {
            ElExpression el = (ElExpression) getBinding();
            el.errorMessage("Couldn't read value with binding " + getBinding() + " in class " + _getTopLevelComponent(true).getClass().getName());
            boundObject = el.eval(_getTopLevelComponent(true), this);
        }
        for (int i = 0; i < model.getOptionCount(); i++) {
            Label option = create().wLabel("option", model.getLabel(i));
            Object value = model.translateValue(model.getValue(i));
            boolean selected = false;
            if (value.equals(boundObject)) {
                selected = true;
                option.attribute("selected", "true");
            }
            option.attribute("value", model.getValue(i));
            enhance(option, selected, i);
            add(option);
        }
    }

    protected String defaultDomEvent() {
        return "onselect";
    }
}
