package javaapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author  Aur�lien
 */
public class XItemPlancheListeVillesAvecSuppr extends XItemPlanche {

    private String nom;

    private boolean afficherBouton;

    private boolean afficherIcone;

    private Vector<String> villes;

    public static final int HAUTEUR_INTITULE = 45;

    public static final int HAUTEUR_ELEMENT = 30;

    public static final int ECART_ENTRE_ELEMENTS_AVEC_ICONE = 10;

    public static final int ECART_ENTRE_ELEMENTS_SANS_ICONE = 0;

    public static final int NB_MAX_ELEMENTS_AFFICHES = 5;

    public XItemPlancheListeVillesAvecSuppr(String str, boolean afficherBouton, boolean afficherIcone) {
        this.nom = str;
        this.villes = new Vector<String>();
        this.afficherBouton = afficherBouton;
        this.afficherIcone = afficherIcone;
        initComponents();
        redimensionner();
    }

    public XItemPlancheListeVillesAvecSuppr(String str) {
        this(str, true, true);
    }

    private void initComponents() {
        intituleListe = new javax.swing.JLabel();
        barreDefilementListe = new javax.swing.JScrollPane();
        conteneurListe = new javax.swing.JPanel();
        setMaximumSize(new java.awt.Dimension(32767, 135));
        setMinimumSize(new java.awt.Dimension(0, 135));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(0, 135));
        intituleListe.setFont(new java.awt.Font("Tw Cen MT", 1, 26));
        intituleListe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        intituleListe.setText(nom);
        intituleListe.setPreferredSize(new Dimension(0, HAUTEUR_INTITULE));
        barreDefilementListe.setBackground(Framemain.FOND_VOLET);
        barreDefilementListe.setBorder(null);
        barreDefilementListe.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        barreDefilementListe.setOpaque(false);
        conteneurListe.setLayout(new java.awt.GridLayout(4, 1, 0, afficherIcone ? ECART_ENTRE_ELEMENTS_AVEC_ICONE : ECART_ENTRE_ELEMENTS_SANS_ICONE));
        conteneurListe.setBackground(Framemain.FOND_VOLET);
        conteneurListe.setMinimumSize(new java.awt.Dimension(1, 1));
        conteneurListe.setOpaque(false);
        barreDefilementListe.setViewportView(conteneurListe);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(intituleListe, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(barreDefilementListe, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(intituleListe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(barreDefilementListe, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)));
    }

    public void addElementVille(String nomVille) {
        if (nomVille != null && nomVille != "" && !villes.contains(nomVille)) {
            Element e = new Element(nomVille);
            villes.addElement(nomVille);
            conteneurListe.add(e);
            updateUI();
            redimensionner();
        }
    }

    public void removeElementVille(Element e) {
        if (e != null) {
            villes.remove(e.nomElem);
            conteneurListe.remove(e);
            updateUI();
            redimensionner();
        }
    }

    private void redimensionner() {
        Dimension tailleBloc, tailleListe;
        if (villes == null || villes.size() == 0) {
            tailleBloc = new Dimension(0, 1);
            tailleListe = new Dimension(0, 0);
        } else {
            int ecart = afficherIcone ? ECART_ENTRE_ELEMENTS_AVEC_ICONE : ECART_ENTRE_ELEMENTS_SANS_ICONE;
            int h = villes.size() * (HAUTEUR_ELEMENT + ecart) - ecart;
            int hb = Math.min(villes.size(), NB_MAX_ELEMENTS_AFFICHES) * (HAUTEUR_ELEMENT + ecart) - ecart;
            tailleBloc = new Dimension(0, hb + HAUTEUR_INTITULE + 10);
            tailleListe = new Dimension(0, h + 1);
            conteneurListe.setLayout(new GridLayout(villes.size(), 1, 0, ecart));
        }
        setPreferredSize(tailleBloc);
        setMinimumSize(tailleBloc);
        setMaximumSize(new Dimension(Short.MAX_VALUE, tailleBloc.height));
        conteneurListe.setPreferredSize(tailleListe);
    }

    private javax.swing.JScrollPane barreDefilementListe;

    private javax.swing.JPanel conteneurListe;

    private javax.swing.JLabel intituleListe;

    private class Element extends JPanel {

        private JLabel intituleElement;

        public String nomElem;

        public Element(String nom) {
            this.nomElem = nom;
            initComponents();
        }

        private void initComponents() {
            intituleElement = new JLabel(nomElem);
            setLayout(new BorderLayout());
            setBackground(Framemain.FOND_VOLET);
            intituleElement.setFont(new Font(NOM_POLICE, 1, 26));
            intituleElement.setHorizontalAlignment(SwingConstants.LEFT);
            add(intituleElement, BorderLayout.CENTER);
            if (afficherIcone) {
                JPanel icone = new JPanel();
                icone.setBackground(new Color(0, 204, 204));
                icone.setPreferredSize(new Dimension(30, 30));
                add(icone, BorderLayout.WEST);
            }
            if (afficherBouton) {
                XItemPlancheBoutonRetirerDeLaListe boutonElement = new XItemPlancheBoutonRetirerDeLaListe();
                boutonElement.setPreferredSize(new Dimension(110, 110));
                add(boutonElement, BorderLayout.EAST);
            }
            setPreferredSize(new Dimension(HAUTEUR_ELEMENT, HAUTEUR_ELEMENT));
        }

        private Element getCetElement() {
            return this;
        }

        private class XItemPlancheBoutonRetirerDeLaListe extends XItemPlancheBouton {

            public XItemPlancheBoutonRetirerDeLaListe() {
                super("Retirer");
            }

            protected void actionClicSouris() {
                removeElementVille(getCetElement());
            }
        }
    }
}
