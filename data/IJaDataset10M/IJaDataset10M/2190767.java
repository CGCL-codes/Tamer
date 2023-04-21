package spaceopera.gui.window;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import spaceopera.gui.SpaceOpera;
import spaceopera.gui.menu.PlanetMenu;
import spaceopera.universe.SOConstants;
import spaceopera.universe.elements.Planet;
import spaceopera.universe.elements.SunSystem;

/**
 * The SunSystemDisplay displays the number of planets and the orbitals of a
 * sunsystem in the the top right part of the main window
 */
public class SunSystemDisplay extends JPanel implements MouseListener, MouseMotionListener, SOConstants {

    private SunSystem sunSystem;

    private SpaceOpera spaceOpera;

    private PlanetMenu planetMenu = null;

    private int planetMenu_X = 0;

    private int planetMenu_Y = 0;

    public void setSunSystem(SunSystem s) {
        sunSystem = s;
    }

    public SunSystemDisplay(SunSystem s, SpaceOpera so) {
        sunSystem = s;
        spaceOpera = so;
        addMouseListener(this);
        addMouseMotionListener(this);
        setCursor(Cursor.getDefaultCursor());
        setBounds(0, 0, SUNSYSTEMFRAMEX, SUNSYSTEMFRAMEY);
        setOpaque(true);
    }

    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        boolean refresh = false;
        boolean inside = false;
        Vector planets = sunSystem.getPlanets();
        for (int i = 0; i < planets.size(); i++) {
            Planet planet = (Planet) planets.elementAt(i);
            if (event.getClickCount() > 1) {
                if (planet.getSelected()) {
                    spaceOpera.displayPlanet(planet);
                    if (planet.isColonized() && planet.getColony().getPlayer().equals(spaceOpera.getUniverse().getPlayer())) {
                        spaceOpera.displayColony(planet.getColony());
                    }
                }
            } else if (event.getModifiers() == InputEvent.BUTTON1_MASK) {
                planet.setSelected(false);
                if (planet.inside(x, y)) {
                    if (!inside) {
                        refresh = true;
                        inside = true;
                        planet.setSelected(true);
                        if (sunSystem.getExplored().containsKey(spaceOpera.getCurrentPlayerName())) {
                            spaceOpera.getCurrentPlayer().setCurrentPlanet(planet);
                            spaceOpera.changePlanetImage(planet);
                        }
                    }
                }
            } else {
                if (planet.getSelected()) {
                    planetMenu = new PlanetMenu(spaceOpera, sunSystem.getSun(), planet);
                    this.add(planetMenu);
                    planetMenu_X = event.getX();
                    planetMenu_Y = event.getY();
                    planetMenu.show(this, planetMenu_X, planetMenu_Y);
                }
            }
        }
        if (refresh) {
            repaint();
        }
    }

    public void mouseDragged(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseMoved(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setOpaque(true);
        Graphics g2 = g.create();
        Dimension d = getSize();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, d.width, d.height);
        g.setColor(getForeground());
        int x = SUNSYSTEMFRAMEX / 2;
        int y = SUNSYSTEMFRAMEY / 2;
        g2.setColor(Color.white);
        for (int i = 0; i < SSBACKGROUNDSTARS; i++) {
            g2.drawOval(sunSystem.getBackgroundStarX(i), sunSystem.getBackgroundStarY(i), 1, 1);
        }
        g2.setColor(sunSystem.getSun().getColor());
        int middle = STARSIZE / 2;
        g2.fillOval(x, y, STARSIZE, STARSIZE);
        g2.drawLine(x + middle - 1, y - 2, x + middle - 1, y + STARSIZE + 1);
        g2.drawLine(x + middle, y - 2, x + middle, y + STARSIZE + 1);
        g2.drawLine(x - 2, y + middle, x + STARSIZE + 1, y + middle);
        g2.drawLine(x - 2, y + middle - 1, x + STARSIZE + 1, y + middle - 1);
        Vector planets = sunSystem.getPlanets();
        if (sunSystem.getExplored().containsKey(spaceOpera.getCurrentPlayerName())) {
            for (int i = 0; i < planets.size(); i++) {
                Planet p = (Planet) planets.elementAt(i);
                g2.setColor(Color.white);
                int radius = p.getOrbit().getRadius();
                int position = p.getOrbit().getPosition();
                g2.drawOval((x + STARSIZE / 2) - radius, (y + STARSIZE / 2) - radius, 2 * radius, 2 * radius);
                int px = x + STARSIZE / 2 + (int) (radius * Math.cos(position * 2.0 * Math.PI / 360.0));
                int py = y + STARSIZE / 2 + (int) (radius * Math.sin(position * 2.0 * Math.PI / 360.0));
                p.display(g2, px, py);
            }
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 100, 30);
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(sunSystem.getSun().getName(), 10, 20);
        } else {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 100, 50);
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(sunSystem.getSun().getName(), 10, 20);
            g2.drawString("System not explored", 10, 35);
        }
        g2.dispose();
    }
}
