package tefkat.engine;

import java.util.Collection;

/**
 * @author lawley
 *
 */
public interface ExtentListener {

    public void highlightEdge(Object src, Object dst, int reason);

    public void highlightNode(Object object, int reason);

    public void highlightNodes(Collection objects, int reason);

    public void highlightNodes(Binding binding, int reason);
}
