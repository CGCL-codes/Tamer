package net.sf.gamearea.room;

import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class Element implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2684393074810717818L;

    public int id;

    public int x;

    public int y;

    public abstract void paint(Graphics2D g);

    public abstract boolean inRange(int x, int y);
}
