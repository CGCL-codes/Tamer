package co.fxl.gui.filter.impl;

import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.form.impl.Validation;

class StringFilter extends FilterTemplate<String> {

    class StringPrefixConstraint implements IStringPrefixConstraint {

        @Override
        public String column() {
            return name;
        }

        @Override
        public String prefix() {
            return text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    ITextField textField;

    private String text;

    StringFilter(FilterGrid panel, String name, int filterIndex) {
        super(panel, name, filterIndex);
        ICell width = panel.cell(filterIndex);
        textField = textField(width, filterIndex).width(width());
        panel.heights().decorate(textField);
        panel.register(textField);
    }

    ITextField textField(ICell c, int filterIndex) {
        return c.textField();
    }

    int width() {
        return WIDTH_SINGLE_CELL;
    }

    @Override
    public boolean update() {
        text = textField.text().trim();
        if (text.equals("")) text = null;
        return text != null;
    }

    @Override
    public void clear() {
        textField.text("");
        text = null;
    }

    @Override
    public boolean applies(String value) {
        return value.startsWith(text);
    }

    @Override
    public void addUpdateListener(final FilterListener l) {
        textField.addUpdateListener(new IUpdateListener<String>() {

            @Override
            public void onUpdate(String value) {
                l.onActive(!textField.text().trim().equals(""));
            }
        });
    }

    @Override
    public IFilterConstraint asConstraint() {
        update();
        return new StringPrefixConstraint();
    }

    @Override
    public void validate(Validation validation) {
        validation.linkInput(textField);
    }

    @Override
    boolean fromConstraint(IFilterConstraints constraints) {
        if (constraints.isAttributeConstrained(name)) {
            String prefix = constraints.stringValue(name);
            textField.text(prefix);
            return true;
        } else return false;
    }

    @Override
    public IUpdateable<String> addUpdateListener(co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
        throw new UnsupportedOperationException();
    }
}
