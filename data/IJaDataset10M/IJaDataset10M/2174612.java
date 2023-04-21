package spacefaring.ui.game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import spacefaring.game.starsystem.Planet;
import spacefaring.graphics.Renderer;
import spacefaring.Spacefaring;
import spacefaring.ui.DisplayController;
import spacefaring.util.IntPoint2D;

/**
 * This control shows a planet with a space backdrop.
 *
 * @author astrometric
 */
public class PlanetIconView extends GameView {

    private Planet planet = null;

    private boolean pressed = false;

    public PlanetIconView(String viewname, Spacefaring sfinstance) {
        super(viewname, sfinstance);
        setSize(100, 100);
    }

    public void setPlanet(Planet newplanet) {
        planet = newplanet;
    }

    public Planet getPlanet() {
        return planet;
    }

    /**
     * Paints the planet with space backdrop in a box.
     */
    @Override
    public void paint(Graphics2D g, Renderer renderer) {
        if (planet == null) return;
        int planethash = planet.hashCode();
        int backdropx = planethash & 0x3ff;
        int backdropy = (planethash >> 10) & 0x1ff;
        int planetpixelsize = (int) (planet.getSize() / 200);
        Rectangle currentrect = getRectangle();
        IntPoint2D centerpos = getCenterPos();
        Rectangle backdroprect = new Rectangle(backdropx, backdropy, currentrect.width, currentrect.height);
        setFullClip(g);
        sf.dc.getRenderer().getSpaceBackdrop().paintBackdrop(g, currentrect, backdroprect);
        renderer.paintPlanet(g, centerpos.x, centerpos.y, planetpixelsize, planet.getColor());
        paintOnlyEdges(g, DisplayController.maincolor);
    }

    /**
     * Shows message box with planet info.
     */
    public void showPlanetInfo() {
        sf.dc.vc.displayMessage(planet.getInfo());
    }

    /**
     * Shows message box with planet info on click.
     */
    @Override
    public void processMouseEvent(MouseEvent me) {
        if (me.getID() == MouseEvent.MOUSE_PRESSED) {
            pressed = true;
        }
        if (me.getID() == MouseEvent.MOUSE_RELEASED) {
            if (pressed && isInRectangle(me.getX(), me.getY())) {
                showPlanetInfo();
            }
            pressed = false;
        }
    }
}
