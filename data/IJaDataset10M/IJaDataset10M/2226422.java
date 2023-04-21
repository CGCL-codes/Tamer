package org.viewaframework.swing.binding.core;

/**
 * @author Mario Garcia
 *
 * @param <SS>
 * @param <SP>
 * @param <TS>
 * @param <TP>
 */
public interface SelectionBinding<SS, SP, TS, TP> extends Binding<SS> {

    /**
	 * @return
	 */
    public BeanAdapter<SS> getSource();

    /**
	 * @return
	 */
    public Property<SP> getSourceProperty();

    /**
	 * @return
	 */
    public BeanAdapter<TS> getTarget();

    /**
	 * @return
	 */
    public Property<TP> getTargetProperty();

    /**
	 * @param source
	 */
    public void setSource(BeanAdapter<SS> source);

    /**
	 * @param sourceProperty
	 */
    public void setSourceProperty(Property<SP> sourceProperty);

    /**
	 * @param target
	 */
    public void setTarget(BeanAdapter<TS> target);

    /**
	 * @param targetProperty
	 */
    public void setTargetProperty(Property<TP> targetProperty);
}
