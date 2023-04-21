package infothello.gui;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * La classe qui permet d'afficher quelque chose en premier plan
 * (utilisé par partie pour afficher des informations)
 */
public class PremierPlan extends JPanel {

    public static final int VIDE = 0;

    public static final int AFFICH = 1;

    private BufferedImage imagefond;

    private String message = new String();

    private int etat = VIDE;

    /** Constructeur de la classe qui initialise la taille et l'alignement du premier plan
	 */
    public PremierPlan() {
        setSize(400, 200);
        setMaximumSize(new Dimension(400, 200));
        setAlignmentX(0.5f);
        setAlignmentY(0.5f);
        setOpaque(false);
        chargementImage();
    }

    /** Méthode qui dessine dans le JPanel
	 */
    public void paintComponent(Graphics g) {
        if (etat != VIDE) {
            g.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
            FontMetrics metrics = g.getFontMetrics();
            int largeur = metrics.stringWidth(message);
            g.drawImage(imagefond, 0, 0, null);
            g.setColor(Color.BLACK);
            g.drawString(message, (400 - largeur) / 2, 50);
        }
    }

    /** Méthode qui charge l'image de fond
	 */
    public void chargementImage() {
        try {
            imagefond = ImageIO.read(getClass().getResource("/infothello/images/popup.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur: l'image de fond n'as pas pu etre chargé");
        }
    }

    /** Méthode qui affiche un message sur le premier plan
	 */
    public void affichMessage(String text) {
        message = text;
        etat = AFFICH;
        repaint();
    }

    /** Méthode qui enleve le message sur le premier plan (donc n'affiche rien)
	 */
    public void nettoyerMessage() {
        etat = VIDE;
        repaint();
    }
}
