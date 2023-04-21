package org.fudaa.fudaa.simboat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCheckBox;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;

/**
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:10:27 $ by $Author: deniger $
 * @author       Nicolas Maillot
 */
class SimboatRadarInfos extends BuPanel implements ActionListener {

    private int nbMobiles_ = 0;

    private int pasCercles_ = 0;

    private int portee_ = 0;

    private BuCheckBox cb_ = null;

    private BuTextField tfNbMobiles_ = null;

    private BuTextField tfPasCercles_ = null;

    public SimboatRadarInfos() {
    }

    public SimboatRadarInfos(final int _nbMobiles, final int _pasCercles, final int _portee) {
        nbMobiles_ = _nbMobiles;
        pasCercles_ = _pasCercles;
        portee_ = _portee;
        setLayout(new BuGridLayout(2, 1, 1, true, true));
        setBackground(Color.white);
        final BuTextField tf1 = new BuTextField(SimboatResource.SIMBOAT.getString("Nombre de Mobiles: "));
        tf1.setEditable(false);
        add(tf1);
        tfNbMobiles_ = new BuTextField(new String("" + nbMobiles_));
        tfNbMobiles_.setEditable(false);
        add(tfNbMobiles_);
        final BuTextField tf2 = new BuTextField(SimboatResource.SIMBOAT.getString("Precision:"));
        tf2.setEditable(false);
        add(tf2);
        tfPasCercles_ = new BuTextField(new String("" + pasCercles_));
        tfPasCercles_.setEditable(false);
        add(tfPasCercles_);
        final BuTextField tf3 = new BuTextField(SimboatResource.SIMBOAT.getString("Mode prevision:"));
        tf3.setEditable(false);
        add(tf3);
        cb_ = new BuCheckBox();
        cb_.addActionListener(this);
        cb_.setActionCommand("PCHECKED");
        add(cb_);
        final BuTextField tf4 = new BuTextField(SimboatResource.SIMBOAT.getString("Modif Precision:"));
        tf4.setEditable(false);
        add(tf4);
        final BuPanel bp = new BuPanel();
        bp.setLayout(new BuGridLayout(2));
        final BuButton bplus = new BuButton("+");
        bplus.setActionCommand("PLUS");
        bplus.addActionListener(this);
        final BuButton bmoins = new BuButton("_");
        bmoins.setActionCommand("MOINS");
        bmoins.addActionListener(this);
        bp.add(bplus);
        bp.add(bmoins);
        add(bp);
    }

    public void setNbMobiles(final int _nbMobiles) {
        nbMobiles_ = _nbMobiles;
    }

    public int getNbMobiles() {
        return nbMobiles_;
    }

    public boolean getModePrevision() {
        return cb_.isSelected();
    }

    public int getPasCercles() {
        return pasCercles_;
    }

    public void setPasCercles(final int _pasCercles) {
        pasCercles_ = _pasCercles;
    }

    public void setPortee(final int _portee) {
        portee_ = _portee;
    }

    public int getPortee() {
        return portee_;
    }

    public void actionPerformed(final ActionEvent _evt) {
        if ("PLUS".equals(_evt.getActionCommand())) {
            pasCercles_++;
            if (pasCercles_ > portee_) {
                pasCercles_ = portee_;
            }
            tfPasCercles_.setText("" + pasCercles_);
        }
        if ("MOINS".equals(_evt.getActionCommand())) {
            pasCercles_--;
            if (pasCercles_ < 1) {
                pasCercles_ = 1;
            }
            tfPasCercles_.setText("" + pasCercles_);
        }
    }
}
