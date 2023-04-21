package AstroAttack;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;

/**	The Ship class represents the player's ship entity. */
public class Ship extends Entity {

    /**	The maximum amount of health the Ship can have. */
    public static final int MAX_HEALTH = 100;

    /**	The single instance of the Ship. */
    private static Ship m_ref;

    /**	The width of the border, it's needed in the move() method. */
    private int m_borderWidth;

    /**	Returns the single instance of the Ship class.
	*	@return	the instance
	*/
    public static Ship getShip() {
        if (m_ref == null) m_ref = new Ship();
        return m_ref;
    }

    /**	Constructs the ship with it's image and default location. */
    private Ship() {
        super(null, null, MAX_HEALTH);
        ImageCache cache = ImageCache.getCache();
        Image shipImg = cache.getImage(WorldManager.getManager().getShipImage());
        setImage(shipImg);
        Canvas canvas = Engine.getEngine().getCanvas();
        double xPos = (canvas.getWidth() / 2) - (shipImg.getWidth(null) / 2);
        double yPos = canvas.getHeight() - shipImg.getHeight(null) - 36;
        Point2D point = new Point2D.Double(xPos, yPos);
        super.setPoint(point);
    }

    public void setPoint(Point2D point) {
        Canvas canvas = Engine.getEngine().getCanvas();
        double minX = 0;
        double minY = canvas.getHeight() / 2 + 25;
        double maxX = canvas.getWidth() - getBounds().getWidth() - m_borderWidth;
        double maxY = canvas.getHeight() - getBounds().getHeight();
        double newXPos = point.getX();
        double newYPos = point.getY();
        if (!(newXPos > minX) || !(newXPos < maxX)) point.setLocation(getPoint().getX(), point.getY());
        if (!(newYPos > minY) | !(newYPos < maxY)) point.setLocation(point.getX(), getPoint().getY());
        super.setPoint(point);
    }

    /**	Draws the Ship and its health bar.
	*	@param	gfx	the graphics context to use
	*/
    public void render(Graphics2D gfx) {
        super.render(gfx);
        Canvas canvas = Engine.getEngine().getCanvas();
        int health = getHealth();
        int borderPadding = canvas.getWidth() * 3 / AstroFrame.DEFAULT_WIDTH;
        int healthBarWidth = canvas.getWidth() * 10 / AstroFrame.DEFAULT_WIDTH;
        int healthBarMaxHeight = canvas.getHeight() - borderPadding * 2;
        int healthBarHeight = healthBarMaxHeight * health / MAX_HEALTH;
        m_borderWidth = healthBarWidth + borderPadding * 2;
        int borderHeight = healthBarMaxHeight + borderPadding * 2;
        int xPos = canvas.getWidth() - m_borderWidth;
        int yPos = canvas.getHeight() - borderHeight;
        gfx.setColor(Color.black);
        gfx.fillRect(xPos, yPos, m_borderWidth, borderHeight);
        xPos = canvas.getWidth() - healthBarWidth - borderPadding;
        yPos = canvas.getHeight() - healthBarHeight - borderPadding;
        int yPos2 = canvas.getHeight() - healthBarMaxHeight - borderPadding;
        gfx.setColor(Color.red);
        gfx.fillRect(xPos, yPos2, healthBarWidth, healthBarMaxHeight);
        gfx.setColor(Color.green);
        gfx.fillRect(xPos, yPos, healthBarWidth, healthBarHeight);
    }
}
