package org.gridtrust.vbe.client;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.namespace.*;
import javax.xml.soap.*;
import java.security.cert.*;
import javax.security.auth.Subject;
import java.util.*;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.AttributedURI;
import org.globus.axis.util.Util;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.wsrf.*;
import org.globus.wsrf.impl.security.authorization.*;
import org.globus.wsrf.impl.security.*;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.security.Constants;
import org.globus.wsrf.encoding.*;
import org.globus.exec.generated.*;
import org.globus.exec.utils.rsl.*;
import org.globus.exec.utils.client.*;
import org.globus.exec.utils.*;
import org.globus.wsrf.impl.security.descriptor.*;
import org.globus.delegation.*;
import org.globus.transfer.reliable.service.factory.*;
import org.globus.gsi.*;
import org.globus.gsi.gssapi.*;
import org.globus.rft.generated.*;
import org.oasis.wsrf.properties.*;
import org.oasis.wsn.*;
import org.oasis.wsrf.lifetime.Destroy;
import org.gridforum.jgss.*;
import org.ietf.jgss.*;
import org.gridtrust.vbe.*;
import org.gridtrust.vbe.service.*;
import org.gridtrust.Array_String;
import org.gridtrust.IndividualState;
import org.gridtrust.ppm.Array_UserProfile;
import org.gridtrust.ppm.FindServiceProvider;
import org.gridtrust.ppm.PPManagerPortType;
import org.gridtrust.ppm.UserProfile;
import org.gridtrust.ppm.Service;
import org.gridtrust.ppm.ServiceProvider;
import org.gridtrust.ppm.service.PPManagerServiceAddressingLocator;
import org.gridtrust.srb.*;
import org.gridtrust.srb.service.*;
import org.gridtrust.vbe.VBEMException;
import org.gridtrust.vbe.impl.CA;
import org.gridtrust.vom.VOManager;
import org.gridtrust.trs.TrustReputationServicePortType;
import org.gridtrust.trs.service.TrustReputationServiceAddressingLocator;
import org.gridtrust.util.*;

public class Client {

    static {
        Util.registerTransport();
    }

    VBEManagerPortType vbe;

    String serviceURI = "http://mng2.moviquity.com:9000/wsrf/services/gridtrust/vbe/VBEManager";

    String userId = "aaaaB@moviquity.com";

    private String ppmURI = "https://130.37.193.48:8443/wsrf/services/gridtrust/ppm/PPManager";

    private String trsURI = "https://195.182.241.75:9000/wsrf/services/TrustReputationService";

