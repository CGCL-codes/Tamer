package org.fudaa.fudaa.sipor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.sipor.SParametresBassin;
import org.fudaa.dodico.corba.sipor.SParametresCategorie;
import org.fudaa.dodico.corba.sipor.SParametresCategories;
import org.fudaa.dodico.corba.sipor.SParametresPeriode;
import org.fudaa.dodico.corba.sipor.SParametresQuaiPreferentiel;
import org.fudaa.dodico.corba.sipor.SParametresQuais;

/**
 * Description de l'onglet des parametres des categories.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Bertrand Audinet
 */
public class SiporCategoriesParametres extends BuPanel implements ActionListener, FocusListener {

    protected JComboBox listeCategories;

    protected JCheckBox active;

    protected BuTextField piedPilote;

    protected BuTextField nom;

    protected JComboBox priorite;

    protected BuTextField longueur;

    protected TextFieldsMeters tirantEauEntree;

    protected TextFieldsMeters tirantEauSortie;

    protected JComboBox categorieSortie;

    protected BuButton quaiPreferentiel;

    protected BuButton periodeArrivee;

    protected SiporQuaisPreferentielsParametres quaiBox;

    protected SiporPeriodeParametres periodeBox;

    private BuLabel label;

    private JDialog message;

    private int oldIndex = -1;

    private SiporImplementation sipor;

    private BuCommonInterface appli_;

    private SParametresCategories localCategories;

    private SParametresQuais localQuais;

    /**
   * Constructeur du panneau d'affichage de l'onglet categories.
   */
    public SiporCategoriesParametres(final BuCommonInterface _appli) {
        super();
        appli_ = _appli;
        sipor = ((SiporImplementation) appli_.getImplementation());
        localCategories = sipor.outils_.getCategories();
        localQuais = sipor.outils_.getQuais();
        final GridBagLayout baglayout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        setLayout(baglayout);
        c.fill = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        label = new BuLabel("Pied de pilote (% du tirant d'eau maxi 30) :");
        baglayout.setConstraints(label, c);
        add(label);
        listeCategories = new JComboBox();
        for (int i = 0; i < 30; i++) {
            listeCategories.addItem("Cat�gories n� " + (i + 1));
        }
        baglayout.setConstraints(listeCategories, c);
        add(listeCategories);
        label = new BuLabel(" ");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Nom de la cat�gorie :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Niveau de priorite :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Longueur de la cat�gorie :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Tirant d'eau en entr�e :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Tirant d'eau en sortie :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Cat�gorie cr��e en sortie :");
        baglayout.setConstraints(label, c);
        add(label);
        quaiPreferentiel = new BuButton("Quais Accessibles");
        quaiPreferentiel.addActionListener(this);
        baglayout.setConstraints(quaiPreferentiel, c);
        add(quaiPreferentiel);
        c.gridx = 1;
        piedPilote = BuTextField.createIntegerField();
        piedPilote.setColumns(3);
        piedPilote.setHorizontalAlignment(SwingConstants.RIGHT);
        piedPilote.addFocusListener(this);
        baglayout.setConstraints(piedPilote, c);
        add(piedPilote);
        active = new JCheckBox("Activer cette cat�gorie");
        baglayout.setConstraints(active, c);
        add(active);
        label = new BuLabel(" ");
        baglayout.setConstraints(label, c);
        add(label);
        nom = new BuTextField();
        nom.setColumns(20);
        nom.addFocusListener(this);
        baglayout.setConstraints(nom, c);
        add(nom);
        priorite = new JComboBox();
        priorite.addItem("                ");
        priorite.addItem("prioritaire");
        priorite.addItem("non prioritaire");
        baglayout.setConstraints(priorite, c);
        add(priorite);
        longueur = BuTextField.createIntegerField();
        longueur.setColumns(10);
        longueur.setHorizontalAlignment(SwingConstants.RIGHT);
        baglayout.setConstraints(longueur, c);
        add(longueur);
        tirantEauEntree = new TextFieldsMeters();
        baglayout.setConstraints(tirantEauEntree, c);
        add(tirantEauEntree);
        tirantEauSortie = new TextFieldsMeters();
        baglayout.setConstraints(tirantEauSortie, c);
        add(tirantEauSortie);
        categorieSortie = new JComboBox();
        baglayout.setConstraints(categorieSortie, c);
        add(categorieSortie);
        periodeArrivee = new BuButton("P�riodes d'arriv�es");
        periodeArrivee.addActionListener(this);
        baglayout.setConstraints(periodeArrivee, c);
        add(periodeArrivee);
    }

