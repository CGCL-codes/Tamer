package org.fudaa.ebli.palette;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuResource;
import com.memoire.bu.BuVerticalLayout;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.controle.BMolette;

/**
 * @author Fred Deniger
 * @version $Id: BSelecteurFacteur.java,v 1.8 2006-09-19 14:55:51 deniger Exp $
 */
public class BSelecteurFacteur extends BuPanel implements ChangeListener, ActionListener {

    public void actionPerformed(final ActionEvent _e) {
        if (target_ == null) {
            return;
        }
        target_.setDefaultFacteur();
    }

    public void stateChanged(final ChangeEvent _e) {
        if (target_ == null) {
            return;
        }
        double d = target_.getFacteur();
        double inc = d * 0.2;
        if (molette_.getDirection() == BMolette.MOINS) {
            inc = -inc;
        }
        d += inc;
        target_.setFacteur(d);
    }

    BSelecteurFacteurTargetInterface target_;

    BMolette molette_;

    BuButton bt_;

    /**
   * intialise la molette et le bouton d'initialisation.
   */
    public BSelecteurFacteur() {
        setLayout(new BuVerticalLayout(5));
        molette_ = new BMolette();
        molette_.setOrientation(BMolette.HORIZONTAL);
        molette_.addChangeListener(this);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(molette_);
        bt_ = new BuButton();
        bt_.setText(EbliLib.getS("Valeur par d�faut"));
        bt_.setIcon(BuResource.BU.getIcon("rafraichir"));
        bt_.setToolTipText(EbliLib.getS("Initialiser avec la valeur par d�faut"));
        bt_.addActionListener(this);
        add(bt_);
    }

    /**
   * @param _target la cible pour le facteur
   */
    public void setFacteurTarget(final BSelecteurFacteurTargetInterface _target) {
        target_ = _target;
        bt_.setEnabled(target_ != null);
        molette_.setEnabled(target_ != null);
    }
}
