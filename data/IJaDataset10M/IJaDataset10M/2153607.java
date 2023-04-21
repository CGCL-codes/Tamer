package jade.core.replication;

import java.util.Vector;
import jade.core.NodeDescriptor;
import jade.core.Service;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.IMTPException;
import jade.core.ServiceException;
import jade.core.NameClashException;
import jade.core.NotFoundException;
import jade.mtp.MTPDescriptor;
import jade.security.Credentials;
import jade.security.JADESecurityException;

/**
   The horizontal interface for the JADE kernel-level service managing
   the main-container replication subsystem installed in the platform.

   @author Giovanni Rimassa - FRAMeTech s.r.l.
*/
public interface MainReplicationSlice extends Service.Slice {

    /**
       The name of this service.
    */
    static final String NAME = "jade.core.replication.MainReplication";

    /**
     * The vertical command issued on the new master main container just after leadership acquisition
     */
    static final String LEADERSHIP_ACQUIRED = "Leadership-Acquired";

    static final String H_GETLABEL = "1";

    static final String H_GETPLATFORMMANAGERADDRESS = "2";

    static final String H_ADDREPLICA = "3";

    static final String H_REMOVEREPLICA = "4";

    static final String H_FILLGADT = "5";

    static final String H_BORNAGENT = "6";

    static final String H_DEADAGENT = "7";

    static final String H_SUSPENDEDAGENT = "8";

    static final String H_RESUMEDAGENT = "9";

    static final String H_NEWMTP = "10";

    static final String H_DEADMTP = "11";

    static final String H_NEWTOOL = "12";

    static final String H_DEADTOOL = "13";

    static final String H_INVOKESERVICEMETHOD = "14";

    int getLabel() throws IMTPException;

    String getPlatformManagerAddress() throws IMTPException;

    void addReplica(String sliceName, String smAddr, int sliceIndex, NodeDescriptor dsc, Vector services) throws IMTPException;

    void removeReplica(String smAddr, int sliceIndex) throws IMTPException;

    void fillGADT(AID[] agents, ContainerID[] containers) throws IMTPException;

    void suspendedAgent(AID name) throws IMTPException, NotFoundException;

    void newTool(AID tool) throws IMTPException;
}
