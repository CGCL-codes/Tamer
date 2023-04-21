package il.ac.biu.cs.grossmm.api.presence;

import nu.xom.Element;
import il.ac.biu.cs.grossmm.api.data.NodeInterface;
import il.ac.biu.cs.grossmm.api.data.Property;
import il.ac.biu.cs.grossmm.api.data.Unique;
import static il.ac.biu.cs.grossmm.api.data.NodeTypeByInterface.*;

/**
 * Data node type for representation of extensible PIDF tuple.
 */
public interface XomPidfTuple extends NodeInterface {

    /**
	 * Unique identifier of this tuple
	 */
    Unique<XomPidfTuple, String> ID = unique(XomPidfTuple.class, String.class);

    /**
	 * XOM element which represents this tuple
	 */
    Property<XomPidfTuple, Element> TUPLE = property(XomPidfTuple.class, Element.class);
}
