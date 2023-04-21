package org.viewaframework.swing.binding;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.viewaframework.swing.binding.collection.EventList;
import org.viewaframework.swing.binding.core.BeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapterBinding;
import org.viewaframework.swing.binding.core.Binding;
import org.viewaframework.swing.binding.core.Property;
import org.viewaframework.swing.binding.table.ColumnInfo;

/**
 * @author Mario Garcia
 * 
 * This class acts as a binding group. The user can chain as many bindings as he want before binding them all
 * at once.
 * 
 * This way a chain of bindings can be created this way:
 * 
 *
 */
public class SwingBinding implements Binding<Object> {

    private List<Binding<?>> bindingList = new ArrayList<Binding<?>>();

    public void bind() {
        for (Binding<?> binding : this.bindingList) {
            binding.bind();
        }
    }

    /**
	 * @param <SS>
	 * @param <SP>
	 * @param <TS>
	 * @param <TP>
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 * @return
	 */
    public <SS, SP, TS, TP> SwingBinding createBeanAdapterBinding(BeanAdapter<SS> source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        BeanAdapterBinding<SS, SP, TS, TP> binding = new BeanAdapterBinding<SS, SP, TS, TP>(source, sourceProperty, target, targetProperty);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <SS>
	 * @param <SP>
	 * @param <TS>
	 * @param <TP>
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 * @return
	 */
    public <SS, SP, TS, TP> SwingBinding createBeanAdapterBinding(SS source, Property<SP> sourceProperty, TS target, Property<TP> targetProperty) {
        BeanAdapterBinding<SS, SP, TS, TP> binding = new BeanAdapterBinding<SS, SP, TS, TP>(source, sourceProperty, target, targetProperty);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <SP>
	 * @param <TS>
	 * @param <TP>
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 * @return
	 */
    public <SP, TS, TP> SwingBinding createButtonBinding(JButton source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        ButtonBinding<JButton, SP, TS, TP> binding = new ButtonBinding<JButton, SP, TS, TP>(source, sourceProperty, target, targetProperty);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param list
	 * @return
	 */
    public <TS> SwingBinding createComboBoxListBinding(JComboBox source, EventList<TS> list) {
        ComboBoxListBinding<TS> binding = new ComboBoxListBinding<TS>(source, list);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param target
	 * @return
	 */
    public <TS> SwingBinding createComboBoxSelectionBinding(JComboBox source, BeanAdapter<TS> target) {
        ComboBoxSelectionBinding<JComboBox, TS> binding = new ComboBoxSelectionBinding<JComboBox, TS>(source, target);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <SP>
	 * @param <TS>
	 * @param <TP>
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 * @return
	 */
    public <SP, TS, TP> SwingBinding createLabelBinding(JLabel source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        LabelBinding<JLabel, SP, TS, TP> binding = new LabelBinding<JLabel, SP, TS, TP>(source, sourceProperty, target, targetProperty);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param list
	 * @return
	 */
    public <TS> SwingBinding createListBinding(JList source, EventList<TS> list) {
        ListBinding<TS> binding = new ListBinding<TS>(source, list);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param target
	 * @return
	 */
    public <TS> SwingBinding createListSelectionBinding(JList source, BeanAdapter<TS> target) {
        ListSelectionBinding<JList, TS> binding = new ListSelectionBinding<JList, TS>(source, target);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param list
	 * @param tableColumns
	 * @return
	 */
    public <TS> SwingBinding createTableListBinding(JTable source, EventList<TS> list, List<ColumnInfo> tableColumns) {
        TableListBinding<TS> binding = new TableListBinding<TS>(source, list, tableColumns);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <TS>
	 * @param source
	 * @param target
	 * @return
	 */
    public <TS> SwingBinding createTableSelectionBinding(JTable source, BeanAdapter<TS> target) {
        TableSelectionBinding<JTable, TS> binding = new TableSelectionBinding<JTable, TS>(source, target);
        this.bindingList.add(binding);
        return this;
    }

    /**
	 * @param <SP>
	 * @param <TS>
	 * @param <TP>
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 * @return
	 */
    public <SP, TS, TP> SwingBinding createTextFieldBinding(JTextField source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        TextFieldBinding<JTextField, SP, TS, TP> binding = new TextFieldBinding<JTextField, SP, TS, TP>(source, sourceProperty, target, targetProperty);
        this.bindingList.add(binding);
        return this;
    }

    public void unbind() {
        for (Binding<?> binding : this.bindingList) {
            binding.unbind();
        }
    }
}
