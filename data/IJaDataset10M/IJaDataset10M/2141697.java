package org.viewaframework.swing.binding;

import javax.swing.JTextField;
import org.viewaframework.swing.binding.core.AbstractSelectionBinding;
import org.viewaframework.swing.binding.core.BasicBeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapter;
import org.viewaframework.swing.binding.core.BeanAdapterPropertyListener;
import org.viewaframework.swing.binding.core.Property;
import org.viewaframework.swing.binding.text.TextFieldBindingListener;

/**
 * @author Mario Garcia
 *
 * @param <SS>
 * @param <SP>
 * @param <TS>
 * @param <TP>
 */
public class TextFieldBinding<SS extends JTextField, SP, TS, TP> extends AbstractSelectionBinding<SS, SP, TS, TP> {

    /**
	 * @param source
	 * @param sourceProperty
	 * @param target
	 * @param targetProperty
	 */
    public TextFieldBinding(SS source, Property<SP> sourceProperty, BeanAdapter<TS> target, Property<TP> targetProperty) {
        super(new BasicBeanAdapter<SS>(source), sourceProperty, target, targetProperty);
        this.setSourceListener(new TextFieldBindingListener<TS, TP, SP>(getTarget(), getTargetProperty(), getSourceProperty()));
        this.setTargetListener(new BeanAdapterPropertyListener<SS, SP, TP>(getSource(), getSourceProperty(), getTargetProperty()));
    }

    public void bind() {
        this.getSource().getSource().getAccessibleContext().addPropertyChangeListener(this.getSourceListener());
        this.getTarget().addPropertyChangeListener(this.getTargetListener());
        this.getTarget().setSource(this.getTarget().getSource());
    }

    public void unbind() {
        this.getSource().getSource().removePropertyChangeListener(this.getSourceListener());
        this.getTarget().removePropertyChangeListener(this.getTargetListener());
    }
}
