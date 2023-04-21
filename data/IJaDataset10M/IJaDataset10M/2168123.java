package net.sf.gef.core.model;

import java.util.List;

public abstract class AbstractEndPoint extends AbstractGEFNode implements RouteEndPoint {

    private static final long serialVersionUID = 2014543229905972868L;

    public AbstractEndPoint() {
    }

    public abstract void addConnection(Route wire);

    public List<Route> getSourceConnections() {
        return null;
    }

    public List<Route> getTargetConnections() {
        return null;
    }

    public abstract void removeConnection(Route conn);

    public abstract void setSource(Route wire);

    public abstract void setTarget(Route wire);
}
