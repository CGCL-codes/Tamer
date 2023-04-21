package org.fudaa.fudaa.hydraulique1d.editor.profil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import javax.swing.border.EtchedBorder;
import org.fudaa.ebli.dialog.BDialog;
import org.fudaa.ebli.dialog.IDialogInterface;
import org.fudaa.ebli.geometrie.GrPoint;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;

/**
 * Forme trap�ze d'un profil de forme simple.
 * @version      $Revision: 1.9 $ $Date: 2006-09-12 08:36:42 $ by $Author: opasteur $
 * @author       Axel von Arnim
 */
public class Hydraulique1dProfilFormeTrapeze implements Hydraulique1dProfilFormeSimple {

    Hydraulique1dProfilFormeTrapezeEditor editor_;

    GrPoint startPoint_;

    double largeurPlaf_, hauteur_, pente_;

    PropertyChangeSupport prop_;

    IDialogInterface parent_;

    int mode_;

    public Hydraulique1dProfilFormeTrapeze() {
        editor_ = null;
        startPoint_ = new GrPoint(0, 0, 0);
        hauteur_ = largeurPlaf_ = pente_ = 0.;
        prop_ = new PropertyChangeSupport(this);
        mode_ = NO_CONNECT;
    }

    public void setConnectMode(int mode) {
        mode_ = mode;
    }

    public void setParent(IDialogInterface p) {
        parent_ = p;
    }

    public BDialog getEditor() {
        if (editor_ == null) {
            editor_ = new Hydraulique1dProfilFormeTrapezeEditor(parent_, this);
            if (parent_ != null) editor_.setLocationRelativeTo(parent_.getComponent());
        }
        editor_.setModal(true);
        return editor_;
    }

    public GrPoint[] getPoints() {
        GrPoint[] ps = null;
        Vector v = new Vector();
        GrPoint prec = startPoint_;
        v.add(new GrPoint(prec.x_, prec.y_, prec.z_));
        prec = (GrPoint) v.lastElement();
        if (mode_ != CONNECT_LEFT) {
            v.add(new GrPoint(prec.x_ + pente_ * hauteur_, prec.y_ - hauteur_, prec.z_));
            prec = (GrPoint) v.lastElement();
        }
        v.add(new GrPoint(prec.x_ + largeurPlaf_, prec.y_, prec.z_));
        prec = (GrPoint) v.lastElement();
        if (mode_ != CONNECT_RIGHT) v.add(new GrPoint(prec.x_ + pente_ * hauteur_, prec.y_ + hauteur_, prec.z_));
        ps = new GrPoint[v.size()];
        for (int i = 0; i < v.size(); i++) ps[i] = (GrPoint) v.get(i);
        return ps;
    }

    public void setStartPoint(GrPoint p) {
        startPoint_ = p;
    }

    public GrPoint getEndPoint() {
        GrPoint res = null;
        if (mode_ == CONNECT_RIGHT) res = new GrPoint(startPoint_.x_ + largeurPlaf_ + pente_ * hauteur_, startPoint_.y_ - hauteur_, startPoint_.z_); else res = new GrPoint(startPoint_.x_ + largeurPlaf_ + 2 * pente_ * hauteur_, startPoint_.y_, startPoint_.z_);
        return res;
    }

    public String getName() {
        return "Trap�ze";
    }

    public boolean isEditable() {
        return true;
    }

    public void setHauteur(double h) {
        if (hauteur_ == h) return;
        double vp = hauteur_;
        hauteur_ = h;
        prop_.firePropertyChange("hauteur", new Double(vp), new Double(hauteur_));
    }

    public double getHauteur() {
        return hauteur_;
    }

    public void setLargeurPlafond(double l) {
        if (largeurPlaf_ == l) return;
        double vp = largeurPlaf_;
        largeurPlaf_ = l;
        prop_.firePropertyChange("largeurPlafond", new Double(vp), new Double(largeurPlaf_));
    }

    public double getLargeurPlafond() {
        return largeurPlaf_;
    }

    public void setPente(double h) {
        if (pente_ == h) return;
        double vp = pente_;
        pente_ = h;
        prop_.firePropertyChange("pente", new Double(vp), new Double(pente_));
    }

    public double getPente() {
        return pente_;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        prop_.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        prop_.removePropertyChangeListener(l);
    }
}

class Hydraulique1dProfilFormeTrapezeEditor extends BDialog implements ActionListener {

    Hydraulique1dProfilFormeTrapeze forme_;

    BuTextField tfLargeurPlaf_;

    BuTextField tfHauteur_;

    BuTextField tfPente_;

    public Hydraulique1dProfilFormeTrapezeEditor(IDialogInterface parent, Hydraulique1dProfilFormeTrapeze f) {
        super(null, parent);
        setTitle(f.getName());
        setModal(true);
        forme_ = f;
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        int n = 0;
        BuPanel pnEdit = new BuPanel();
        pnEdit.setLayout(new BuGridLayout(3, 1, 1, true, true));
        pnEdit.setBorder(new EtchedBorder());
        pnEdit.add(new BuLabel("Largeur plafond:"), n++);
        tfLargeurPlaf_ = BuTextField.createDoubleField();
        tfLargeurPlaf_.setColumns(5);
        pnEdit.add(tfLargeurPlaf_, n++);
        pnEdit.add(new BuLabel("m"), n++);
        pnEdit.add(new BuLabel("Hauteur:"), n++);
        tfHauteur_ = BuTextField.createDoubleField();
        tfHauteur_.setColumns(5);
        pnEdit.add(tfHauteur_, n++);
        pnEdit.add(new BuLabel("m"), n++);
        pnEdit.add(new BuLabel("Fruit:"), n++);
        tfPente_ = BuTextField.createDoubleField();
        tfPente_.setColumns(5);
        pnEdit.add(tfPente_, n++);
        pnEdit.add(new BuLabel(""), n++);
        cp.add(BorderLayout.CENTER, pnEdit);
        BuButton btOk = new BuButton("Valider");
        btOk.setActionCommand("VALIDER");
        btOk.addActionListener(this);
        cp.add(BorderLayout.SOUTH, btOk);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("VALIDER".equals(cmd)) {
            Double val = (Double) tfHauteur_.getValue();
            if (val != null) forme_.setHauteur(val.doubleValue());
            val = (Double) tfLargeurPlaf_.getValue();
            if (val != null) forme_.setLargeurPlafond(val.doubleValue());
            val = (Double) tfPente_.getValue();
            if (val != null) forme_.setPente(val.doubleValue());
            dispose();
        }
    }
}
