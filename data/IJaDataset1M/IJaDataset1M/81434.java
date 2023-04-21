package org.fudaa.ctulu.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import com.memoire.bu.BuInsets;
import org.fudaa.ctulu.CtuluResource;

/**
 * Modifie pour ctulu.
 * 
 * @author santhosh kumar - santhosh@in.fiorano.com
 * @version $Id: CtuluButtonForPopup.java,v 1.3 2007-05-04 13:43:23 deniger Exp $
 */
public abstract class CtuluButtonForPopup extends JToggleButton implements PropertyChangeListener, ChangeListener, PopupMenuListener, ActionListener {

    final JButton mainButton_;

    JPopupMenu popup_;

    public CtuluButtonForPopup() {
        this(null);
    }

    public CtuluButtonForPopup(final JButton _main) {
        super(CtuluResource.CTULU.getIcon("popup.png"));
        setMargin(BuInsets.INSETS0000);
        getModel().addChangeListener(this);
        addActionListener(this);
        mainButton_ = _main;
        if (mainButton_ != null) {
            mainButton_.addPropertyChangeListener("enabled", this);
            mainButton_.getModel().addChangeListener(this);
        }
    }

    private void hidePopup() {
        if (mainButton_ != null) {
            mainButton_.getModel().setRollover(false);
        }
        getModel().setSelected(false);
        popup_.removePopupMenuListener(this);
        popup_ = null;
    }

    protected abstract JPopupMenu buildPopup(JPopupMenu _popup);

    protected JPopupMenu createPopupMenu() {
        return buildPopup(new JPopupMenu());
    }

    protected void processMouseEvent(final MouseEvent _e) {
        super.processMouseEvent(_e);
    }

    public void actionPerformed(final ActionEvent _evt) {
        if (popup_ == null) {
            popup_ = createPopupMenu();
            popup_.addPopupMenuListener(this);
            popup_.show(this, 0, getHeight());
        } else {
            hidePopup();
        }
    }

    public void popupMenuCanceled(final PopupMenuEvent _e) {
    }

    public void popupMenuWillBecomeInvisible(final PopupMenuEvent _e) {
        doClick();
        setSelected(false);
    }

    public void popupMenuWillBecomeVisible(final PopupMenuEvent _e) {
        if (mainButton_ != null) {
            mainButton_.getModel().setRollover(true);
        }
        getModel().setSelected(true);
    }

    public void propertyChange(final PropertyChangeEvent _evt) {
        if (mainButton_ != null) {
            setEnabled(mainButton_.isEnabled());
        }
    }

    public void stateChanged(final ChangeEvent _e) {
        if (mainButton_ != null && _e.getSource() == mainButton_.getModel()) {
            if (popup_ != null && !mainButton_.getModel().isRollover()) {
                mainButton_.getModel().setRollover(true);
                return;
            }
            getModel().setRollover(mainButton_.getModel().isRollover());
            setSelected(mainButton_.getModel().isArmed() && mainButton_.getModel().isPressed());
        } else {
            if (popup_ != null && !getModel().isSelected()) {
                getModel().setSelected(true);
                return;
            }
            if (mainButton_ != null) {
                mainButton_.getModel().setRollover(getModel().isRollover());
            }
        }
    }
}
