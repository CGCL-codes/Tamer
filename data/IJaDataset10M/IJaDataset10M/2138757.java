package org.fudaa.ebli.repere;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;

/**
 * Un editeur de TransformationRepere.
 *
 * @version      $Revision: 1.6 $ $Date: 2004-06-30 15:21:01 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public class BTransformationReperePropertyEditor extends JComponent implements PropertyEditor {

    private BTransformationRepere objet_;

    private PropertyChangeSupport changeListeners;

    public BTransformationReperePropertyEditor() {
        super();
        objet_ = null;
    }

    public void setValue(Object _value) {
        if ((objet_ != _value) && (_value instanceof BTransformationRepere)) {
            Object vp = objet_;
            objet_ = (BTransformationRepere) _value;
            firePropertyChange(null, vp, objet_);
        }
    }

    public Object getValue() {
        return objet_;
    }

    public boolean isPaintable() {
        return true;
    }

    public void paintValue(Graphics _gfx, Rectangle _box) {
        objet_.paint(_gfx);
    }

    public String getJavaInitializationString() {
        return "new org.fudaa.ebli.repere.BTransformationRepere()";
    }

    public String getAsText() {
        String r = "";
        return r;
    }

    public void setAsText(String _texte) throws IllegalArgumentException {
        try {
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("nombre d'arguments incorrect");
        }
    }

    public String[] getTags() {
        return null;
    }

    public Component getCustomEditor() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (changeListeners != null) changeListeners = new PropertyChangeSupport(this);
        changeListeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (changeListeners != null) return;
        changeListeners.removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String _dsc, Object _avant, Object _apres) {
        if (changeListeners != null) return;
        changeListeners.firePropertyChange(_dsc, _avant, _apres);
    }
}
