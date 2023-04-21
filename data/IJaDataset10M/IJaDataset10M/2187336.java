package eu.more.permissionmanagerservice;

import javax.xml.bind.JAXBException;
import org.mortbay.log.LogFactory;
import org.osgi.framework.BundleContext;
import org.soda.dpws.DPWSContext;
import org.soda.dpws.DPWSException;
import eu.more.core.internal.MOREService;
import eu.more.cryptographicservice.Cryptographicservice;
import eu.more.cryptographicservice.proxies.DPWSUtils;
import eu.more.cryptographicservice.proxies.KDSProxy;
import eu.more.cryptographicservicecore.caches.PMSCache;
import eu.more.cryptographicservicecore.commons.CryptographicCommons;
import eu.more.cryptographicservicecore.commons.Logger;
import eu.more.cryptographicservicecore.commons.SecurityException;
import eu.more.cryptographicservicecore.responses.BooleanResponse;
import eu.more.cryptographicservicecore.responses.CollectionStringResponse;
import eu.more.cryptographicservicecore.ticket.Ticket;
import eu.more.permissionmanagerservice.generated.permissionmanagerserviceWSDLInfoFactory;
import eu.more.permissionmanagerservice.generated.pms;
import eu.more.permissionmanagerservice.generated.jaxb.EString;
import eu.more.permissionmanagerservice.generated.jaxb.ObjectFactory;

public class PMSActivator extends MOREService implements pms {

    public static CryptographicCommons pmsCI = new CryptographicCommons("pms_CryptoFile", "123");

    public static PMSCache pmsCache = new PMSCache();

    private Cryptographicservice crypto = new Cryptographicservice();

