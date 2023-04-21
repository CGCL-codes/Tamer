package com.c4j.diagram.impl;

import static com.c4j.diagram.impl.PortHandler.NODE_PORT_DIRECTIONS;
import static com.c4j.diagram.impl.PortHandler.NODE_PORT_DISTANCES;
import static java.lang.String.format;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.c4j.diagram.Direction;
import com.c4j.diagram.INode;
import com.c4j.diagram.INodeRenderer;
import com.c4j.diagram.INodeRepresentative;
import com.c4j.diagram.IPort;
import com.c4j.diagram.IPortRepresentative;
import com.c4j.diagram.Raster;
import com.c4j.diagram.basic.ChainedRestriction;
import com.c4j.diagram.basic.FreePoint;
import com.c4j.diagram.basic.IHoverListener;
import com.c4j.diagram.basic.RasterRestriction;
import com.c4j.diagram.basic.RectHoverable;
import com.c4j.diagram.basic.ZOrderGroup;
import com.c4j.sre.C4JRuntimeException;

public class Node extends ZOrderGroup implements INode, IHoverListener {

    private final Diagram diagram;

    private final INodeRepresentative nodeRepresentative;

    private final Raster raster;

    private final FreePoint f1;

    final FreePoint f2;

    private final Map<IPortRepresentative, Port> portMap;

    private final Map<IPortRepresentative, PortHandler> portHandlerMap;

    private INodeRenderer renderer;

    private boolean isHovered;

    Node(final Diagram diagram, final DiagramHandler backNodeHandler, final INodeRepresentative represenative, final Raster raster, final ZOrderGroup parentGroup) {
        super(parentGroup);
        this.diagram = diagram;
        this.nodeRepresentative = represenative;
        this.raster = raster;
        f1 = new FreePoint(this, 150, 150);
        f2 = new FreePoint(this, 350, 300);
        f1.setSetterRestriction(new ChainedRestriction(backNodeHandler.getTopLeftNodeRestriction(), new RasterRestriction(raster)));
        f2.setSetterRestriction(new ChainedRestriction(backNodeHandler.getButtomRightNodeRestriction(), new RasterRestriction(raster)));
        f1.move(0, 0);
        f1.commit();
        f2.move(0, 0);
        f2.commit();
        NodeHandler.createNodeHandler(this, this, f1, f2);
        portMap = new HashMap<IPortRepresentative, Port>();
        portHandlerMap = new HashMap<IPortRepresentative, PortHandler>();
    }

    @Override
    public Port addPort(final IPortRepresentative representative, final Direction direction) {
        if (portMap.containsKey(representative)) throw new C4JRuntimeException("The port representative is already registered at the node.");
        final Port result = new Port(this, f1, f2, raster, NODE_PORT_DIRECTIONS, NODE_PORT_DISTANCES, representative, direction);
        portHandlerMap.put(representative, result.getPortHandler());
        portMap.put(representative, result);
        diagram.getNodePortMap().put(representative, result);
        return result;
    }

    @Override
    public void removePort(final IPortRepresentative representative) {
        if (!portMap.containsKey(representative)) throw new C4JRuntimeException("The port representative is not registered at the node.");
        this.remove(portMap.get(representative));
        portMap.remove(representative);
        portHandlerMap.get(representative).dispose();
        portHandlerMap.remove(representative);
    }

    @Override
    public Set<IPort> getPorts() {
        return new HashSet<IPort>(portMap.values());
    }

    @Override
    public Port getPort(final IPortRepresentative represenative) {
        return portMap.get(represenative);
    }

    @Override
    public Port getPort(final String id) {
        for (final IPortRepresentative representative : portMap.keySet()) if (representative.getIdentifier().equals(id)) return getPort(representative);
        throw new C4JRuntimeException(format("Unkown port ‘%s’ on node ‘%s’.", id, nodeRepresentative.getIdentifier()));
    }

    @Override
    public INodeRepresentative getRepresentative() {
        return nodeRepresentative;
    }

    @Override
    public void setRenderer(final INodeRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void paint(final Graphics2D g) {
        if (renderer != null) {
            renderer.paint(g, RectHoverable.getNormedRectangle(f1.getPosition(), f2.getPosition()), isHovered, false);
        }
        super.paint(g);
    }

    @Override
    public Rectangle getRectangle() {
        return RectHoverable.getNormedRectangle(f1.getPosition(), f2.getPosition());
    }

    @Override
    public void setRectangle(final Rectangle rect) {
        f1.setPosition(new Point(rect.x, rect.y));
        f2.setPosition(new Point(rect.x + rect.width, rect.y + rect.height));
    }

    void setPosition(final Point p1, final Point p2) {
        f1.setPosition(p1);
        f2.setPosition(p2);
    }

    @Override
    public void entered() {
        isHovered = true;
    }

    @Override
    public void exited() {
        isHovered = false;
    }
}
