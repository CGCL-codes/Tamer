package de.parsemis.visualisation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

/**
 * @author Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 * 
 */
public interface GraphPanel extends PropertyChangeListener {

    public void addToPropertyChangeListener(JComponent propertyChanger);

    public GraphPanel clone() throws CloneNotSupportedException;

    public JComponent getComponent();

    public void paintOffscreen(Graphics graphic, Dimension d);
}
