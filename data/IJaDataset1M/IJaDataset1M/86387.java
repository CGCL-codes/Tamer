package org.vue.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.vue.fenetre.Fenetre;
import org.vue.menu.listener.MenuFichierDeconnexionListener;
import org.vue.menu.listener.MenuFichierFermerListener;

public class Menu extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private JMenu fichier, edition, affichage;

    private JMenuItem fermer, deconnexion;

    private Fenetre fenetre;

    public Menu(Fenetre f) {
        setFenetre(f);
        fichier = new JMenu("Fichier");
        edition = new JMenu("Edition");
        affichage = new JMenu("Affichage");
        fermer = new JMenuItem("Fermer");
        deconnexion = new JMenuItem("D�connexion");
        fermer.addActionListener(new MenuFichierFermerListener());
        deconnexion.addActionListener(new MenuFichierDeconnexionListener(fenetre));
        fichier.add(deconnexion);
        fichier.add(fermer);
        add(fichier);
        add(edition);
        add(affichage);
    }

    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    public Fenetre getFenetre() {
        return fenetre;
    }

    public static void main(String[] args) {
        Fenetre fen = new Fenetre();
        fen.setVisible(true);
    }
}
