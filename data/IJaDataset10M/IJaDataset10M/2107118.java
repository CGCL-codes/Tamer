package com.um2.simplexe.serveur.ui.zone;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
 * Ce panneau est le panneau par defaut,
 * celui donc est base tout les panneaux de l'appli.
 * 
 * 
 * Il est compose d'un tabbedPane.
 * 
 * @author jean-marie codol
 *
 */
@SuppressWarnings("serial")
public class Zone extends JPanel {

    /**
	 * L'instance courante du panneau 1
	 */
    private static Zone instance;

    private JTabbedPane panneauxAOnglets = new JTabbedPane();

    private JPanel panneauNord = new JPanel(new FlowLayout(FlowLayout.LEFT));

    /**
	 * Constructeur par defaut
	 *
	 */
    public Zone() {
        Zone.instance = this;
        this.setLayout(new BorderLayout());
        this.add(this.panneauNord, BorderLayout.NORTH);
        this.add(this.panneauxAOnglets, BorderLayout.CENTER);
    }

    /**
	 * retourne l'instance unique de Zone1
	 * @return
	 */
    private static Zone getInstance() {
        return Zone.instance;
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static void ajouterPanel(String nom, JPanel p) {
        Zone.getInstance().panneauxAOnglets.addTab(nom, p);
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static void enleverPanel(int indice) {
        Zone.getInstance().panneauxAOnglets.remove(indice);
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static void affichePanel(int indice) {
        Zone.getInstance().panneauxAOnglets.setSelectedIndex(indice);
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static int getNombrePanels() {
        return Zone.getInstance().panneauxAOnglets.getTabCount();
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static Component getPanel(int i) {
        return Zone.getInstance().panneauxAOnglets.getComponentAt(i);
    }

    /**
	 * 
	 * On peut ajouter un pannneau au tabbedPane interne.
	 * 
	 */
    public static Component getCurrentPanel() {
        return Zone.getInstance().panneauxAOnglets.getSelectedComponent();
    }

    public static JPanel getNord() {
        return Zone.getInstance().panneauNord;
    }

    public static int getCurrentOpenTab() {
        return Zone.getInstance().panneauxAOnglets.getSelectedIndex();
    }
}
