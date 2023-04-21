package gpsGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import donnees.PointGPS;

/**
 *
 * @author Foudil
 */
public class PlanGPS extends JPanel {

    public static double zoom = 1;

    ArrayList<DessinPointGPS> pointListe = new ArrayList<DessinPointGPS>();

    ArrayList<DessinVecteur> vecteurListe = new ArrayList<DessinVecteur>();

    ArrayList<DessinRadar> radarListe = new ArrayList<DessinRadar>();

    ArrayList<DessinTravaux> travauxListe = new ArrayList<DessinTravaux>();

    ArrayList<DessinBatiment> batimentsListe = new ArrayList<DessinBatiment>();

    int maxW = 200;

    int maxH = 200;

    DessinPointGPS ptG;

    ComparePointX comX = new ComparePointX();

    ComparePointY comY = new ComparePointY();

    Dimension dim = new Dimension(maxW, maxH);

    double maxY = 200;

    ArrayList<PointGPS> result = null;

    public PlanGPS() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        this.setBackground(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < vecteurListe.size(); i++) {
            vecteurListe.get(i).dessineToi(g2d, getMaxY(), PlanGPS.zoom);
        }
        for (int i = 0; i < pointListe.size(); i++) {
            pointListe.get(i).dessineToi(g2d, getMaxY(), PlanGPS.zoom);
        }
        for (int i = 0; i < radarListe.size(); i++) {
            radarListe.get(i).dessineToi(g2d, getMaxY(), PlanGPS.zoom);
        }
        for (int i = 0; i < travauxListe.size(); i++) {
            travauxListe.get(i).dessineToi(g2d, getMaxY(), PlanGPS.zoom);
        }
        for (int i = 0; i < batimentsListe.size(); i++) {
            batimentsListe.get(i).dessineToi(g2d, getMaxY(), PlanGPS.zoom);
        }
        for (int i = 0; i < vecteurListe.size(); i++) {
            vecteurListe.get(i).ecrireRoute(g2d, getMaxY(), PlanGPS.zoom);
        }
        if (result != null && result.size() >= 2) {
            Color c = g.getColor();
            for (int i = 0; i < (result.size() - 1); i++) {
                g.setColor(c);
                g.fillOval((int) ((result.get(i).getX()) * PlanGPS.zoom) - (int) (10 * PlanGPS.zoom), (int) ((getMaxY() - result.get(i).getY()) * PlanGPS.zoom) - (int) (10 * PlanGPS.zoom), (int) (20 * PlanGPS.zoom), (int) (20 * PlanGPS.zoom));
                g.drawLine((int) (result.get(i).getX() * PlanGPS.zoom), (int) ((getMaxY() - result.get(i).getY()) * PlanGPS.zoom), (int) ((result.get(i + 1).getX()) * PlanGPS.zoom), (int) ((getMaxY() - result.get(i + 1).getY()) * PlanGPS.zoom));
                g.fillOval((int) ((result.get(i + 1).getX()) * PlanGPS.zoom) - (int) (10 * PlanGPS.zoom), (int) ((getMaxY() - result.get(i + 1).getY()) * PlanGPS.zoom) - (int) (10 * PlanGPS.zoom), (int) (20 * PlanGPS.zoom), (int) (20 * PlanGPS.zoom));
                g.setColor(Color.WHITE);
                g.drawString(result.get(i).getNom(), (int) ((result.get(i).getX() - 10) * PlanGPS.zoom), (int) ((maxY - result.get(i).getY() + 4) * PlanGPS.zoom));
            }
            g.setColor(Color.WHITE);
            g.drawString(result.get(result.size() - 1).getNom(), (int) (result.get(result.size() - 1).getX() - 10), (int) (maxY - result.get(result.size() - 1).getY() + 4));
            g.setColor(c);
        }
        g.drawString("" + this.getPreferredSize().getHeight() + " , " + this.getPreferredSize().getWidth(), 100, 100);
    }

    public void dessineChemin(ArrayList<PointGPS> sol) {
        if (sol != null) {
            result = sol;
            repaint();
        } else {
            System.out.println("ohh lala !!!");
        }
    }

    public void initRes() {
        result = null;
        repaint();
    }

    public void unSelectAll() {
        result = null;
        for (DessinPointGPS p : pointListe) {
            p.setClicked(false);
        }
        repaint();
    }

    public void ajouter(DessinPointGPS pt) {
        pointListe.add(pt);
        this.setPreferredSize(this.getMaxDim());
        maxY = ((Collections.max(pointListe, comY).get_Y() + 20) * PlanGPS.zoom);
        repaint();
    }

    public Dimension getMaxDim() {
        ptG = Collections.max(pointListe, comX);
        maxW = (int) ((ptG.get_X() + 20) * PlanGPS.zoom);
        ptG = Collections.max(pointListe, comY);
        maxH = (int) ((ptG.get_Y() + 20) * PlanGPS.zoom);
        dim.setSize(maxW, maxH);
        return dim;
    }

    public double getMaxY() {
        return maxY;
    }

    public void refresh() {
        this.setPreferredSize(this.getMaxDim());
        revalidate();
        repaint();
    }

    public void ajouter(DessinVecteur vect) {
        vecteurListe.add(vect);
        repaint();
    }

    public void ajouter(DessinRadar rd) {
        radarListe.add(rd);
        repaint();
    }

    public void ajouter(DessinTravaux tr) {
        travauxListe.add(tr);
        repaint();
    }

    public void ajouter(DessinBatiment bat) {
        batimentsListe.add(bat);
        repaint();
    }

    public ArrayList<DessinPointGPS> getPointGPS_List() {
        return this.pointListe;
    }

    public void updatePointGPS(int i, boolean b) {
        pointListe.get(i).setClicked(b);
        repaint();
    }

    public boolean getSelected(int i) {
        return pointListe.get(i).getClicked();
    }

    public ArrayList<DessinBatiment> getBatiment_list() {
        return batimentsListe;
    }

    public DessinPointGPS getPt(int i) {
        return pointListe.get(i);
    }

    public int rech(Point point) {
        int res = -12;
        for (int i = 0; i < pointListe.size(); i++) {
            DessinPointGPS pt = pointListe.get(i);
            if ((int) (point.getX()) >= (int) ((pt.point.getX() - 11) * PlanGPS.zoom) && (int) (point.getX()) <= (int) ((pt.point.getX() + 11) * PlanGPS.zoom)) {
                if ((int) (point.getY()) >= (int) (((getMaxY() - pt.point.getY() - 11)) * PlanGPS.zoom) && (int) (point.getY()) < (int) (((getMaxY() - pt.point.getY() + 11)) * PlanGPS.zoom)) {
                    res = i;
                    break;
                }
            }
        }
        return res;
    }
}
