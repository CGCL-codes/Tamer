package org.fudaa.fudaa.diapre;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.bu.BuLabel;
import org.fudaa.dodico.corba.diapre.SParametresDiapre;
import org.fudaa.dodico.corba.diapre.SResultatsDiapre;
import org.fudaa.fudaa.commun.projet.FudaaProjet;

/**
 * Fenetre d'affichage d'un graphique.
 *
 * @version      $Revision: 1.9 $ $Date: 2006-09-19 15:02:14 $ by $Author: deniger $
 * @author       Jean de Malafosse
 */
public class DiaprePrecision extends JDialog implements ActionListener {

    DiapreTableauValeurs tableau;

    FudaaProjet projet_;

    BuInformationsDocument id_;

    BuCommonInterface appli_;

    private BuDialogMessage message;

    boolean un = true;

    BuButton tab = new BuButton("Tableau");

    BuButton graph = new BuButton("Graphique");

    BuButton ok = new BuButton("Valider");

    TextFieldsDiapre saisie = new TextFieldsDiapre();

    int precision;

    TextFieldsDiapre hauteur = new TextFieldsDiapre(true);

    double nbr;

    TextFieldsDiapre nombre = new TextFieldsDiapre();

    int nbr1;

    int bool;

    boolean bool1 = true;

    boolean bool2 = true;

    public void placeComposant(final GridBagLayout lm, final Component composant, final GridBagConstraints c) {
        lm.setConstraints(composant, c);
        getContentPane().add(composant);
    }

    public DiaprePrecision(final BuCommonInterface _appli, final BuInformationsDocument _id) {
        super(_appli instanceof Frame ? (Frame) _appli : (Frame) null, "Pr�cision des r�sultats ", true);
        appli_ = _appli;
        id_ = _id;
        setSize(650, 300);
        setLocation(520, 500);
        setModal(false);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final GridBagLayout lm = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(lm);
        c.anchor = GridBagConstraints.WEST;
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 100;
        c.weightx = 100;
        c.insets.left = 15;
        c.insets.right = 15;
        c.gridx = 0;
        c.gridy = 0;
        final BuLabel lab0 = new BuLabel("Votre �cran de sout�nement mesure");
        placeComposant(lm, lab0, c);
        c.gridy = 1;
        final BuLabel lab = new BuLabel("Il est discr�tis� par ");
        placeComposant(lm, lab, c);
        c.gridy = 2;
        c.gridwidth = 5;
        final BuLabel lab1 = new BuLabel("Vous souhaitez obtenir l'affichage des r�sultats avec la pr�cision suivante :");
        placeComposant(lm, lab1, c);
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        final BuLabel lab2 = new BuLabel("un point tous les");
        placeComposant(lm, lab2, c);
        c.gridy = 4;
        c.anchor = GridBagConstraints.CENTER;
        placeComposant(lm, tab, c);
        tab.addActionListener(new DiapretabListener());
        tableau = new DiapreTableauValeurs(_appli, projet_);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        placeComposant(lm, hauteur, c);
        hauteur.setEditable(false);
        c.gridy = 1;
        placeComposant(lm, nombre, c);
        nombre.setEditable(false);
        c.gridy = 3;
        precision = 1;
        saisie.setValue(precision);
        placeComposant(lm, saisie, c);
        c.gridy = 4;
        c.anchor = GridBagConstraints.CENTER;
        placeComposant(lm, graph, c);
        graph.addActionListener(new DiapregraphListener());
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        final BuLabel lab3 = new BuLabel("m  de hauteur. ");
        placeComposant(lm, lab3, c);
        c.gridy = 1;
        final BuLabel lab4 = new BuLabel("segments. ");
        placeComposant(lm, lab4, c);
        c.gridy = 3;
        final BuLabel lab5 = new BuLabel("segments. ");
        placeComposant(lm, lab5, c);
        c.gridy = 4;
        c.anchor = GridBagConstraints.CENTER;
        placeComposant(lm, ok, c);
        ok.addActionListener(new okListener());
        c.gridx = 3;
        saisie.addFocusListener(new SaisieAction());
    }

    public void affiche() {
        nbr1 = ((SParametresDiapre) projet_.getParam(DiapreResource.PARAMETRES)).options.nombreSegmentsEcran;
        System.out.println("nombre de segments" + nbr1);
        nombre.setValue(nbr1);
        final double n = nbr1;
        nbr = n / 100;
        System.out.println("hauteur" + nbr);
        hauteur.setValue(nbr);
    }

