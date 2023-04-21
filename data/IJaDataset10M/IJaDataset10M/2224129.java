package org.fudaa.fudaa.sipor;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import org.fudaa.dodico.corba.sipor.SParametresDonneesGenerales;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;

/**
 * Classe impl�mentant l'onglet "Donn�es g�n�rales" pour les param�tres de sipor.
 * 
 * @version $Revision: 1.8 $ $Date: 2007-07-23 12:46:33 $ by $Author: hadouxad $
 * @author Nicolas Chevalier
 */
public class SiporDonneesGeneralesParametres extends BuPanel {

    SParametresDonneesGenerales donneesGenerales;

    final Vector nombresIterations = new Vector();

    final String[] jours = { "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi" };

    BuTextField tfTitre1 = new BuTextField();

    BuTextField tfTitre2 = new BuTextField();

    BuTextField tfTitre3 = new BuTextField();

    JComboBox boxNombrePassages = new JComboBox(nombresIterations);

    BuTextField tfGraineDepart = BuTextField.createIntegerField();

    JComboBox boxPremierJour = new JComboBox(jours);

    DureeField dfDureeInitSimul = new DureeField(true, true, true, false);

    DureeField dfDureeSimulReelle = new DureeField(true, true, true, false);

    DureeField dfEcrTpsAtt = new DureeField(false, false, true, true);

    DureeField dfArrHeuReglPrio = new DureeField(false, false, true, true);

    /**
   * M�thode utile pour placer un composant dans l'onglet.
   * 
   * @param lm layout manager � utiliser.
   * @param composant composant � placer
   * @param c contraintes � utiliser.
   */
    public void placeComposant(final GridBagLayout lm, final Component composant, final GridBagConstraints c) {
        lm.setConstraints(composant, c);
        add(composant);
    }

    /** Constructeur. appli doit etre instance de SiporImplementation */
    public SiporDonneesGeneralesParametres(final BuCommonInterface appli) {
        super();
        final SiporImplementation implementation = (SiporImplementation) appli.getImplementation();
        donneesGenerales = implementation.outils_.getDonneesGenerales();
        final GridBagLayout lm = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(lm);
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.ipadx = 5;
        c.ipady = 5;
        c.gridx = 0;
        c.gridheight = 3;
        placeComposant(lm, new BuLabel("Titre : "), c);
        c.gridheight = 1;
        placeComposant(lm, new BuLabel("Nombre de passages : "), c);
        placeComposant(lm, new BuLabel("Graine de d�part : "), c);
        placeComposant(lm, new BuLabel("Premier jour de la simulation : "), c);
        placeComposant(lm, new BuLabel("Dur�e d'initialisation de la simulation : "), c);
        placeComposant(lm, new BuLabel("Dur�e de la simulation r�elle : "), c);
        placeComposant(lm, new BuLabel("Ecretage des temps d'attente : "), c);
        placeComposant(lm, new BuLabel("Arrondi des heures pour les r�gles de priorit� : "), c);
        for (int i = 1; i < 7; i++) {
            nombresIterations.add(new Integer(i));
        }
        tfTitre1.setColumns(20);
        tfTitre2.setColumns(20);
        tfTitre3.setColumns(20);
        tfGraineDepart.setColumns(4);
        c.gridx = 1;
        placeComposant(lm, tfTitre1, c);
        placeComposant(lm, tfTitre2, c);
        placeComposant(lm, tfTitre3, c);
        placeComposant(lm, boxNombrePassages, c);
        placeComposant(lm, tfGraineDepart, c);
        placeComposant(lm, boxPremierJour, c);
        placeComposant(lm, dfDureeInitSimul, c);
        placeComposant(lm, dfDureeSimulReelle, c);
        placeComposant(lm, dfEcrTpsAtt, c);
        placeComposant(lm, dfArrHeuReglPrio, c);
        setDonneesGenerales(donneesGenerales);
    }

    /**
   * Accesseur de donneesGenerales avec enregistrement pr�alable des donn�es contenues dans l'onglet. Permet de lire les
   * donn�es pr�sentes dans l'onglet.
   */
    public SParametresDonneesGenerales getDonneesGenerales() {
        if (donneesGenerales != null) {
            donneesGenerales.titre1 = tfTitre1.getText();
            donneesGenerales.titre2 = tfTitre2.getText();
            donneesGenerales.titre3 = tfTitre3.getText();
            donneesGenerales.nombrePassages = ((Integer) boxNombrePassages.getSelectedItem()) == null ? 0 : ((Integer) boxNombrePassages.getSelectedItem()).intValue();
            donneesGenerales.graineDepart = tfGraineDepart.getValue() == null ? 0 : ((Integer) tfGraineDepart.getValue()).intValue();
            donneesGenerales.premierJourSimulation = boxPremierJour.getSelectedIndex();
            donneesGenerales.heureDebutReelSimulation = dfDureeInitSimul.getDureeFieldLong();
            donneesGenerales.heureFinSimulation = donneesGenerales.heureDebutReelSimulation + dfDureeSimulReelle.getDureeFieldLong();
            donneesGenerales.ecretageTempsAttente = dfEcrTpsAtt.getDureeFieldLong();
            donneesGenerales.arrondiHeuresReglesPriorite = dfArrHeuReglPrio.getDureeFieldLong();
        }
        return donneesGenerales;
    }

    /**
   * Mutateur de donneesGenerales avec affichage des nouvelles valeurs dans l'onglet. Permet de d�finir les donn�es
   * trait�es dans l'onget. Elles sont imm�diatement affich�es.
   */
    public void setDonneesGenerales(final SParametresDonneesGenerales dg) {
        donneesGenerales = dg;
        if (dg != null) {
            tfTitre1.setText(dg.titre1);
            tfTitre2.setText(dg.titre2);
            tfTitre3.setText(dg.titre3);
            boxNombrePassages.setSelectedItem(new Integer(dg.nombrePassages));
            tfGraineDepart.setText(Integer.toString(dg.graineDepart));
            boxPremierJour.setSelectedIndex(dg.premierJourSimulation);
            dfDureeInitSimul.setDureeField(dg.heureDebutReelSimulation);
            dfDureeSimulReelle.setDureeField((dg.heureFinSimulation - dg.heureDebutReelSimulation));
            dfEcrTpsAtt.setDureeField(dg.ecretageTempsAttente);
            dfArrHeuReglPrio.setDureeField(dg.arrondiHeuresReglesPriorite);
        }
    }
}
