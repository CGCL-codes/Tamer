package org.coos.javaframe;

import org.coos.actorframe.ActorPartSpec;
import org.coos.actorframe.ActorPortSpec;
import org.coos.actorframe.RoleSpec;
import org.coos.util.serialize.AFClassLoader;
import org.coos.util.serialize.AFSerializer;
import org.coos.util.serialize.StringHelper;
import org.coos.util.serialize.VectorHelper;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @author Geir Melby, Tellu AS
 */
public class ActorSpec implements AFSerializer {

    private Vector connectorDesc;

    private Vector partDesc;

    private Vector portDesc;

    private Vector roleDesc;

    private String actorType;

    private String actorClassName;

    public ActorSpec() {
    }

    public ActorSpec(String actorType, String actorClassName) {
        this.actorType = actorType;
        this.actorClassName = actorClassName;
    }

    /**
	 * Gets the Role spec for the actor
	 * 
	 * @param partName
	 *            is the name / type of the part (actor)
	 * @return a part spec
	 */
    public PartSpec getPartSpec(String partName) {
        if (partName == null) throw new NullPointerException("Part Name is null");
        if (partDesc != null) {
            for (int i = 0; i < partDesc.size(); i++) {
                PartSpec spec = (PartSpec) partDesc.elementAt(i);
                if (spec.getRoleType().endsWith(partName)) {
                    return spec;
                }
            }
        }
        return null;
    }

    /**
	 * Gets the Port spec for the actor
	 * 
	 * @param portName
	 * @return the port spec
	 */
    public PortSpec getPortSpec(String portName) {
        if (portDesc != null) {
            for (int i = 0; i < portDesc.size(); i++) {
                PortSpec spec = (PortSpec) portDesc.elementAt(i);
                if (spec.getPortName().endsWith(portName)) {
                    return spec;
                }
            }
        }
        return null;
    }

    /**
	 * Gets the Role spec for the actor
	 * 
	 * @param roleType
	 * @return
	 */
    public RoleSpec getRoleSpec(String roleType) {
        if (roleDesc != null) {
            for (int i = 0; i < roleDesc.size(); i++) {
                RoleSpec spec = (RoleSpec) roleDesc.elementAt(i);
                if (spec.getType().endsWith(roleType)) {
                    return spec;
                }
            }
        }
        return null;
    }

    /**
	 * Add a part spec to part description
	 * 
	 * @param partSpec
	 *            is the part spec
	 */
    public void addPartSpec(PartSpec partSpec) {
        if (partDesc == null) {
            partDesc = new Vector();
        }
        partDesc.addElement(partSpec);
    }

    /**
	 * Add a part spec to part description
	 * 
	 * @param instance
	 *            is the instance name of the part
	 * @param actorType
	 *            is the type of the part
	 * @param visible
	 */
    public void addPartSpec(String instance, String actorType, boolean visible) {
        PartSpec ps = new ActorPartSpec(instance, actorType);
        ps.setVisible(visible);
        addPartSpec(ps);
    }

    /**
	 * Adds a connector sperc to the actor spec
	 * 
	 * @param from
	 *            is the actor address of the parts that will set up the port
	 *            connector specified as a string
	 * @param to
	 *            is the actor address of the part of the connector specified as
	 *            a string
	 * @param bidirctional
	 *            if true the connector will be bidirctional, that means both
	 *            ports can send messages through the port
	 */
    public void addConnectorSpec(String from, String to, boolean bidirctional) {
        addConnectorSpec(new ConnectorSpec(new ActorAddress(from), new ActorAddress(to), bidirctional));
    }

    /**
	 * Adds a connector sperc to the actor spec
	 * 
	 * @param from
	 *            is the actor address of the parts that will set up the port
	 *            connector
	 * @param to
	 *            is the actor address of the part of the connector
	 * @param bidirctional
	 *            if true the connector will be bidirctional, that means both
	 *            ports can send messages through the port
	 */
    public void addConnectorSpec(ActorAddress from, ActorAddress to, boolean bidirctional) {
        addConnectorSpec(new ConnectorSpec(from, to, bidirctional));
    }

