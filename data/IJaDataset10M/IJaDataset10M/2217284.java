package javaapplication;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author  Aur�lien
 */
public abstract class XItemPlancheBouton extends XItemPlanche {

    private String nom;

    private boolean estPresse;

    public BufferedImage imageIcone;

    public static final Color fond_repos = new Color(65, 200, 149);

    public static final Color tour_repos = new Color(0, 153, 153);

    public static final Color fond_survol = new Color(45, 180, 129);

    public static final Color fond_clique = new Color(85, 69, 120);

    public XItemPlancheBouton(String nom, boolean actif) {
        this.nom = nom;
        initComponents();
        if (actif) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new GestionSouris());
        } else {
            setEnabled(false);
        }
    }

    public XItemPlancheBouton(String nom) {
        this(nom, true);
    }

    public XItemPlancheBouton(String nom, boolean actif, BufferedImage img) {
        this(nom, actif);
        this.imageIcone = img;
    }

    public XItemPlancheBouton(String nom, BufferedImage img) {
        this(nom, true, img);
    }

    public void setTexte(String nom) {
        this.nom = nom;
        texte.setText(nom);
    }

    public void setPresse(boolean presse) {
        this.estPresse = presse;
        setCouleurNormale();
    }

    public boolean estPresse() {
        return estPresse;
    }

    public void setTailleTexte(int hauteur) {
        texte.setFont(new Font("Tw Cen MT", 1, hauteur));
    }

    protected abstract void actionClicSouris();

    public class GestionSouris extends MouseAdapter {

        boolean clicIci = false;

        public void mouseClicked(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
            setBackground(fond_survol);
            clicIci = false;
        }

        public void mouseExited(MouseEvent evt) {
            setCouleurNormale();
            clicIci = false;
        }

        public void mousePressed(MouseEvent evt) {
            setBackground(fond_clique);
            clicIci = true;
        }

        public void mouseReleased(MouseEvent evt) {
            if (clicIci) {
                actionClicSouris();
                setCouleurNormale();
            } else {
                setBackground(fond_survol);
            }
        }
    }

    public void setCouleurNormale() {
        if (estPresse) {
            setBackground(fond_clique);
        } else {
            setBackground(fond_repos);
        }
    }

    private void initComponents() {
        texte = new JLabel();
        setLayout(new BorderLayout());
        setBackground(fond_repos);
        setBorder(new LineBorder(tour_repos, 5, true));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        setMinimumSize(new Dimension(0, 45));
        setPreferredSize(new Dimension(0, 45));
        texte.setFont(new Font("Tw Cen MT", 1, 26));
        texte.setForeground(new Color(255, 255, 255));
        texte.setText(nom);
        add(texte, BorderLayout.CENTER);
        if (imageIcone != null) {
            JPanel icone = new JPanel();
            icone.setBackground(new Color(102, 102, 0));
            icone.setPreferredSize(new Dimension(35, 25));
            add(icone, BorderLayout.WEST);
        }
    }

    private javax.swing.JLabel texte;
}
