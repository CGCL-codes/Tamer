package org.fudaa.fudaa.sinavi2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonImplementation;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuInternalFrame;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import org.fudaa.dodico.corba.sinavi2.SParametresBief;
import org.fudaa.dodico.corba.sinavi2.SParametresVitesses;
import org.fudaa.dodico.sinavi2.Sinavi2Helper;

/**
 * impl�mentation d'une fen�tre interne permettant de sair les param�tres des vitesses
 * 
 * @version $Revision: 1.10 $ $Date: 2006-09-19 15:08:58 $ by $Author: deniger $
 * @author Fatimatou Ka , Beno�t Maneuvrier
 */
public class Sinavi2FilleModVitesses extends BuInternalFrame implements ActionListener, InternalFrameListener {

    private final BuLabel lTitre_ = new BuLabel("Vitesse du bateau dans le bief");

    /** bouton pour annuler */
    private final BuButton bAnnuler_ = new BuButton(Sinavi2Resource.SINAVI2.getIcon("QUITTER"), "Quitter");

    /** sauver */
    private final BuButton bSauver_ = new BuButton(Sinavi2Resource.SINAVI2.getIcon("VALIDER"), "Sauver");

    private final BuButton bAfficher_ = new BuButton(Sinavi2Resource.SINAVI2.getIcon("EDITER"), "Afficher");

    private final BuLabel lTypeBateau_ = new BuLabel("Type de bateau");

    private final BuLabel vTypeBateau_ = new BuLabel("");

    private final BuLabel lBief_ = new BuLabel("Nom du Bief");

    private final BuLabel vBief_ = new BuLabel("");

    private final BuLabel lMontant_ = new BuLabel("Vitesse des Montants");

    private final LongueurField vMontant_ = new LongueurField(true, false, false);

    private final BuLabel lAvalant_ = new BuLabel("Vitesse des Avalants");

    private final LongueurField vAvalant_ = new LongueurField(true, false, false);

    private final BuPanel pBoutons_ = new BuPanel();

    /**
   * panel contenant les diff�rents champs de la fen�tre
   */
    private final BuPanel pTitre_ = new BuPanel();

    private final BuPanel pDonnees2_ = new BuPanel();

    public ArrayList listeVitesses_;

    public Sinavi2Implementation imp_ = null;

    public Sinavi2FilleModVitesses(final BuCommonImplementation _appli, final ArrayList _liste, final String _bateauCourant, final String _biefCourant, final Sinavi2TableauVitesse tb) {
        super("Vitesse du type de bateau dans le bief", true, true, true, false);
        listeVitesses_ = _liste;
        imp_ = (Sinavi2Implementation) _appli.getImplementation();
        bSauver_.addActionListener(this);
        bAnnuler_.addActionListener(this);
        bAfficher_.addActionListener(this);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(25, 25, 25, 25));
        pTitre_.add(lTitre_, "center");
        final GridLayout g1 = new GridLayout(4, 2);
        pDonnees2_.setLayout(g1);
        pDonnees2_.add(lTypeBateau_);
        vTypeBateau_.setText(_bateauCourant);
        pDonnees2_.add(vTypeBateau_);
        pDonnees2_.add(lBief_);
        vBief_.setText(_biefCourant);
        pDonnees2_.add(vBief_);
        final ListIterator iter = listeVitesses_.listIterator();
        boolean trouve = false;
        while (iter.hasNext() & !trouve) {
            SParametresVitesses v = new SParametresVitesses();
            v = (SParametresVitesses) iter.next();
            if (v.bateau.equalsIgnoreCase(_bateauCourant) && v.bief.equalsIgnoreCase(_biefCourant)) {
                vAvalant_.setLongueurField(v.vitesseDesAvalants * 1000);
                vMontant_.setLongueurField(v.vitesseDesMontants * 1000);
                trouve = true;
            }
        }
        pDonnees2_.add(lMontant_);
        pDonnees2_.add(vMontant_);
        pDonnees2_.add(lAvalant_);
        pDonnees2_.add(vAvalant_);
        pBoutons_.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBoutons_.add(bAnnuler_);
        pBoutons_.add(bSauver_);
        pBoutons_.add(bAfficher_);
        getContentPane().add(pTitre_, BorderLayout.NORTH);
        getContentPane().add(pDonnees2_, BorderLayout.CENTER);
        getContentPane().add(pBoutons_, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        imp_.addInternalFrame(this);
    }

