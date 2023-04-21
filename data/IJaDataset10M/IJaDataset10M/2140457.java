package application.bureaux;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import ihm.biens.listes.FenetreBiens;
import ihm.config.FenetreConfiguration;
import ihm.etres.bestiaire.FenetreBestiaire;
import ihm.scenedejeu.partie.FenetrePartie;
import ihm.systemedejeu.FenetreRegles;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import outils.ihm.boutons.JideSplitButtonPerso;
import com.jidesoft.swing.JideSplitButton;
import application.FenetreAPropos;
import application.config.Env;
import application.config.Reference.TypeImageFond;
import application.outils.PanelBorderLayout;

public class BureauOngletGeneral {

    private PanelBorderLayout panel;

    private JToggleButton btListeBiens;

    private JideSplitButton btEquipement;

    private FenetreBiens fenetreBiens;

    private JideSplitButton btApplication;

    private JToggleButton btGestion;

    private JButton btRegles;

    private JToggleButton btPersonnages;

    private FenetreBestiaire fenetreEtres;

    private JToggleButton btPartie;

    private FenetrePartie fenetrePartie;

    private JideSplitButton btGestionMoteur;

    public BureauOngletGeneral(PanelBorderLayout panel) {
        this.panel = panel;
        this.fenetreBiens = null;
        this.fenetrePartie = null;
        remplirBarre(panel.getPanelBarre());
        ajouteImageFond();
    }

    public void ajouteImageFond() {
        BufferedImage imageFond = null;
        try {
            imageFond = ImageIO.read(new File(Env.rep_images_fond_ecran + File.separator + Env.config_image_fond));
            panel.getPanelBureau().ajouterImageDeFond(imageFond, TypeImageFond.Redimensionnee, 1.0, 100f, false, false, 1.0f);
        } catch (IOException e) {
            System.out.println("ERREUR Ouverture du fichier " + Env.rep_images_fond_ecran + File.separator + Env.config_image_fond);
            e.printStackTrace();
        }
    }

    private JideSplitButton creerBoutonMenu(String nom, Icon icone) {
        JideSplitButtonPerso bouton = new JideSplitButtonPerso(nom);
        bouton.setIcon(icone);
        bouton.setAlwaysDropdown(true);
        return bouton;
    }

