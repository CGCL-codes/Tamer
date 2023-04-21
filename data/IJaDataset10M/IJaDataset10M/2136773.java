package corina.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.PageFormat;

/**
   A thin horizontal line, similar to HTML's &lt;HR&gt;.
   It defaults to the middle 1/2 of the page, but anything's possible.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: ThinLine.java,v 1.1 2004/01/18 18:09:10 aaron Exp $
*/
public class ThinLine implements Line {

    private float start = 0.25f, finish = 0.75f;

    /** Create a new default line, which covers the middle 1/2 of the
	page. */
    public ThinLine() {
    }

    /**
       Create a new line which covers only part of the width of the
       page.  The parameters are given in "fraction of the way across
       the visible page".

       @param start the left end of the line
       @param finish the right end of the line
    */
    public ThinLine(float start, float finish) {
        this.start = start;
        this.finish = finish;
    }

    public void print(Graphics g, PageFormat pf, float y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(THICKNESS));
        float left = (float) pf.getImageableX();
        float right = (float) (pf.getImageableX() + pf.getImageableWidth());
        float realLeft = left + (right - left) * start;
        float realRight = left + (right - left) * finish;
        g2.drawLine((int) realLeft, (int) (y + 1), (int) realRight, (int) (y + 1));
    }

    public int height(Graphics g) {
        return 2;
    }

    private static final float THICKNESS = 0.1f;
}
