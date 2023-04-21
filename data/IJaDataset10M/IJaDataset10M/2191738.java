package org.apache.batik.transcoder.keys;

import org.apache.batik.transcoder.TranscodingHints;
import org.w3c.dom.DOMImplementation;

/**
 * A transcoding Key represented as a DOMImplementation.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id: DOMImplementationKey.java,v 1.1 2005/11/21 09:51:30 dev Exp $
 */
public class DOMImplementationKey extends TranscodingHints.Key {

    public boolean isCompatibleValue(Object v) {
        return (v instanceof DOMImplementation);
    }
}
