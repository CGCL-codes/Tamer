package corina.print;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.print.PageFormat;

/**
   A line which can be added to a printable text document.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Line.java,v 1.2 2004/01/18 18:09:10 aaron Exp $
*/
public interface Line {

    /** A good size for titles: 18 points. */
    public static final int TITLE_SIZE = 18;

    /** A good size for section headings: 14 points. */
    public static final int SECTION_SIZE = 14;

    /** A good size for normal text: 10 points. */
    public static final int NORMAL_SIZE = 10;

    /** Normal font for printing. */
    public static final Font NORMAL = new Font("serif", Font.PLAIN, NORMAL_SIZE);

    /** Print the line.  You're given a Graphics object for drawing, a
	y-position to print it at, and the PageFormat which you can
	use to figure out how wide the page is.  (You will not be
	asked to draw it at a position that would go off the top or
	bottom of the page.)
	@param g the Graphics object to use for drawing
	@param pf the PageFormat of this page
	@param y the position from the top of the page to start
	drawing this line */
    public void print(Graphics g, PageFormat pf, float y);

    /** How tall is this line, in points?
	@param g Graphics used to measure the line
	@return the height of this line, in points */
    public int height(Graphics g);
}
