package org.opencarto.style;

/**
 * A style with transparency.
 * 
 * @author julien Gaffuri
 */
public interface WithTransparencyStyle extends Style {

    /**
	 * @return the transparency is an integer value within [0,255]
	 */
    public int getTransparency();

    /**
	 * @param transparency an integer value within [0,255]
	 */
    public void setTransparency(int transparency);
}
