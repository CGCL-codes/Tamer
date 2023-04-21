package uqdsd.infosec.model;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JLabel;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;

/**
 * @author InfoSec Project (c) 2008 UQ
 * 
 */
public class InstanceComponent extends AbstractComponent implements GraphModelListener, RepaintListener, RepaintProducer {

    static final long serialVersionUID = 0;

    protected RepaintListener repaintListener = null;

    private static final String DEFAULTNAME = "no-instance-name";

    private static final String DEFAULTTYPE = "no-instance-type";

    String instanceName;

    String instanceType;

    public InstanceComponent(ComponentLabel aSpecificLabel) {
        super(aSpecificLabel);
    }

    public InstanceComponent() {
        this(new ComponentLabel(null, DEFAULTNAME + " : " + DEFAULTTYPE));
    }

    @Override
    public Vector<PortComponent> getNeighbours() {
        syncEnvironment();
        return neighbours;
    }

    public void repaintEvent() {
        fireRepaintEvent();
    }

    public void setRepaintListener(RepaintListener repaintListener) {
        this.repaintListener = repaintListener;
        for (PortComponent pc : getNeighbours()) {
            pc.setRepaintListener(this);
        }
    }

    public void removeRepaintListener() {
        repaintListener = null;
    }

    public void fireRepaintEvent() {
        if (repaintListener != null) {
            repaintListener.repaintEvent();
        }
    }

    @Override
    public String getFullName() {
        return instanceName + "!!" + System.identityHashCode(this);
    }

    @Override
    public void selfNameChange(String oldName, String newName) {
        String oldInstanceName = instanceName == null ? DEFAULTNAME : instanceName;
        String oldInstanceType = instanceType == null ? DEFAULTTYPE : instanceType;
        int colon = newName.indexOf(':');
        if (colon >= 0) {
            instanceName = newName.substring(0, colon).trim();
            instanceType = newName.substring(colon + 1).trim();
        } else {
            instanceName = newName.trim();
            instanceType = DEFAULTTYPE;
        }
        if ("".equals(instanceName)) {
            instanceName = DEFAULTNAME;
        }
        if ("".equals(instanceType)) {
            instanceType = DEFAULTTYPE;
        }
        String proposedName = instanceName + " : " + instanceType;
        if (!oldInstanceName.equals(instanceName)) {
            setUserObject(proposedName);
            for (PortComponent ec : getNeighbours()) {
                if (ec.getDisplayName().startsWith(oldInstanceName + "::")) {
                    ec.setUserObject(new ComponentLabel(this, ec.getDisplayName().replaceFirst(oldInstanceName, instanceName)));
                }
            }
        } else {
            if (!oldInstanceType.equals(instanceType)) {
                setUserObject(new ComponentLabel(null, proposedName));
            }
        }
    }

    private void syncEnvironment() {
        if (!ignoreSyncRequests) {
            Enumeration<?> prt_e = children();
            neighbours.clear();
            int number_now = 0;
            if (prt_e.hasMoreElements()) {
                Port prt = (Port) prt_e.nextElement();
                Iterator<?> edge_itr = prt.edges();
                while (edge_itr.hasNext()) {
                    Object edge_next = edge_itr.next();
                    if (edge_next instanceof PortConnector) {
                        PortConnector pc = (PortConnector) edge_next;
                        AbstractComponent source = pc.getSource() != null ? (AbstractComponent) ((DefaultPort) pc.getSource()).getParent() : null;
                        AbstractComponent target = pc.getTarget() != null ? (AbstractComponent) ((DefaultPort) pc.getTarget()).getParent() : null;
                        if (target == this) {
                            target = source;
                            source = this;
                        }
                        if (target != null) {
                            number_now++;
                            neighbours.add((PortComponent) target);
                            ((PortComponent) target).setRepaintListener(this);
                        }
                    }
                }
            }
        }
    }

    public void graphChanged(GraphModelEvent e) {
        syncEnvironment();
    }

    @Override
    public Object clone() {
        InstanceComponent clone = new InstanceComponent((ComponentLabel) ((ComponentLabel) getUserObject()).clone());
        clone.setHierarchyNode(hierarchyNode);
        return clone;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getInstanceType() {
        return instanceType;
    }

    @Override
    public void autoSizeComponent() {
        Rectangle2D bounds = GraphConstants.getBounds(attributes);
        if (bounds != null) {
            int w = (new JLabel(getDisplayName())).getPreferredSize().width + 20;
            if (bounds.getWidth() < w) {
                GraphConstants.setBounds(attributes, new Rectangle((int) bounds.getX(), (int) bounds.getY(), w, (int) bounds.getHeight()));
            }
        }
    }
}