    public Client(String[] args) {
        try {
            VBEManagerServiceAddressingLocator locator = new VBEManagerServiceAddressingLocator();
            EndpointReferenceType endpoint = new EndpointReferenceType();
            endpoint.setAddress(new Address(serviceURI));
            vbe = locator.getVBEManagerPortTypePort(endpoint);
            ((Stub) vbe)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
            String CLIENT_DESC = "src/org/gridtrust/vbe/client/client-security-config-gridtrustcertificate.xml";
            ((Stub) vbe)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
            if (args.length < 1) System.out.println("Usage: Client {registerUser|deleteUser|updateUser} email"); else {
                if (args[0].equals("registerUser")) registerUser(args[1]); else if (args[0].equals("deleteUser")) deleteUser(args[1]); else if (args[0].equals("updateUser")) updateUser(args[1]); else if (args[0].equals("searchUser")) searchUser(args[1]); else if (args[0].equals("registerVO")) registerVO(args[1]); else if (args[0].equals("deleteVO")) deleteVO(args[1]); else if (args[0].equals("updateVO")) updateVO(args[1]); else if (args[0].equals("searchVO")) searchVO(args[1]); else if (args[0].equals("registerServiceProvider")) registerServiceProvider(args[1]); else if (args[0].equals("deleteServiceProvider")) deleteServiceProvider(args[1]); else if (args[0].equals("searchServiceProvider")) searchServiceProvider(args[1]); else if (args[0].equals("registerVOUser")) registerVOUser(args[1]); else if (args[0].equals("registerVOServiceProvider")) registerVOServiceProvider(args[1]); else if (args[0].equals("proxyTest")) proxyTest(); else if (args[0].equals("submitGRAMJob")) submitGRAMJob(); else if (args[0].equals("submitGRAMJobLocal")) submitGRAMJobLocal(); else if (args[0].equals("getUserReputation")) getUserReputation(); else if (args[0].equals("createWebServerCertificate")) createWebServerCertificate(); else if (args[0].equals("searchServiceProviderInVO")) searchServiceProviderInVO(args[1]); else if (args[0].equals("deleteVOUser")) deleteVOUser(args[1]); else if (args[0].equals("deleteVOServiceProvider")) deleteVOServiceProvider(args[1]); else if (args[0].equals("userRating")) userRating(); else if (args[0].equals("registerVBEInTRS")) registerVBEInTRS(); else if (args[0].equals("registerVBEInTRSVO")) registerVBEinTRSVO(); else if (args[0].equals("certTest")) certTest(); else System.out.println("Function not found");
            }
        } catch (Exception e) {
            System.out.println("############# EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void registerUser(String email) throws Exception {
        RegisterUser regUser = new RegisterUser();
        regUser.setCountry("ES");
        regUser.setState("Madrid");
        regUser.setLocality("Madrid");
        regUser.setOrganization("Moviquity");
        regUser.setOrganizationalUnit("Development");
        regUser.setCommonName("Alejandro Sanchez");
        regUser.setEmailAddress(email);
        GridTrustPublicKey publicKey = new GridTrustPublicKey("/home/shalmak/.globus/userpublickey.pem");
        regUser.setPublicKey(publicKey.getEncoded());
        try {
            String CLIENT_DESC = "src/org/gridtrust/vbe/client/client-security-config-anonymous.xml";
            ((Stub) vbe)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
            RegisterUserResponse response = vbe.registerUser(regUser);
            GridTrustCertificate certificate = new GridTrustCertificate(response.getCertificate());
            certificate.writeCertificateToFile("/home/shalmak/.globus/usercert.pem");
            System.out.println("User registered");
        } catch (VBEMException e) {
            System.out.println("User not registered. " + e.getVBEmessage());
        }
    }

    void deleteUser(String email) throws Exception {
        PPManagerPortType ppm = getPPManagerPortType();
        org.gridtrust.ppm.DeleteUser delUser = new org.gridtrust.ppm.DeleteUser();
        String[] userIdList = new String[1];
        userIdList[0] = email;
        delUser.setUserIdList(userIdList);
        org.gridtrust.ppm.DeleteUserResponse ppmResponse = ppm.deleteUser(delUser);
        IndividualState ppmState = ppmResponse.getDeleteUserReturn().getStateList().getItem(0);
        if (ppmState.isState()) System.out.println("User deleted."); else System.out.println("User not deleted. " + ppmState.getFailureReason());
    }

    void updateUser(String email) throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setId(email);
        try {
            UpdateUserResponse response = vbe.updateUser(updateUser);
            if (response.isState()) {
                System.out.println("User updated.");
            } else {
                System.out.println("User not updated.");
            }
        } catch (VBEMException e) {
            System.out.println("User not update. " + e.getVBEmessage());
        }
    }

    void searchUser(String email) throws Exception {
        SearchUser searchUser = new SearchUser();
        searchUser.setUserId(email);
        searchUser.setMinTrValue(20);
        searchUser.setVoId("vo2");
        try {
            SearchUserResponse response = vbe.searchUser(searchUser);
            for (UserProfile profile : response.getSearchUserReturn()) {
                System.out.println("User Id: " + profile.getUserId() + " TR Value: " + profile.getCurrentTRValue());
            }
        } catch (VBEMException e) {
            System.out.println("Users not found. " + e.getVBEmessage());
        }
    }

    void registerVO(String id) throws Exception {
        RegisterVO regVO = new RegisterVO();
        regVO.setVoId(id);
        regVO.setDescription("Description");
        try {
            RegisterVOResponse response = vbe.registerVO(regVO);
            GridTrustCertificate certificate = new GridTrustCertificate(response.getCertificate());
            certificate.writeCertificateToFile("/var/tmp/vocert.pem");
            System.out.println("VO registered");
        } catch (VBEMException e) {
            System.out.println("VO registration failed. " + e.getVBEmessage());
        }
    }

    void deleteVO(String id) throws Exception {
        DeleteVO deleteVO = new DeleteVO();
        try {
            DeleteVOResponse response = vbe.deleteVO(deleteVO);
            if (response.isState()) {
                System.out.println("VO deleted.");
            } else {
                System.out.println("VO not deleted");
            }
        } catch (VBEMException e) {
            System.out.println("VO not deleted. " + e.getVBEmessage());
        }
    }

    void updateVO(String id) throws Exception {
        UpdateVO updateVO = new UpdateVO();
        updateVO.setDescription("Updated description");
        try {
            UpdateVOResponse response = vbe.updateVO(updateVO);
            if (response.isState()) {
                System.out.println("VO updated.");
            } else {
                System.out.println("VO not updated.");
            }
        } catch (VBEMException e) {
            System.out.println("VO not updated. " + e.getVBEmessage());
        }
    }

    void searchVO(String id) throws Exception {
        SearchVO searchVO = new SearchVO();
        searchVO.setVoId(id);
        try {
            SearchVOResponse response = vbe.searchVO(searchVO);
            org.gridtrust.ppm.VO vo = response.getSearchVOReturn();
            if (vo == null) System.out.println("VO not found"); else {
                System.out.println("VO Id: " + vo.getVoId() + " Admin: " + vo.getAdminId() + " Description : " + vo.getDescription());
                System.out.println("Users number: " + vo.getUserProfileList().getItem().length);
                for (UserProfile profile : vo.getUserProfileList().getItem()) {
                    if (profile == null) System.out.println("Profile null"); else System.out.println("User: " + profile.getUserId());
                }
                for (ServiceProvider provider : vo.getServiceProviderList().getItem()) {
                    if (provider == null) System.out.println("Provider null"); else System.out.println("Service Provider: " + provider.getSpId() + " Type: " + provider.getService().getServiceType());
                }
            }
        } catch (VBEMException e) {
            System.out.println("Users not found. " + e.getVBEmessage());
        }
    }

    void registerServiceProvider(String url) throws Exception {
        RegisterServiceProvider regSP = new RegisterServiceProvider();
        Service service = new Service();
        service.setServiceId("Service1");
        service.setDescriptionURL("http://13.37.30.252:9000/wsrf/services/gridtrust/ppm/PPManager?wsdl");
        service.setServiceType("Example Type");
        regSP.setCountry("ES");
        regSP.setState("Madrid");
        regSP.setLocality("Madrid");
        regSP.setOrganization("Moviquity");
        regSP.setOrganizationalUnit("Development");
        regSP.setCommonName(url);
        regSP.setEmailAddress("asm@moviquity.com");
        regSP.setType(GridTrustCertificate.SERVICE_PROVIDER_CERTIFICATE);
        regSP.setService(service);
        try {
            String CLIENT_DESC = "src/org/gridtrust/vbe/client/client-security-config-anonymous.xml";
            ((Stub) vbe)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
            RegisterServiceProviderResponse response = vbe.registerServiceProvider(regSP);
            GridTrustCertificate certificate = new GridTrustCertificate(response.getCertificate());
            certificate.writeCertificateToFile("/var/tmp/spcert.pem");
            System.out.println("Service Provider registered");
        } catch (VBEMException e) {
            System.out.println("Service Provider registration failed. " + e.getVBEmessage());
        }
    }

    void deleteServiceProvider(String id) throws Exception {
        PPManagerPortType ppm = getPPManagerPortType();
        org.gridtrust.ppm.CleanServiceProvider delSP = new org.gridtrust.ppm.CleanServiceProvider();
        String[] serviceProviderIdList = new String[1];
        serviceProviderIdList[0] = id;
        delSP.setServiceProviderIdList(serviceProviderIdList);
        org.gridtrust.ppm.CleanServiceProviderResponse ppmResponse = ppm.cleanServiceProvider(delSP);
        IndividualState ppmState = ppmResponse.getCleanServiceProviderReturn().getStateList().getItem(0);
        if (ppmState.isState()) System.out.println("Service provider deleted"); else System.out.println("Service provider not deleted. " + ppmState.getFailureReason());
    }

    void searchServiceProvider(String type) throws Exception {
        try {
            SearchServiceProvider searchSP = new SearchServiceProvider();
            searchSP.setServiceType("Example Type");
            SearchServiceProviderResponse response = vbe.searchServiceProvider(searchSP);
            ServiceProvider[] providers = response.getSearchServiceProviderReturn();
            if (providers != null && providers.length >= 1) {
                for (ServiceProvider provider : providers) {
                    System.out.println("Service Provider: " + provider.getSpId());
                    System.out.println("Service: " + provider.getService().getServiceType());
                }
            } else System.out.println("Service providers not found.");
        } catch (VBEMException e) {
            System.out.println("Error searching service providers: " + e.getVBEmessage());
        }
    }

    void registerVOUser(String userId) throws Exception {
        VOManager vom = new VOManager(serviceURI, "src/org/gridtrust/vbe/client/client-security-config-gridtrustcertificate.xml");
        GridTrustCertificate voCert = vom.registerVO("vo3499", "VO 3499", null);
        voCert.writeCertificateToFile("/var/tmp/vo3498cert.pem");
        GridTrustCertificate userCert = new GridTrustCertificate("/var/tmp/cnr_cert.pem");
        GridTrustCertificate voUserCert = vom.registerVOUser(voCert, userCert);
        voUserCert.writeCertificateToFile("/var/tmp/uservocert.pem");
        GridTrustMapFile mapFile = new GridTrustMapFile("/etc/grid-security/grid-mapfile");
        mapFile.addVO(voUserCert.getSubjectDN(), "gridftp");
    }

    void deleteVOUser(String userId) throws Exception {
        GridTrustCertificate voCert = new GridTrustCertificate("/var/lib/transporter/transportervocert.pem");
        VOManager vom = new VOManager(serviceURI, "/var/lib/transporter/security-descriptor.xml");
        try {
            if (vom.deleteVOUser(voCert, userId)) System.out.println("User deleted from the VO");
        } catch (VBEMException e) {
            System.out.println("User not deleted from the VO");
        }
    }

    void deleteVOServiceProvider(String spId) throws Exception {
        GridTrustCertificate voCert = new GridTrustCertificate("/var/lib/transporter/transportervocert.pem");
        VOManager vom = new VOManager(serviceURI, "/var/lib/transporter/security-descriptor.xml");
        try {
            if (vom.deleteVOServiceProvider(voCert, spId)) System.out.println("Service provider deleted from the VO");
        } catch (VBEMException e) {
            System.out.println("Service provider not deleted from the VO");
        }
    }

    void registerVOServiceProvider(String spId) throws Exception {
        RegisterVOServiceProvider registerVOServiceProvider = new RegisterVOServiceProvider();
        try {
            RegisterVOServiceProviderResponse response = vbe.registerVOServiceProvider(registerVOServiceProvider);
        } catch (VBEMException e) {
            System.out.println("VO Service Provider not registered. " + e.getVBEmessage());
        }
    }

    void proxyTest() throws Exception {
        GridTrustProxyCertificate proxy = new GridTrustProxyCertificate("/tmp/x509up_u1000");
        proxy.writeCertificateToFile("/tmp/gridtrustcertificate.pem");
        proxy.writeKeyPairToFile("/tmp/gridtrustkeypair.pem");
        proxy.writeProxyCertificateToFile("/tmp/proxy.pem");
    }

    void submitGRAMJob() {
        try {
            String contactString = "https://gridnode1.iit.cnr.it:8443/wsrf/services/ManagedJobFactoryService";
            String jobDescriptorPath = "/usr/local/gridtrust/workspace/GridTrust/src/org/gridtrust/vbe/client/gram-job-cnr.xml";
            String delegationUri = "https://gridnode1.iit.cnr.it:8443/wsrf/services/DelegationFactoryService";
            String proxyFilePath = "/tmp/x509up_u1000";
            String jobId = "job464";
            File jobDescriptionFile = new File(jobDescriptorPath);
            JobDescriptionType jobDescription = RSLHelper.readRSL(jobDescriptionFile);
            URL factoryUrl = ManagedJobFactoryClientHelper.getServiceURL(contactString).getURL();
            String factoryType = ManagedJobFactoryConstants.FACTORY_TYPE.FORK;
            EndpointReferenceType factoryEndpoint = ManagedJobFactoryClientHelper.getFactoryEndpoint(factoryUrl, factoryType);
            ManagedJobFactoryPortType factoryPort = ManagedJobFactoryClientHelper.getPort(factoryEndpoint);
            File proxyFile = new File(proxyFilePath);
            byte[] proxyData = new byte[(int) proxyFile.length()];
            FileInputStream inputStream = new FileInputStream(proxyFile);
            inputStream.read(proxyData);
            inputStream.close();
            ExtendedGSSManager manager = (ExtendedGSSManager) ExtendedGSSManager.getInstance();
            GSSCredential proxy = manager.createCredential(proxyData, ExtendedGSSCredential.IMPEXP_OPAQUE, GSSCredential.DEFAULT_LIFETIME, null, GSSCredential.INITIATE_AND_ACCEPT);
            ClientSecurityDescriptor clientDescriptor = new ClientSecurityDescriptor();
            clientDescriptor.setGSITransport(Constants.ENCRYPTION);
            clientDescriptor.setAuthz(NoAuthorization.getInstance());
            clientDescriptor.setGSSCredential(proxy);
            ((Stub) factoryPort)._setProperty(Constants.CLIENT_DESCRIPTOR, clientDescriptor);
            EndpointReferenceType delegFactoryEndpoint = DelegationServiceEndpoint.getInstance().getDelegationServiceEndpoint();
            delegFactoryEndpoint.setAddress(new AttributedURI(delegationUri));
            X509Certificate certToSign = DelegationUtil.getCertificateChainRP(delegFactoryEndpoint, clientDescriptor)[0];
            GSSCredential cred = manager.createCredential(GSSCredential.INITIATE_AND_ACCEPT);
            GlobusCredential userGlobusCred = ((GlobusGSSCredentialImpl) cred).getGlobusCredential();
            EndpointReferenceType credentialEndpoint = DelegationUtil.delegate(delegationUri, userGlobusCred, certToSign, 1000, true, clientDescriptor);
            jobDescription.setJobCredentialEndpoint(credentialEndpoint);
            jobDescription.setStagingCredentialEndpoint(credentialEndpoint);
            TransferRequestType stageIn = jobDescription.getFileStageIn();
            stageIn.setTransferCredentialEndpoint(credentialEndpoint);
            TransferRequestType stageOut = jobDescription.getFileStageOut();
            stageOut.setTransferCredentialEndpoint(credentialEndpoint);
            NotificationConsumerManager notifConsumerManager = NotificationConsumerManager.getInstance();
            notifConsumerManager.startListening();
            List topicPath = new LinkedList();
            topicPath.add(ManagedJobConstants.RP_STATE);
            ResourceSecurityDescriptor resourceSecDesc = new ResourceSecurityDescriptor();
            resourceSecDesc.setAuthz(Authorization.AUTHZ_NONE);
            Vector authMethods = new Vector();
            authMethods.add(GSITransportAuthMethod.BOTH);
            resourceSecDesc.setAuthMethods(authMethods);
            TopicExpressionType topicExpression = new TopicExpressionType(WSNConstants.SIMPLE_TOPIC_DIALECT, ManagedJobConstants.RP_STATE);
            CreateManagedJobInputType jobInput = new CreateManagedJobInputType();
            jobInput.setJobID(new AttributedURI("uuid:" + jobId));
            jobInput.setJob(jobDescription);
            CreateManagedJobOutputType createResponse = factoryPort.createManagedJob(jobInput);
            EndpointReferenceType jobEndpoint = createResponse.getManagedJobEndpoint();
            ManagedJobPortType jobPort = ManagedJobClientHelper.getPort(jobEndpoint);
            ((Stub) jobPort)._setProperty(Constants.CLIENT_DESCRIPTOR, clientDescriptor);
            while (true) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (RSLParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void submitGRAMJobLocal() {
        try {
            String contactString = "http://mng.moviquity.com:9002/wsrf/services/ManagedJobFactoryService";
            String jobDescriptorPath = "/usr/local/gridtrust/workspace/GridTrust/src/org/gridtrust/vbe/client/gram-job.xml";
            String delegationUri = "http://mng.moviquity.com:9002/wsrf/services/DelegationFactoryService";
            String proxyFilePath = "/tmp/x509up_u1000";
            String jobId = "job437";
            File jobDescriptionFile = new File(jobDescriptorPath);
            JobDescriptionType jobDescription = RSLHelper.readRSL(jobDescriptionFile);
            URL factoryUrl = ManagedJobFactoryClientHelper.getServiceURL(contactString).getURL();
            String factoryType = ManagedJobFactoryConstants.FACTORY_TYPE.FORK;
            EndpointReferenceType factoryEndpoint = ManagedJobFactoryClientHelper.getFactoryEndpoint(factoryUrl, factoryType);
            ManagedJobFactoryPortType factoryPort = ManagedJobFactoryClientHelper.getPort(factoryEndpoint);
            File proxyFile = new File(proxyFilePath);
            byte[] proxyData = new byte[(int) proxyFile.length()];
            FileInputStream inputStream = new FileInputStream(proxyFile);
            inputStream.read(proxyData);
            inputStream.close();
            ExtendedGSSManager manager = (ExtendedGSSManager) ExtendedGSSManager.getInstance();
            GSSCredential proxy = manager.createCredential(proxyData, ExtendedGSSCredential.IMPEXP_OPAQUE, GSSCredential.DEFAULT_LIFETIME, null, GSSCredential.INITIATE_AND_ACCEPT);
            ClientSecurityDescriptor clientDescriptor = new ClientSecurityDescriptor();
            clientDescriptor.setGSISecureConv(Constants.ENCRYPTION);
            clientDescriptor.setAuthz(NoAuthorization.getInstance());
            clientDescriptor.setGSSCredential(proxy);
            ((Stub) factoryPort)._setProperty(Constants.CLIENT_DESCRIPTOR, clientDescriptor);
            EndpointReferenceType delegFactoryEndpoint = DelegationServiceEndpoint.getInstance().getDelegationServiceEndpoint();
            delegFactoryEndpoint.setAddress(new AttributedURI(delegationUri));
            X509Certificate certToSign = DelegationUtil.getCertificateChainRP(delegFactoryEndpoint, clientDescriptor)[0];
            GSSCredential cred = manager.createCredential(GSSCredential.INITIATE_AND_ACCEPT);
            GlobusCredential userGlobusCred = ((GlobusGSSCredentialImpl) cred).getGlobusCredential();
            EndpointReferenceType credentialEndpoint = DelegationUtil.delegate(delegationUri, userGlobusCred, certToSign, 1000, false, clientDescriptor);
            jobDescription.setJobCredentialEndpoint(credentialEndpoint);
            jobDescription.setStagingCredentialEndpoint(credentialEndpoint);
            TransferRequestType stageIn = jobDescription.getFileStageIn();
            stageIn.setTransferCredentialEndpoint(credentialEndpoint);
            TransferRequestType stageOut = jobDescription.getFileStageOut();
            stageOut.setTransferCredentialEndpoint(credentialEndpoint);
            NotificationConsumerManager notifConsumerManager = NotificationConsumerManager.getInstance();
            notifConsumerManager.startListening();
            List topicPath = new LinkedList();
            topicPath.add(ManagedJobConstants.RP_STATE);
            ResourceSecurityDescriptor resourceSecDesc = new ResourceSecurityDescriptor();
            resourceSecDesc.setAuthz(Authorization.AUTHZ_NONE);
            Vector authMethods = new Vector();
            authMethods.add(GSISecureConvAuthMethod.BOTH);
            resourceSecDesc.setAuthMethods(authMethods);
            Subscribe subscriptionReq = new Subscribe();
            TopicExpressionType topicExpression = new TopicExpressionType(WSNConstants.SIMPLE_TOPIC_DIALECT, ManagedJobConstants.RP_STATE);
            subscriptionReq.setTopicExpression(topicExpression);
            CreateManagedJobInputType jobInput = new CreateManagedJobInputType();
            jobInput.setJobID(new AttributedURI("uuid:" + jobId));
            jobInput.setJob(jobDescription);
            jobInput.setSubscribe(subscriptionReq);
            CreateManagedJobOutputType createResponse = factoryPort.createManagedJob(jobInput);
            EndpointReferenceType jobEndpoint = createResponse.getManagedJobEndpoint();
            ManagedJobPortType jobPort = ManagedJobClientHelper.getPort(jobEndpoint);
            ((Stub) jobPort)._setProperty(Constants.CLIENT_DESCRIPTOR, clientDescriptor);
            while (true) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (RSLParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserReputation() throws Exception {
        String userId = "ts.company5.com";
        String voId = "RoutingVO34";
        TrustReputationServicePortType trs = getTrustReputationPortType();
        org.gridtrust.trs.ObtainUserReputation userRep = new org.gridtrust.trs.ObtainUserReputation();
        userRep.setUserId(userId);
        userRep.setVbeId("1");
        try {
            org.gridtrust.trs.ObtainUserReputationResponse trsResponse = trs.obtainUserReputation(userRep);
            float reputation = trsResponse.getReputation();
            System.out.println("Reputation: " + reputation);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registerVBEInTRS() {
        try {
            TrustReputationServicePortType trs = getTrustReputationPortType();
            org.gridtrust.trs.RegisterServiceResponse trsResponse = new org.gridtrust.trs.RegisterServiceResponse();
            org.gridtrust.trs.RegisterService regService = new org.gridtrust.trs.RegisterService();
            regService.setVbeId("1");
            regService.setServiceId("VBEM");
            regService.setAction("OK");
            regService.setPenalty(new Float(0.7));
            trsResponse = trs.registerService(regService);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerVBEinTRSVO() {
        org.gridtrust.trs.SetupVOResponse trsResponse = null;
        try {
            TrustReputationServicePortType trs = getTrustReputationPortType();
            org.gridtrust.trs.SetupVO setupVO = new org.gridtrust.trs.SetupVO();
            setupVO.setServiceId("VBE");
            setupVO.setVbeId("1");
            setupVO.setVoId("vo8006");
            trsResponse = trs.setupVO(setupVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userRating() {
        String userId = "ts.company5.com";
        String voId = "RoutingVO37";
        String action = "OK";
        try {
            TrustReputationServicePortType trs = getTrustReputationPortType();
            for (int x = 0; x < 10; x++) {
                org.gridtrust.trs.UserRating rating = new org.gridtrust.trs.UserRating();
                rating.setAction(action);
                rating.setServiceId("ts.company4.com");
                rating.setUserId(userId);
                rating.setVoId(voId);
                trs.userRating(rating);
                rating.setAction(action);
                rating.setServiceId("ts.company5.com");
                rating.setUserId(userId);
                rating.setVoId(voId);
                trs.userRating(rating);
                rating.setAction(action);
                rating.setServiceId("ts.company6.com");
                rating.setUserId(userId);
                rating.setVoId(voId);
                trs.userRating(rating);
                rating.setAction(action);
                rating.setServiceId("ts.company7.com");
                rating.setUserId(userId);
                rating.setVoId(voId);
                trs.userRating(rating);
                rating.setAction(action);
                rating.setServiceId("ts.company8.com");
                rating.setUserId(userId);
                rating.setVoId(voId);
                trs.userRating(rating);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createWebServerCertificate() throws Exception {
        String cakey = "/usr/local/gridtrust/demo/ca/cakey.pem";
        String cacert = "/usr/local/gridtrust/demo/ca/cacert.pem";
        GridTrustPublicKey key = new GridTrustPublicKey("/usr/local/gridtrust/demo/publickey.pem");
        CA ca = new CA(cakey, cacert);
        Hashtable<String, String> extensions = new Hashtable<String, String>();
        extensions.put(GridTrustCertificate.ID, "user303");
        GridTrustCertificate certificate = ca.createCertificate(BigInteger.valueOf(3434325), "ES", "Madrid", "Madrid", "Moviquity", "Development", "mng2.moviquity.com", "asm@moviquity.com", GridTrustCertificate.WEB_SERVER_CERTIFICATE, extensions, key);
        certificate.writeCertificateToFile("/tmp/certificate.pem");
    }

    public void searchServiceProviderInVO(String voId) throws Exception {
        System.out.println("With VO Manager");
        String policyPath = "/usr/local/gridtrust/demo3/policy.xml";
        VOManager vom = new VOManager(serviceURI, "/var/lib/transporter/security-descriptor.xml");
        ServiceProvider[] providers2 = vom.searchServiceProvider(null, 0, policyPath, null, "Computational", null, voId);
        if (providers2 == null) {
            System.out.println("Service provider not found");
        } else {
            for (ServiceProvider provider : providers2) {
                System.out.println("Service provider: " + provider.getSpId() + " Service Name: " + provider.getService().getServiceName());
            }
        }
    }

    public void certTest() throws Exception {
        GridTrustCertificate vo0004VOO = new GridTrustCertificate("/tmp/vo0004VOO.pem");
        GridTrustCertificate voOwnerCERT = new GridTrustCertificate("/tmp/voOwnerCERT.pem");
        System.out.println(vo0004VOO.getCommonName());
        System.out.println(voOwnerCERT.getCommonName());
    }

    public void deliver(java.util.List topicPath, org.apache.axis.message.addressing.EndpointReferenceType producer, java.lang.Object message) {
        System.out.println("Works");
    }

    private PPManagerPortType getPPManagerPortType() throws MalformedURIException, ServiceException {
        PPManagerServiceAddressingLocator locator = new PPManagerServiceAddressingLocator();
        EndpointReferenceType endpoint = new EndpointReferenceType();
        endpoint.setAddress(new Address(ppmURI));
        PPManagerPortType ppm = locator.getPPManagerPortTypePort(endpoint);
        String CLIENT_DESC = "/var/lib/transporter/security-descriptor-transport.xml";
        ((Stub) ppm)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
        ((Stub) ppm)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
        ((Stub) ppm)._setProperty(Constants.GSI_TRANSPORT, Constants.ENCRYPTION);
        return ppm;
    }

    private SRBManagerPortType getSRBManagerPortType() throws MalformedURIException, ServiceException {
        SRBManagerServiceAddressingLocator locator = new SRBManagerServiceAddressingLocator();
        EndpointReferenceType endpoint = new EndpointReferenceType();
        endpoint.setAddress(new Address(ppmURI));
        SRBManagerPortType srb = locator.getSRBManagerPortTypePort(endpoint);
        String CLIENT_DESC = "/var/lib/transporter/security-descriptor-transport.xml";
        ((Stub) srb)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
        ((Stub) srb)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
        ((Stub) srb)._setProperty(Constants.GSI_TRANSPORT, Constants.ENCRYPTION);
        return srb;
    }

    private TrustReputationServicePortType getTrustReputationPortType() throws MalformedURIException, ServiceException {
        TrustReputationServiceAddressingLocator locator = new TrustReputationServiceAddressingLocator();
        EndpointReferenceType endpoint = new EndpointReferenceType();
        endpoint.setAddress(new Address(trsURI));
        TrustReputationServicePortType trs = locator.getTrustReputationServicePortTypePort(endpoint);
        String CLIENT_DESC = "/var/lib/transporter/security-descriptor.xml";
        ((Stub) trs)._setProperty(Constants.AUTHORIZATION, NoAuthorization.getInstance());
        ((Stub) trs)._setProperty(Constants.CLIENT_DESCRIPTOR_FILE, CLIENT_DESC);
        ((Stub) trs)._setProperty(Constants.GSI_TRANSPORT, Constants.ENCRYPTION);
        return trs;
    }

    public static void main(String[] args) {
        new Client(args);
    }
}
