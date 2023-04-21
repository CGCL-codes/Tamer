package org.viewaframework.swing.binding.combo;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import org.viewaframework.swing.binding.collection.EventList;
import org.viewaframework.swing.binding.core.AbstractSelectionBinding;
import org.viewaframework.swing.binding.core.BasicBeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapter;
import org.viewaframework.swing.binding.core.Property;

/**
 * @author Mario Garcia
 * 
 * @param <U>
 * @param <SS>
 * @param <SP>
 * @param <TS>
 * @param <TP>
 */
public class ComboBoxSelectionPropertyBinding<SS extends JComboBox, SP, TS, TP> extends AbstractSelectionBinding<SS, SP, TS, TP> {

    private ComboBoxBindingModel<TS> comboModel;

    private EventList<TS> modelList;

    /**
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 */
    public ComboBoxSelectionPropertyBinding(SS source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        super(new BasicBeanAdapter<SS>(source), sourceProperty, target, targetProperty);
        this.setSourceListener(new ComboBoxSelectionBindListener<TS, TP, SP>(this.getTarget(), this.getTargetProperty(), this.getSourceProperty()));
    }

    public void bind() {
        this.comboModel = getWorkingComboBoxAdapter(this.getSource().getSource(), modelList);
        this.getSource().getSource().setModel(comboModel);
        this.comboModel.addPropertyChangeListener(this.getSourceListener());
    }

    /**
	 * @param combo
	 * @param list
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private ComboBoxBindingModel<TS> getWorkingComboBoxAdapter(JComboBox combo, EventList<TS> list) {
        ComboBoxModel model = combo.getModel();
        ComboBoxBindingModel<TS> adapter = null;
        if (!(model instanceof ComboBoxBindingModel)) {
            adapter = new ComboBoxBindingModel<TS>();
        } else {
            adapter = (ComboBoxBindingModel) model;
        }
        if (list != null) {
            adapter.setModelList(list);
        }
        return adapter;
    }

    public void unbind() {
        this.comboModel.removePropertyChangeListener(getSourceListener());
    }
}
