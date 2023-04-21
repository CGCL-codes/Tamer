package org.esme.kroak.ihm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.esme.kroak.core.Preference;
import org.esme.kroak.core.PreferenceManager;

/**
 * Classe permettant de creer la fenetre de changement de la police et d la couleur de l'ecriture de l'utilisateur
 * Elle est appelee par la classe FenetreDiscussion lorsque l'utilisateur clique sur le "Changer la police"
 * 
 */
class FenetrePolice extends JFrame implements ActionListener {

    private JPanel pan;

    private JLabel etiqCouleurEcriture, etiqPolice;

    private Font fonteBouton, fonteTexte;

    private JButton bValider, bAnnuler;

    private String nvoPolice, chaineCEcriture;

    private String[] fontes;

    private JComboBox comboCouleurEcriture, comboPolice;

    private PreferenceManager paramList;

    private Preference param;

    private Style sUtilisateur;

    /**
	 * Constructeur de la classe FenetreAjoutContact
	 * 
	 * @param tmpParamList : permet de sauvegarder les preferences de l'utilisateur
	 * @param param : permet de recuperer les preferences de l'utilisateur
	 * @param tmpSUtilisateur : permet de recuperer le style de l'utilisateur afin de la mettre a jour
	 * 
	 */
    public FenetrePolice(PreferenceManager tmpParamList, Preference tmpParam, Style tmpSUtilisateur) {
        paramList = tmpParamList;
        param = tmpParam;
        sUtilisateur = tmpSUtilisateur;
        nvoPolice = param.getElement(param.POS_USER_POLICE);
        chaineCEcriture = param.getElement(param.POS_USER_WRITECOLOR);
        fontes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        setTitle("Polices et couleurs");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimEcran = tk.getScreenSize();
        int largE = dimEcran.width, hautE = dimEcran.height;
        setSize(largE * 8 / 12, hautE * 1 / 3);
        super.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contenu = getContentPane();
        Image icone = Toolkit.getDefaultToolkit().getImage("./grenouille_verte.jpg");
        setIconImage(icone);
        fonteTexte = new Font(KroakMessenger.police, Font.BOLD, 14);
        fonteBouton = new Font(KroakMessenger.police, Font.BOLD, 18);
        pan = new PaneauPolice(param);
        contenu.add(pan);
        pan.setLayout(null);
        Dimension dimF = super.getSize();
        int largF = dimF.width, hautF = dimF.height;
        int Xchamp = largF * 9 / 25, lgrChamp = largF * 12 / 20, Xetiq = largF / 40;
        int lgrEtiq = largF * 7 / 20;
        etiqCouleurEcriture = new JLabel("Couleur d'ecriture :");
        pan.add(etiqCouleurEcriture);
        etiqCouleurEcriture.setBounds(Xetiq, hautF * 18 / 50, lgrEtiq, 25);
        etiqCouleurEcriture.setForeground(KroakMessenger.cEcriture);
        etiqCouleurEcriture.setFont(fonteTexte);
        comboCouleurEcriture = new JComboBox(FenetreOutils.nomCouleursEcriture);
        comboCouleurEcriture.setEditable(false);
        comboCouleurEcriture.setBounds(Xchamp, hautF * 18 / 50, lgrChamp, 25);
        comboCouleurEcriture.setForeground(KroakMessenger.cEcriture);
        comboCouleurEcriture.setFont(fonteTexte);
        pan.add(comboCouleurEcriture);
        comboCouleurEcriture.addActionListener(this);
        etiqPolice = new JLabel("Police :");
        pan.add(etiqPolice);
        etiqPolice.setBounds(Xetiq, hautF * 25 / 50, lgrEtiq, 25);
        etiqPolice.setForeground(KroakMessenger.cEcriture);
        etiqPolice.setFont(fonteTexte);
        comboPolice = new JComboBox(fontes);
        comboPolice.setEditable(false);
        comboPolice.setBounds(Xchamp, hautF * 25 / 50, lgrChamp, 25);
        comboPolice.setForeground(KroakMessenger.cEcriture);
        comboPolice.setFont(fonteTexte);
        pan.add(comboPolice);
        comboPolice.addActionListener(this);
        bAnnuler = new JButton("Annuler");
        pan.add(bAnnuler);
        bAnnuler.setBounds(largF * 10 / 20, hautF * 33 / 50, largF * 4 / 20, 30);
        bAnnuler.setForeground(KroakMessenger.cEcriture);
        bAnnuler.setFont(fonteBouton);
        bAnnuler.addActionListener(this);
        bValider = new JButton("Valider");
        pan.add(bValider);
        bValider.setBounds(largF * 5 / 20, hautF * 33 / 50, largF * 4 / 20, 30);
        bValider.setForeground(KroakMessenger.cEcriture);
        bValider.setFont(fonteBouton);
        bValider.addActionListener(this);
    }