    public void setResultatsProjet(final FudaaProjet _projet) {
        projet_ = _projet;
        tableau.setProjet(projet_);
    }

    public void actionPerformed(final ActionEvent e) {
    }

    class DiapretabListener implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            final SParametresDiapre para = (SParametresDiapre) projet_.getParam(DiapreResource.PARAMETRES);
            bool2 = true;
            final int i = saisie.getInt();
            if (i > para.options.nombreSegmentsEcran || i < 1) {
                messageAssistant("La pr�cision doit �tre comprise entre 1 et " + para.options.nombreSegmentsEcran + " .");
                tableau.setVisible(false);
            } else {
                precision = saisie.getInt();
                tableau.setPrecision(precision);
                tableau.setVisible(true);
            }
        }
    }

    class DiapregraphListener implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            final SParametresDiapre para = (SParametresDiapre) projet_.getParam(DiapreResource.PARAMETRES);
            final SResultatsDiapre result = (SResultatsDiapre) projet_.getResult(DiapreResource.RESULTATS);
            final double[][] valeursX = new double[4][para.options.nombreSegmentsEcran];
            final double[][] valeursY = new double[1][para.options.nombreSegmentsEcran];
            if (para.parametresGeneraux.choixCalcul.equals("L")) {
                for (int i = 0; i < para.options.nombreSegmentsEcran; i++) {
                    valeursX[0][i] = result.pointsDiagrammePression[i].pressionTotale;
                    valeursX[1][i] = result.pointsDiagrammePression[i].pressionEffective;
                    valeursX[2][i] = result.pointsDiagrammePression[i].pressionInterstitielle;
                    valeursX[3][1] = 1;
                    valeursY[0][i] = result.pointsDiagrammePression[i].ordonneeMilieuSegment;
                }
            }
            if (para.parametresGeneraux.choixCalcul.equals("C")) {
                for (int i = 0; i < para.options.nombreSegmentsEcran; i++) {
                    valeursX[0][i] = result.pointsDiagrammePression[i].pressionEffective;
                    valeursX[3][1] = 0;
                    valeursY[0][i] = result.pointsDiagrammePression[i].ordonneeMilieuSegment;
                }
            }
            if (un) {
                final String nomaxeX = "Pressions (kN/m�)";
                final String nomaxeY = "\n\n\n\n\n\n       Cote du rideau (m)";
                final String[] nomcourbe = { "Pression totale", "Pression effective", "Pression interstitielle" };
                final DiapreDiagrammePression diag = new DiapreDiagrammePression(valeursX, valeursY, nomaxeX, nomaxeY, nomcourbe);
                final DiapreDiagramme dd = new DiapreDiagramme(appli_, id_);
                dd.setGraphe(diag);
                dd.setVisible(true);
                un = false;
            }
        }
    }

    class okListener implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            dispose();
        }
    }

    void messageAssistant(final String s) {
        message = new BuDialogMessage(appli_, appli_.getInformationsSoftware(), s);
        message.setSize(600, 150);
        message.setTitle(" ERREUR ");
        message.setResizable(false);
        message.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final Point pos = this.getLocationOnScreen();
        pos.x = pos.x + this.getWidth() / 2 - message.getWidth() / 2;
        pos.y = pos.y + this.getHeight() / 2 - message.getHeight() / 2;
        message.setLocation(pos);
        message.setVisible(true);
    }

    public void testChampsComplet() {
        if (bool == 2 || bool == 0) {
            tab.setEnabled(true);
            graph.setEnabled(true);
        }
        if (bool == 1) {
            tab.setEnabled(false);
            if (bool1 && bool2) {
                bool1 = false;
                messageAssistant("Vous devez saisir la pr�cision");
            }
            bool2 = false;
        }
    }

    class SaisieAction implements FocusListener {

        public void focusGained(final FocusEvent e) {
            bool1 = false;
            testChampsComplet();
            if (saisie.getTextDiapre() != null || saisie.getTextDiapre() != "") {
                bool = 2;
            }
            bool1 = false;
            testChampsComplet();
        }

        public void focusLost(final FocusEvent e) {
            bool1 = false;
            testChampsComplet();
            if (saisie.getTextDiapre() == null || saisie.getTextDiapre().equals("")) {
                bool = 1;
            }
            bool1 = true;
            testChampsComplet();
        }
    }
}
