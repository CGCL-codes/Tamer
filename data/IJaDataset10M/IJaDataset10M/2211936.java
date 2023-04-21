package org.apache.batik.svggen;

/**
 * The <code>ErrorHandler</code> interface allows you to specialize
 * how the error will be set on an SVG <code>Element</code>.
 *
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @version $Id: ErrorHandler.java,v 1.1 2005/11/21 09:51:19 dev Exp $
 */
public interface ErrorHandler {

    /**
     * This method handles the <code>SVGGraphics2DIOException</code>.
     */
    public void handleError(SVGGraphics2DIOException ex) throws SVGGraphics2DIOException;

    /**
     * This method handles the <code>SVGGraphics2DRuntimeException</code>.
     */
    public void handleError(SVGGraphics2DRuntimeException ex) throws SVGGraphics2DRuntimeException;
}
