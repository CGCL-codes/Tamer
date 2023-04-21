package org.progeeks.meta.echo2.editor;

import org.progeeks.meta.echo2.MetaPropertyEditor;
import org.progeeks.meta.echo2.FactoryRegistry;
import org.progeeks.meta.*;
import org.progeeks.util.WeakPropertyChangeListener;
import org.progeeks.util.beans.BeanChangeSupport;
import nextapp.echo2.app.*;
import nextapp.echo2.app.event.*;
import nextapp.echo2.app.layout.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 *  A dialog the can interact with a PropertyEditor to
 *  create or edit a specific type of object.  This dialog
 *  does not need an actual PropertyMutator to do its job.
 *
 *  @version   $Revision: 1.1 $
 *  @author    J. Dave Sheremata
 */
public class EditorDialog extends WindowPane {

    private WindowPane mainPane;

    private Grid metaObjectGrid;

    private Button okButton;

    private Button cancelButton;

    private MetaPropertyEditor editor;

    private EditorDialog.PropertyMutatorAdapter adapter;

    private boolean canceled = false;

    private boolean changed = false;

    private Object originalValue = null;

    private Object value;

    private boolean directEdit = false;

    private EditorDialog.MetaObjectListener objectListener = new EditorDialog.MetaObjectListener();

    public EditorDialog(boolean modal) {
        buildContents();
    }

    public EditorDialog() {
        super("Edit Value", null, null);
        buildContents();
    }

    protected void buildContents() {
        mainPane = this;
        Column column = new Column();
        add(column);
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        add(splitPane);
        metaObjectGrid = new Grid();
        splitPane.add(metaObjectGrid);
        Row controlRow = new Row();
        splitPane.add(controlRow);
        okButton = new Button("OK");
        cancelButton = new Button("OK");
        EditorDialog.ButtonListener buttonListener = new ButtonListener();
        okButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);
        controlRow.add(okButton);
        controlRow.add(cancelButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected void close(boolean saveData) {
    }

    public void setOriginalValue(Object val) {
        this.originalValue = val;
    }

    public Object getOriginalValue() {
        return (originalValue);
    }

    /**
     *  Sets whether or not this dialog directly edits
     *  a live object.  When true, this dialog will not have
     *  a cancel button.  This value defaults to false.
     */
    public void setDirectEdit(boolean b) {
        directEdit = b;
        cancelButton.setVisible(!directEdit);
    }

    public boolean isDirectEdit() {
        return (directEdit);
    }

    public boolean isCanceled() {
        return (canceled);
    }

    public boolean hasChanged() {
        return (changed);
    }

    public Object getValue() {
        return (value);
    }

    public void popupEdit(String title, String prompt, MetaObject value, FactoryRegistry factories, Component relativeTo) {
        popupEdit(title, prompt, value, new MetaClassPropertyType(value.getMetaClass()), factories, relativeTo);
    }

    public void popupEdit(String title, String prompt, Object value, PropertyType type, FactoryRegistry factories) {
        popupEdit(title, prompt, value, type, factories, null);
    }

    public void popupEdit(String title, String prompt, Object value, PropertyType type, FactoryRegistry factories, Component relativeTo) {
        setEditParameters(title, prompt, value, type, factories);
        if (relativeTo != null) {
        }
        show();
    }

    public void setEditParameters(String title, String prompt, MetaObject value, FactoryRegistry factories) {
        setEditParameters(title, prompt, value, new MetaClassPropertyType(value.getMetaClass()), factories);
    }

    public void setEditParameters(String title, String prompt, Object value, PropertyType type, FactoryRegistry factories) {
        setTitle(title);
        this.originalValue = value;
        this.value = value;
        if (metaObjectGrid != null) metaObjectGrid.removeAll();
        editor = factories.createPropertyEditor(type);
        adapter = new EditorDialog.PropertyMutatorAdapter(value, "popupEdit", prompt, type);
        editor.setPropertyMutator(adapter);
        if (value instanceof MetaObject) {
            ((MetaObject) value).addPropertyChangeListener(new WeakPropertyChangeListener(objectListener));
        }
        Column metaObjectColumn = new Column();
        GridLayoutData gridLayoutData = new GridLayoutData();
        gridLayoutData.setColumnSpan(2);
        metaObjectColumn.setLayoutData(gridLayoutData);
        metaObjectGrid.add(metaObjectColumn);
        if (editor.isSingleColumn() && !editor.isLabeled()) {
            metaObjectGrid.add(new Label(prompt + ":"));
        }
        Component c = editor.getUIComponent();
        metaObjectGrid.add(c);
    }

    public void show() {
        super.setVisible(true);
    }

    public void hide() {
        super.setVisible(false);
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == okButton) {
                editor.flushEdits();
                editor.release();
                canceled = false;
                hide();
            } else if (event.getSource() == cancelButton) {
                editor.release();
                canceled = true;
                changed = false;
                value = originalValue;
                hide();
            }
        }
    }

    private class PropertyMutatorAdapter extends BeanChangeSupport implements PropertyMutator {

        private PropertyInfo info;

        public PropertyMutatorAdapter(Object value, String propName, String name, PropertyType type) {
            info = new PropertyInfo(propName, name, type);
        }

        public String getPropertyName() {
            return (info.getPropertyName());
        }

        public MetaObject getParentObject() {
            return (null);
        }

        public PropertyInfo getPropertyInfo() {
            return (info);
        }

        public Object getValue() {
            return (value);
        }

        public void setValue(Object value) {
            changed = true;
            Object old = EditorDialog.this.value;
            EditorDialog.this.value = value;
            firePropertyChange(info.getPropertyName(), old, value);
        }
    }

    /**
     *  Listens to edited MetaObjects to detect changes.
     */
    private class MetaObjectListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent event) {
            changed = true;
        }
    }
}
