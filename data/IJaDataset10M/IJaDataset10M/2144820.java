package eu.popeye.networkabstraction.communication.basic;

import java.io.Serializable;
import eu.popeye.middleware.groupmanagement.management.WorkgroupManager;
import eu.popeye.middleware.groupmanagement.management.WorkgroupManagerImpl;
import eu.popeye.middleware.invitation.InvitationService;
import eu.popeye.middleware.networkinformation.NetworkInformation;
import eu.popeye.middleware.networkinformation.omcast.PopeyeNetworkInformation;
import eu.popeye.middleware.networkinformation.pds.PDSNetworkInformation;
import eu.popeye.networkabstraction.communication.basic.util.SuperPeerConfig;

/**
 * This class is the point of access to the basic middleware
 * services:
 * <ul>
 * <li>Workgroup Manager
 * <li>Network Information
 * <li>Invitation
 * </ul>
 * @author Marcel Arrufat Arias
 * @author Gerard Paris Aixala
 */
public class BasicServicesManager {

    private WorkgroupManagerImpl workgroupManager;

    private NetworkInformation networkInfo;

    private InvitationService invitation;

    /**
	 * The constructor initializes all the services provided by the 
	 * middleware
	 * @param userName The name of the user
	 * @param publicKey The public key of the user
	 */
    public BasicServicesManager(String userName, Serializable publicKey) {
        SuperPeerConfig.setUSERNAME(userName);
        SuperPeerConfig.setPUBLICKEY(publicKey);
        workgroupManager = new WorkgroupManagerImpl();
        if (SuperPeerConfig.USE_PDS) {
            networkInfo = new PDSNetworkInformation();
        } else {
            networkInfo = new PopeyeNetworkInformation(workgroupManager);
        }
        invitation = new InvitationService(null);
    }

    /**
	 * @return Returns the invitation.
	 */
    public InvitationService getInvitationService() {
        return invitation;
    }

    /**
	 * @return Returns the networkInfo.
	 */
    public NetworkInformation getNetworkInformartion() {
        return networkInfo;
    }

    /**
	 * @return Returns the group Manager.
	 */
    public WorkgroupManager getWorkgroupManager() {
        return workgroupManager;
    }
}
