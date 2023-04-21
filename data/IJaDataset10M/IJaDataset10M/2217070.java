package jaxlib.swing.beans.editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import jaxlib.array.ObjectArrays;
import jaxlib.util.CheckArg;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: PropertyEditorProxy.java 2271 2007-03-16 08:48:23Z joerg_wassmer $
 */
public class PropertyEditorProxy<T> extends AbstractPropertyEditor<T> implements PropertyChangeListener {

    private final PropertyEditor delegate;

    public PropertyEditorProxy(final PropertyEditor delegate) {
        super();
        this.delegate = delegate;
        delegate.addPropertyChangeListener(this);
    }

    @Override
    public String getAsText() {
        return this.delegate.getAsText();
    }

    @Override
    public Component getCustomEditor() {
        return supportsCustomEditor() ? this.delegate.getCustomEditor() : null;
    }

    @Override
    public String getJavaInitializationString() {
        return this.delegate.getJavaInitializationString();
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor) this.delegate).getListCellRenderer() : super.getListCellRenderer();
    }

    @Override
    public TableCellEditor getTableCellEditor() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor) this.delegate).getTableCellEditor() : super.getTableCellEditor();
    }

    @Override
    public TableCellRenderer getTableCellRenderer() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor) this.delegate).getTableCellRenderer() : super.getTableCellRenderer();
    }

    @Override
    public String[] getTags() {
        return this.delegate.getTags();
    }

    @Override
    public TreeCellEditor getTreeCellEditor() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor) this.delegate).getTreeCellEditor() : super.getTreeCellEditor();
    }

    @Override
    public TreeCellRenderer getTreeCellRenderer() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor) this.delegate).getTreeCellRenderer() : super.getTreeCellRenderer();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        return (this.delegate instanceof BeanPropertyEditor) ? ((BeanPropertyEditor<T>) this.delegate).getType() : (Class) Object.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) this.delegate.getValue();
    }

    @Override
    public boolean isPaintable() {
        return this.delegate.isPaintable();
    }

    @Override
    public void paintValue(final Graphics g, final Rectangle box) {
        if (isPaintable()) this.delegate.paintValue(g, box);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent e) {
        if ((e == null) || (e.getPropertyName() == null)) firePropertyChange();
    }

    @Override
    public void setAsText(final String text) {
        this.delegate.setAsText(text);
    }

    @Override
    public void setValue(final Object value) {
        this.delegate.setValue(value);
    }

    @Override
    public boolean supportsCustomEditor() {
        return this.delegate.supportsCustomEditor();
    }
}
