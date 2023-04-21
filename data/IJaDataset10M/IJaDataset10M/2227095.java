package org.apache.batik.ext.awt.image.renderable;

import java.awt.geom.Rectangle2D;

/**
 * Implements a filter operation.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: FilterChainRable.java,v 1.1 2005/11/21 09:51:20 dev Exp $
 */
public interface FilterChainRable extends Filter {

    /**
     * Returns the resolution along the X axis.
     */
    public int getFilterResolutionX();

    /**
     * Sets the resolution along the X axis, i.e., the maximum
     * size for intermediate images along that axis.
     * The value should be greater than zero to have an effect.
     */
    public void setFilterResolutionX(int filterResolutionX);

    /**
     * Returns the resolution along the Y axis.
     */
    public int getFilterResolutionY();

    /**
     * Sets the resolution along the Y axis, i.e., the maximum
     * size for intermediate images along that axis.
     * The value should be greater than zero to have an effect.
     */
    public void setFilterResolutionY(int filterResolutionY);

    /**
     * Sets the filter output area, in user space. 
     */
    public void setFilterRegion(Rectangle2D filterRegion);

    /**
     * Returns the filter output area, in user space
     */
    public Rectangle2D getFilterRegion();

    /**
     * Sets the source for this chain. Should not be null
     */
    public void setSource(Filter src);

    /**
     * Returns this filter's source.
     */
    public Filter getSource();
}
