package org.apache.batik.bridge;

import org.w3c.dom.Element;

/**
 * A tagging interface that bridges for elements child of <tt>GraphicsNodeBridge</tt>
 * should implement.
 *
 * @author <a href="mailto:vincent.hardy@apache.org">Vincent Hardy</a>
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id: GenericBridge.java,v 1.1 2005/11/21 09:51:18 dev Exp $
 */
public interface GenericBridge extends Bridge {

    /**
     * Invoked to handle an <tt>Element</tt> for a given <tt>BridgeContext</tt>.
     * For example, see the <tt>SVGTitleElementBridge</tt>.
     *
     * @param ctx the bridge context to use
     * @param e the element that describes the graphics node to build
     */
    void handleElement(BridgeContext ctx, Element e);
}
