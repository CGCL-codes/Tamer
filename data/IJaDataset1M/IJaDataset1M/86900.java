package com.lts.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;

/**
 * A class that listens to changes to a JFormattedTextField and allows or forbids
 * changes.
 * 
 * <P>
 * The class listens for changes to the "value" property.  When such an event occurs,
 * the class gets the new value and calls allowChangeTo.  If that method returns true,
 * then the listener calls {@link JFormattedTextField#commitEdit()}, otherwise it calls
 * {@link JFormattedTextField#rev
 * </P>
 * 
 * @author cnh
 *
 */
public class JFormattedTextFilterListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
