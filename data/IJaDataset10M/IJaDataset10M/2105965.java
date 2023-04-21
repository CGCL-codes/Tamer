package models.terrains;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

/**
 * Classe de gestion du fameux terrain Element TD repris de chez Blizzard.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 13 decembre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class ElementTD extends Terrain {

    private static final long serialVersionUID = 1L;

    public static final Image IMAGE_DE_FOND;

    public static final Image IMAGE_MENU;

    public static final File FICHIER_MUSIQUE_DE_FOND;

    public static final String NOM = "ElementTD";

    static {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/ambient/Filippo Vicarelli - The War Begins.mp3");
        IMAGE_MENU = Toolkit.getDefaultToolkit().getImage("img/cartes/menu_principal/elementTD.png");
        IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/elementTD.png");
    }

    /**
     * Constructeur d'un terrain ElementTD selon la celebre map de Blizzard.
     */
    public ElementTD(Jeu jeu) {
        super(jeu, 480, 500, 100, 20, 0, -40, 480, 540, ModeDeJeu.MODE_SOLO, new Color(197, 148, 90), new Color(91, 123, 43), IMAGE_DE_FOND, NOM);
        opaciteMurs = 0.f;
        Equipe e = new Equipe(1, "Equipe par defaut", Color.BLACK);
        e.ajouterZoneDepartCreatures(new Rectangle(110, -40, 80, 20));
        e.setZoneArriveeCreatures(new Rectangle(250, 0, 40, 40));
        e.ajouterEmplacementJoueur(new EmplacementJoueur(1, new Rectangle(0, 0, 480, 500)));
        equipes.add(e);
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
        ajouterMur(new Rectangle(0, -40, 100, 60));
        ajouterMur(new Rectangle(0, 20, 20, 480));
        ajouterMur(new Rectangle(20, 480, 440, 20));
        ajouterMur(new Rectangle(460, 20, 20, 480));
        ajouterMur(new Rectangle(320, -40, 160, 60));
        ajouterMur(new Rectangle(200, -40, 20, 140));
        ajouterMur(new Rectangle(120, 100, 240, 20));
        ajouterMur(new Rectangle(120, 120, 20, 20));
        ajouterMur(new Rectangle(340, 120, 20, 260));
        ajouterMur(new Rectangle(120, 360, 220, 20));
        ajouterMur(new Rectangle(20, 240, 220, 20));
        ajouterMur(new Rectangle(220, 220, 20, 20));
    }
}
