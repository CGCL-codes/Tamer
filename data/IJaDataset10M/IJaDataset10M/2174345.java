package pl.edu.amu.wmi.kino.visualjavafx.model.animation.interpolators;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Linear implements Interpolation, java.io.Serializable {

    protected PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public Linear() {
    }

    @Override
    public double getValue(double howMuch, double startVal, double endVal) {
        if (howMuch >= 0) return startVal + (endVal - startVal) * howMuch; else throw new ArithmeticException();
    }

    @Override
    public double getValueAtTime(double howMuch, double Time, double startVal, double endVal) {
        return this.getValue(howMuch, startVal, endVal);
    }

    @Override
    public String getName() {
        return this.name;
    }

    private String name = "LINEAR";
}
