package vues;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import models.outils.GestionnaireSons;
import models.outils.Son;

/**
 * Fenetre du menu principal du jeu.
 * <p>
 * Affiche un menu permettant au joueur de choisir son mode de jeu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 20 avril 2010
 * @since jdk1.6.0_16
 */
public class Fenetre_MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/icone_pgm.png");

    public static final int LARGEUR_FENETRE = 800;

    public static final int HAUTEUR_FENETRE = 600;

    public static final File FICHIER_MUSIQUE_MENU = new File("snd/ambient/Jazzduphoodlum - Medieval In vestri Ass.mp3");

    /**
     * Constructeur de la fenetre du menu principal
     */
    public Fenetre_MenuPrincipal() {
        setIconImage(I_FENETRE.getImage());
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Son musiqueDAmbiance = new Son(FICHIER_MUSIQUE_MENU);
        GestionnaireSons.ajouterSon(musiqueDAmbiance);
        musiqueDAmbiance.lire(0);
        getContentPane().add(new Panel_MenuPrincipal(this), BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
