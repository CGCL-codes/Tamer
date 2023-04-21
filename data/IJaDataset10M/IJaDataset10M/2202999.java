package gov.sandia.ccaffeine.dc.user_iface.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * The holding area for components dragged down from the palette.
 * The <code>Arena</code> is the working area where the framework
 * is created.
 */
public class Arena extends JPanel {

    static final long serialVersionUID = 1;

    public Arena(GlobalData global) {
        this.global = global;
        initialize();
        global.setArena(this);
    }

    private void initialize() {
        builder = global.getBuilder();
        connections = new Vector();
        componentInstances = new Hashtable();
        clickListener = new ClickListener();
        addMouseListener(clickListener);
        this.setName("Arena");
        theArena = new JPanel();
        setLayout(null);
        add(theArena);
        setBorder(BorderFactory.createTitledBorder("Arena"));
    }

    /**
   * Adds a new <code>ComponentInstance</code> to this <code>
   * Arena</code>
   */
    public void addComponentInstance(ComponentInstance instance) {
        add(instance);
        componentInstances.put(instance.getInstanceName(), instance);
        validateTree();
        global.informOfChange();
    }

    public void removeComponentInstance(ComponentInstance instance) {
        Iterator i = connections.iterator();
        Vector v = new Vector();
        while (i.hasNext()) {
            Connection con = (Connection) i.next();
            if (con.getSource() == instance) {
                v.addElement(con);
            } else if (con.getTarget() == instance) {
                v.addElement(con);
            }
        }
        for (int j = 0; j < v.size(); j++) {
            successfulRemoveConnection((Connection) v.elementAt(j));
        }
        remove(instance);
        componentInstances.remove(instance.getInstanceName());
        global.informOfChange();
        repaint();
    }

    public void moveComponentInstance(String s) {
        StringTokenizer tokens = new StringTokenizer(s);
        tokens.nextToken();
        ComponentInstance instance = getComponentInstance(tokens.nextToken());
        int x = Integer.parseInt(tokens.nextToken());
        int y = Integer.parseInt(tokens.nextToken());
        instance.setLocation(x, y);
        instance.updateConnections();
    }

    public ComponentInstance getComponentInstance(String instanceName) {
        return (ComponentInstance) componentInstances.get(instanceName);
    }

    public Hashtable getComponentInstances() {
        return componentInstances;
    }

