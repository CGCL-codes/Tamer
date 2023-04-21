package Vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Classe permettant de créer les cases qui vont composer le damier
 * @see DamierVue
 * @author El Meknassi Hamza (10806466) - Migeon Cyril (11007322)
 */
public class CaseVue extends JPanel {

    private int joueur = 0;

    private boolean estActive = false;

    private boolean selection = false;

    private boolean propose = false;

    private boolean dame = false;

    /**
     * Méthode permettant de dessiner des composantes graphiques
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (!estActive) {
            g2d.setBackground(Color.white);
            g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            if (selection) {
                g2d.setBackground(Color.yellow);
                if (propose) {
                    Color marron = new Color(133, 109, 77);
                    g2d.setBackground(marron);
                }
            } else {
                Color gris = new Color(204, 204, 204);
                g2d.setBackground(gris);
            }
            g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
            switch(joueur) {
                case 1:
                    g2d.setColor(Color.white);
                    g2d.fillOval(6, 6, this.getWidth() - 12, this.getHeight() - 12);
                    g2d.setColor(Color.black);
                    g2d.fillOval(8, 8, this.getWidth() - 16, this.getHeight() - 16);
                    if (dame) {
                        g2d.setColor(Color.white);
                        g2d.fillOval(20, 20, this.getWidth() - 40, this.getHeight() - 40);
                    }
                    break;
                case 2:
                    g2d.setColor(Color.black);
                    g2d.fillOval(6, 6, this.getWidth() - 12, this.getHeight() - 12);
                    g2d.setColor(Color.white);
                    g2d.fillOval(8, 8, this.getWidth() - 16, this.getHeight() - 16);
                    if (dame) {
                        g2d.setColor(Color.black);
                        g2d.fillOval(20, 20, this.getWidth() - 40, this.getHeight() - 40);
                    }
                    break;
                default:
                    break;
            }
            if (propose) {
                Color rougeB = new Color(144, 0, 32);
                g2d.setColor(rougeB);
                g2d.fillOval(8, 8, this.getWidth() - 16, this.getHeight() - 16);
                if (dame) {
                    g2d.setColor(Color.white);
                    g2d.fillOval(20, 20, this.getWidth() - 40, this.getHeight() - 40);
                }
            }
        }
    }

    /**
     * Définit à quelle joueur appartient le pion sur la case
     * @param num
     */
    public void setJoueur(int num) {
        this.joueur = num;
    }

    /**
     * Définit si la case est active ou pas
     * @param val
     */
    public void setEstActive(boolean val) {
        this.estActive = val;
    }

    /**
     * Définit si la case contient un pion susceptible de faire une prise ou pas
     * @param val
     */
    public void setPropose(boolean val) {
        this.propose = val;
    }

    /**
     * Définit si la case est sélectionnée ou pas
     * @param val
     */
    public void setSelection(boolean val) {
        this.selection = val;
    }

    /**
     * Définit si la case contient une dame ou pas
     * @param val
     */
    public void setDame(boolean val) {
        dame = val;
    }

    /**
     * Constructeur
     */
    public CaseVue() {
        super();
        setDoubleBuffered(true);
    }
}
