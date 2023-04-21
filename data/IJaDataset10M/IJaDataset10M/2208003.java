package objectif.lyon.gui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import objectif.lyon.data.Route;
import objectif.lyon.data.Segment;
import objectif.lyon.designer.gui.DesignerPane;

public class Plateau extends JPanel implements MouseListener, MouseMotionListener {

    /**
     * Num�ro de version pour s�rialisation
     */
    private static final long serialVersionUID = 1L;

    /**
     * Noeuds composant toutes les routes du plateau
     */
    private LinkedList<NoeudElement> noeuds = null;

    private LinkedList<Route> routes = null;

    private Segment nodeHover = null;

    private BufferedImage background = null;

    /**
     * Construit un plateau de jeu.
     * TODO : Comment d�terminer de quel plateau il s'agit ?
     */
    public Plateau() {
        this.noeuds = new LinkedList<NoeudElement>();
        this.routes = new LinkedList<Route>();
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.addMouseMotionListener(this);
        try {
            background = ImageIO.read(DesignerPane.class.getResourceAsStream("resources/background2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajouter un noeud
     * @param element Noeud � ajouter
     */
    public void add(NoeudElement element) {
        noeuds.add(element);
        if (!routes.contains(element.getNoeud().getRoute())) {
            routes.add(element.getNoeud().getRoute());
        }
    }

    /**
     * @return the background
     */
    public BufferedImage getBackgroundImage() {
        return background;
    }

    /**
     * @param background the background to set
     */
    public void setBackground(BufferedImage background) {
        this.background = background;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, 1000, 570, null);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new Color(255, 255, 255, 100));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (NoeudElement element : noeuds) {
            element.paintComponent(g2d);
        }
        g2d.setPaint(new Color(189, 193, 197));
        g2d.drawLine(1000, 0, 1000, 570);
        g2d.drawLine(0, 570, 1000, 570);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (NoeudElement element : noeuds) {
            if (element.getArea() != null) {
                if (element.getArea().contains(e.getPoint().getX(), e.getPoint().getY())) {
                } else if (element.getNoeud().equals(nodeHover)) {
                }
            }
        }
    }
}
