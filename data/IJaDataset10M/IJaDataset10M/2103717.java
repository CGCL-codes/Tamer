package org.jcrpg.world.ai.flora;

/**
 * Used to add data to a flora in a Belt/Level map.
 * @author pali
 *
 */
public class FloraListElement {

    public Flora flora;

    public boolean alwaysPresent = false;

    /**
	 * 0-1000, likeness to grow, X:1000.
	 */
    public int likenessToGrow = 0;

    public FloraListElement(Flora flora, int likenessToGrow) {
        super();
        this.flora = flora;
        this.alwaysPresent = false;
        this.likenessToGrow = likenessToGrow;
    }

    /**
	 * 
	 * @param flora
	 * @param alwaysPresent
	 */
    public FloraListElement(Flora flora) {
        super();
        this.flora = flora;
        this.alwaysPresent = true;
        this.likenessToGrow = 1000;
    }
}