    public void addConnectorSpec(ConnectorSpec cs) {
        if (connectorDesc == null) {
            connectorDesc = new Vector();
        }
        connectorDesc.addElement(cs);
    }

    /**
	 * Checks the from and to port that they are set. At least they should
	 * contain a "defaultInPort"
	 * 
	 * @param cs
	 *            is the connector spec
	 * @return a modified connector spec
	 */
    private ConnectorSpec checkConnectorSpec(ConnectorSpec cs) {
        if (cs.getFrom().getActorPort() == null) {
            cs.getFrom().setActorPort("defaultInPort");
        }
        if (cs.getTo().getActorPort() == null) {
            cs.getTo().setActorPort("defaultInPort");
        }
        return cs;
    }

    /**
	 * Get the port names as a Vector of String.
	 * 
	 * @return Vector of Strings containing the Port names.
	 */
    public Vector getPortNames() {
        Vector v = new Vector();
        if (portDesc != null) {
            Enumeration e = portDesc.elements();
            while (e.hasMoreElements()) {
                PortSpec ps = (PortSpec) e.nextElement();
                v.addElement(ps.getPortName());
            }
        }
        return v;
    }

    /**
	 * Check if the portname exists in the port description.
	 * 
	 * @return true if the portname exists.
	 */
    public boolean containsPortName(String portName) {
        if (portDesc != null) {
            Enumeration e = portDesc.elements();
            while (e.hasMoreElements()) {
                PortSpec ps = (PortSpec) e.nextElement();
                if (ps.getPortName().equals(portName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Add a port spec to part description
	 * 
	 * @param portSpec
	 *            is the port spec
	 */
    public void addPortSpec(PortSpec portSpec) {
        if (portDesc == null) {
            portDesc = new Vector();
        }
        deletePortSpec(portSpec);
        portDesc.addElement(portSpec);
    }

    public void addPortSpec(String portName) {
        if (portDesc == null) {
            portDesc = new Vector();
        }
        portDesc.addElement(new ActorPortSpec(portName));
    }

    /**
	 * Add a role spec to part description
	 * 
	 * @param roleSpec
	 *            is the part spec
	 */
    public void addRoleSpec(RoleSpec roleSpec) {
        if (roleSpec == null) return;
        if (roleDesc == null) {
            roleDesc = new Vector();
        }
        deleteRoleSpec(roleSpec);
        roleDesc.addElement(roleSpec);
    }

    public Vector getPartDesc() {
        return partDesc == null ? new Vector() : partDesc;
    }

    public void setPartDesc(Vector partDesc) {
        this.partDesc = partDesc;
    }

    public Vector getPortDesc() {
        return portDesc == null ? new Vector() : portDesc;
    }

    public void setPortDesc(Vector portDesc) {
        this.portDesc = portDesc;
    }

    public Vector getRoleDesc() {
        return roleDesc == null ? new Vector() : roleDesc;
    }

    public void setRoleDesc(Vector roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getActorClassName() {
        return actorClassName;
    }

    public void setActorClassName(String actorClassName) {
        this.actorClassName = actorClassName;
    }

    public void setConnectorDesc(Vector connectorDesc) {
        for (int i = 0; i < connectorDesc.size(); i++) {
            ConnectorSpec cs = (ConnectorSpec) connectorDesc.elementAt(i);
        }
        this.connectorDesc = connectorDesc;
    }

    public ConnectorSpec getConnectorDesc(String name) {
        if (connectorDesc != null) {
            Enumeration e = connectorDesc.elements();
            while (e.hasMoreElements()) {
                ConnectorSpec cs = (ConnectorSpec) e.nextElement();
                if (cs.getName().equals(name)) {
                    return cs;
                }
            }
        }
        return null;
    }

    /**
	 * Get the connectors as a Vector.
	 * 
	 * @param actorType
	 *            is the type of the actor
	 * @return Vector of connectors spec.
	 */
    public Vector getConnectorDesc(String actorType, String portName) {
        Vector v = new Vector();
        if (connectorDesc != null) {
            Enumeration e = connectorDesc.elements();
            while (e.hasMoreElements()) {
                ConnectorSpec cs = (ConnectorSpec) e.nextElement();
                ActorAddress aa = cs.getFrom();
                if ((aa.getActorType() == null) || (aa.getActorPort() == null) || (actorType == null)) {
                    System.out.println("ActorSpec.getConnectorDesc: ERROR: " + cs);
                } else if (aa.getActorType().equals(actorType) && ((portName == null) || aa.getActorPort().equals(portName))) {
                    v.addElement(cs);
                }
            }
        }
        return v;
    }

    public boolean hasPartSpecs() {
        return getPartDesc() != null && !getPartDesc().isEmpty();
    }

    /**
	 * Check if there is a difference between to vectors
	 * 
	 * @param from
	 *            ia vector pf elements
	 * @param to
	 *            is vector of elements of same kind
	 * @return the elements in from vector that is not in the to vector
	 */
    private Vector compareSpecs(Vector from, Vector to, boolean equal) {
        Vector res = new Vector();
        for (int i = 0; i < from.size(); i++) {
            Object oldcs = from.elementAt(i);
            boolean found = false;
            for (int j = 0; j < to.size(); j++) {
                Object newcs = to.elementAt(j);
                if (oldcs.equals(newcs)) {
                    found = true;
                    break;
                }
            }
            if (equal && found) {
                res.addElement(oldcs);
            } else if (!equal && !found) {
                res.addElement(oldcs);
            }
        }
        return res;
    }

    public static String DELETED_CONNECTORS = "removedConnectors";

    public static String ADDED_CONNECTOR = "addedConnectors";

    public static String DELETED_PARTS = "removedParts";

    public static String ADDED_PARTS = "addedParts";

    public static String ADDED_PORTS = "addedPorts";

    public static String DELETED_PORTS = "deletedPorts";

    public Hashtable analyseDifference(ActorSpec newActorSpec) {
        Hashtable res = new Hashtable();
        Vector removed = compareSpecs(connectorDesc, newActorSpec.getConnectorDesc(), false);
        res.put(DELETED_CONNECTORS, removed);
        Vector added = compareSpecs(newActorSpec.getConnectorDesc(), connectorDesc, false);
        res.put(ADDED_CONNECTOR, added);
        Vector equal = compareSpecs(newActorSpec.getConnectorDesc(), connectorDesc, true);
        res.put("equalConnectors", equal);
        removed = compareSpecs(partDesc, newActorSpec.getPartDesc(), false);
        res.put(DELETED_PARTS, removed);
        added = compareSpecs(newActorSpec.getPartDesc(), partDesc, false);
        res.put(ADDED_PARTS, added);
        removed = compareSpecs(portDesc, newActorSpec.getPortDesc(), false);
        res.put(DELETED_PORTS, removed);
        added = compareSpecs(newActorSpec.getPortDesc(), portDesc, false);
        res.put(ADDED_PORTS, added);
        return res;
    }

    public static void main(String[] args) {
        ActorSpec oldAS = new ActorSpec();
        oldAS.addConnectorSpec("a1:p1@A", "b:P2@B", false);
        oldAS.addConnectorSpec("a1:p1@A", "c:P3@C", false);
        oldAS.addPartSpec("a1", "A", true);
        ActorSpec newAS = new ActorSpec();
        newAS.addConnectorSpec("a1:p1@A", "b:P2@B", false);
        newAS.addConnectorSpec("a1:p1@A", "c:P3@C", false);
        newAS.addConnectorSpec("a2:p1@A", "c:P3@C", false);
        newAS.addConnectorSpec("a1:p2@A", "b:P2@B", false);
        newAS.addPartSpec("a2", "B", true);
        Hashtable res = oldAS.analyseDifference(newAS);
        System.out.println("RES: " + res);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorSpec)) return false;
        ActorSpec actorSpec = (ActorSpec) o;
        if (actorClassName != null ? !actorClassName.equals(actorSpec.actorClassName) : actorSpec.actorClassName != null) return false;
        if (actorType != null ? !actorType.equals(actorSpec.actorType) : actorSpec.actorType != null) return false;
        if (connectorDesc != null ? !connectorDesc.equals(actorSpec.connectorDesc) : actorSpec.connectorDesc != null) return false;
        if (partDesc != null ? !partDesc.equals(actorSpec.partDesc) : actorSpec.partDesc != null) return false;
        if (portDesc != null ? !portDesc.equals(actorSpec.portDesc) : actorSpec.portDesc != null) return false;
        return true;
    }

    public int hashCode() {
        return 0;
    }

    public Vector getConnectorDesc() {
        if (connectorDesc != null) {
            return connectorDesc;
        } else {
            return new Vector();
        }
    }

    protected Object clone() {
        try {
            ActorSpec as = new ActorSpec();
            as.deSerialize(serialize(), null);
            return as;
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass().getName()).log(TraceConstants.tlError, "IOException clone(). Returning null.");
            return null;
        }
    }

    public String toString() {
        String parts = ((partDesc != null) && !partDesc.isEmpty()) ? "\n  Parts      :" + partDesc.toString() : "";
        String roles = ((roleDesc != null) && !roleDesc.isEmpty()) ? "\n  Roles      :" + roleDesc.toString() : "";
        String ports = ((portDesc != null) && !portDesc.isEmpty()) ? "\n  Ports      :" + portDesc.toString() : "";
        String connectors = ((connectorDesc != null) && !connectorDesc.isEmpty()) ? "\n  Connectors :" + connectorDesc.toString() : "";
        return "\nACTOR: " + actorType + "  ClassName: " + actorClassName + parts + roles + ports + connectors;
    }

    /**
	 * This function must implement the serialization of the object.
	 * 
	 * @return a byte array with the objects data
	 * @throws java.io.IOException
	 */
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        dout.write(VectorHelper.persist(partDesc));
        dout.write(VectorHelper.persist(portDesc));
        dout.write(VectorHelper.persist(roleDesc));
        dout.write(VectorHelper.persist(connectorDesc));
        dout.write(StringHelper.persist(actorType));
        dout.write(StringHelper.persist(actorClassName));
        dout.flush();
        return bout.toByteArray();
    }

    /**
	 * Use this function for resurrection of the object
	 * 
	 * @param data
	 *            The serialized data containing the object data
	 * @throws java.io.IOException
	 */
    public ByteArrayInputStream deSerialize(byte[] data, AFClassLoader cl) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bin);
        partDesc = VectorHelper.resurrect(din, cl);
        portDesc = VectorHelper.resurrect(din, cl);
        roleDesc = VectorHelper.resurrect(din, cl);
        connectorDesc = VectorHelper.resurrect(din, cl);
        actorType = StringHelper.resurrect(din);
        actorClassName = StringHelper.resurrect(din);
        return bin;
    }