    /**
   * Accesseur de l'onglet des cat�gories.
   */
    public SParametresCategories getCategories() {
        if (localCategories.nombreCategories != 0) {
            sauvegardeCategorie(oldIndex);
            localCategories.piedPilote = ((Integer) piedPilote.getValue()).intValue();
            return localCategories;
        }
        return null;
    }

    /**
   * Accesseur de l'onglet des cat�gories.
   */
    public void setCategories(final SParametresCategories params) {
        localCategories = sipor.outils_.getCategories();
        localQuais = sipor.outils_.getQuais();
        localCategories.nombreCategories = params.nombreCategories;
        localCategories.piedPilote = params.piedPilote;
        piedPilote.setValue(new Integer(params.piedPilote));
        for (int i = 0; i < params.nombreCategories; i++) {
            localCategories.categories[i] = params.categories[i];
        }
        if (categorieSortie.getItemCount() != 0) {
            categorieSortie.removeAllItems();
        }
        categorieSortie.addItem("aucune              ");
        for (int i = 0; i < localCategories.nombreCategories; i++) {
            if (params.categories[i].nomCategorie.length() > 22) {
                categorieSortie.addItem("" + (i + 1) + ": " + params.categories[i].nomCategorie.substring(0, 16) + "...");
            } else {
                categorieSortie.addItem("" + (i + 1) + ": " + params.categories[i].nomCategorie);
            }
        }
        listeCategories.removeActionListener(this);
        listeCategories.setSelectedIndex(0);
        listeCategories.addActionListener(this);
        oldIndex = 0;
        afficheCategorie(0);
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == active) {
            if (active.isSelected()) {
                if (oldIndex == localCategories.nombreCategories) {
                    localCategories.nombreCategories++;
                    categorieSortie.addItem(nom.getValue());
                    localCategories.categories[oldIndex] = new SParametresCategorie("", 0, 0, 0.0, 0.0, 0, 0, new SParametresQuaiPreferentiel[20], 0, new SParametresPeriode[3]);
                } else {
                    messageAssistant("L'activation des cat�gories se fait dans\n" + "l'ordre croissant des num�ros de quais.");
                }
            } else {
                if (oldIndex == (localCategories.nombreCategories - 1)) {
                    if (categorieLibre(oldIndex) == -1) {
                        localCategories.nombreCategories--;
                        categorieSortie.removeItemAt(oldIndex + 1);
                        localCategories.categories[oldIndex] = null;
                    } else {
                        messageAssistant("Avant de supprimer cette cat�gorie, vous devez\n" + "supprimer ou modifier le ou les cat�gories\n" + "qui l'utilisent comme cat�gorie de sortie,\n" + "notamment la cat�gorie n� " + (categorieLibre(oldIndex) + 1) + CtuluLibString.DOT);
                    }
                } else {
                    messageAssistant("La suppression des quais se fait dans\n" + "l'ordre d�croissant des num�ros de quais.");
                }
            }
        } else if (e.getSource() == listeCategories) {
            sauvegardeCategorie(oldIndex);
            oldIndex = listeCategories.getSelectedIndex();
        } else if (e.getSource() == quaiPreferentiel) {
            sauvegardeCategorie(oldIndex);
            quaiBox = new SiporQuaisPreferentielsParametres(appli_);
            quaiBox.setValue(oldIndex);
            quaiBox.setVisible(true);
        } else if (e.getSource() == periodeArrivee) {
            sauvegardeCategorie(oldIndex);
            periodeBox = new SiporPeriodeParametres(appli_);
            periodeBox.setValue(oldIndex);
            periodeBox.setVisible(true);
        }
        afficheCategorie(oldIndex);
    }

    public void focusGained(final FocusEvent e) {
    }

    public void focusLost(final FocusEvent e) {
        if (e.getSource() == piedPilote) {
            zonePiedPilote();
        } else if (e.getSource() == nom) {
            sauvegardeCategorie(oldIndex);
            for (int i = oldIndex; i < categorieSortie.getItemCount(); i++) {
                categorieSortie.removeItemAt(oldIndex + 1);
            }
            for (int i = oldIndex; i < localCategories.nombreCategories; i++) {
                if (localCategories.categories[i].nomCategorie.length() > 22) {
                    categorieSortie.addItem("" + (i + 1) + ": " + localCategories.categories[i].nomCategorie.substring(0, 16) + "...");
                } else {
                    categorieSortie.addItem("" + (i + 1) + ": " + localCategories.categories[i].nomCategorie);
                }
            }
            afficheCategorie(oldIndex);
        } else if (e.getSource() == longueur) {
            if (!longueurValide(oldIndex)) {
                messageAssistant("Aucun quai n'est assez long pour cette Cat�gorie.\n" + "Pensez � la redimensionner ou � cr�er un nouveau quai.");
            }
        }
    }

    /**
   * v�rifie les parametres saisis et renvoie les erreurs fatales.
   */
    public static String chercheErreur(final SParametresCategories localCategories, final SParametresQuais localQuais, final SParametresBassin localBassin1, final SParametresBassin localBassin2, final SParametresBassin localBassin3) {
        String mes = "";
        if (localCategories.nombreCategories == 0) {
            mes += "Une cat�gorie obligatoire pour la simulation.\n";
        } else {
            for (int i = 0; i < localCategories.nombreCategories; i++) {
                if (localCategories.categories[i].nomCategorie.equalsIgnoreCase("")) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : nom inconnu.\n";
                } else {
                    for (int j = 0; j < localCategories.nombreCategories; j++) {
                        if ((localCategories.categories[i].nomCategorie.equalsIgnoreCase(localCategories.categories[j].nomCategorie)) && (i != j)) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : nom identique pour la categorie n� " + (j + 1) + ".\n";
                        }
                    }
                }
                if (localCategories.categories[i].niveauPriorite == 0) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : niveau de priorit� inconnu.\n";
                }
                if (localCategories.categories[i].longueurCategorie == 0) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : longueur incorrecte.\n";
                }
                if ((!localBassin1.creneauMaree.presenceCreneauMaree) && ((tirantEauEntree(localCategories, i) > localBassin1.hauteurEau) || (tirantEauEntree(localCategories, i) > localBassin1.profondeurChenal)) && (!localBassin2.creneauMaree.presenceCreneauMaree) && ((tirantEauEntree(localCategories, i) > localBassin2.hauteurEau) || (tirantEauEntree(localCategories, i) > localBassin2.profondeurChenal)) && (!localBassin3.creneauMaree.presenceCreneauMaree) && ((tirantEauEntree(localCategories, i) > localBassin3.hauteurEau) || (tirantEauEntree(localCategories, i) > localBassin3.profondeurChenal))) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : tirant d'eau en entr�e trop grand, pas d'acc�s au port.\n";
                }
                if ((!localBassin1.creneauMaree.presenceCreneauMaree) && ((tirantEauSortie(localCategories, i) > localBassin1.hauteurEau) || (tirantEauSortie(localCategories, i) > localBassin1.profondeurChenal)) && (!localBassin2.creneauMaree.presenceCreneauMaree) && ((tirantEauSortie(localCategories, i) > localBassin2.hauteurEau) || (tirantEauSortie(localCategories, i) > localBassin2.profondeurChenal)) && (!localBassin3.creneauMaree.presenceCreneauMaree) && ((tirantEauSortie(localCategories, i) > localBassin3.hauteurEau) || (tirantEauSortie(localCategories, i) > localBassin3.profondeurChenal))) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : tirant d'eau en sortie trop grand, ne peut pas sortir du port.\n";
                }
                if (localCategories.categories[i].nombreQuaisPreferentiels == 0) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : un quai pr�f�rentiel obligatoire.\n";
                } else {
                    for (int j = 0; j < localCategories.categories[i].nombreQuaisPreferentiels; j++) {
                        if (localCategories.categories[i].quaisPreferentiels[j].numeroQuai == 0) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : aucun quai choisi\n";
                        } else {
                            if (localCategories.categories[i].longueurCategorie > localQuais.quais[localCategories.categories[i].quaisPreferentiels[j].numeroQuai - 1].longueurQuai) {
                                mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : longueur de la cat�gorie > longueur du quai.\n";
                            }
                            if (j > 0) {
                                if (localCategories.categories[i].quaisPreferentiels[j].dureeAttenteAdmissible > localCategories.categories[i].quaisPreferentiels[j - 1].dureeAttenteAdmissible) {
                                    mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : dur�e attente admissible > � la pr�f�rence pr�c�dente\n";
                                }
                            }
                            if (localCategories.categories[i].quaisPreferentiels[j].dureeMoyOperations < localCategories.categories[i].quaisPreferentiels[j].dureeMinOperations) {
                                mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : dur�e moyenne < dur�e minimale\n";
                            }
                            if (localCategories.categories[i].quaisPreferentiels[j].dureeMaxOperations < localCategories.categories[i].quaisPreferentiels[j].dureeMoyOperations) {
                                mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : dur�e maximale < dur�e moyenne\n";
                            }
                            if (localCategories.categories[i].quaisPreferentiels[j].ordreLoiErlang == 0) {
                                mes += "Erreur Categorie n� " + (i + 1) + " : Erreur quai pr�f�rentiel n� " + (j + 1) + " : aucun ordre d'Erlang choisi\n";
                            }
                        }
                    }
                }
                if (localCategories.categories[i].nombrePeriodes == 0) {
                    mes += "Erreur Categorie n� " + (i + 1) + " : Une p�riode obligatoire.\n";
                } else {
                    for (int j = 0; j < localCategories.categories[i].nombrePeriodes; j++) {
                        if (localCategories.categories[i].periodes[j].dateFinPeriode < localCategories.categories[i].periodes[j].dateDebutPeriode) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : date de fin < date de d�but\n";
                        } else {
                            if (j > 0) {
                                if (localCategories.categories[i].periodes[j].dateFinPeriode < localCategories.categories[i].periodes[j - 1].dateDebutPeriode) {
                                    mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : chevauchement avec la p�riode pr�c�dente.\n";
                                }
                            }
                            if ((localCategories.categories[i].periodes[j].datePremiereArrivee < localCategories.categories[i].periodes[j].dateDebutPeriode) || (localCategories.categories[i].periodes[j].datePremiereArrivee > localCategories.categories[i].periodes[j].dateFinPeriode)) {
                                mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : date arriv�e hors de la p�riode.\n";
                            }
                        }
                        if (localCategories.categories[i].periodes[j].ecartMoyArrivees < localCategories.categories[i].periodes[j].ecartMinArrivees) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : �cart moyen < �cart minimal\n";
                        }
                        if (localCategories.categories[i].periodes[j].ecartMaxArrivees < localCategories.categories[i].periodes[j].ecartMoyArrivees) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : �cart maximal < �cart moyen\n";
                        }
                        if (localCategories.categories[i].periodes[j].ordreLoiErlang == 0) {
                            mes += "Erreur Categorie n� " + (i + 1) + " : p�riode n� " + (j + 1) + " : aucun ordre d'Erlang choisi\n";
                        }
                    }
                }
            }
        }
        return mes;
    }

    /**
   * v�rifie les parametres saisis et renvoie les avertissement.
   */
    public static String chercheAvertissement(final SParametresCategories localCategories) {
        String mes = "";
        if ((localCategories.piedPilote == 0) && (localCategories.nombreCategories != 0)) {
            mes += "pied de pilote = 0\n";
        }
        for (int i = 0; i < localCategories.nombreCategories; i++) {
            if (localCategories.categories[i].tirantEauEntree == 0) {
                mes += "Categorie n� " + (i + 1) + " : tirant d'eau en entr�e = 0\n";
            }
            if (localCategories.categories[i].tirantEauSortie == 0) {
                mes += "Categorie n� " + (i + 1) + " : tirant d'eau en sortie = 0\n";
            }
        }
        return mes;
    }

    private void composantsEnabled(final boolean b) {
        nom.setEnabled(b);
        priorite.setEnabled(b);
        longueur.setEnabled(b);
        tirantEauEntree.setEnabled(b);
        tirantEauSortie.setEnabled(b);
        categorieSortie.setEnabled(b);
        if (localQuais.nombreQuais == 0) {
            quaiPreferentiel.setEnabled(false);
        } else {
            quaiPreferentiel.setEnabled(b);
        }
        periodeArrivee.setEnabled(b);
    }

    private void zonePiedPilote() {
        if (((Integer) piedPilote.getValue()).intValue() > 30) {
            piedPilote.setValue(new Integer(0));
            messageAssistant("le pied de Pilote doit est compris\n" + "entre 0 et 30%\n");
        }
    }

    private void afficheCategorie(final int index) {
        active.removeActionListener(this);
        if (localCategories.categories[index] != null) {
            active.setSelected(true);
            composantsEnabled(true);
            nom.setValue(localCategories.categories[index].nomCategorie);
            priorite.setSelectedIndex(localCategories.categories[index].niveauPriorite);
            longueur.setValue(new Integer(localCategories.categories[index].longueurCategorie));
            tirantEauEntree.setValue(localCategories.categories[index].tirantEauEntree);
            tirantEauSortie.setValue(localCategories.categories[index].tirantEauSortie);
            categorieSortie.setSelectedIndex(localCategories.categories[index].numeroCategorieSortie);
        } else {
            active.setSelected(false);
            composantsEnabled(false);
            nom.setValue("");
            priorite.setSelectedIndex(0);
            longueur.setValue(new Integer(0));
            tirantEauEntree.setValue(0);
            tirantEauSortie.setValue(0);
            categorieSortie.setSelectedIndex(0);
        }
        active.addActionListener(this);
    }

    private void sauvegardeCategorie(final int index) {
        if (localCategories.categories[index] != null) {
            if (((String) nom.getValue()).length() > 80) {
                localCategories.categories[index].nomCategorie = ((String) nom.getValue()).substring(0, 80);
            } else {
                localCategories.categories[index].nomCategorie = (String) nom.getValue();
            }
            localCategories.categories[index].niveauPriorite = priorite.getSelectedIndex();
            localCategories.categories[index].longueurCategorie = ((Integer) longueur.getValue()).intValue();
            localCategories.categories[index].tirantEauEntree = tirantEauEntree.toDouble();
            localCategories.categories[index].tirantEauSortie = tirantEauSortie.toDouble();
            localCategories.categories[index].numeroCategorieSortie = categorieSortie.getSelectedIndex();
        }
    }

    private int categorieLibre(final int index) {
        for (int i = 0; i < localCategories.nombreCategories; i++) {
            if ((localCategories.categories[i].numeroCategorieSortie == (index + 1)) && (index != i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean longueurValide(final int index) {
        if (localCategories.categories[index].longueurCategorie == 0) {
            return false;
        }
        for (int i = 0; i < localQuais.nombreQuais; i++) {
            if (localQuais.quais[i].longueurQuai >= localCategories.categories[index].longueurCategorie) {
                return true;
            }
        }
        return false;
    }

    public static double tirantEauEntree(final SParametresCategories localCategories, final int index) {
        return (localCategories.categories[index].tirantEauEntree * (1 + (localCategories.piedPilote / 100)));
    }

    public static double tirantEauSortie(final SParametresCategories localCategories, final int index) {
        return (localCategories.categories[index].tirantEauSortie * (1 + (localCategories.piedPilote / 100)));
    }

    private void messageAssistant(final String s) {
        message = new BuDialogMessage(appli_, SiporImplementation.isSipor_, s);
        message.setSize(500, 200);
        message.setResizable(false);
        message.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final Point pos = this.getLocationOnScreen();
        pos.x = pos.x + this.getWidth() / 2 - message.getWidth() / 2;
        pos.y = pos.y + this.getHeight() / 2 - message.getHeight() / 2;
        message.setLocation(pos);
        message.setVisible(true);
    }
}
