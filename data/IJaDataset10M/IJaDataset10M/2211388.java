package ca.petersens.gwt.databinding.client.impl;

import ca.petersens.gwt.databinding.client.BoundField;
import ca.petersens.gwt.databinding.client.Editor;
import ca.petersens.gwt.databinding.client.EditorChangeEvent;
import ca.petersens.gwt.databinding.client.EditorChangeListener;
import ca.petersens.gwt.databinding.client.Field;
import ca.petersens.gwt.databinding.client.SourcesPropertyChangeEvents;
import java.beans.PropertyChangeListener;

final class BoundFieldImpl<B, P> extends BoundFormulaImpl<B, P> implements BoundField<B, P>, PropertyChangeListener, BoundFormMember<B> {

    private final class Listener implements EditorChangeListener<P> {

        public void valueChanged(EditorChangeEvent<? extends P> event) {
            B bean = getForm().getBean();
            if (bean == null) {
                assert editor == event.getEditor();
                editor.display(null);
                return;
            }
            detachChangeListeners(bean);
            boolean ignoringBean = ignoreBean;
            ignoreBean = true;
            try {
                refreshBean();
            } finally {
                ignoreBean = ignoringBean;
                attachChangeListeners(getForm().getBean());
            }
        }
    }

    private Editor<P> editor;

    private final Field<B, P> field;

    private boolean ignoreBean;

    public BoundFieldImpl(BoundFormImpl<B> form, Field<B, P> field) {
        super(form, field);
        this.field = field;
    }

    public Editor<P> getEditor() {
        if (editor == null) {
            editor = field.constructEditor();
            editor.addChangeListener(new Listener());
        }
        return editor;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public void refreshBean() {
        assert getForm().getBean() != null;
        if (editor != null) {
            setValue(editor.readValue());
        }
    }

    @Override
    public void refreshWidgets() {
        super.refreshWidgets();
        if (editor != null) {
            editor.display(getValue());
        }
    }

    public void setValue(P value) {
        field.setValue(getForm().getBean(), value);
    }

    private void attachChangeListeners(B bean) {
        if (bean instanceof SourcesPropertyChangeEvents) {
            attachTo((SourcesPropertyChangeEvents) bean);
        }
    }

    private void detachChangeListeners(B bean) {
        if (bean instanceof SourcesPropertyChangeEvents) {
            detachFrom((SourcesPropertyChangeEvents) bean);
        }
    }

    @Override
    protected void attachTo(SourcesPropertyChangeEvents bean) {
        if (ignoreBean) {
            return;
        }
        super.attachTo(bean);
    }
}