    public void deleteConnectorSpec(ConnectorSpec connectorSpec) {
        if (connectorSpec == null || connectorDesc == null) {
            return;
        }
        for (int i = 0; i < connectorDesc.size(); i++) {
            ConnectorSpec spec = (ConnectorSpec) connectorDesc.elementAt(i);
            if (spec.equals(connectorSpec)) {
                connectorDesc.removeElementAt(i);
            }
        }
    }

    public boolean deletePartSpec(String partSpecType) {
        for (int i = 0; i < partDesc.size(); i++) {
            PartSpec partSpec = (PartSpec) partDesc.elementAt(i);
            if (partSpec.getRoleType().equals(partSpecType)) {
                partDesc.removeElementAt(i);
                return true;
            }
        }
        return false;
    }

    public boolean deletePortSpec(PortSpec portSpec) {
        if (portSpec == null || portSpec == null) return true;
        for (int i = 0; i < portDesc.size(); i++) {
            PortSpec ps = (PortSpec) portDesc.elementAt(i);
            if (portSpec.getPortName().equals(ps.getPortName())) {
                portDesc.removeElementAt(i);
                return true;
            }
        }
        return false;
    }

    public boolean deleteRoleSpec(RoleSpec roleSpec) {
        if (roleSpec == null || roleSpec == null) return true;
        for (int i = 0; i < roleDesc.size(); i++) {
            RoleSpec spec = (RoleSpec) roleDesc.elementAt(i);
            if (roleSpec.getInstance().equals(spec.getInstance())) {
                roleDesc.removeElementAt(i);
                return true;
            }
        }
        return false;
    }
}
