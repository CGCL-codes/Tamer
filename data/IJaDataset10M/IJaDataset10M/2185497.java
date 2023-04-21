package ch.ethz.mxquery.update.store.domImpl;

import ch.ethz.mxquery.xdmio.xmlAdapters.RealDOMElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class RealDomDocument extends Document {

    protected RealDomDocument() {
    }

    public final native RealDOMElement createElementNS(String namespace, String name);

    public final native Node createComment(String value);
}
