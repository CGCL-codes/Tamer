package dr.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Barre de menu commune aux deux fenetres
 */
public class BarreDeMenu extends JMenuBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Sous menu a propos affichant une boite de dialogue
	 */
    public JMenuItem about;

    /**
	 * Sous menu nouveau ramenant � l'interface de creation de jeu
	 */
    public JMenuItem nouveau;

    /**
	 * Sous menu quitter permettant de quitter les fenetres a tout moment
	 */
    public JMenuItem quitter;

    /**
	 * Constructeur de la barre de menu
	 */
    public BarreDeMenu() {
        JMenu desertRush = new JMenu("DesertRush");
        about = new JMenuItem("About");
        desertRush.add(about);
        JMenu fichier = new JMenu("Fichier");
        nouveau = new JMenuItem("Nouvelle partie");
        fichier.add(nouveau);
        fichier.addSeparator();
        quitter = new JMenuItem("Quitter la partie");
        fichier.add(quitter);
        this.add(desertRush);
        this.add(fichier);
    }
}
