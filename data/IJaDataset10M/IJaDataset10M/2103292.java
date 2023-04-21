package jade.core.replication;

import jade.core.Node;
import jade.core.Service;
import jade.core.SliceProxy;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.ServiceException;

/**
   The remote proxy for the JADE kernel-level service distributing the
   <i>Service Manager</i> address list throughout the platform.

   @author Giovanni Rimassa - FRAMeTech s.r.l.
*/
public class AddressNotificationProxy extends SliceProxy implements AddressNotificationSlice {

    public void addServiceManagerAddress(String addr) throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_ADDSERVICEMANAGERADDRESS, NAME, null);
            cmd.addParam(addr);
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

    public String getServiceManagerAddress() throws IMTPException {
        try {
            GenericCommand cmd = new GenericCommand(H_GETSERVICEMANAGERADDRESS, NAME, null);
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
}