    /**
   * @param _e
   */
    public void actionPerformed(final ActionEvent _e) {
        if (_e.getSource() == bAnnuler_) {
            annuler();
        } else if (_e.getSource() == bSauver_) {
            final int x = -1;
            final SParametresVitesses v = new SParametresVitesses();
            controler_entrees(x, v);
            annuler();
        } else if (_e.getSource() == bAfficher_) {
            afficher_vitesses();
        }
    }

    private void annuler() {
        imp_.removeInternalFrame(this);
    }

    private void controler_entrees(int _x, SParametresVitesses _vit) {
        final ListIterator itBief = imp_.listeBiefs_.listIterator();
        double vitMax = 0;
        boolean trouveBief = false;
        while (itBief.hasNext() & !trouveBief) {
            SParametresBief b = new SParametresBief();
            b = (SParametresBief) itBief.next();
            if (b.identification.equalsIgnoreCase(vBief_.getText())) {
                vitMax = b.vitesse;
                trouveBief = true;
            }
        }
        _x = -1;
        if (vAvalant_.getLongueurField() == 0.0) {
            affMessage("Entrez la vitesse en km/h");
        } else if (vMontant_.getLongueurField() == 0.0) {
            affMessage("Entrez la vitesse en km/h");
        } else if (vAvalant_.getLongueurField() / 1000 > vitMax || vMontant_.getLongueurField() / 1000 > vitMax) {
            affMessage("La vitesse doit �tre inf�rieure � la vitesse maximale du bief :" + vitMax);
        } else {
            final SParametresVitesses vitesseCourant = new SParametresVitesses();
            vitesseCourant.bateau = vTypeBateau_.getText();
            vitesseCourant.bief = vBief_.getText();
            vitesseCourant.vitesseDesAvalants = vAvalant_.getLongueurField();
            vitesseCourant.vitesseDesMontants = vMontant_.getLongueurField();
            final ListIterator iter = listeVitesses_.listIterator();
            boolean trouve = false;
            while (iter.hasNext() & !trouve) {
                SParametresVitesses v = new SParametresVitesses();
                v = (SParametresVitesses) iter.next();
                System.out.println("avant test");
                if (Sinavi2Helper.typeVitessesEquals(vitesseCourant, v)) {
                    System.out.println("si �gal test");
                    v.vitesseDesAvalants = vitesseCourant.vitesseDesAvalants / 1000;
                    v.vitesseDesMontants = vitesseCourant.vitesseDesMontants / 1000;
                    trouve = true;
                }
                System.out.println("apr�s test test");
            }
            if (trouve) {
                _x = iter.hashCode();
                _vit = vitesseCourant;
                affMessage("Modification des vitesses effectu�e");
            }
        }
    }

    private void afficher_vitesses() {
        annuler();
    }

    public void internalFrameActivated(final InternalFrameEvent _e) {
    }

    public void internalFrameClosed(final InternalFrameEvent _e) {
    }

    public void internalFrameClosing(final InternalFrameEvent _e) {
    }

    public void internalFrameDeactivated(final InternalFrameEvent _e) {
    }

    public void internalFrameDeiconified(final InternalFrameEvent e) {
    }

    public void internalFrameIconified(final InternalFrameEvent e) {
    }

    public void internalFrameOpened(final InternalFrameEvent e) {
    }

    public void affMessage(final String _t) {
        final BuDialogMessage dialog_mess = new BuDialogMessage(imp_.getApp(), imp_.getInformationsSoftware(), "" + _t);
        dialog_mess.activate();
    }
}
