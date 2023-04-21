package org.itsnat.impl.core.event.fromclient.domstd;

import org.w3c.dom.Node;

/**
 * Esta clase es debido a que la resoluci�n de un path
 * es costoso pues hay que recorrer el �rbol, sirve para
 * que se haga s�lo una vez (se supone que el nodo no cambia
 * de sitio en el �rbol).
 *
 * @author jmarranz
 */
public class NodeContainer {

    protected Node node;

    /**
     * Creates a new instance of NodeContainer
     */
    public NodeContainer(Node node) {
        this.node = node;
    }

    public Node get() {
        return node;
    }
}
