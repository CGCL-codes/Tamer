package org.viewaframework.swing.binding;

import javax.swing.JLabel;
import org.viewaframework.swing.binding.core.AbstractSelectionBinding;
import org.viewaframework.swing.binding.core.BasicBeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapterPropertyListener;
import org.viewaframework.swing.binding.core.Property;

/**
 * @author Mario Garcia
 *
 * @param <SS>
 * @param <SP>
 * @param <TS>
 * @param <TP>
 */
public class LabelBinding<SS extends JLabel, SP, TS, TP> extends AbstractSelectionBinding<SS, SP, TS, TP> {

    /**
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 */
    public LabelBinding(SS source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        super(new BasicBeanAdapter<SS>(source), sourceProperty, target, targetProperty);
    }

    public void bind() {
        this.setSourceListener(new BeanAdapterPropertyListener<TS, TP, SP>(this.getTarget(), this.getTargetProperty(), this.getSourceProperty()));
        this.setTargetListener(new BeanAdapterPropertyListener<SS, SP, TP>(this.getSource(), this.getSourceProperty(), this.getTargetProperty()));
        this.getSource().getSource().addPropertyChangeListener(getSourceListener());
        this.getTarget().addPropertyChangeListener(getTargetListener());
        this.getTarget().setSource(this.getTarget().getSource());
    }

    public void unbind() {
        this.getSource().getSource().removePropertyChangeListener(getSourceListener());
        this.getTarget().removePropertyChangeListener(getTargetListener());
    }
}
