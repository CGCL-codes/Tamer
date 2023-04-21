package org.fudaa.fudaa.reflux;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import com.memoire.bu.BuDialogError;
import org.fudaa.fudaa.commun.impl.FudaaDialog;

/**
 * Affiche/modifie les propri�t�s d'un domaine poreux.
 * 
 * @version $Id: RefluxDialogProprietesSegment.java,v 1.5 2007-01-19 13:14:36 deniger Exp $
 * @author Bertrand Marchand
 */
public class RefluxDialogProprietesSegment extends FudaaDialog {

    Border bdpnProprietes;

    Border bdpnPoint2;

    Border bdpnPoint1;

    Border border1;

    TitledBorder titledBorder1;

    Border border2;

    Border border3;

    TitledBorder titledBorder2;

    Border border4;

    Border border5;

    TitledBorder titledBorder3;

    Border border6;

    Border border7;

    /** Domaine poreux. */
    private RefluxSegment sg_;

    /** Retourne le bouton activ�. */
    public JButton reponse;

    private JPanel pnValeur = new JPanel();

    private JLabel lbValeur = new JLabel();

    private JCheckBox cbAutomatique = new JCheckBox();

    private JTextField tfValeur = new JTextField();

    /**
   * Cr�ation d'un dialogue sans parent.
   */
    public RefluxDialogProprietesSegment() {
        this(null);
    }

    /**
   * Cr�ation d'un dialogue avec parent.
   */
    public RefluxDialogProprietesSegment(Frame _parent) {
        super(_parent);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        bdpnPoint1 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Premier point saisi"), BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bdpnPoint2 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Deuxi�me point saisi"), BorderFactory.createEmptyBorder(5, 5, 5, 5));
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        titledBorder1 = new TitledBorder(border1, "Premier point");
        border2 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Deuxi�me point"), BorderFactory.createEmptyBorder(2, 5, 2, 5));
        border3 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        titledBorder2 = new TitledBorder(border3, "Premier point");
        border4 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Premier point"), BorderFactory.createEmptyBorder(2, 5, 2, 5));
        border5 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        titledBorder3 = new TitledBorder(border5, "Deuxi�me point");
        border6 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Troisi�me point"), BorderFactory.createEmptyBorder(2, 5, 2, 5));
        border7 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        this.setTitle("Propri�t�s de la ligne");
        bdpnProprietes = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        lbValeur.setText("Valeur :");
        cbAutomatique.setHorizontalTextPosition(SwingConstants.LEFT);
        cbAutomatique.setText("Automatique :");
        cbAutomatique.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        cbAutomatique.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent _e) {
                cbAutomatique_itemStateChanged(_e);
            }
        });
        tfValeur.setPreferredSize(new Dimension(70, 21));
        tfValeur.setText("");
        this.getContentPane().add(pnValeur, BorderLayout.NORTH);
        pnValeur.add(lbValeur, null);
        pnValeur.add(tfValeur, null);
        pnValeur.add(cbAutomatique, null);
        this.pack();
    }

    protected void btOkActionPerformed(ActionEvent _evt) {
        if (majBDD()) {
            reponse = (JButton) _evt.getSource();
            super.btOkActionPerformed(_evt);
        }
    }

    /**
   * Mise � jour de la base de donn�es.
   * 
   * @return <i>true</i> : La mise � jour s'est bien pass�e.
   */
    private boolean majBDD() {
        try {
            if (!cbAutomatique.isSelected()) {
                sg_.setHauteur(Double.parseDouble(tfValeur.getText()));
            }
            sg_.setHAutomatique(cbAutomatique.isSelected());
        } catch (NumberFormatException _exc) {
            new BuDialogError(null, RefluxImplementation.informationsSoftware(), "Un des param�tres n'a pas un format valide").activate();
            return false;
        }
        return true;
    }

    /**
   * Affectation du segment � modifier.
   * 
   * @param _sg Segment.
   */
    public void setSegment(RefluxSegment _sg) {
        sg_ = _sg;
    }

    /**
   * FD: prj_ ne sert a rien Affectation de la g�om�trie (necessaire pour savoir si les points du domaine poreux sont
   * hors g�om�trie ou non.
   * 
   * @param _prj Le projet refonde.
   */
    public void setProjet(PRProjet _prj) {
    }

    /**
   * Visualisation de la fenetre. Surcharg�.
   */
    public void show() {
        if (sg_ != null) {
            if (!sg_.isHAutomatique()) {
                tfValeur.setText("" + sg_.getHauteur());
            } else {
                tfValeur.setText("** Calcul� **");
            }
            cbAutomatique.setSelected(sg_.isHAutomatique());
            setTitle("Propri�t�s de la ligne " + sg_.getNum());
        }
        super.show();
    }

    void cbAutomatique_itemStateChanged(ItemEvent _e) {
        tfValeur.setEnabled(!cbAutomatique.isSelected());
    }
}