    private void growWindowToFitComponents() {
        minX = 0;
        minY = 0;
        maxX = getWidth();
        maxY = getHeight();
        for (int i = 0; i < getComponentCount(); i++) {
            thisComponent = (ComponentInstance) getComponent(i);
            thisX = thisComponent.getX();
            thisY = thisComponent.getY();
            thisWidth = thisComponent.getWidth();
            thisHeight = thisComponent.getHeight();
            if (thisX - componentBuffer < minX) {
                minX = thisX - componentBuffer;
            }
            if (thisX + thisWidth + componentBuffer > maxX) {
                maxX = thisX + thisWidth + componentBuffer;
            }
            if (thisY - componentBuffer < minY) {
                minY = thisY - componentBuffer;
            }
            if (thisY + thisHeight + componentBuffer > maxY) {
                maxY = thisY + thisHeight + componentBuffer;
            }
        }
        if (maxX - minX > getWidth()) {
            growWindowX = maxX - minX - getWidth();
        } else {
            growWindowX = 0;
        }
        if (maxY - minY > getHeight()) {
            growWindowY = maxY - minY - getHeight();
        } else {
            growWindowY = 0;
        }
        if (growWindowX + growWindowY > 0) {
            builder.setSize(builder.getWidth() + growWindowX, builder.getHeight() + growWindowY);
        }
        if (minX < 0) {
            componentShiftX = Math.abs(minX);
        } else {
            componentShiftX = 0;
        }
        if (minY < 0) {
            componentShiftY = Math.abs(minY);
        } else {
            componentShiftY = 0;
        }
        if (componentShiftX + componentShiftY > 0) {
            for (int i = 0; i < getComponentCount(); i++) {
                thisComponent = (ComponentInstance) getComponent(i);
                thisComponent.setLocation(thisComponent.getX() + componentShiftX, thisComponent.getY() + componentShiftY);
            }
            for (int i = 0; i < connections.size(); i++) {
                thisConnection = (Connection) connections.get(i);
                thisConnection.setPoints();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintConnections(g);
    }

    public void paintConnections(Graphics g) {
        for (int i = 0; i < connections.size(); i++) {
            ((Connection) (connections.get(i))).drawPath(g);
        }
    }

    public void addConnection(Connection c) {
        connections.add(c);
        repaint();
    }

    public void removeConnection(Connection c) {
        global.breakConnection(c);
    }

    /** Remove the connection matching the argument Connection.  Throws
   * RuntimeException if no such connection exists. */
    public void successfulRemoveConnection(Connection c) {
        connections.remove(c);
        global.informOfChange();
        repaint();
    }

    public Vector getConnectionsForPort(Port p) {
        String portName = p.getInstanceName();
        String cmpName = p.getComponentInstanceName();
        Enumeration e = connections.elements();
        Vector retVec = new Vector();
        while (e.hasMoreElements()) {
            Connection test = (Connection) e.nextElement();
            if (test.getSource().getInstanceName().equals(cmpName)) {
                if (test.getSourcePort().getInstanceName().equals(portName)) {
                    retVec.add(test);
                }
            }
            if (test.getTarget().getInstanceName().equals(cmpName)) {
                if (test.getTargetPort().getInstanceName().equals(portName)) {
                    retVec.add(test);
                }
            }
        }
        return retVec;
    }

    public void setSelectedComponentOffsets(Dimension d) {
    }

    public void setSelectedComponentInstance(ComponentInstance instance) {
        selectedComponentInstance = instance;
    }

    public ComponentInstance getSelectedComponentInstance() {
        return selectedComponentInstance;
    }

    public void nullifySelectedComponentInstance() {
        selectedComponentInstance = null;
    }

    public void setSelectedComponentClass(ComponentClass button) {
        selectedComponentClass = button;
    }

    public ComponentClass getSelectedComponentClass() {
        return selectedComponentClass;
    }

    public void nullifySelectedComponentClass() {
        selectedComponentClass = null;
    }

    public void setConnectionSource(ComponentInstance source) {
        connectionSource = source;
    }

    public ComponentInstance getConnectionSource() {
        return connectionSource;
    }

    public void nullifyConnectionSource() {
        connectionSource = null;
    }

    public void setConnectionTarget(ComponentInstance target) {
        connectionTarget = target;
    }

    public ComponentInstance getConnectionTarget() {
        return connectionTarget;
    }

    public void nullifyConnectionTarget() {
        connectionTarget = null;
    }

    public void setConnectionSourcePort(Port port) {
        connectionSourcePort = port;
    }

    public Port getConnectionSourcePort() {
        return connectionSourcePort;
    }

    public void nullifyConnectionSourcePort() {
        connectionSourcePort = null;
    }

    public void setConnectionTargetPort(Port port) {
        connectionTargetPort = port;
    }

    public Port getConnectionTargetPort() {
        return connectionTargetPort;
    }

    public void nullifyConnectionTargetPort() {
        connectionTargetPort = null;
    }

    public Vector getConnections() {
        return connections;
    }

    class ClickListener extends MouseAdapter {

        public ClickListener() {
            super();
        }

        public void mousePressed(MouseEvent me) {
            if (selectedComponentInstance != null) {
                selectedComponentInstance.beSelected(false);
                selectedComponentInstance = null;
            }
        }
    }

    private Builder builder;

    private GlobalData global;

    private Hashtable componentInstances;

    private ComponentInstance selectedComponentInstance;

    private ComponentClass selectedComponentClass;

    private ComponentInstance connectionSource;

    private ComponentInstance connectionTarget;

    private Port connectionSourcePort;

    private Port connectionTargetPort;

    private int minX;

    private int minY;

    private int maxX;

    private int maxY;

    private int thisX;

    private int thisY;

    private int thisWidth;

    private int thisHeight;

    private int growWindowX;

    private int growWindowY;

    private int componentShiftX;

    private int componentShiftY;

    private Vector connections;

    private ComponentInstance thisComponent;

    private Connection thisConnection;

    private ClickListener clickListener;

    private final int componentBuffer = 16;

    private JPanel theArena;
}
