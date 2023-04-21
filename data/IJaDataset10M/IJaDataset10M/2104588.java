package risk.game;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *
 */
public class MapView extends JPanel {

    /**
     * Karte, auf der gespielt wird.
     */
    private Image img;

    /**
     * Die Instanz der WorldMap, die alle Kontinente 
     * und Laender beinhaltet.
     */
    private WorldMap worldMap;

    /**
     * temporäre territory
     */
    private ArrayList<Territory> territoryBattle = null;

    /**
     * Konstruktor MapView
     * Aufgabe ist das Setzen der WorldMap
     * und die Hintergrundgrafik (Landkarte)
     * zu setzen.
     *
     * @param worldMap   Zum View gehoerige Instanz der Klasse WorldMap.
     */
    public MapView(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.territoryBattle = new ArrayList<Territory>();
        this.setLayout(null);
        String path = System.getProperty("java.class.path") + "/risk/maps/" + worldMap.getGraphic();
        MediaTracker mt = new MediaTracker(this);
        this.img = Toolkit.getDefaultToolkit().getImage(path);
        mt.addImage(this.img, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        this.setVisible(true);
    }

    /**
     * paintComponent schreibt alle Laendernamen an die entsprechende
     * Stelle der Landkarte (Hintergrundgrafik) und zeichnet die 
     * Laendergrenzen auf der Landkarte nach.
     *
     * @param g  Zeichenflaeche der Landkarte
     */
    public void paintComponent(Graphics g) {
        Shape shape;
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        Font myFont = new Font("Times New Roman", Font.BOLD, 12);
        g2d.setFont(myFont);
        g2d.setStroke(new BasicStroke(2.0f));
        Territory territory;
        Color color;
        ListIterator territories = worldMap.getTerritories().listIterator();
        while (territories.hasNext()) {
            territory = (Territory) territories.next();
            if (territory.getOwner() != null) {
                color = territory.getOwner().getPlayerColor();
            } else {
                color = Color.WHITE;
            }
            g2d.setColor(color);
            g2d.drawString(territory.getName(), (int) territory.getMidpoint().getX() - 15, (int) territory.getMidpoint().getY() - 10);
            g2d.drawString(new Integer(territory.getArmySize()).toString(), (int) territory.getMidpoint().getX(), (int) territory.getMidpoint().getY());
        }
        if (territoryBattle.size() != 0) {
            for (int j = 0; j < territoryBattle.size(); j++) {
                g2d.setColor(territoryBattle.get(j).getOwner().getPlayerColor());
                g2d.drawPolygon(territoryBattle.get(j).getFrontiers());
            }
        }
        repaint();
    }

    /**
     * Zeichnen der Grenzen eines Landes. Methode setzt die Instanzvariable
     * territoryTmp und zeichnet das Spielbrett neu
     *
     * @param Territory territory von dem die Grenzen gezeichnet werden sollen
     */
    public void paintFrontiers(Territory territory) {
        this.territoryBattle.add(territory);
        repaint();
    }

    /**
     * Loeschen der Grenzen
     */
    public void removeBattleTerritories() {
        this.territoryBattle.clear();
        repaint();
    }

    /**
     * Gibt zurueck, ob Grenzen gezeichnet sind
     *
     * @return boolean bool Wahrheitswert, ob Grenzen gezeichnet sind
     */
    public boolean isPaintingFrontiers() {
        return !this.territoryBattle.isEmpty();
    }
}
