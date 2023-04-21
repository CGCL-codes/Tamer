package org.fudaa.ebli.commun;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.AbstractButton;

/**
 * @author deniger
 * @version $Id: EbliSelectedChangeListener.java,v 1.6 2006-09-19 14:55:55 deniger Exp $
 */
public class EbliSelectedChangeListener implements PropertyChangeListener {

    WeakReference button_;

    /**
   * @param _b le bouton concerne
   *
   */
    public EbliSelectedChangeListener(final AbstractButton _b) {
        button_ = new WeakReference(_b);
    }

    public void propertyChange(final PropertyChangeEvent _evt) {
        final Object o = button_.get();
        if (o == null) {
            return;
        }
        final AbstractButton bt = (AbstractButton) o;
        if ("selected".equals(_evt.getPropertyName())) {
            final boolean newValue = ((Boolean) _evt.getNewValue()).booleanValue();
            if (newValue != bt.isSelected()) {
                bt.setSelected(newValue);
            }
        }
    }
}
