package org.rakiura.cpn.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import org.rakiura.draw.ParentFigure;
import org.rakiura.draw.figure.CompositeFigure;
import org.rakiura.draw.util.BoxHandleKit;

/**
 * Represents a single token from a Place multiset. This is a circle or a text 
 * representation of the token.
 * 
 * <br><br>
 * CPNTokenFigure.java created on 6/07/2003 18:03:24<br><br>
 *
 *@author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 *@version @version@ 
 */
public class CPNTokenFigure extends CompositeFigure {

    private static final long serialVersionUID = 3617290108257317170L;

    /** The actual token handle. */
    private Object token;

    /**
	 * Creates new child text figure.
	 * @param t
	 */
    public CPNTokenFigure(Object t) {
        this.token = t;
    }

    public Object getToken() {
        return this.token;
    }

    public boolean canBeParent(@SuppressWarnings("unused") final ParentFigure aFig) {
        return false;
    }

    public Vector handles() {
        Vector handles = new Vector();
        BoxHandleKit.addHandles(this, handles);
        return handles;
    }

    public void basicDisplayBox(final Point origin, final Point corner) {
        Rectangle box = displayBox();
        int movex, movey;
        movex = origin.x - box.x;
        movey = origin.y - box.y;
        basicMoveBy(movex, movey);
    }
}
