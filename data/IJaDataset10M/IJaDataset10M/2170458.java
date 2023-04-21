package org.objectstyle.jstaple.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.objectstyle.jstaple.STException;
import org.objectstyle.jstaple.bind.STBinding;
import org.objectstyle.jstaple.bind.STTableBindings;
import org.objectstyle.jstaple.spi.STBindingFactory;

/**
 * @author Andrei Adamchik
 */
public class TableBindingSet extends GenericBindingSet implements STTableBindings {

    public TableBindingSet(Object root, STBindingFactory bindingFactory) {
        super(root, bindingFactory);
    }

    public void bind(Object view, PropertyChangeSupport modelEvents, String bindingName, String bindingExpression, int bindingType) {
        if (!(view instanceof JTable)) {
            throw new STException("Only JTable is supported, attempt to bind unsupported view: " + view);
        }
        JTable table = (JTable) view;
        TableModel originalModel = table.getModel();
        if (!(originalModel instanceof BindableTableModel)) {
            table.setModel(new BindableTableModel(this));
        }
        BindableTableModel model = (BindableTableModel) table.getModel();
        if (model.getParentBindingSet() != this) {
            model.setParentBindingSet(this);
        }
        createBinding(bindingName, bindingExpression);
        if (LIST_BINDING.equals(bindingName)) {
            bindList(model, modelEvents, bindingType);
        } else if (HEADERS_BINDING.equals(bindingName)) {
            bindHeaders(model, modelEvents, bindingType);
        } else if (LABELS_BINDING.equals(bindingName)) {
        } else {
            bindProperty(view, modelEvents, bindingName, bindingType);
        }
    }

    protected void bindList(final BindableTableModel model, PropertyChangeSupport modelEvents, int bindingType) {
        final STBinding binding = getBinding(LIST_BINDING);
        if (bindingType == PUSH_TO_MODEL_BINDING || bindingType == TWO_WAY_SYNC_BINDING) {
        }
        if (bindingType == PULL_FROM_MODEL_BINDING || bindingType == TWO_WAY_SYNC_BINDING) {
            PropertyChangeListener listener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent e) {
                    listPullFromModel(binding, model);
                }
            };
            modelEvents.addPropertyChangeListener(binding.getSpecification(), listener);
            getEventsSupport().addPropertyChangeListener(listener);
        }
    }

    protected void bindHeaders(final BindableTableModel model, PropertyChangeSupport modelEvents, int bindingType) {
        final STBinding binding = getBinding(HEADERS_BINDING);
        if (bindingType == PULL_FROM_MODEL_BINDING || bindingType == TWO_WAY_SYNC_BINDING) {
            PropertyChangeListener listener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent e) {
                    headersPullFromModel(binding, model);
                }
            };
            modelEvents.addPropertyChangeListener(binding.getSpecification(), listener);
            getEventsSupport().addPropertyChangeListener(listener);
        }
    }

    protected void listPullFromModel(STBinding binding, BindableTableModel listModel) {
        Object value = binding.getValue(getRoot());
        listModel.setList((List) value);
    }

    protected void headersPullFromModel(STBinding binding, BindableTableModel listModel) {
        List value = (List) binding.getValue(getRoot());
        listModel.setHeaders(value != null ? value.toArray() : null);
    }
}
