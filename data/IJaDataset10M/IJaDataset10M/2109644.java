package org.fudaa.ebli.controle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.memoire.bu.BuComboBox;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuPanel;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.trace.TraceIconButton;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.ebli.trace.TraceLigneChooser;
import org.fudaa.ebli.trace.TraceLigneModel;

/**
 * @author Fred Deniger
 * @version $Id: BSelecteurTraceLigne.java,v 1.4 2006-09-19 14:55:52 deniger Exp $
 */
public class BSelecteurTraceLigne extends Observable implements ItemListener, ChangeListener, ActionListener {

    TraceLigneChooser typeModel_;

    BuComboBox ligneTypeCb_;

    JSpinner spTaille_;

    TraceIconButton btColor_;

    SpinnerNumberModel tailleModel_;

    public BSelecteurTraceLigne() {
        this(null, true);
    }

    public BSelecteurTraceLigne(final TraceLigneModel _data, final boolean _addInvisible) {
        typeModel_ = new TraceLigneChooser(_addInvisible);
        ligneTypeCb_ = new BuComboBox();
        ligneTypeCb_.setModel(typeModel_);
        ligneTypeCb_.addItemListener(this);
        final BTraitRenderer r = new BTraitRenderer();
        ligneTypeCb_.setRenderer(r);
        ligneTypeCb_.setPreferredSize(new Dimension(60, 15));
        spTaille_ = new JSpinner();
        float val = 1f;
        if (_data != null) {
            val = _data.getEpaisseur();
        }
        tailleModel_ = new SpinnerNumberModel(val, 0, 15, 0.5);
        tailleModel_.addChangeListener(this);
        spTaille_.setModel(tailleModel_);
        btColor_ = new TraceIconButton();
        btColor_.setMaximumSize(new Dimension(20, 20));
        btColor_.addActionListener(this);
        if (_data != null) {
            typeModel_.setSelectedType(_data.getTypeTrait());
            btColor_.setForeground(_data.getCouleur());
        }
    }

    public JPanel buildPanel() {
        final JPanel r = new BuPanel();
        buildPanel(r, true);
        return r;
    }

    public void buildPanel(final JPanel _pn, final boolean _layout) {
        if (_layout) {
            _pn.setLayout(new BuGridLayout(3, 3, 3, true, true));
        }
        _pn.add(ligneTypeCb_);
        _pn.add(spTaille_);
        _pn.add(btColor_);
    }

    public void actionPerformed(final ActionEvent _e) {
        if (_e.getSource() == btColor_) {
            final Color c = JColorChooser.showDialog(btColor_, EbliLib.getS("Couleur"), btColor_.getSavedColor());
            if (c != null) {
                btColor_.setForeground(c);
                fireDataChanged(false, false, true);
            }
        }
    }

    boolean isUpdating_;

    public void update(final TraceLigneModel _ic) {
        isUpdating_ = true;
        final int type = _ic == null ? -1 : _ic.getTypeTrait();
        if (type >= 0) {
            typeModel_.setSelectedType(type);
        } else {
            ligneTypeCb_.setSelectedItem(null);
        }
        float taille = _ic == null ? 0 : _ic.getEpaisseur();
        if (taille < 0) {
            taille = 0;
        }
        tailleModel_.setValue(new Double(taille));
        btColor_.setForeground(_ic == null ? null : _ic.getCouleur());
        isUpdating_ = false;
    }

    protected void fireDataChanged(final boolean _type, final boolean _epaisseur, final boolean _color) {
        if (isUpdating_) {
            return;
        }
        final TraceLigneModel d = getNewData();
        if (_type) {
            d.setColorIgnored();
            d.setEpaisseurIgnored();
        } else if (_epaisseur) {
            d.setColorIgnored();
            d.setTypeIgnored();
        } else if (_color) {
            d.setTypeIgnored();
            d.setEpaisseurIgnored();
        }
        setChanged();
        notifyObservers(d);
    }

    public String getPropertyName() {
        return "traceLigne";
    }

    public TraceLigneModel getNewData() {
        final TraceLigneModel r = new TraceLigneModel();
        r.setColor(btColor_.getForeground());
        r.setTypeTrait(typeModel_.getSelectedType());
        r.setEpaisseur(tailleModel_.getNumber().floatValue());
        return r;
    }

    public void itemStateChanged(final ItemEvent _e) {
        if (_e.getStateChange() == ItemEvent.DESELECTED) {
            return;
        }
        final boolean enable = typeModel_.getSelectedType() != TraceLigne.INVISIBLE;
        btColor_.setEnabled(enable);
        spTaille_.setEnabled(enable);
        fireDataChanged(true, false, false);
    }

    public void stateChanged(final ChangeEvent _e) {
        fireDataChanged(false, true, false);
    }

    public final TraceIconButton getBtColor() {
        return btColor_;
    }

    public final TraceLigneChooser getTypeModel() {
        return typeModel_;
    }

    public final SpinnerNumberModel getTailleModel() {
        return tailleModel_;
    }
}