    private JMenuItem ajouteMenuOuvertureFenetre(String libelle, Icon icone, final String className) {
        JMenuItem item = new JMenuItem(libelle, icone);
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class<?> myClass;
                    myClass = Class.forName(className);
                    JInternalFrame fenetre = (JInternalFrame) myClass.newInstance();
                    panel.getPanelBureau().add(fenetre);
                    fenetre.setLocation(110, 5);
                    fenetre.setVisible(true);
                } catch (ClassNotFoundException ev) {
                    ev.printStackTrace();
                } catch (InstantiationException evt) {
                    evt.printStackTrace();
                } catch (IllegalAccessException evv) {
                    evv.printStackTrace();
                }
            }
        });
        return item;
    }

    private void ajouterMenuBoutonGestion(JideSplitButton bouton) {
        bouton.add(ajouteMenuOuvertureFenetre("Attributs", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddAttribut"));
        bouton.add(ajouteMenuOuvertureFenetre("Races", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddRace"));
        bouton.add(ajouteMenuOuvertureFenetre("Ordres", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddOrdre"));
        bouton.add(ajouteMenuOuvertureFenetre("Ordres d'élite", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddOrdreElite"));
        bouton.add(ajouteMenuOuvertureFenetre("Avantages", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddAvantages"));
        bouton.add(ajouteMenuOuvertureFenetre("Handicaps", Env.getIconeAppli("icone_tete"), "ihm.gestion.TabBddHandicaps"));
        bouton.add(new JSeparator());
        bouton.add(ajouteMenuOuvertureFenetre("Présences", Env.getIconeAppli("icone_equipement"), "ihm.gestion.TabBddBien"));
    }

    private void ajouterMenuBoutonEquipement(JideSplitButton bouton) {
        bouton.add(ajouteMenuOuvertureFenetre("Armes", Env.getIconeEquipement("icone_arme_33"), "ihm.biens.listes.FenetreListeArmes"));
        bouton.add(ajouteMenuOuvertureFenetre("Armures", Env.getIconeEquipement("icone_armure_33"), "ihm.biens.listes.FenetreListeArmures"));
        bouton.add(ajouteMenuOuvertureFenetre("Boucliers", Env.getIconeEquipement("icone_bouclier_33"), "ihm.biens.listes.FenetreListeBoucliers"));
        bouton.add(ajouteMenuOuvertureFenetre("Nourriture", Env.getIconeEquipement("icone_nourriture_33"), "ihm.biens.listes.FenetreListeNourriture"));
        bouton.add(ajouteMenuOuvertureFenetre("Herbes", Env.getIconeEquipement("icone_herbe_33"), "ihm.biens.listes.FenetreListeHerbes"));
        bouton.add(ajouteMenuOuvertureFenetre("Poisons", Env.getIconeEquipement("icone_poison_33"), "ihm.biens.listes.FenetreListePoisons"));
        bouton.add(ajouteMenuOuvertureFenetre("Vêtements", Env.getIconeEquipement("icone_vetement_33"), "ihm.biens.listes.FenetreListeVetement"));
        bouton.add(ajouteMenuOuvertureFenetre("Objets", Env.getIconeEquipement("icone_objet_33"), "ihm.biens.listes.FenetreListeObjet"));
        bouton.add(ajouteMenuOuvertureFenetre("Véhicules", Env.getIconeEquipement("icone_vehicule_33"), "ihm.biens.listes.FenetreListeVehicule"));
        bouton.add(ajouteMenuOuvertureFenetre("Services", Env.getIconeEquipement("icone_service_33"), "ihm.biens.listes.FenetreListeServices"));
    }

    private void remplirBarre(JToolBar panelBarre) {
        btRegles = new JButton("Règles", Env.getIconeAppli("icone_regles"));
        btRegles.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionBoutonRegles();
            }
        });
        panelBarre.add(btRegles);
        panelBarre.add(Box.createHorizontalStrut(15));
        btEquipement = creerBoutonMenu("Equipement", Env.getIconeAppli("icone_equipement"));
        ajouterMenuBoutonEquipement(btEquipement);
        panelBarre.add(btEquipement);
        panelBarre.add(Box.createHorizontalStrut(15));
        btPersonnages = new JToggleButton("Bestiaire", Env.getIconeAppli("icone_personnage"));
        btPersonnages.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionBoutonPersonnages();
            }
        });
        panelBarre.add(btPersonnages);
        panelBarre.add(Box.createHorizontalStrut(15));
        btPartie = new JToggleButton("Partie", Env.getIconeAppli("icone_scene"));
        btPartie.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionBoutonPartie();
            }
        });
        panelBarre.add(btPartie);
        panelBarre.add(Box.createGlue());
        btGestionMoteur = creerBoutonMenu("Gestion", Env.getIconeAppli("icone_modifier"));
        ajouterMenuBoutonGestion(btGestionMoteur);
        panelBarre.add(btGestionMoteur);
        panelBarre.add(Box.createHorizontalStrut(15));
        btApplication = creerBoutonMenu("SDA Master", Env.getIconeAppli("icone_appli"));
        JMenuItem menuItemConfig = new JMenuItem("Préférences", Env.getIconeAppli("icone_preferences"));
        menuItemConfig.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FenetreConfiguration fen = new FenetreConfiguration();
                Env.appli_bureau.getBureau().add(fen);
                fen.setVisible(true);
            }
        });
        btApplication.add(menuItemConfig);
        JMenuItem menuItemAPropos = new JMenuItem("à propos ...");
        menuItemAPropos.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FenetreAPropos fenetre = new FenetreAPropos();
                Env.appli_bureau.getBureau().add(fenetre);
                fenetre.setVisible(true);
                fenetre.moveToFront();
            }
        });
        btApplication.add(menuItemAPropos);
        JMenuItem menuItemQuitter = new JMenuItem("Quitter", Env.getIconeAppli("icone_quitter"));
        menuItemQuitter.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Env.appli_bureau.fermerApplication();
            }
        });
        btApplication.add(menuItemQuitter);
        panelBarre.add(btApplication);
    }

    private void actionBoutonRegles() {
        FenetreRegles fenetreRegles = new FenetreRegles();
        panel.getPanelBureau().add(fenetreRegles);
        fenetreRegles.setVisible(true);
        fenetreRegles.toFront();
    }

    private void actionBoutonPersonnages() {
        if (btPersonnages.isSelected()) {
            if (fenetreEtres == null) {
                fenetreEtres = new FenetreBestiaire(panel.getPanelBureau(), this);
                panel.getPanelBureau().add(fenetreEtres);
                fenetreEtres.toFront();
            }
            fenetreEtres.setVisible(true);
        } else {
            fenetreEtres.setVisible(false);
        }
    }

    private void actionBoutonPartie() {
        if (btPartie.isSelected()) {
            if (fenetrePartie == null) {
                fenetrePartie = new FenetrePartie(this);
                fenetrePartie.setLocation(100, 5);
                panel.getPanelBureau().add(fenetrePartie);
                fenetrePartie.toFront();
            } else {
                fenetrePartie.setVisible(true);
            }
        } else {
            fenetrePartie.setVisible(false);
        }
    }

    public FenetreBiens getFenetreBiens() {
        return fenetreBiens;
    }

    public FenetreBestiaire getFenetreEtres() {
        return fenetreEtres;
    }

    public FenetrePartie getFenetrePartie() {
        return fenetrePartie;
    }

    public PanelBorderLayout getPanel() {
        return panel;
    }

    public JToggleButton getBtListeBiens() {
        return btListeBiens;
    }

    public JToggleButton getBtGestion() {
        return btGestion;
    }

    public JToggleButton getBtPersonnages() {
        return btPersonnages;
    }

    public JToggleButton getBtPartie() {
        return btPartie;
    }
}
