package fr.alma.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import fr.alma.coeur.GestionPlugins;
import fr.alma.coeur.Loggeur;
import fr.alma.observation.Observateur;

/**
 * Classe d'affichage de la fenetre graphique.
 * @author AUBERT Sebastien
 * @author BOUVET Frederic
 * @author BRAUD Jeremy
 * @author KROMMENHOEK Cedric
 */
public class Fenetre extends JFrame implements ActionListener, Observateur {

    /** Generated serial ID. */
    private static final long serialVersionUID = -4488755599571870952L;

    /** Outil de gestion des plugins. */
    private GestionPlugins gestion;

    /** Item Charger. */
    private JMenuItem menuItemCharger;

    /** Item configurer. */
    private JMenuItem menuItemConfigurer;

    /** Item quitter. */
    private JMenuItem menuItemQuitter;

    /** Item demarrer. */
    private JMenuItem menuItemDemarrer;

    /** Item arreter. */
    private JMenuItem menuItemArreter;

    /** Item aide. */
    private JMenuItem menuItemAide;

    /** Item a propos. */
    private JMenuItem menuItemAPropos;

    /** Zone de texte centrale. */
    private JTextArea zoneTexte;

    /**
	 * Constructeur.
	 */
    public Fenetre() {
        this.initialisationFenetre();
        Loggeur.getLoggeur().ajoutObservateur(this);
        this.gestion = new GestionPlugins();
        this.gestion.chargerDossierPlugins();
    }

    /**
	 * Methode d'initialisation de la fenetre.
	 */
    private void initialisationFenetre() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");
        JMenu menuPlugins = new JMenu("Execution des plugins");
        JMenu menuAide = new JMenu("?");
        this.menuItemCharger = new JMenuItem("Chargement de plugins");
        this.menuItemCharger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        this.menuItemConfigurer = new JMenuItem("Configuration des plugins");
        this.menuItemConfigurer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        this.menuItemQuitter = new JMenuItem("Quitter");
        this.menuItemQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        this.menuItemDemarrer = new JMenuItem("Demarrer les plugins");
        this.menuItemDemarrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        this.menuItemArreter = new JMenuItem("Arreter les plugins");
        this.menuItemArreter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        this.menuItemArreter.setEnabled(false);
        this.menuItemAide = new JMenuItem("Aide");
        this.menuItemAPropos = new JMenuItem("A propos");
        this.menuItemCharger.addActionListener(this);
        this.menuItemConfigurer.addActionListener(this);
        this.menuItemQuitter.addActionListener(this);
        this.menuItemDemarrer.addActionListener(this);
        this.menuItemArreter.addActionListener(this);
        this.menuItemAide.addActionListener(this);
        this.menuItemAPropos.addActionListener(this);
        this.zoneTexte = new JTextArea();
        this.zoneTexte.setEditable(false);
        JScrollPane scroll = new JScrollPane(this.zoneTexte);
        menuBar.add(menuFichier);
        menuBar.add(menuPlugins);
        menuBar.add(menuAide);
        menuFichier.add(this.menuItemCharger);
        menuFichier.add(this.menuItemConfigurer);
        menuFichier.addSeparator();
        menuFichier.add(this.menuItemQuitter);
        menuPlugins.add(this.menuItemDemarrer);
        menuPlugins.add(this.menuItemArreter);
        menuAide.add(this.menuItemAide);
        menuAide.addSeparator();
        menuAide.add(this.menuItemAPropos);
        this.setTitle("Gestion des plugins");
        this.setSize(500, 300);
        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setAlwaysOnTop(false);
        this.getContentPane().add(scroll);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
	 * Action declenchee par l'activation d'un element d'un menu.
	 * @param arg0 l'action declenchee
	 */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == this.menuItemQuitter) {
            System.exit(0);
        } else if (arg0.getSource() == this.menuItemCharger) {
            this.chargerJarPlugin();
        } else if (arg0.getSource() == this.menuItemConfigurer) {
            new ConfigurationDialog(this, "Configuration des plugins", true, this.gestion).setVisible(true);
        } else if (arg0.getSource() == this.menuItemDemarrer) {
            this.gestion.demarrerPlugins();
            this.menuItemArreter.setEnabled(true);
            this.menuItemDemarrer.setEnabled(false);
            this.menuItemConfigurer.setEnabled(false);
            this.menuItemCharger.setEnabled(false);
        } else if (arg0.getSource() == this.menuItemArreter) {
            this.gestion.arreterPlugins();
            this.menuItemArreter.setEnabled(false);
            this.menuItemDemarrer.setEnabled(true);
            this.menuItemConfigurer.setEnabled(true);
            this.menuItemCharger.setEnabled(true);
        } else if (arg0.getSource() == this.menuItemAPropos) {
            String mess = "Architecture a plugins\n";
            mess += "\nProgrammé par :\n" + "    -Aubert Sébastien\n" + "    -Bouvet Frédéric\n" + "    -Braud Jérémy\n" + "    -Krommenhoek Cédric\n\n" + "Contacts :\n" + "     prenom.nom@etu.univ-nantes.fr";
            JOptionPane.showMessageDialog(null, mess, "A propos", JOptionPane.INFORMATION_MESSAGE);
        } else if (arg0.getSource() == this.menuItemAide) {
            ControleNavigateur.displayURL("manuel.html");
        }
    }

    /**
	 * Methode de chargement des plugins contenus dans une archive .jar.
	 */
    private void chargerJarPlugin() {
        JFileChooser choixFichiers = new JFileChooser(".");
        choixFichiers.addChoosableFileFilter(new FiltreFichiers(".jar", "Archive Java"));
        if (choixFichiers.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.gestion.chargerJarPlugin(choixFichiers.getSelectedFile().getAbsolutePath());
        }
    }

    /**
	 * Actualise la zone de texte.
	 * @param objet la chaine de caracteres recue
	 */
    @Override
    public void update(Object objet) {
        this.zoneTexte.setText(this.zoneTexte.getText() + (String) objet + "\n");
    }
}
