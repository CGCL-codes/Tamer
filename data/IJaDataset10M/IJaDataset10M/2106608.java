package jade.core.replication;

import java.util.Vector;
import jade.core.Node;
import jade.core.NodeDescriptor;
import jade.core.SliceProxy;
import jade.core.GenericCommand;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.IMTPException;
import jade.core.ServiceException;
import jade.core.NotFoundException;

/**
   The remote proxy for the JADE kernel-level service managing
   the main-container replication subsystem installed in the platform.

   @author Giovanni Rimassa - FRAMeTech s.r.l.
*/
public class MainReplicationProxy extends SliceProxy implements MainReplicationSlice {

    public int getLabel() throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_GETLABEL, NAME, null);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
            return ((Integer) result).intValue();
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public String getPlatformManagerAddress() throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_GETPLATFORMMANAGERADDRESS, NAME, null);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
            return (String) result;
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public void addReplica(String sliceName, String smAddr, int sliceIndex, NodeDescriptor dsc, Vector services) throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_ADDREPLICA, NAME, null);
            cmd.addParam(sliceName);
            cmd.addParam(smAddr);
            cmd.addParam(new Integer(sliceIndex));
            cmd.addParam(dsc);
            cmd.addParam(services);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public void removeReplica(String smAddr, int sliceIndex) throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_REMOVEREPLICA, NAME, null);
            cmd.addParam(smAddr);
            cmd.addParam(new Integer(sliceIndex));
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public void fillGADT(AID[] agents, ContainerID[] containers) throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_FILLGADT, NAME, null);
            cmd.addParam(agents);
            cmd.addParam(containers);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public void suspendedAgent(AID name) throws IMTPException, NotFoundException {
        try {
            GenericCommand cmd = new GenericCommand(H_SUSPENDEDAGENT, NAME, null);
            cmd.addParam(name);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else if (result instanceof NotFoundException) {
                    throw (NotFoundException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }

    public void newTool(AID tool) throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_NEWTOOL, NAME, null);
            cmd.addParam(tool);
            Node n = getNode();
            Object result = n.accept(cmd);
            if ((result != null) && (result instanceof Throwable)) {
                if (result instanceof IMTPException) {
                    throw (IMTPException) result;
                } else {
                    throw new IMTPException("An undeclared exception was thrown", (Throwable) result);
                }
            }
        } catch (ServiceException se) {
            throw new IMTPException("Unable to access remote node", se);
        }
    }
}
