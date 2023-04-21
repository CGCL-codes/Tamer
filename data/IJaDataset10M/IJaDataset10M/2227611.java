package ch.ethz.mxquery.update.store.domImpl.tokens;

import com.google.gwt.dom.client.Node;
import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.Source;
import ch.ethz.mxquery.datamodel.StructuralIdentifier;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.XDMScope;
import ch.ethz.mxquery.update.store.domImpl.DOMSimpleNodeIdentifier;
import ch.ethz.mxquery.update.store.domImpl.DOMStore;
import ch.ethz.mxquery.update.store.domImpl.DOMXDMScope;
import ch.ethz.mxquery.xdmio.xmlAdapters.RealDOMElement;
import ch.ethz.mxquery.xdmio.xmlAdapters.RealDOMNode;

public class DOMTextToken extends DOMSimpleNodeToken {

    public DOMTextToken(Node node, DOMElementToken parent, DOMStore store) {
        super(node, parent, store);
    }

    @Override
    public int getEventType() {
        return Type.TEXT_NODE_UNTYPED_ATOMIC;
    }

    ;
}
