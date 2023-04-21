package org.insight.model.graphs;

import java.awt.image.BufferedImage;

/**
 * Superclass for all graph implementations.
 *
 * <pre>
 * Version History:
 *
 * $Log: Graph.java,v $
 * Revision 1.6  2006/02/03 10:15:11  cjn
 * Fixed checkstyle violations.
 *
 * Revision 1.5  2003/03/19 09:59:25  cjn
 * Refactored packages
 *
 * Revision 1.4  2002/08/30 12:53:42  cjn
 * Added Chart2D line graph implementation
 *
 * Revision 1.3  2002/08/22 08:24:38  cjn
 * Added common methods, moved image types constants to ImageType class
 *
 * Revision 1.2  2002/07/05 09:08:42  cjn
 * Added CVS keywords
 * </pre>
 *
 * @author Chris Nappin
 * @version $Revision: 1.6 $
 */
public abstract class Graph {

    /**
     * Get the image object for this graph.
     * @return The image
     */
    public abstract BufferedImage getImage();

    /**
     * Set the title of the graph.
     * @param title The title
     */
    public abstract void setTitle(String title);
}
