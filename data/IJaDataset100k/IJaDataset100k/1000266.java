package commonapp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
   Implements an icon that can be used on a button to indicate a "previous"
   action.

   @author Juan Heyns, 2004
*/
public class JPreviousIcon implements Icon {

    protected int myWidth;

    protected int myHeight;

    protected int[] myXPoints = new int[3];

    protected int[] myYPoints = new int[3];

    public JPreviousIcon(int theWidth, int theHeight) {
        setDimension(theWidth, theHeight);
    }

    public int getIconWidth() {
        return myWidth;
    }

    public int getIconHeight() {
        return myHeight;
    }

    public void setDimension(int theWidth, int theHeight) {
        myWidth = theWidth;
        myHeight = theHeight;
    }

    public void paintIcon(Component theComponent, Graphics theGraphics, int theX, int theY) {
        myXPoints[0] = theX;
        myYPoints[0] = theY + (myHeight / 2);
        myXPoints[1] = theX + myWidth;
        myYPoints[1] = theY - 1;
        myXPoints[2] = theX + myWidth;
        myYPoints[2] = theY + myHeight;
        theGraphics.setColor(Color.BLACK);
        theGraphics.fillPolygon(myXPoints, myYPoints, 3);
    }
}
