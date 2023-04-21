package iso;

import java.awt.*;

/**
 * It contains an Element. 
 * (Element is all drawable object in ISOCalc)
 *
 * If you want to add a building or something to the draw you have to 
 * make an Element for that!
 * <br><br>
 * An element stores it's bounding box, in ISO coordinates, to decide the order 
 * of the draw in a Viewport.
 * 
 * @author Garik
 * @version 0.1
 */
public class Element {

    /**This ID identifies what type of element made this from, in the element set. */
    private int setID;

    /**This name can be use to identify a unique element. */
    public String name;

    /**The Drawing coordinate of the image, what you use. */
    public int drawX, drawY;

    /**Package level flag variable to Viewports sorting algorithms. */
    boolean compared;

    /**The bounding box's X ISO coordinates. */
    public int ImaxX, IminX;

    /**The bounding box's Y ISO coordinates. */
    public int ImaxY, IminY;

    /**The bounding box's Z ISO coordinates. */
    public int ImaxZ, IminZ;

    /** Collision variable */
    private boolean moved;

    /**
	 * Constructor of an element.<br>
	 * <b>ATTENTION!</b> You should looking for a picture about this Element conception to the perfect understand. 
	 * 
	 * @param drawX the image's drawing x position
	 * @param drawY the image's drawing y position
	 * @param imgWidth the image's width (real == plot)
	 * @param imgHeight the image's height (real == plot)
	 * @param a_segment is the segment between the drawing position and the most left corner of the element image (in X) 
	 * @param startZ the starting Z coordinate in ISO or Plot (it's same)
	 * @param bodyHeight the element's height. It means that how many pixels higher with the image, than it would be just the base "tile"'s image.
	 */
    public Element(int drawX, int drawY, int imgWidth, int imgHeight, int a_segment, int startZ, int bodyHeight) {
        this.drawX = drawX;
        this.drawY = drawY;
        this.IminZ = startZ;
        this.ImaxZ = startZ + bodyHeight;
        Point p = new Point(drawX, drawY + a_segment);
        p = Transformer.plotToIso(p);
        this.IminX = p.x;
        this.IminY = p.y;
        p = new Point(drawX + imgWidth, drawY + imgHeight - a_segment + bodyHeight);
        p = Transformer.plotToIso(p);
        this.ImaxX = p.x;
        this.ImaxY = p.y;
    }

    /**
	 * It can move an Element in ISO coordinates.
	 * <br>
	 * <b>WARNING!</b> Do not use it directly! Use the Viewport's moveElementinIso method!
	 * 
	 * @param plusIx the moving plus x coordinate in ISO.
	 * @param plusIy the moving plus y coordinate in ISO.
	 * @param plusIz the moving plus z coordinate in ISO.
	 */
    public void moveInIso(int plusIx, int plusIy, int plusIz) {
        this.ImaxX += plusIx;
        this.IminX += plusIx;
        this.ImaxY += plusIy;
        this.IminY += plusIy;
        this.ImaxZ += plusIz;
        this.IminZ += plusIz;
        Point PMoveVektor = Transformer.isoToPlot(plusIx, plusIy);
        this.drawX += PMoveVektor.x;
        this.drawY += PMoveVektor.y;
        this.drawY -= plusIz;
        this.moved = true;
    }

    /**
	 * It can move an Element in plot (real) coordinates.
	 * <br>
	 * <b>WARNING!</b> Do not use it directly! Use the Viewport's moveElementinPlot method!
	 * 
	 * @param plusPx the moving plus x coordinate in Plot (real).
	 * @param plusPy the moving plus y coordinate in Plot (real).
	 * @param plusPz the moving plus z coordinate in Plot (real).
	 */
    public void moveInPlot(int plusPx, int plusPy, int plusPz) {
        this.drawX += plusPx;
        this.drawY += plusPy;
        this.drawY -= plusPz;
        Point IMoveVektor = Transformer.plotToIso(plusPx, plusPy);
        this.ImaxX += IMoveVektor.x;
        this.IminX += IMoveVektor.x;
        this.ImaxY += IMoveVektor.y;
        this.IminY += IMoveVektor.y;
        this.ImaxZ += plusPz;
        this.IminZ += plusPz;
        this.moved = true;
    }

    /**
	 * Gives the A side's length of the Element's bounding box in Plot (real).
	 * <br>
	 * <b>ATTENTION:</b> Show the image to know what side is A.
	 * <br>
	 * (It's practical to get how far should take Elements from each other
	 * to be next of.) 
	 * 
	 * @return the A side's length.
	 */
    public int getASideLength() {
        Point PaLength = Transformer.isoToPlot(ImaxX - IminX, 0);
        return PaLength.x;
    }

    /**
	 * Gives the B side's length of the Element's bounding box in Plot (real).
	 * <br>
	 * <b>ATTENTION:</b> Show the image to know what side is B.
	 * <br>
	 * (It's practical to get how far should take Elements from each other
	 * to be next of.) 
	 * 
	 * @return the B side's length.
	 */
    public int getBSideLength() {
        Point PbLength = Transformer.isoToPlot(0, ImaxY - IminY);
        return PbLength.x;
    }

    /**
	 * Set an ID for the Element.<br>
	 * (The ID identify the element. It can be used to identify what image 
	 * have to draw with this Element.)
	 * 
	 * @param id the new ID to be set.
	 */
    public void setID(int id) {
        this.setID = id;
    }

    /**
	 * Get the Element's ID.<br>
	 * (The ID identify the element. It can be used to identify what image 
	 * have to draw with this Element.)
	 * 
	 * @return the current ID of the Element.
	 */
    public int getID() {
        return setID;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean m) {
        this.moved = m;
    }
}
