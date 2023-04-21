package org.fudaa.fudaa.tr.rubar;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;
import com.memoire.bu.BuVerticalLayout;
import org.fudaa.ctulu.CtuluNumberFormat;
import org.fudaa.dodico.h2d.rubar.H2dRubarDonneesBrutes;
import org.fudaa.dodico.h2d.rubar.H2dRubarNumberFormatter;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.calque.ZCalqueClikInteractionListener;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.data.TrSaisiePoint;
import org.fudaa.fudaa.tr.data.TrSaisiePointAction;
import org.fudaa.fudaa.tr.data.TrVisuPanel;

/**
 * @author Fred Deniger
 * @version $Id: TrRubarDonneesBrutesPointSaisie.java,v 1.5 2005-01-31 17:14:37 deniger Exp $
 */
public final class TrRubarDonneesBrutesPointSaisie extends TrSaisiePoint implements ZCalqueClikInteractionListener, InternalFrameListener, TreeSelectionListener, ActionListener, PropertyChangeListener, DocumentListener {

    protected static void activeSaisiePoint(TrRubarDonneesBrutesLayerGroup _g, TrSaisiePointAction _act) {
        final TrVisuPanel pn = _g.panel_;
        if (pn.getArbreCalqueModel().getSelectedCalque() instanceof TrRubarDonneesBrutesNuageLayer) {
            TrRubarDonneesBrutesNuageLayer l = (TrRubarDonneesBrutesNuageLayer) pn.getArbreCalqueModel().getSelectedCalque();
            TrRubarDonneesBrutesPointSaisie panel = new TrRubarDonneesBrutesPointSaisie(pn, l);
            panel.activeSaisiePoint(_act);
        }
    }

    BuTextField[] fields_;

    List values_;

    /**
   * @param _pn la panneau des calques 
   * @param _layer le calque a modifier
   */
    public TrRubarDonneesBrutesPointSaisie(TrVisuPanel _pn, TrRubarDonneesBrutesNuageLayer _layer) {
        super(_pn, _layer);
    }

    protected void buildPanel() {
        setLayout(new BuVerticalLayout(5));
        add(infoLabel_);
        H2dRubarDonneesBrutes db = ((TrRubarDonneesBrutesNuageLayer) layer_).br_;
        int max = db.getNbVariable();
        values_ = new ArrayList(max);
        BuPanel pnValues = new BuPanel();
        pnValues.setLayout(new BuGridLayout(2));
        fields_ = new BuTextField[max];
        for (int i = 0; i < max; i++) {
            if (db.isVariableActive(i)) {
                Object id = db.getVariableId(i);
                pnValues.add(new BuLabel(id.toString()));
                fields_[i] = BuTextField.createDoubleField();
                fields_[i].setColumns(10);
                fields_[i].getDocument().addDocumentListener(this);
                pnValues.add(fields_[i]);
                double initVal = 0;
                if ((db.getNuage() != null) && (db.getNuage().getNbPoint() > 0)) {
                    initVal = db.getNuage().getValue(i, db.getNuage().getNbPoint() - 1);
                }
                CtuluNumberFormat nb = H2dRubarNumberFormatter.getFormatterFor((H2dVariableType) id);
                if (nb != null) {
                    nb.initTextField(fields_[i], initVal);
                } else fields_[i].setText(Double.toString(initVal));
            }
        }
        add(infoLabel_);
        add(pnValues);
        add(bt_);
    }

    public void pointClicked(int _xEcran, int _yEcran) {
        if (values_.size() == 0) {
            for (int i = 0; i < fields_.length; i++) {
                if (fields_[i] != null) {
                    Object o = fields_[i].getValue();
                    if (!fields_[i].getValueValidator().isValueValid(o)) {
                        infoLabel_.setForeground(Color.RED);
                        infoLabel_.setText(TrResource.getS("Donn�es invalides"));
                        java.awt.Toolkit.getDefaultToolkit().beep();
                        values_.clear();
                        return;
                    }
                    values_.add(fields_[i].getValue());
                } else values_.add(null);
            }
        }
        GrPoint p = new GrPoint(_xEcran, _yEcran, 0);
        p.autoApplique(layer_.getVersReel());
        CtuluNumberFormat nf = H2dRubarNumberFormatter.getXY();
        String x = nf.format(p.x);
        String y = nf.format(p.y);
        if (((TrRubarDonneesBrutesNuageLayer) layer_).br_.getNuage().add(Double.parseDouble(x), Double.parseDouble(y), 0, values_, pn_.getCmdMng())) {
            pointAjouteOk(x, y);
        } else {
            pointErrone();
        }
    }

    public void changedUpdate(DocumentEvent _e) {
        values_.clear();
    }

    public void insertUpdate(DocumentEvent _e) {
        values_.clear();
    }

    public void removeUpdate(DocumentEvent _e) {
        values_.clear();
    }

    public void valueChanged(TreeSelectionEvent _e) {
        if (pn_.getArbreCalqueModel().getSelectedCalque() != layer_) {
            removePalette();
        }
    }
}