    /**
	 * Methode permettant de reagir aux evenements crees par un clic sur le bouton "valider"
	 * ou le changement de la valeur selectionner dans les combo box
	 */
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bValider) {
            param.setElement(param.POS_USER_POLICE, nvoPolice);
            param.setElement(param.POS_USER_WRITECOLOR, chaineCEcriture);
            paramList.SaveConfig(param.getElement(param.POS_USER_ADRESS));
            StyleConstants.setFontFamily(sUtilisateur, param.getElement(param.POS_USER_POLICE));
            Color cUtilisateur = KroakMessenger.cEcriture;
            for (int i = 0; i < FenetreOutils.nomCouleursEcriture.length; i++) {
                if (param.getElement(param.POS_USER_WRITECOLOR).equals(FenetreOutils.nomCouleursEcriture[i])) {
                    cUtilisateur = FenetreOutils.couleursEcriture[i];
                }
            }
            StyleConstants.setForeground(sUtilisateur, cUtilisateur);
            dispose();
        } else if (ev.getSource() == bAnnuler) {
            dispose();
        } else if (ev.getSource() == comboCouleurEcriture) {
            Object ObjetCouleurEcriture = comboCouleurEcriture.getSelectedItem();
            chaineCEcriture = (String) ObjetCouleurEcriture;
        } else if (ev.getSource() == comboPolice) {
            Object ObjetPolice = comboPolice.getSelectedItem();
            nvoPolice = (String) ObjetPolice;
        }
    }
}

/**
 * Classe permet de creer le panneau qui servira de de trame de fond a la fenetre
 * C'est grace a cette classe qu'on peut inserer des images et les reactualiser 
 */
class PaneauPolice extends JPanel {

    /**
	 * Constructeur de la classe PanneauAjoutContact
	 * 
	 * @param param : permet de recuperer les preferences de l'utilisateur (son pseuso, sa phrase, sa photo...)
	 * afin de les afficher
	 * 
	 */
    public PaneauPolice(Preference param) {
        fond = new ImageIcon(param.getElement(param.POS_USER_SKIN));
        icone = new ImageIcon("croak-messenger.png");
    }

    /**
	 * Methode permmettant d'aaficher des images et de les reactualiser
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getSize();
        int larg = dim.width, haut = dim.height;
        int coin = 50;
        g.setColor(Color.white);
        g.fillRoundRect(15, 15, larg - 30, haut - 30, coin, coin);
        g.drawImage(fond.getImage(), 0, 0, larg, haut, null);
        g.setColor(KroakMessenger.cEcriture);
        g.setFont(new Font(KroakMessenger.police, Font.BOLD, 18));
        g.drawString("Police et couleur d'ecriture", larg * 2 / 10, haut * 4 / 25);
        g.drawImage(icone.getImage(), larg * 31 / 40, haut / 13, haut / 2, haut / 4, null);
    }

    private ImageIcon icone, fond;
}
