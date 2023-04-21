package org.fudaa.fudaa.sipor;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuResource;
import org.fudaa.dodico.corba.sipor.SParametresEcluse;

/**
 * Boite de dialogue permettant de saisir les param�tres d'une �cluse.
 * Les donn�es sont automatiquement enregistr�es dans le champ ecluse avant la fermeture de la boite.
 *
 * @version      $Revision: 1.5 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Nicolas Chevalier , Bertrand Audinet
 */
public class SiporEcluseParametres extends JDialog implements ActionListener {

    /** Donn�e trait�e dans la boite de dialogue : vaut null par d�faut. */
    SParametresEcluse ecluse;

    JTextField tfIntitule = new JTextField("", 20);

    DureeField dfDureeEclusee = new DureeField(false, false, true, true);

    DureeField dfDureeFausseBassinee = new DureeField(false, false, true, true);

    DureeField dfDureePassageEcluseEtale = new DureeField(false, false, true, true);

    DureeField dfCreneauEtaleAvantPleineMer = new DureeField(false, false, true, true);

    DureeField dfCreneauEtaleApresPleineMer = new DureeField(false, false, true, true);

    public void placeComposant(final GridBagLayout lm, final Component composant, final GridBagConstraints c) {
        lm.setConstraints(composant, c);
        getContentPane().add(composant);
    }

    /** Construit une boite de dialogue pour saisir les parametres d'une �cluse. */
    public SiporEcluseParametres(final BuCommonInterface _appli) {
        super(_appli instanceof Frame ? (Frame) _appli : (Frame) null, "Description d'�cluse", true);
        setSize(600, 300);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        if (_appli instanceof Frame) {
            final Point pos = ((Frame) _appli).getLocationOnScreen();
            pos.x += (((Frame) _appli).getWidth() - getWidth()) / 2;
            pos.y += (((Frame) _appli).getHeight() - getHeight()) / 2;
            setLocation(pos);
        }
        final GridBagLayout lm = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(lm);
        c.gridy = GridBagConstraints.RELATIVE;
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 2;
        c.weighty = 1;
        tfIntitule.setEnabled(false);
        placeComposant(lm, tfIntitule, c);
        c.gridwidth = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        placeComposant(lm, new JLabel("Dur�e d'une �clus�e : "), c);
        placeComposant(lm, new JLabel("Dur�e d'une fausse bassin�e : "), c);
        placeComposant(lm, new JLabel("Dur�e de passage de l'�cluse � l'�tale : "), c);
        placeComposant(lm, new JLabel("Cr�neau d'�tale   - avant pleine mer : "), c);
        placeComposant(lm, new JLabel("                  - apr�s pleine mer : "), c);
        c.gridx = 1;
        placeComposant(lm, dfDureeEclusee, c);
        placeComposant(lm, dfDureeFausseBassinee, c);
        placeComposant(lm, dfDureePassageEcluseEtale, c);
        placeComposant(lm, dfCreneauEtaleAvantPleineMer, c);
        placeComposant(lm, dfCreneauEtaleApresPleineMer, c);
        final BuButton bFermer = new BuButton(BuResource.BU.getIcon("FERMER"), "Fermer");
        bFermer.addActionListener(this);
        c.gridx = 0;
        c.weighty = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        placeComposant(lm, bFermer, c);
    }

    /** Enregistrement des donn�es de la boite de dialogue avant sa fermeture. */
    public void actionPerformed(final ActionEvent e) {
        getEcluse();
        setVisible(false);
    }

    /** Changement de l'intitul� de la boite de dialogue. */
    public void setIntitule(final String intitule) {
        tfIntitule.setText(intitule);
    }

    /** Mutateur d' ecluse avec affichage des nouvelles donn�es dans la boite de dialogue.
      Si ecluse_ vaut null, une nouvelle instance est cr�ee */
    public void setEcluse(final SParametresEcluse ecluse_) {
        ecluse = ecluse_;
        if (ecluse == null) {
            ecluse = new SParametresEcluse();
        }
        dfDureeEclusee.setDureeField(ecluse.dureeEclusee);
        dfDureeFausseBassinee.setDureeField(ecluse.dureeFausseBassinee);
        dfDureePassageEcluseEtale.setDureeField(ecluse.dureePassageEcluseEtale);
        dfCreneauEtaleAvantPleineMer.setDureeField(ecluse.creneauEtaleAvantPleineMer);
        dfCreneauEtaleApresPleineMer.setDureeField(ecluse.creneauEtaleApresPleineMer);
    }

    /** Accesseur de ecluse avec enregistrement pr�alable des donn�es de la boite de dialogue dans ecluse.<BR>
      Si ecluse vaut null, une nouvelle instance est cr�ee */
    public SParametresEcluse getEcluse() {
        if (ecluse == null) {
            ecluse = new SParametresEcluse();
        }
        ecluse.dureeEclusee = dfDureeEclusee.getDureeFieldLong();
        ecluse.dureeFausseBassinee = dfDureeFausseBassinee.getDureeFieldLong();
        ecluse.dureePassageEcluseEtale = dfDureePassageEcluseEtale.getDureeFieldLong();
        ecluse.creneauEtaleAvantPleineMer = dfCreneauEtaleAvantPleineMer.getDureeFieldLong();
        ecluse.creneauEtaleApresPleineMer = dfCreneauEtaleApresPleineMer.getDureeFieldLong();
        return ecluse;
    }
}
