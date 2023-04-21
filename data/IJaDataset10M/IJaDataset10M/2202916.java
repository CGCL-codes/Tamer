package org.orangegears.ofbiz;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IController {

    public List<Node> getHandlers();

    public List<Node> getViewHandlers();

    public List<Node> getRequestHandlers();

    public List<Node> getViewMaps();

    public List<Node> getRequestMaps();

    public Node getRequestMap(String uri) throws NullPointerException;

    public Node getViewMap(String name) throws NullPointerException;
}
