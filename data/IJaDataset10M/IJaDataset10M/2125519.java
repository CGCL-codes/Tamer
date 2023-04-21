package org.ndx.majick.ui.number;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.lang.ref.WeakReference;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.ndx.majick.properties.Property;
import org.ndx.majick.properties.VetoableProperty;

public class NumberBackedSpinnerModel<Type extends Number> extends SpinnerNumberModel implements SpinnerModel, PropertyChangeListener {

    private class PropertyUpdater implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if (source.get() != null) source.get().set((Type) getNumber());
        }
    }

    private class VetoablePropertyUpdater implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if (source.get() != null) {
                try {
                    ((VetoableProperty<Type>) source.get()).setVeto((Type) getNumber());
                } catch (PropertyVetoException e1) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            updateModelFromProperty();
                        }
                    });
                }
            }
        }
    }

    private WeakReference<Property<Type>> source;

    public NumberBackedSpinnerModel(Property<Type> source, NumberPropertyMetadata numberMetadata) {
        super();
        this.source = new WeakReference<Property<Type>>(source);
        source.addPropertyChangeListener(this);
        numberMetadata.addPropertyChangeListener(this);
        configureFromMetadata(numberMetadata);
        updateModelFromProperty();
        if (source instanceof VetoableProperty) {
            addChangeListener(new VetoablePropertyUpdater());
        } else {
            addChangeListener(new PropertyUpdater());
        }
    }

    private void configureFromMetadata(NumberPropertyMetadata numberMetadata) {
        if (numberMetadata.getMax().get() != null) setMaximum((Comparable) numberMetadata.getMax().get());
        if (numberMetadata.getMin().get() != null) setMinimum((Comparable) numberMetadata.getMin().get());
        if (numberMetadata.getStep().get() != null) setStepSize(numberMetadata.getStep().get());
    }

    private void updateModelFromProperty() {
        if (source.get() != null) {
            setValue(source.get().get());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof NumberPropertyMetadata) {
            configureFromMetadata((NumberPropertyMetadata) evt.getSource());
        } else {
            updateModelFromProperty();
        }
    }
}
