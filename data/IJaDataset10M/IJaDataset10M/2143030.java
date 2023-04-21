package org.fudaa.fudaa.sinavi3;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.memoire.bu.BuTable;

/**
 * Panel d'affichage des donn�es des Navires sous forme d un tableau: avec la possibilit� de modifier ou de
 * supprimer une donn�e: NOTE TRES PRATIQUE SUR LE FONCTIONNEMENT DES BuTable IL FAUT AFFICHER LE HEADER SEPAREMMENT
 * SI ON UTILISE PAS DE JSCROLLPANE DIRECTEMENT ON PEUT POUR CELA UTILISER LE LAYOUT BORDERLAYOUT ET AFFICHER LE
 * TABLEHEADER EN DEBUT DE FICHIER ET AFFICHER LE TABLEAU AU CENTER
 * 
 * @author Adrien Hadoux
 */
public class Sinavi3PanelAffichageBateaux extends JPanel implements Observer {

    String[] titreColonnes_ = { "Nom", "priorite", "Longueur", "Largeur", "Vitesse avalant", "vitesse montant", "tirant eau", "Type (charge ou lege)", "cr.journalier1", "cr.journalier2", "cr.journalier3" };

    /**
   * Tableau de type BuTable qui contiendra les donn�es des quais
   */
    Object[][] data_ = {};

    Object[][] ndata_;

    BuTable tableau_ = new BuTable(data_, titreColonnes_);

    DefaultTableModel modele_;

    /**
   * Panel ascenceur
   */
    JScrollPane ascenceur_;

    /**
   * Bordure du tableau
   */
    Border borduretab_ = BorderFactory.createLoweredBevelBorder();

    Sinavi3DataSimulation donnees_;

    /**
   * constructeur du panel d'affichage des quais
   * 
   * @param d donn�es de la simulation
   */
    Sinavi3PanelAffichageBateaux(final Sinavi3DataSimulation d) {
        donnees_ = d;
        d.addObservers(this);
        setLayout(new BorderLayout());
        this.maj(d);
    }

    /**
   * Methode d'ajout d'une cellule
   */
    void maj(final Sinavi3DataSimulation d) {
        System.out.println(" taille des navires: " + d.listeBateaux_.listeNavires_.size());
        ndata_ = new Object[d.listeBateaux_.listeNavires_.size()][titreColonnes_.length];
        for (int i = 0; i < d.listeBateaux_.listeNavires_.size(); i++) {
            final Sinavi3Bateau nav = d.listeBateaux_.retournerNavire(i);
            int cpt = 0;
            ndata_[i][cpt++] = nav.nom;
            ndata_[i][cpt++] = "" + nav.priorite;
            ndata_[i][cpt++] = "" + (float) nav.longueurMax;
            ndata_[i][cpt++] = "" + (float) nav.largeurMax;
            ndata_[i][cpt++] = "" + (float) nav.vitesseDefautAvalant;
            ndata_[i][cpt++] = "" + (float) nav.vitesseDefautMontant;
            ndata_[i][cpt++] = "" + (float) nav.tirantEau;
            ndata_[i][cpt++] = "" + nav.typeTirant;
            ndata_[i][cpt++] = "" + (float) nav.creneauxJournaliers_.semaineCreneau1HeureDep + " � " + (float) nav.creneauxJournaliers_.semaineCreneau1HeureArrivee;
            ndata_[i][cpt++] = "" + (float) nav.creneauxJournaliers_.semaineCreneau2HeureDep + " � " + (float) nav.creneauxJournaliers_.semaineCreneau2HeureArrivee;
            ndata_[i][cpt++] = "" + (float) nav.creneauxJournaliers_.semaineCreneau3HeureDep + " � " + (float) nav.creneauxJournaliers_.semaineCreneau3HeureArrivee;
        }
        this.tableau_ = new BuTable(ndata_, this.titreColonnes_) {

            public boolean isCellEditable(final int row, final int col) {
                return false;
            }
        };
        tableau_.getTableHeader();
        tableau_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableau_.setBorder(this.borduretab_);
        for (int i = 0; i < titreColonnes_.length; i++) {
            TableColumn column = tableau_.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
        TableColumn column = tableau_.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column = tableau_.getColumnModel().getColumn(2);
        column.setPreferredWidth(75);
        tableau_.getColumnModel().getColumn(3).setPreferredWidth(75);
        tableau_.getColumnModel().getColumn(4).setPreferredWidth(75);
        tableau_.revalidate();
        this.removeAll();
        this.add(tableau_.getTableHeader(), BorderLayout.PAGE_START);
        this.add(tableau_, BorderLayout.CENTER);
        this.revalidate();
        this.updateUI();
    }

    public void update(Observable o, Object arg) {
        if (arg.equals("gare")) maj(donnees_);
    }
}
