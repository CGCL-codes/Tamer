package co.fxl.gui.input.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.form.impl.Validation.IValidation;
import co.fxl.gui.impl.ElementPopUp;
import co.fxl.gui.input.api.IMultiComboBoxWidget;

class MultiComboBoxWidgetImpl implements IMultiComboBoxWidget, co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> {

    private List<IUpdateListener<String[]>> listeners = new LinkedList<IUpdateListener<String[]>>();

    private List<String> texts = new LinkedList<String>();

    private List<String> selection = new LinkedList<String>();

    private ITextField textField;

    private Map<String, ICheckBox> checkBoxes = new HashMap<String, ICheckBox>();

    private ElementPopUp popUp;

    MultiComboBoxWidgetImpl(IContainer container) {
        textField = container.textField().visible(false);
        ElementPopUp.HEIGHTS.decorate(textField);
        popUp = new ElementPopUp(textField).rowHeight(23);
    }

    @Override
    public void onUpdate(Boolean value) {
        if (value) {
            clearPopUp();
            createPopUp();
            popUp.visible(true);
        }
    }

    @Override
    public IUpdateable<String[]> addUpdateListener(co.fxl.gui.api.IUpdateable.IUpdateListener<String[]> listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public IMultiComboBoxWidget selection(String[] selection) {
        setSelection(selection);
        StringBuilder b = new StringBuilder();
        for (String s : selection) {
            if (b.length() > 0) b.append(";");
            b.append(s);
        }
        textField.text(b.toString());
        notifyListeners();
        return this;
    }

    private void notifyListeners() {
        for (IUpdateListener<String[]> l : listeners) l.onUpdate(selection());
    }

    private void setSelection(String[] selection) {
        this.selection = new LinkedList<String>(Arrays.asList(selection));
    }

    @Override
    public String[] selection() {
        return selection.toArray(new String[0]);
    }

    @Override
    public IMultiComboBoxWidget addText(String... texts) {
        this.texts.addAll(Arrays.asList(texts));
        popUp.lines(this.texts.size());
        return this;
    }

    private void createPopUp() {
        IVerticalPanel v = popUp.create();
        for (final String text : texts) {
            ICheckBox cb = v.add().panel().horizontal().align().begin().add().panel().horizontal().align().begin().add().checkBox().text(text);
            checkBoxes.put(text, cb);
            cb.addUpdateListener(new IUpdateListener<Boolean>() {

                @Override
                public void onUpdate(Boolean value) {
                    String t = text;
                    if (value) MultiComboBoxWidgetImpl.this.selection.add(t); else MultiComboBoxWidgetImpl.this.selection.remove(t);
                    selection(selection.toArray(new String[0]));
                }
            });
            cb.checked(selection.contains(text));
        }
    }

    private void clearPopUp() {
        popUp.clear();
    }

    @Override
    public IMultiComboBoxWidget width(int width) {
        textField.width(width);
        return this;
    }

    @Override
    public IMultiComboBoxWidget clear() {
        clearPopUp();
        texts.clear();
        selection.clear();
        selection(new String[0]);
        return this;
    }

    @Override
    public IMultiComboBoxWidget visible(boolean visible) {
        textField.visible(visible);
        textField.addFocusListener(this);
        textField.addUpdateListener(new IUpdateListener<String>() {

            @Override
            public void onUpdate(String value) {
                List<String> result = new LinkedList<String>(Arrays.asList(extract(value)));
                boolean changed = false;
                for (String t : texts) {
                    if (!result.contains(t)) {
                        selection.remove(t);
                        changed = true;
                    }
                }
                for (String t : result) {
                    if (!selection.contains(t) && texts.contains(t)) {
                        selection.add(t);
                        changed = true;
                    }
                }
                if (changed) {
                    updateCheckBoxes();
                    notifyListeners();
                }
            }
        });
        return this;
    }

    private void updateCheckBoxes() {
        for (String t : texts) {
            ICheckBox cb = checkBoxes.get(t);
            if (cb != null) cb.checked(selection.contains(t));
        }
    }

    @Override
    public IMultiComboBoxWidget validation(Validation validation) {
        validation.validate(textField, new IValidation<String>() {

            @Override
            public boolean validate(String trim) {
                String[] s = extract(trim);
                List<String> tokens = new LinkedList<String>(texts);
                for (String s0 : s) {
                    boolean r = tokens.remove(s0);
                    if (!r) return false;
                }
                return true;
            }
        });
        return this;
    }

    private String[] extract(String value) {
        return value.split(";");
    }
}
