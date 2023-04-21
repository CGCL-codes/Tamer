package ronp.lib.tools;

import java.awt.image.BufferedImage;
import ronp.lib.entities.Entity;
import ronp.lib.tools.graphics.NPImageInterface;

/**
 * Handles collisions
 * 
 * @author Daniel Centore
 *
 */
public class CollisionDetector {

    public static final int DEFAULT_OPACITY_THRESHOLD = 1;

    /**
	 * Checks if two entities would collide if entity a were to move
	 * First checks if their surrounding boxes collide, then uses the pixel method if so
	 * @param a Entity a
	 * @param xOffset How much it moves on the x and y axis
	 * @param yOffset
	 * @param b Entity b
	 * @return True if they would collide; false otherwise
	 */
    public static boolean hasCollision(Entity a, double xOffset, double yOffset, Entity b) {
        return (hasRectangleCollision(a, xOffset, yOffset, b)) && hasPixelCollision(a, xOffset, yOffset, b);
    }

    /**
	 * Checks if two entity's bounding boxes would collide if entity a were to move (fast). Recommended for large
	 * numbers of entities.
	 * @param a Entity a
	 * @param xOffset How much it moves on the x and y axis
	 * @param yOffset
	 * @param b Entity b
	 * @return True if they would collide; false otherwise
	 */
    public static boolean hasRectangleCollision(Entity a, double xOffset, double yOffset, Entity b) {
        int x = (int) (a.getX() + xOffset);
        int y = (int) (a.getY() + yOffset);
        int x1 = (int) Math.max(x, b.getX());
        int y1 = (int) Math.max(y, b.getY());
        int x2 = (int) Math.min(x + a.getWidth(), b.getX() + b.getWidth());
        int y2 = (int) Math.min(y + a.getHeight(), b.getY() + b.getHeight());
        return (x2 - x1 > 0 && y2 - y1 > 0);
    }

    /**
	 * Checks if two entities would collide by pixel if entity a were to move (slow)
	 * @param a Entity a
	 * @param xOffset How much it moves on the x and y axis
	 * @param yOffset
	 * @param b Entity b
	 * @return True if they would collide; false otherwise
	 */
    private static boolean hasPixelCollision(Entity a, double xOffset, double yOffset, Entity b) {
        return isPixelCollide((int) (a.getX() + xOffset), (int) (a.getY() + yOffset), a.getCollisionImage(), (int) b.getX(), (int) b.getY(), b.getCollisionImage());
    }

    /**
	 * Checks if an image collides with an entity
	 * @param a1 The image
	 * @param x The image's X and Y coordinates
	 * @param y
	 * @param b The entity
	 * @return Whether or not there is a collision
	 */
    public static boolean hasCollision(NPImageInterface a1, int x, int y, Entity b) {
        return (hasRectangleCollision(a1.getCollisionImage(), x, y, b) && hasPixelCollision(a1.getCollisionImage(), x, y, b));
    }

    private static boolean hasPixelCollision(BufferedImage a1, int x, int y, Entity b) {
        return isPixelCollide(x, y, a1, (int) b.getX(), (int) b.getY(), b.getImage());
    }

    private static boolean hasRectangleCollision(BufferedImage a1, int x, int y, Entity b) {
        int x1 = (int) Math.max(x, b.getX());
        int y1 = (int) Math.max(y, b.getY());
        int x2 = (int) Math.min(x + a1.getWidth(), b.getX() + b.getWidth());
        int y2 = (int) Math.min(y + a1.getHeight(), b.getY() + b.getHeight());
        return (x2 - x1 > 0 && y2 - y1 > 0);
    }

    private static boolean isPixelCollide(int x1, int y1, BufferedImage image1, int x2, int y2, BufferedImage image2) {
        int width1 = x1 + image1.getWidth();
        int height1 = y1 + image1.getHeight();
        int width2 = x2 + image2.getWidth();
        int height2 = y2 + image2.getHeight();
        int xstart = Math.max(x1, x2);
        int ystart = Math.max(y1, y2);
        int xend = Math.min(width1, width2);
        int yend = Math.min(height1, height2);
        int toty = Math.abs(yend - ystart);
        int totx = Math.abs(xend - xstart);
        for (int y = 0; y < toty; y++) {
            int ny = Math.abs(ystart - y1) + y;
            int ny1 = Math.abs(ystart - y2) + y;
            for (int x = 0; x < totx; x++) {
                int nx = Math.abs(xstart - x1) + x;
                int nx1 = Math.abs(xstart - x2) + x;
                try {
                    if (!isTransparent(image1.getRGB(nx, ny)) && !isTransparent(image2.getRGB(nx1, ny1))) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    /**
	 * Checks if a color can be considered transparent. It is considered so if its alpha level is below {@link DEFAULT_OPACITY_THRESHOLD}
	 * @param color The ARGB color to check
	 * @return True if it is transparent; False otherwise
	 */
    public static boolean isTransparent(int color) {
        return (((color >> 24) & 0xff) <= DEFAULT_OPACITY_THRESHOLD);
    }
}
