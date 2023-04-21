package co.fxl.gui.form.impl;

import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.IFormField;

class FormTextFieldImpl<R> extends FormFieldImpl<ITextField, R> {

    ITextField textField;

    FormTextFieldImpl(FormWidgetImpl widget, int index, String name) {
        super(widget, index, name);
    }

    @Override
    public IFormField<ITextField, R> editable(boolean editable) {
        valueElement().editable(editable);
        return super.editable(editable);
    }

    ITextField addTextField(FormWidgetImpl widget, int index) {
        return widget.addFormValueTextField(index, withFocus());
    }

    boolean withFocus() {
        return true;
    }

    @Override
    public ITextField valueElement() {
        return textField;
    }

    @Override
    void createContentColumn(int index) {
        textField = addTextField(widget, index);
        editable(widget.saveListener != null);
    }
}
