package org.apache.batik.svggen;

import java.util.List;
import org.apache.batik.ext.awt.g2d.GraphicContext;

/**
 * Defines the interface for classes that are able to convert
 * part or all of a GraphicContext.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: SVGConverter.java,v 1.1 2005/11/21 09:51:19 dev Exp $
 * @see           org.apache.batik.ext.awt.g2d.GraphicContext
 */
public interface SVGConverter extends SVGSyntax {

    /**
     * Converts part or all of the input GraphicContext into
     * a set of attribute/value pairs and related definitions
     *
     * @param gc GraphicContext to be converted
     * @return descriptor of the attributes required to represent
     *         some or all of the GraphicContext state, along
     *         with the related definitions
     * @see org.apache.batik.svggen.SVGDescriptor
     */
    public SVGDescriptor toSVG(GraphicContext gc);

    /**
     * @return set of definitions referenced by the attribute
     *         values created by the implementation since its
     *         creation. The return value should never be null.
     *         If no definition is needed, an empty set should be
     *         returned.
     */
    public List getDefinitionSet();
}
