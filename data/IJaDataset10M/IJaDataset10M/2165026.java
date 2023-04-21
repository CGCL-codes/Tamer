package core.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import core.common.MainWindow;
import core.common.JavaLog;
import core.common.MyFileChooser;
import core.menu.configuration.DisplayConfig;
import core.menu.io.Save;
import core.menu.io.Load;

public class Menu extends JMenuBar implements ActionListener, ItemListener {

    private static final long serialVersionUID = -5232234526624281434L;

    private JMenuItem jmi_Ouvrir;

    private JMenuItem jmi_Enregistrer;

    private JMenuItem jmi_Quitter;

    MyFileChooser fc;

    private MainWindow fp;

    private JMenuItem jmi_Affichage;

    private DisplayConfig ca;

    private ResourceBundle i18n;

    private JRadioButtonMenuItem jrbmi_fr;

    private JRadioButtonMenuItem jrbmi_en;

    private JMenu jm_Fichier;

    private JMenu jm_Fenetre;

    private JMenu jmi_Langage;

    /**
	 * @param fp la fenêtre principale associée
	 */
    public Menu(MainWindow fp) {
        this.fp = fp;
        this.fc = new MyFileChooser(".jla");
        this.ca = new DisplayConfig(this);
        this.i18n = ResourceBundle.getBundle("core/i18n/I18nMenu", JavaLog.locale);
        this.jmi_Ouvrir = new JMenuItem(this.i18n.getString("ouvrir"), this.i18n.getString("ouvrir_cle").charAt(0));
        this.jmi_Enregistrer = new JMenuItem(this.i18n.getString("enregistrer"), this.i18n.getString("enregistrer_cle").charAt(0));
        this.jmi_Quitter = new JMenuItem(this.i18n.getString("quitter"), this.i18n.getString("quitter_cle").charAt(0));
        this.jmi_Ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        this.jmi_Enregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        this.jmi_Quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        this.jmi_Ouvrir.addActionListener(this);
        this.jmi_Enregistrer.addActionListener(this);
        this.jmi_Quitter.addActionListener(this);
        this.jm_Fichier = new JMenu(this.i18n.getString("fichier"));
        this.jm_Fichier.setMnemonic(this.i18n.getString("fichier_cle").charAt(0));
        this.jm_Fichier.add(this.jmi_Ouvrir);
        this.jm_Fichier.add(this.jmi_Enregistrer);
        this.jm_Fichier.add(this.jmi_Quitter);
        this.jmi_Affichage = new JMenuItem(this.i18n.getString("affichage"), this.i18n.getString("affichage_cle").charAt(0));
        this.jm_Fenetre = new JMenu(this.i18n.getString("fenetre"));
        this.jmi_Langage = new JMenu(this.i18n.getString("langage"));
        ButtonGroup groupe = new ButtonGroup();
        this.jrbmi_fr = new JRadioButtonMenuItem("Français");
        this.jrbmi_en = new JRadioButtonMenuItem("English");
        groupe.add(this.jrbmi_fr);
        groupe.add(this.jrbmi_en);
        this.jmi_Langage.add(this.jrbmi_fr);
        this.jmi_Langage.add(this.jrbmi_en);
        this.jm_Fenetre.add(this.jmi_Affichage);
        this.jm_Fenetre.add(this.jmi_Langage);
        this.jm_Fenetre.setMnemonic(this.i18n.getString("fenetre_cle").charAt(0));
        this.jmi_Langage.setMnemonic(this.i18n.getString("langage_cle").charAt(0));
        this.jmi_Affichage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        this.jmi_Affichage.addActionListener(this);
        this.jrbmi_fr.addItemListener(this);
        this.jrbmi_en.addItemListener(this);
        this.add(this.jm_Fichier);
        this.add(this.jm_Fenetre);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == this.jmi_Ouvrir) {
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    new Load(this, f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (arg0.getSource() == this.jmi_Enregistrer) {
            int returnVal = fc.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                int extension = f.getName().lastIndexOf(".");
                if (extension == -1) f = new File(f.getAbsolutePath() + ".jla");
                try {
                    new Save(this, f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (JavaLog.DEBUGMENU) System.out.println("Enregistrer[actionPerformed] : f.getAbsolutePath()=" + f.getAbsolutePath());
            }
        }
        if (arg0.getSource() == this.jmi_Quitter) {
            int rep = JOptionPane.showConfirmDialog(this, this.i18n.getString("etes_vous_certain_de_quitter"));
            if (rep == 0) System.exit(0);
            if (JavaLog.DEBUGMENU) System.out.println("Menu[actionPerformed] : rep=" + rep);
        }
        if (arg0.getSource() == this.jmi_Affichage) {
            this.ca.setVisible(true);
        }
    }

    /**
	 * Retourne le chemin de chacun des fichiers ouverts dans le content
	 * @return le chemins de chacun des fichiers ouverts
	 */
    public Vector<String> getFichiersOuverts() {
        return (this.fp.getFichiersOuverts());
    }

    /**
	 * Restaure les onglets
	 * @param fichiers les chemins de fichier à restaurer
	 */
    public void restaurerOnglets(Vector<File> fichiers) {
        this.fp.restaurerOnglets(fichiers);
    }

    /**
	 * Renvoie l'ensemble des filtres textes créés
	 */
    public Vector<String> getFiltresTextes() {
        return this.fp.getFiltresTextes();
    }

    /**
	 * initialise les filtres textes
	 * @param vTexte filtres textes
	 */
    public void restaurerFiltresTexte(Vector<String> vTexte) {
        this.fp.restaurerFiltresTexte(vTexte);
    }

    /**
	 * initialise les filtres couleurs
	 * @param vClees les clées des filtres couleurs
	 * @param vCouleurs les couleurs associées aux clées
	 */
    public void restaurerFiltresCouleurs(Vector<String> vClees, Vector<Color> vCouleurs) {
        this.fp.restaurerFiltresCouleurs(vClees, vCouleurs);
    }

    /**
	 * Retourne l'ensemble des clées (noir, rouge ,etc.)
	 * @return ensemble des clées
	 */
    public Vector<String> getClees() {
        return this.fp.getClees();
    }

    /**
	 * Retourne l'ensemble des couleurs associées aux clées (Color.Black, Color.Red, etc.)
	 * @return ensemble des couleurs
	 */
    public Vector<Color> getCouleurs() {
        return this.fp.getCouleurs();
    }

    /**
	 * Configure le nombre de lignes maximum à afficher dans un document
	 * @param nbLignes le nombre de ligne à afficher
	 */
    public void setNbLignes(int nbLignes) {
        this.fp.setNbLignes(nbLignes);
    }

    /**
	 * Récupération de la configuration de l'affichage
	 * @return élément de vecteur concernant la configuration de l'affichage
	 * Le premier concerne le nombre de lignes maximum à afficher
	 */
    public Vector<String> getConfigurationAffichage() {
        return this.ca.getConfigureAffichage();
    }

    /**
	 * Restauration de la configuration de l'affichage
	 * @param i le nombre de lignes maximum concernant l'affichage
	 */
    public void restaurationConfigurationAffichage(String i) {
        this.ca.restaurationConfigurationAffichage(i);
    }

    public void itemStateChanged(ItemEvent e) {
        if ((e.getStateChange() == ItemEvent.SELECTED) && (e.getSource() == this.jrbmi_fr)) {
            if (JavaLog.DEBUGMENU) System.out.println("Menu[itemStateChanged] : La langue française est choisie");
            JavaLog.locale = Locale.FRENCH;
        }
        if ((e.getStateChange() == ItemEvent.SELECTED) && (e.getSource() == this.jrbmi_en)) {
            if (JavaLog.DEBUGMENU) System.out.println("Menu[itemStateChanged] : La langue anglaise est choisie");
            JavaLog.locale = Locale.ENGLISH;
        }
        if (e.getStateChange() == ItemEvent.SELECTED) this.fp.rechargeI18n();
    }

    public void rechargeI18n() {
        this.i18n = ResourceBundle.getBundle("core/i18n/I18nMenu", JavaLog.locale);
        if (JavaLog.DEBUGMENU) System.out.println("Menu[rechargeI18n] : JavaLog.locale=" + JavaLog.locale);
        this.jmi_Affichage.setText(this.i18n.getString("affichage"));
        this.jmi_Ouvrir.setText(this.i18n.getString("ouvrir"));
        this.jmi_Enregistrer.setText(this.i18n.getString("enregistrer"));
        this.jmi_Quitter.setText(this.i18n.getString("quitter"));
        this.jm_Fichier.setText(this.i18n.getString("fichier"));
        this.jm_Fenetre.setText(this.i18n.getString("fenetre"));
        this.jmi_Langage.setText(this.i18n.getString("langage"));
    }
}
