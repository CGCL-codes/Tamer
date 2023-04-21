package at.ac.tuwien.j3dvn.control;

/**
 * 
 */
public interface PropertyChangeListener {

    <T> void propertyChanged(Connector sender, String propertyName, T oldValue, T newValue);
}
