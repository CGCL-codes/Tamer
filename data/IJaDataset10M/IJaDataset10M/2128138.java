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
 * Forme rectangle d'un profil de forme simple.
 * @version      $Revision: 1.8 $ $Date: 2006-09-12 08:36:42 $ by $Author: opasteur $
 * @author       Axel von Arnim
 */
public class Hydraulique1dProfilFormeRectangle implements Hydraulique1dProfilFormeSimple {

    Hydraulique1dProfilFormeRectangleEditor editor_;

    GrPoint startPoint_;

    double hauteur_, largeur_;

    PropertyChangeSupport prop_;

    IDialogInterface parent_;

    int mode_;

    static final double ANTI_VERTICAL = 0.01;

    public Hydraulique1dProfilFormeRectangle() {
        editor_ = null;
        startPoint_ = new GrPoint(0, 0, 0);
        hauteur_ = largeur_ = 0.;
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
            editor_ = new Hydraulique1dProfilFormeRectangleEditor(parent_, this);
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
            v.add(new GrPoint(Math.ceil((prec.x_ + ANTI_VERTICAL) * 100) / 100, prec.y_ - hauteur_, prec.z_));
            prec = (GrPoint) v.lastElement();
        }
        if (mode_ == CONNECT_RIGHT) v.add(new GrPoint(Math.ceil((prec.x_ + largeur_ - ANTI_VERTICAL) * 100) / 100, prec.y_, prec.z_)); else if (mode_ == CONNECT_LEFT) v.add(new GrPoint(Math.ceil((prec.x_ + largeur_ - ANTI_VERTICAL) * 100) / 100, prec.y_, prec.z_)); else v.add(new GrPoint(Math.ceil((prec.x_ + largeur_ - 2 * ANTI_VERTICAL) * 100) / 100, prec.y_, prec.z_));
        prec = (GrPoint) v.lastElement();
        if (mode_ != CONNECT_RIGHT) v.add(new GrPoint(Math.ceil((prec.x_ + ANTI_VERTICAL) * 100) / 100, prec.y_ + hauteur_, prec.z_));
        ps = new GrPoint[v.size()];
        for (int i = 0; i < v.size(); i++) ps[i] = (GrPoint) v.get(i);
        return ps;
    }

    public void setStartPoint(GrPoint p) {
        startPoint_ = p;
    }

    public GrPoint getEndPoint() {
        GrPoint res = null;
        if (mode_ == CONNECT_RIGHT) res = new GrPoint(startPoint_.x_ + largeur_, startPoint_.y_ - hauteur_, startPoint_.z_); else res = new GrPoint(startPoint_.x_ + largeur_, startPoint_.y_, startPoint_.z_);
        return res;
    }

    public String getName() {
        return "Rectangle";
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

    public void setLargeur(double l) {
        if (largeur_ == l) return;
        double vp = largeur_;
        largeur_ = l;
        prop_.firePropertyChange("largeur", new Double(vp), new Double(largeur_));
    }

    public double getLargeur() {
        return largeur_;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        prop_.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        prop_.removePropertyChangeListener(l);
    }
}

class Hydraulique1dProfilFormeRectangleEditor extends BDialog implements ActionListener {

    Hydraulique1dProfilFormeRectangle forme_;

    BuTextField tfLargeur_;

    BuTextField tfHauteur_;

    public Hydraulique1dProfilFormeRectangleEditor(IDialogInterface parent, Hydraulique1dProfilFormeRectangle f) {
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
        pnEdit.add(new BuLabel("Largeur:"), n++);
        tfLargeur_ = BuTextField.createDoubleField();
        tfLargeur_.setColumns(5);
        pnEdit.add(tfLargeur_, n++);
        pnEdit.add(new BuLabel("m"), n++);
        pnEdit.add(new BuLabel("Hauteur:"), n++);
        tfHauteur_ = BuTextField.createDoubleField();
        tfHauteur_.setColumns(5);
        pnEdit.add(tfHauteur_, n++);
        pnEdit.add(new BuLabel("m"), n++);
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
            val = (Double) tfLargeur_.getValue();
            if (val != null) forme_.setLargeur(val.doubleValue());
            dispose();
        }
    }
}
