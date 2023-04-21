package interface_jeu;

import ia.IAdebile;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import javax.swing.*;
import donnees.Joueur;
import donnees.Moteur;

public class Fenetre extends JFrame implements ActionListener {

    private int x, y;

    private final int largeur = 1024;

    private final int hauteur = 768;

    private JMenuBar menuBar = new JMenuBar();

    private JMenu fichier;

    private JMenuItem quitter;

    private boolean deco = true;

    private int largeurCarte = 94;

    private int hauteurCarte = 60;

    private Color bgColor = new Color(205, 185, 131);

    private PanneauJoueur jpmj;

    private PanneauPiocheWagons jppw;

    private PanneauPiocheObjectifs jppo;

    private PanneauPlateau jpp;

    private Image image;

    private Moteur moteur;

    public Fenetre() {
        x = 0;
        y = 0;
        Toolkit.getDefaultToolkit().getScreenSize().setSize(largeur, hauteur);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(x, y, largeur, hauteur);
        image = getToolkit().getImage("img/tt_back.jpg");
        commencerPartie();
        setAll();
    }

    public void commencerPartie() {
        Joueur[] j = new Joueur[2];
        j[0] = new IAdebile("IA " + 1, 0);
        j[1] = new IAdebile("IA " + 2, 1);
        moteur = new Moteur(this, j);
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(image, 0, 0, largeur, hauteur, null);
        getPanneauMainJoueur().repaint();
    }

    public PanneauJoueur getPanneauMainJoueur() {
        return jpmj;
    }

    public PanneauPiocheWagons getPanneauPiocheWagons() {
        return jppw;
    }

    public PanneauPiocheObjectifs getPanneauPiocheObjectifs() {
        return jppo;
    }

    public PanneauPlateau getPanneauPlateau() {
        return jpp;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeurCarte() {
        return largeurCarte;
    }

    public int getHauteurCarte() {
        return hauteurCarte;
    }

    public Moteur getMoteur() {
        return moteur;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setAll() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(deco);
        setLayout(new BorderLayout());
        jppw = new PanneauPiocheWagons(this);
        jppo = new PanneauPiocheObjectifs(this);
        jpmj = new PanneauJoueur(this);
        jpp = new PanneauPlateau(this, moteur.getJeu().getPlateau());
        setJMenuBar(menuBar);
        quitter = new JMenuItem("Quitter");
        fichier = new JMenu("Fichier");
        menuBar.add(fichier);
        fichier.add(quitter);
        quitter.addActionListener(this);
        add(jpmj, BorderLayout.SOUTH);
        Box boxPioche = Box.createVerticalBox();
        boxPioche.add(jppw);
        boxPioche.add(jppo);
        Dimension dim = new Dimension(jppw.getWidth(), jppw.getHeight() + jppo.getHeight());
        boxPioche.setPreferredSize(dim);
        boxPioche.setSize(dim);
        boxPioche.setMaximumSize(dim);
        boxPioche.setBackground(this.getBgColor());
        add(boxPioche, BorderLayout.WEST);
        add(jpp, BorderLayout.CENTER);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(moteur, 500, 50);
        setVisible(true);
    }

    public void quitter() {
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitter) {
            quitter();
        }
    }
}
