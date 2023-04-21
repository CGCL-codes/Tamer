package org.fudaa.fudaa.sipor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuResource;
import com.memoire.bu.BuScrollPane;
import com.memoire.yapod.*;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.sipor.SParametresSipor;
import org.fudaa.dodico.corba.sipor.SResultatsIterations;
import org.fudaa.ebli.graphe.BGraphe;
import org.fudaa.fudaa.commun.projet.FudaaProjet;

/**
 * Onglet "histogrammes diff�rentiels" de l'exploitation de plusieurs simulations.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Bertrand Audinet
 */
public class SiporHistogrammesDifferentielsPanel extends BuPanel {

    SiporImplementation sipor;

    String[][] listeOrdonnees2 = { { "Totales", "Quai", "Ecluse", "Mar�e", "Cr�neau" }, { "Lin�aire", "Dur�e" } };

    JTable tQuaiCateg = new JTable();

    JList lOrd2 = new JList(new DefaultListModel());

    JComboBox boxSimul1;

    JComboBox boxSimul2;

    JComboBox boxAbs;

    JComboBox boxOrd1;

    int[] indices;

    Vector donnees = new Vector();

    Vector abscisses = new Vector();

    /**
    *  Constructeur.
    *  @param _appli instance de SiporImplementation.
    *  @param _listeSimulations liste des simulations qu'il est possible d'utiliser.
    */
    public SiporHistogrammesDifferentielsPanel(final BuCommonInterface _appli, final Object[] _listeSimulations) {
        super();
        sipor = (SiporImplementation) _appli.getImplementation();
        final String[] listeAbscisses = { "Cat�gories", "Quais" };
        final String[] listeOrdonnees1 = { "Somme des attentes", "Moyennes des attentes", "Somme attentes �cret�es", "Moyenne attentes �cr�t�es", "Rapport attentes/tps service", "Taux occupation" };
        final int nbElts = _listeSimulations.length;
        final String[] listeSimuls = new String[nbElts];
        for (int i = 0; i < nbElts; i++) {
            listeSimuls[i] = (String) _listeSimulations[i];
        }
        final int[] width = { 10, 100, 125, 125, 125, 10 };
        final int[] height = { 10, 30, 40, 40, 30, 40, 35, 10 };
        final HIGLayout lm = new HIGLayout(width, height);
        final HIGConstraints c = new HIGConstraints();
        setLayout(lm);
        add(new BuLabel("Op�ration : "), c.rc(2, 2));
        boxSimul1 = new JComboBox(listeSimuls);
        boxSimul1.setRenderer(new AfficheurNomsFichiers());
        add(boxSimul1, c.rc(2, 3));
        add(new BuLabel("moins"), c.rc(2, 4, ""));
        boxSimul2 = new JComboBox(listeSimuls);
        boxSimul2.setRenderer(new AfficheurNomsFichiers());
        add(boxSimul2, c.rc(2, 5));
        add(new BuLabel("Abscisses : "), c.rc(5, 2));
        boxAbs = new JComboBox(listeAbscisses);
        add(boxAbs, c.rcwh(5, 3, 2, 1, "l"));
        add(new BuLabel("Ordonn�es : "), c.rc(6, 2));
        boxOrd1 = new JComboBox(listeOrdonnees1);
        final BuScrollPane sp1 = new BuScrollPane(lOrd2);
        add(boxOrd1, c.rcwh(6, 3, 2, 1, "l"));
        add(sp1, c.rc(6, 5, "lltt"));
        boxOrd1.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                Object[] lst;
                final int index = boxOrd1.getSelectedIndex();
                if (index == 5) {
                    lst = listeOrdonnees2[1];
                } else {
                    lst = listeOrdonnees2[0];
                }
                final DefaultListModel modele = (DefaultListModel) lOrd2.getModel();
                modele.removeAllElements();
                for (int i = 0; i < lst.length; i++) {
                    modele.addElement(lst[i]);
                }
            }
        });
        boxOrd1.setSelectedIndex(0);
        final DefaultTableModel modele = (DefaultTableModel) tQuaiCateg.getModel();
        modele.addColumn("Quai");
        modele.addColumn("Cat�gorie");
        Object[] liste = new Object[21];
        liste[0] = "Tous";
        for (int i = 1; i <= 20; i++) {
            liste[i] = "n�" + i;
        }
        TableColumn colonne = tQuaiCateg.getColumnModel().getColumn(0);
        colonne.setCellEditor(new DefaultCellEditor(new JComboBox(liste)));
        liste = new Object[31];
        liste[0] = "Toutes";
        for (int i = 1; i <= 30; i++) {
            liste[i] = "n�" + i;
        }
        colonne = tQuaiCateg.getColumnModel().getColumn(1);
        colonne.setCellEditor(new DefaultCellEditor(new JComboBox(liste)));
        final BuScrollPane sp = new BuScrollPane(tQuaiCateg);
        add(new BuLabel("Utiliser : "), c.rc(3, 2));
        add(sp, c.rcwh(3, 3, 2, 2));
        final BuButton bAjouter = new BuButton(BuResource.BU.getIcon("AJOUTER"), "Ajouter");
        bAjouter.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                final Object[] ligne = {};
                ((DefaultTableModel) tQuaiCateg.getModel()).addRow(ligne);
            }
        });
        add(bAjouter, c.rc(3, 5, "t"));
        final BuButton bEnlever = new BuButton(BuResource.BU.getIcon("ENLEVER"), "Enlever");
        bEnlever.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                final int ligne = tQuaiCateg.getSelectedRow();
                int nbLignes = tQuaiCateg.getRowCount();
                tQuaiCateg.removeEditor();
                if (ligne >= 0 && nbLignes > 0) {
                    ((DefaultTableModel) tQuaiCateg.getModel()).removeRow(ligne);
                }
                nbLignes = tQuaiCateg.getRowCount();
                if (nbLignes > 0) {
                    tQuaiCateg.setRowSelectionInterval(0, 0);
                }
            }
        });
        add(bEnlever, c.rc(4, 5, "b"));
        final BuButton bAfficher = new BuButton(BuResource.BU.getIcon("VOIR"), "Afficher");
        bAfficher.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                final SiporFilleResultatsSimulations dlg = sipor.fResultatsSimulations_;
                final SiporFilleGraphe frame = new SiporFilleGraphe(sipor);
                frame.setSize(dlg.dlgParamAff.largeurGraphe, dlg.dlgParamAff.hauteurGraphe);
                frame.setGraphe(creerGraphique());
            }
        });
        add(bAfficher, c.rcwh(7, 2, 4, 1, "b"));
    }

    /** Filtre les r�sultats pour ne garder que ceux correspondant � la description demand�e.*/
    private YapodQuery filtreResultats(final SResultatsIterations res) {
        final YapodQuery q0 = SiporOutilsCalculResultat.construit(res);
        String chaineQuai, chaineCateg, numero;
        final int nbLignes = tQuaiCateg.getRowCount();
        final Vector vQueries = new Vector();
        for (int i = 0; i < nbLignes; i++) {
            chaineQuai = (String) tQuaiCateg.getValueAt(i, 0);
            chaineCateg = (String) tQuaiCateg.getValueAt(i, 1);
            YapodQuery q1;
            if (chaineQuai != "Tous" && chaineQuai != null) {
                numero = chaineQuai.substring(2);
                q1 = SiporOutilsCalculResultat.recherche("numeroQuai", "==", new Integer(numero), q0);
            } else {
                q1 = q0;
            }
            YapodQuery q2;
            if (chaineCateg != "Toutes" && chaineCateg != null) {
                numero = chaineCateg.substring(2);
                q2 = SiporOutilsCalculResultat.recherche("numeroCategorie", "==", new Integer(numero), q1);
            } else {
                q2 = q1;
            }
            if (chaineQuai != null && chaineCateg != null) {
                vQueries.add(q2);
            }
        }
        final YapodQuery[] queries = new YapodQuery[vQueries.size()];
        for (int i = 0; i < vQueries.size(); i++) {
            queries[i] = (YapodQuery) vQueries.elementAt(i);
        }
        final YapodQuery q3 = new YapodCrossQuery(queries);
        final YapodQuery q4 = new YapodMapQuery(new YapodDistinctQuery(YapodConstants.DUMMY), q3);
        return q4;
    }

    /** Recherche l'ensemble des abscisses � afficher.*/
    private Vector rechercheAbscisses(final SParametresSipor params) {
        final Vector vect = new Vector();
        final int nbLignes = tQuaiCateg.getRowCount();
        final int col = (boxAbs.getSelectedIndex() == 0 ? 1 : 0);
        for (int i = 0; i < nbLignes; i++) {
            final String valeur = (String) tQuaiCateg.getValueAt(i, col);
            if (valeur != null && valeur.startsWith("Tou")) {
                final int nb = (col == 0 ? params.quais.nombreQuais : params.categories.nombreCategories);
                for (int j = 0; j < nb; j++) {
                    vect.add(new Integer(j + 1));
                }
                return vect;
            } else if (valeur != null) {
                vect.add(new Integer(valeur.substring(2)));
            }
        }
        return vect;
    }

    /**
    * Un point de la courbe est (valeur de la simulation 1) - (valeur de la simulation 2). Cette m�thode
    * permet de calculer les op�randes de la diff�rence.
    */
    private double calculerOrdonnees(final YapodQuery _donnees, final int indexOrd1, final int indexOrd2, final SParametresSipor params) {
        if (indexOrd1 >= 0 && indexOrd1 <= 4) {
            return SiporOutilsCalculResultat.calculY(params, 5 * indexOrd1 + indexOrd2, _donnees) / 60.0;
        } else if (indexOrd1 == 5) {
            return SiporOutilsCalculResultat.calculY(params, 36 + indexOrd2, _donnees) * 100.0;
        } else {
            return 0.0;
        }
    }

    /**
    *   V�rifie que tous les quais et les cat�gories demand�s dans tQuaiCateg existent dans
    *   les param�tres des simulations. Si l'un u l'autre n'existe pas un message est affich�.
    *   Cela n'empeche pas la cr�ation des graphes : les valeurs seront �gales � z�ro.
    */
    private void verifierFiltreDemande(final SParametresSipor pFich1, final SParametresSipor pFich2, final String nFich1, final String nFich2) {
        final int nbLignes = tQuaiCateg.getRowCount();
        final Hashtable tQuais = new Hashtable();
        final Hashtable tCategories = new Hashtable();
        for (int i = 0; i < nbLignes; i++) {
            final String quai = (String) tQuaiCateg.getValueAt(i, 0);
            if (quai != "Tous" && quai != null) {
                final Integer numeroQuai = new Integer(quai.substring(2));
                tQuais.put(numeroQuai, numeroQuai);
            }
            final String categorie = (String) tQuaiCateg.getValueAt(i, 1);
            if (categorie != "Toutes" && categorie != null) {
                final Integer numeroCategorie = new Integer(categorie.substring(2));
                tCategories.put(numeroCategorie, numeroCategorie);
            }
        }
        final Object[] quaisDem = tQuais.keySet().toArray();
        for (int i = 0; i < quaisDem.length; i++) {
            final int numQuai = ((Integer) quaisDem[i]).intValue();
            if (numQuai > pFich1.quais.nombreQuais) {
                new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Pas de quai " + numQuai + " dans \n" + nFich1).activate();
            }
            if (numQuai > pFich2.quais.nombreQuais) {
                new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Pas de quai " + numQuai + " dans \n" + nFich2).activate();
            }
        }
        final Object[] categDem = tCategories.keySet().toArray();
        SiporOutilsCalculResultat.print(tCategories.elements());
        for (int i = 0; i < categDem.length; i++) {
            final int numCat = ((Integer) categDem[i]).intValue();
            if (numCat > pFich1.categories.nombreCategories) {
                new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Pas de cat�gorie " + numCat + " dans \n" + nFich1).activate();
            }
            if (numCat > pFich2.categories.nombreCategories) {
                new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Pas de cat�gorie " + numCat + " dans \n" + nFich2).activate();
            }
        }
    }

    /** Calcule les donn�es � afficher dans le graphique.*/
    private void calculerDonnees() {
        final String fichier1 = (String) boxSimul1.getSelectedItem();
        final String fichier2 = (String) boxSimul2.getSelectedItem();
        final SiporOutilsDonnees odFich1 = new SiporOutilsDonnees((FudaaProjet) sipor.projets_.get(fichier1));
        final SiporOutilsDonnees odFich2 = new SiporOutilsDonnees((FudaaProjet) sipor.projets_.get(fichier2));
        final SParametresSipor paramsFich1 = odFich1.getParametres();
        final SParametresSipor paramsFich2 = odFich2.getParametres();
        verifierFiltreDemande(paramsFich1, paramsFich2, fichier1, fichier2);
        System.out.println("Verif filtre termin�e");
        final YapodQuery resFich1 = filtreResultats(odFich1.getResultats());
        final YapodQuery resFich2 = filtreResultats(odFich2.getResultats());
        System.out.println("Filtres r�sultats termin�s");
        final Vector vAbscisses = rechercheAbscisses(paramsFich1);
        vAbscisses.addAll(rechercheAbscisses(paramsFich2));
        final YapodQuery q0 = new YapodStaticVectorQuery(vAbscisses);
        final YapodQuery q1 = new YapodDistinctQuery(q0);
        abscisses.removeAllElements();
        final Enumeration enumeration = q1.getResult();
        while (enumeration.hasMoreElements()) {
            abscisses.add(enumeration.nextElement());
        }
        System.out.println("Recherche abscisse termin�e");
        final String champ = (boxAbs.getSelectedIndex() == 0 ? "numeroCategorie" : "numeroQuai");
        indices = lOrd2.getSelectedIndices();
        final int nbCourbes = indices.length;
        final int indexOrd1 = boxOrd1.getSelectedIndex();
        donnees.removeAllElements();
        for (int i = 0; i < nbCourbes; i++) {
            donnees.add(new Vector(abscisses.size()));
        }
        for (int j = 0; j < abscisses.size(); j++) {
            final Integer abs = (Integer) abscisses.elementAt(j);
            final YapodQuery donneesPourCalcul1 = SiporOutilsCalculResultat.recherche(champ, "==", abs, resFich1);
            final YapodQuery donneesPourCalcul2 = SiporOutilsCalculResultat.recherche(champ, "==", abs, resFich2);
            System.out.println("abscisse " + j);
            for (int i = 0; i < nbCourbes; i++) {
                System.out.println("  courbe " + i);
                final Vector courbe = (Vector) donnees.elementAt(i);
                final double v1 = calculerOrdonnees(donneesPourCalcul1, indexOrd1, indices[i], paramsFich1);
                final double v2 = calculerOrdonnees(donneesPourCalcul2, indexOrd1, indices[i], paramsFich2);
                final Point pt = new Point();
                pt.x = abs.doubleValue();
                pt.y = v2 - v1;
                courbe.add(pt);
            }
        }
    }

    public final class Point {

        public double x;

        public double y;

        public Point() {
            x = 0;
            y = 0;
        }
    }

    /** Cr�� le code du graphique.*/
    BGraphe creerGraphique() {
        calculerDonnees();
        final BGraphe graphe = new BGraphe();
        final SiporFilleResultatsSimulations dlg = sipor.fResultatsSimulations_;
        final boolean bCouleurs = dlg.dlgParamAff.couleur;
        final String[] c1 = { "33CCFF", "FFFFCC", "FFCCCC", "99FFCC" };
        final String[] c2 = { "000000", "606060", "808080", "a0a0a0", "c0c0c0", "e0e0e0" };
        final String[] couleurs = (bCouleurs ? c1 : c2);
        final YapodQuery q0 = new YapodStaticVectorQuery(donnees);
        final YapodQuery q2 = new YapodFlattenQuery(q0);
        final YapodQuery q22 = new YapodCountQuery(q2);
        if (((Integer) YapodLib.first(q22.getResult())).intValue() == 0) {
            return graphe;
        }
        final YapodQuery q3 = new YapodAccessQuery("x", q2);
        final YapodQuery q4 = new YapodAccessQuery("y", q2);
        int minAbs = 0;
        if (dlg.dlgParamAff.minAbsAuto) {
            final YapodQuery q5 = new YapodMinQuery(q3);
            final Double db = (Double) YapodLib.first(q5.getResult());
            minAbs = (int) Math.rint(db.doubleValue()) - 1;
            System.out.println("minAbs auto : " + minAbs);
        } else {
            minAbs = dlg.dlgParamAff.minAbs;
        }
        int maxAbs = 0;
        if (dlg.dlgParamAff.maxAbsAuto) {
            final YapodQuery q5 = new YapodMaxQuery(q3);
            final Double db = (Double) YapodLib.first(q5.getResult());
            maxAbs = (int) Math.rint(db.doubleValue());
            System.out.println("maxAbs auto : " + maxAbs);
        } else {
            maxAbs = dlg.dlgParamAff.maxAbs;
        }
        int minOrd = 0;
        if (dlg.dlgParamAff.minOrdAuto) {
            final YapodQuery q5 = new YapodMinQuery(q4);
            final Double db = (Double) YapodLib.first(q5.getResult());
            minOrd = (int) Math.rint(db.doubleValue());
            System.out.println("minOrd auto : " + minOrd);
        } else {
            minOrd = dlg.dlgParamAff.minOrd;
        }
        int maxOrd = 0;
        if (dlg.dlgParamAff.maxOrdAuto) {
            final YapodQuery q5 = new YapodMaxQuery(q4);
            final Double db = (Double) YapodLib.first(q5.getResult());
            maxOrd = (int) Math.rint(db.doubleValue());
            System.out.println("maxOrd auto : " + maxOrd);
        } else {
            maxOrd = dlg.dlgParamAff.maxOrd;
        }
        String uniteOrd;
        final String uniteAbs = "";
        final int idOrd = boxOrd1.getSelectedIndex();
        if (idOrd < 4) {
            uniteOrd = "heures";
        } else if (idOrd > 4) {
            uniteOrd = "%";
        } else {
            uniteOrd = "";
        }
        String grStr = "";
        grStr += "graphe\n{\n";
        grStr += "  titre \"" + dlg.tfTitre.getText() + "\"\n";
        grStr += "  animation non\n";
        grStr += "  legende " + (dlg.dlgParamAff.legende ? "oui" : "non") + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "  marges\n  {\n";
        grStr += "    gauche " + dlg.dlgParamAff.margeG + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    droite " + dlg.dlgParamAff.margeD + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    haut   " + dlg.dlgParamAff.margeH + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    bas    " + dlg.dlgParamAff.margeB + "\n  }\n";
        grStr += "  axe\n  {\n";
        grStr += "    titre \"" + dlg.tfTitreAbs.getText() + "\"\n";
        grStr += "    unite \"" + uniteAbs + "\"\n";
        grStr += "    orientation horizontal\n";
        grStr += "    graduations non\n";
        grStr += "    minimum " + minAbs + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    maximum " + maxAbs + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    etiquettes {\n";
        for (int i = 0; i < abscisses.size(); i++) {
            final Integer num = (Integer) abscisses.elementAt(i);
            grStr += "      " + (num.doubleValue() - 0.5) + " " + num + CtuluLibString.LINE_SEP_SIMPLE;
        }
        grStr += "    }\n";
        grStr += "  }\n";
        grStr += "  axe\n  {\n";
        grStr += "    titre \"" + dlg.tfTitreOrd.getText() + "\"\n";
        grStr += "    unite \"" + uniteOrd + "\"\n";
        grStr += "    orientation vertical\n";
        grStr += "    graduations oui\n";
        grStr += "    minimum " + minOrd + CtuluLibString.LINE_SEP_SIMPLE;
        grStr += "    maximum " + maxOrd + "\n  }\n";
        final int nbCourbes = donnees.size();
        for (int i = 0; i < nbCourbes; i++) {
            final Vector v = (Vector) donnees.elementAt(i);
            String titre = "" + boxOrd1.getSelectedItem() + " ";
            titre += listeOrdonnees2[(boxOrd1.getSelectedIndex() == 5 ? 1 : 0)][indices[i]].toLowerCase();
            grStr += "  courbe\n  {\n";
            grStr += "    titre \"" + titre + "\"\n";
            grStr += "    type histogramme\n";
            grStr += "    aspect { \n";
            if (bCouleurs) {
                grStr += "      contour.couleur " + couleurs[i % couleurs.length] + CtuluLibString.LINE_SEP_SIMPLE;
            } else {
                grStr += "      contour.couleur 000000\n";
            }
            grStr += "      surface.couleur " + couleurs[i % couleurs.length] + CtuluLibString.LINE_SEP_SIMPLE;
            grStr += "    }\n";
            grStr += "    trace lineaire\n";
            grStr += "    marqueurs oui\n";
            grStr += "    valeurs\n    {\n";
            for (int j = 0; j < v.size(); j++) {
                final Point pt = (Point) v.elementAt(j);
                final double xMin = pt.x - 0.5 - 0.05 * (nbCourbes - 1);
                grStr += "" + (xMin + i * 0.1) + " " + pt.y + CtuluLibString.LINE_SEP_SIMPLE;
            }
            grStr += "    }\n  }\n";
        }
        grStr += "}\n";
        graphe.setFluxDonnees(new ByteArrayInputStream(grStr.getBytes()));
        return graphe;
    }
}
