package org.fudaa.ebli.volume.controles;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.vecmath.Vector3d;
import org.fudaa.ebli.repere.RepereEvent;
import org.fudaa.ebli.repere.RepereEventListener;

/**
 * @version $Revision: 1.14 $ $Date: 2007-05-04 13:49:46 $ by $Author: deniger $
 * @author Christophe Delhorbe
 */
public class TransformTypeIn extends JComponent implements PropertyChangeListener, ActionListener {

    /**
   * Non private.
   */
    boolean mode_;

    private final JLabel[] labels_;

    private final JLabel[] pos_;

    private final List repereEventListeners_;

    private final List itemEventListeners_;

    private final JCheckBox cbRelatif_;

    private DecimalFormat nf_;

    public TransformTypeIn() {
        mode_ = RepereEvent.ABSOLU;
        nf_ = new DecimalFormat("000.00E0");
        nf_.setMinimumFractionDigits(2);
        nf_.setMaximumFractionDigits(2);
        nf_.setMinimumIntegerDigits(3);
        nf_.setMaximumIntegerDigits(3);
        repereEventListeners_ = new ArrayList();
        itemEventListeners_ = new ArrayList();
        setLayout(new GridLayout(4, 4));
        labels_ = new JLabel[3];
        pos_ = new JLabel[3];
        labels_[0] = new JLabel("X", SwingConstants.CENTER);
        labels_[1] = new JLabel("Y", SwingConstants.CENTER);
        labels_[2] = new JLabel("Z", SwingConstants.CENTER);
        pos_[0] = new JLabel(nf_.format(0));
        pos_[1] = new JLabel(nf_.format(0));
        pos_[2] = new JLabel(nf_.format(0));
        cbRelatif_ = new JCheckBox("Relatif");
        cbRelatif_.addActionListener(this);
        add(cbRelatif_);
        add(new JLabel("Position", SwingConstants.CENTER));
        add(new JLabel("T", SwingConstants.CENTER));
        add(new JLabel("R", SwingConstants.CENTER));
        final JTextField[] texte = new JTextField[6];
        final MyActionListener al = new MyActionListener(texte);
        for (int i = 0; i < 3; i++) {
            add(labels_[i]);
            add(pos_[i]);
            texte[2 * i] = new JTextField(5);
            add(texte[2 * i]);
            texte[2 * i].addActionListener(al);
            texte[2 * i + 1] = new JTextField();
            add(texte[2 * i + 1]);
            texte[2 * i + 1].addActionListener(al);
        }
    }

    public void actionPerformed(final ActionEvent _evt) {
        final Object src = _evt.getSource();
        if (src == cbRelatif_) {
            if (cbRelatif_.isSelected()) {
                mode_ = RepereEvent.RELATIF;
            } else {
                mode_ = RepereEvent.ABSOLU;
            }
        }
    }

    public void propertyChange(final PropertyChangeEvent _e) {
        if (_e.getPropertyName() == "position") {
            final Vector3d oldPos = (Vector3d) _e.getOldValue();
            final Vector3d newPos = (Vector3d) _e.getNewValue();
            if (!(oldPos.equals(newPos))) {
                pos_[0].setText(nf_.format(newPos.x));
                pos_[1].setText(nf_.format(newPos.y));
                pos_[2].setText(nf_.format(newPos.z));
            }
        }
    }

    public synchronized void addRepereEventListener(final RepereEventListener _listener) {
        repereEventListeners_.add(_listener);
    }

    public synchronized void addItemListener(final ItemListener _listener) {
        itemEventListeners_.add(_listener);
    }

    public synchronized void removeRepereEventListener(final RepereEventListener _listener) {
        repereEventListeners_.remove(_listener);
    }

    public synchronized void fireRepereEvent(final RepereEvent _evt) {
        for (int i = 0; i < repereEventListeners_.size(); i++) {
            ((RepereEventListener) repereEventListeners_.get(i)).repereModifie(_evt);
        }
    }

    public synchronized void fireItemEvent(final ItemEvent _evt) {
        for (int i = 0; i < itemEventListeners_.size(); i++) {
            ((ItemListener) itemEventListeners_.get(i)).itemStateChanged(_evt);
        }
    }

    /**
   * @author fred deniger
   * @version $Id: TransformTypeIn.java,v 1.14 2007-05-04 13:49:46 deniger Exp $
   */
    class MyActionListener implements ActionListener {

        JTextField[] texte_;

        public MyActionListener(final JTextField[] _texte) {
            super();
            texte_ = _texte;
        }

        public void actionPerformed(final ActionEvent _e) {
            final RepereEvent evt = new RepereEvent(this, false);
            ItemEvent itemEvt = new ItemEvent(new JCheckBox(), ItemEvent.ITEM_STATE_CHANGED, new JCheckBox(), ItemEvent.SELECTED);
            fireItemEvent(itemEvt);
            try {
                evt.ajouteTransformation(RepereEvent.TRANS_X, Float.parseFloat(texte_[0].getText()), mode_);
                texte_[0].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            try {
                evt.ajouteTransformation(RepereEvent.TRANS_Y, Float.parseFloat(texte_[2].getText()), mode_);
                texte_[2].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            try {
                evt.ajouteTransformation(RepereEvent.TRANS_Z, Float.parseFloat(texte_[4].getText()), mode_);
                texte_[4].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            try {
                evt.ajouteTransformation(RepereEvent.ROT_X, Float.parseFloat(texte_[1].getText()) * 2 * Math.PI / 360, mode_);
                texte_[1].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            try {
                evt.ajouteTransformation(RepereEvent.ROT_Y, Float.parseFloat(texte_[3].getText()) * 2 * Math.PI / 360, mode_);
                texte_[3].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            try {
                evt.ajouteTransformation(RepereEvent.ROT_Z, Float.parseFloat(texte_[5].getText()) * 2 * Math.PI / 360, mode_);
                texte_[5].setText("");
            } catch (final NumberFormatException ex) {
            }
            ;
            fireRepereEvent(evt);
            itemEvt = new ItemEvent(new JCheckBox(), ItemEvent.ITEM_STATE_CHANGED, new JCheckBox(), ItemEvent.DESELECTED);
            fireItemEvent(itemEvt);
        }
    }
}