    @Override
    public void start(BundleContext context) throws Exception {
        this.logging = LogFactory.getLog(PMSActivator.class);
        this.SERVICE_ID = "http://www.ist-more.org/pms";
        this.WSDL_NAME = "pms.wsdl";
        this.ServiceName = "pms";
        this.WSDLFactory = permissionmanagerserviceWSDLInfoFactory.class;
        super.start(context);
        pmsCache.loadXmlAcl("pms_Cache", pmsCI.data.servicePuKey, pmsCI.data.servicePrKey, "nonSignedPublicACL.xml", null, "nonSignedAccessACL.xml", null);
        Logger.log("PMS: Group Managment (GMS) loaded MOREID:\"" + pmsCache.cache.gmsServiceName + "\".");
        Logger.log("PMS 0.5 started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Logger.log("PMS 0.5 stopped.");
    }

    /**
	 * Builds a SOAP eString
	 * 
	 * @param string
	 * @return
	 * @throws DPWSException
	 * @throws JAXBException
	 */
    private EString makeString(String string) throws SecurityException {
        ObjectFactory factory = new ObjectFactory();
        EString eString = null;
        try {
            eString = factory.createEString();
        } catch (JAXBException e) {
            throw new SecurityException(e);
        }
        eString.setValue(string);
        return eString;
    }

    /**
	 * Verifies that the given user has permission to carry out the operation
	 * specified
	 * 
	 * @param operationName
	 * @param userName
	 * @return
	 * @throws AccessControlListException
	 * @throws DPWSException
	 */
    private void checkAccessPermissions(String stringTicket, String operationName) throws SecurityException {
        String userName = Ticket.getIdentity(stringTicket);
        boolean hasPermission = pmsCache.cache.accessAcl.isAllowed("pms", userName, operationName);
        Logger.log("PMS: Access permission of  \"" + userName + "\" to performs the operation \"" + operationName + "\" is " + hasPermission);
        if (!hasPermission) {
            throw new SecurityException("PMS: User \"" + userName + "\" has not permission to performs the privileged action \"" + operationName + "\" over \"" + PMSActivator.pmsCI.data.serviceName + "\".");
        }
    }

    public EString listPermissions(DPWSContext context, EString serviceName, EString userName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String servicename = (String) this.crypto.asymmetricDecipherAndVerify(serviceName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "LIST_PERMISSION");
            CollectionStringResponse response = new CollectionStringResponse(pmsCache.cache.publicAcl.getPermissions(servicename, username));
            String cipheredResponse = this.crypto.asymmetricCipherAndSign(response, pmsCI.data.serviceTicket, originID);
            Logger.log("PMS: permission's list of user \"" + username + "\" over \"" + servicename + "\" was successfully retrieved.");
            return makeString(cipheredResponse);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void addPermission(DPWSContext context, EString resourceName, EString userName, EString permissionName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(resourceName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            String permissionname = (String) this.crypto.asymmetricDecipherAndVerify(permissionName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "ADD_PERMISSION");
            pmsCache.cache.publicAcl.addEntry(resourcename, username, permissionname);
            Logger.log("PMS: Permission to \"" + permissionname + "\"  over \"" + resourcename + "\" was successfully added to user \"" + username);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void addUser(DPWSContext context, EString userName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "ADD_USER");
            pmsCache.cache.publicAcl.addUser(username);
            Logger.log("PMS: User \"" + username + "\" was successfully added.");
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public EString checkPermission(DPWSContext context, EString resourceName, EString userName, EString permissionName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String servicename = (String) this.crypto.asymmetricDecipherAndVerify(resourceName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            String operationname = (String) this.crypto.asymmetricDecipherAndVerify(permissionName.getValue(), pmsCI.data.serviceTicket, originID);
            boolean isHimSelf = (username.compareTo(originID) == 0) && (username.compareTo(servicename) == 0);
            boolean aclResult = pmsCache.cache.publicAcl.isAllowed(servicename, username, operationname);
            boolean result = aclResult || isHimSelf;
            Logger.log("PMS: Permission of  \"" + username + "\" to performs the operation \"" + operationname + "\" over \"" + servicename + "\" is " + result);
            BooleanResponse response = new BooleanResponse(result);
            String cipheredResponse = this.crypto.asymmetricCipherAndSign(response, pmsCI.data.serviceTicket, originID);
            return makeString(cipheredResponse);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void removePermission(DPWSContext context, EString resourceName, EString userName, EString permissionName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(resourceName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            String permissionname = (String) this.crypto.asymmetricDecipherAndVerify(permissionName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "REMOVE_PERMISSION");
            pmsCache.cache.publicAcl.removeEntryPermission(resourcename, username, permissionname);
            Logger.log("PMS: Permission to \"" + permissionname + "\"  over \"" + resourcename + "\" was successfully removed to user \"" + username);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void removeUser(DPWSContext context, EString userName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "REMOVE_USER");
            pmsCache.cache.publicAcl.remove(username);
            Logger.log("PMS: User \"" + username + "\" was successfully removed.");
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void addCommunicationGroup(DPWSContext context, EString communicationGroupName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(communicationGroupName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "ADD_COMMUNICATION_GROUP");
            pmsCache.cache.publicAcl.addResource(resourcename);
            pmsCache.cache.publicAcl.addUsersGroup(resourcename);
            pmsCache.cache.publicAcl.addEntry(resourcename, resourcename, "READ_SYMMETRIC_KEY_" + resourcename);
            pmsCache.cache.communicationGroups.add(resourcename);
            Cryptographicservice.addUser(PMSActivator.pmsCI.data.serviceTicket, resourcename);
            KDSProxy.generateSymmetricPassword(PMSActivator.pmsCI.data.serviceTicket, resourcename);
            this.addCommunicationGroupMember(resourcename, PMSActivator.pmsCache.cache.gmsServiceName);
            Logger.log("PMS: Communication group \"" + resourcename + "\" was successfully added.");
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    private void addCommunicationGroupMember(String resourcename, String username) throws SecurityException {
        pmsCache.cache.publicAcl.addGroupMember(resourcename, username);
        pmsCache.cache.publicAcl.addEntry(resourcename, username, "READ_CG_" + resourcename);
        Logger.log("PMS: \"" + username + "\" was successfully added as member of the communication group \"" + resourcename + "\".");
    }

    public void addCommunicationGroupMember(DPWSContext context, EString comunicationGroupName, EString userName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(comunicationGroupName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "ADD_COMMUNICATION_GROUP_MEMBER");
            this.addCommunicationGroupMember(resourcename, username);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void removeCommunicationGroup(DPWSContext context, EString communicationGroupName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(communicationGroupName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "REMOVE_COMMUNICATION_GROUP");
            pmsCache.cache.publicAcl.remove(resourcename);
            pmsCache.cache.communicationGroups.remove(resourcename);
            Logger.log("PMS: Communication group \"" + resourcename + "\" was successfully removed.");
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public void removeCommunicationGroupMember(DPWSContext context, EString comunicationGroupName, EString userName) throws DPWSException {
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            String resourcename = (String) this.crypto.asymmetricDecipherAndVerify(comunicationGroupName.getValue(), pmsCI.data.serviceTicket, originID);
            String username = (String) this.crypto.asymmetricDecipherAndVerify(userName.getValue(), pmsCI.data.serviceTicket, originID);
            checkAccessPermissions(originTicket, "REMOVE_COMMUNICATION_GROUP_MEMBER");
            pmsCache.cache.publicAcl.removeGroupMember(resourcename, username);
            Logger.log("PMS: \"" + username + "\" was successfully removed as member of the communication group \"" + resourcename + "\".");
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }

    public EString existCommunicationGroup(DPWSContext context, EString comunicationGroupName) throws DPWSException {
        String resourcename;
        try {
            String originTicket = DPWSUtils.getOriginID(context);
            String originID = Ticket.getIdentity(originTicket);
            resourcename = (String) this.crypto.asymmetricDecipherAndVerify(comunicationGroupName.getValue(), pmsCI.data.serviceTicket, originID);
            BooleanResponse result = new BooleanResponse(pmsCache.cache.communicationGroups.contains(resourcename));
            String cipheredResponse = this.crypto.asymmetricCipherAndSign(result, pmsCI.data.serviceTicket, originID);
            Logger.log("PMS: It is  \"" + resourcename + "\" a valid communication group? " + result.isAuth);
            return makeString(cipheredResponse);
        } catch (SecurityException e) {
            throw new DPWSException(e.getMessage());
        }
    }
}
