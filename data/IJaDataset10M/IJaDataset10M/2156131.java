package Services;

import Beans.InternalNetworkBean;
import Beans.LogBean;
import Beans.PermissionsBean;
import Beans.Requests.AuthorizationRequest;
import Beans.Requests.DataRetrieval.DataRetrievalRequest;
import Beans.Requests.DataRetrieval.LogsDataRetrievalRequest;
import Beans.Requests.DataRetrieval.MachinesDataRetrievalRequest;
import Beans.Requests.DataRetrieval.MediaDataRetrievalRequest;
import Beans.Requests.DataRetrieval.MiscDataRetrievalRequest;
import Beans.Requests.DataRetrieval.NetworksDataRetrievalRequest;
import Beans.Requests.DataRetrieval.ServersDataRetrievalRequest;
import Beans.Requests.DataRetrieval.SnapshotsDataRetrievalRequest;
import Beans.Requests.DataRetrieval.SystemDataRetrievalRequest;
import Beans.Requests.DataRetrieval.UsersDataRetrievalRequest;
import Beans.Requests.DataRetrieval.VBoxResourceRequest;
import Beans.Requests.MachineManagement.Components.AudioSettings;
import Beans.Requests.MachineManagement.Components.BIOSSettings;
import Beans.Requests.MachineManagement.Components.BootSettings;
import Beans.Requests.MachineManagement.Components.CPUSettings;
import Beans.Requests.MachineManagement.Components.IOSettings;
import Beans.Requests.MachineManagement.Components.MemorySettings;
import Beans.Requests.MachineManagement.Components.NATEngineForwardingRule;
import Beans.Requests.MachineManagement.Components.NATEngineSettings;
import Beans.Requests.MachineManagement.Components.NetworkAdapterSettings;
import Beans.Requests.MachineManagement.Components.SharedFolderSettings;
import Beans.Requests.MachineManagement.Components.VRDPSettings;
import Beans.Requests.MachineManagement.Components.VideoSettings;
import Beans.Requests.NetworkManagement.Components.DHCPServerConfiguration;
import Beans.Requests.NetworkManagement.Components.HostNetworkConfiguration;
import Beans.Requests.NetworkManagement.Components.InternalNetworkConfiguration;
import Beans.Requests.RequestBean;
import Beans.Requests.SimpleAuthorizationRequest;
import Beans.Responses.DataRetrievalComponents.LogDataComponent;
import Beans.Responses.DataRetrievalComponents.MachineDataComponent;
import Beans.Responses.DataRetrievalComponents.MachineSnapshotDataComponent;
import Beans.Responses.DataRetrievalComponents.MachineStorageData;
import Beans.Responses.DataRetrievalComponents.MediumDataComponent;
import Beans.Responses.DataRetrievalComponents.MiscDataComponent;
import Beans.Responses.DataRetrievalComponents.NetworkDataComponent;
import Beans.Responses.DataRetrievalComponents.ServerDataComponent;
import Beans.Responses.DataRetrievalComponents.SystemDataComponent;
import Beans.Responses.DataRetrievalComponents.SystemHostData;
import Beans.Responses.DataRetrievalComponents.SystemVirtualBoxData;
import Beans.Responses.DataRetrievalComponents.UserDataComponent;
import Beans.Responses.DataRetrievalResponse;
import Beans.ServerBean;
import Beans.UserBean;
import Beans.VirtualResourceBean;
import Managers.DatabaseManager;
import Managers.LogsManager;
import Managers.PermissionsManager;
import Managers.SessionsManager;
import Managers.Utilities.RequestResponseWrapper;
import Managers.Utilities.ResponseMessagesContainer;
import Managers.VBoxConnectionsManager;
import Utilities.Constants;
import Utilities.ApplicationException;
import Utilities.ExceptionMessages;
import Utilities.Constants.ResourceIdType;
import Utilities.Constants.ResourceRequestType;
import Utilities.Constants.VirtualResourceType;
import Utilities.Functions;
import Utilities.ParameterNames;
import Utilities.PermissionsConstants;
import com.sun.xml.ws.commons.virtualbox_3_2.IBIOSSettings;
import com.sun.xml.ws.commons.virtualbox_3_2.IDHCPServer;
import com.sun.xml.ws.commons.virtualbox_3_2.IGuestOSType;
import com.sun.xml.ws.commons.virtualbox_3_2.IHostNetworkInterface;
import com.sun.xml.ws.commons.virtualbox_3_2.IMachine;
import com.sun.xml.ws.commons.virtualbox_3_2.IMedium;
import com.sun.xml.ws.commons.virtualbox_3_2.IMediumAttachment;
import com.sun.xml.ws.commons.virtualbox_3_2.IMediumFormat;
import com.sun.xml.ws.commons.virtualbox_3_2.INATEngine;
import com.sun.xml.ws.commons.virtualbox_3_2.INetworkAdapter;
import com.sun.xml.ws.commons.virtualbox_3_2.ISharedFolder;
import com.sun.xml.ws.commons.virtualbox_3_2.IStorageController;
import com.sun.xml.ws.commons.virtualbox_3_2.IVRDPServer;
import com.sun.xml.ws.commons.virtualbox_3_2.IVirtualBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import org.virtualbox_3_2.DeviceType;
import org.virtualbox_3_2.NATProtocol;
import org.virtualbox_3_2.NetworkAttachmentType;

/**
 * This servlet represents the application's Data Retrieval Service. <br><br>
 *
 * This service takes care of request for data retrieval.
 *
 * @author Angel Sanadinov
 */
public class DataService extends HttpServlet {

    private DatabaseManager databaseManager;

    private LogsManager logsManager;

    private PermissionsManager permissionsManager;

    private VBoxConnectionsManager connectionsManager;

    private SessionsManager sessionsManager;

    /**
     * Initializes the service and retrieves all necessary managers.
     *
     * @throws ServletException if an exception occurs that interrupts the servlet's
     *                          normal operation
     *
     * @see HttpServlet#init()
     */
    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        databaseManager = (DatabaseManager) context.getAttribute(Constants.CONTEXT_DATABASE_MANAGER);
        logsManager = (LogsManager) context.getAttribute(Constants.CONTEXT_LOGS_MANAGER);
        permissionsManager = (PermissionsManager) context.getAttribute(Constants.CONTEXT_PERMISSIONS_MANAGER);
        connectionsManager = (VBoxConnectionsManager) context.getAttribute(Constants.CONTEXT_VBOX_CONNECTIONS_MANAGER);
        sessionsManager = (SessionsManager) getServletContext().getAttribute(Constants.CONTEXT_SESSIONS_MANAGER);
    }

    /**
     * Shuts down the service and cleans up any object references.
     *
     * @see HttpServlet#destroy()
     */
    @Override
    public void destroy() {
        databaseManager = null;
        logsManager = null;
        permissionsManager = null;
        connectionsManager = null;
        sessionsManager = null;
    }

    /**
     * Processes a <code>GET</code> request from a client.
     *
     * @param request an HttpServletRequest object that contains the request the
     *                client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *                 the servlet sends to the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the servlet
     *         handles the GET request
     *
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes a <code>POST</code> request from a client.
     *
     * @param request an HttpServletRequest object that contains the request the
     *                client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *                 the servlet sends to the client
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException if an input or output error is detected when the servlet
     *         handles the POST request
     *
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes all <code>GET</code> and <code>POST</code> requests and carries out
     * operations based on requested actions.
     *
     * @param request an HttpServletRequest object that contains the request the
     *                client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *                 the servlet sends to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the service
     *         handles the request
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataRetrievalRequest dataRequest = (DataRetrievalRequest) request.getAttribute(ParameterNames.REQUEST_DATA_RETRIEVAL);
        if (dataRequest != null && dataRequest.isValid()) {
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_DEBUG, "Request: " + dataRequest.toString());
            boolean isActionAuthorized = false;
            if (dataRequest.getResourceIdType() == ResourceIdType.NUMERIC_ID) isActionAuthorized = true; else if (dataRequest.getResourceIdType() == ResourceIdType.UUID) {
                if (dataRequest.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) {
                    AuthorizationRequest authRequest = new AuthorizationRequest(dataRequest.getRequestorId(), dataRequest.getServerId(), dataRequest.getRequestedAction(), ((VBoxResourceRequest) dataRequest).getResourceType(), ((VBoxResourceRequest) dataRequest).getResourceId());
                    isActionAuthorized = permissionsManager.isActionAuthorized(authRequest);
                } else {
                    SimpleAuthorizationRequest authRequest = new SimpleAuthorizationRequest(dataRequest.getRequestorId(), dataRequest.getServerId(), dataRequest.getRequestedAction(), ((VBoxResourceRequest) dataRequest).getResourceType());
                    isActionAuthorized = permissionsManager.isActionAuthorized(authRequest);
                }
            } else ;
            if (isActionAuthorized) {
                switch(dataRequest.getRequestedAction()) {
                    case PermissionsConstants.ACTION_RETRIEVE_USERS:
                        {
                            processResponse(request, retrieveUsersData((UsersDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_SERVERS:
                        {
                            processResponse(request, retrieveServersData((ServersDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_LOGS:
                        {
                            processResponse(request, retrieveLogsData((LogsDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_SYSTEM:
                        {
                            processResponse(request, retrieveSystemData((SystemDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_MACHINES:
                        {
                            processResponse(request, retrieveMachinesData((MachinesDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_SNAPSHOTS:
                        {
                            processResponse(request, retrieveSnapshotsData((SnapshotsDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_MEDIA:
                        {
                            processResponse(request, retrieveMediaData((MediaDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_NETWORKS:
                        {
                            processResponse(request, retrieveNetworksData((NetworksDataRetrievalRequest) dataRequest));
                        }
                        break;
                    case PermissionsConstants.ACTION_RETRIEVE_MISC:
                        {
                            processResponse(request, retrieveMiscellaneousData((MiscDataRetrievalRequest) dataRequest));
                        }
                        break;
                    default:
                        {
                            processResponse(request, new DataRetrievalResponse(new ApplicationException(ExceptionMessages.APP_E_INVALID_REQUESTED_ACTION)));
                        }
                        break;
                }
            } else processResponse(request, new DataRetrievalResponse(new ApplicationException(ExceptionMessages.APP_E_OPERATION_NOT_ALLOWED)));
        } else processResponse(request, new DataRetrievalResponse(new ApplicationException(ExceptionMessages.APP_E_INVALID_REQUEST)));
    }

    /**
     * Processes all responses going out of the service.
     *
     * @param request an HttpServletRequest object that contains the request the
     *                client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *                 the servlet sends to the client
     * @param dataResponse data retrieval response object
     * 
     * @throws ServletException if the processing of the response could not be handled
     * @throws IOException if an input or output error is detected when the response
     *                     is handled
     */
    private void processResponse(HttpServletRequest request, DataRetrievalResponse dataResponse) throws ServletException, IOException {
        RequestBean requestData = (RequestBean) request.getAttribute(ParameterNames.REQUEST_DATA_RETRIEVAL);
        request.removeAttribute(ParameterNames.REQUEST_DATA_RETRIEVAL);
        request.setAttribute(ParameterNames.RESPONSE_DATA_RETRIEVAL, dataResponse);
        if (!dataResponse.getRequestResult()) {
            ResponseMessagesContainer messagesContainer = (ResponseMessagesContainer) request.getSession(false).getAttribute(ParameterNames.SESSION_REQUEST_MESSAGES_OBJECT);
            messagesContainer.put(new RequestResponseWrapper(requestData, dataResponse));
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_DEBUG, "Request execution failed: " + dataResponse.getException().toString());
        } else ;
    }

    /**
     * Retrieves user(s) data.
     *
     * @param request user(s) data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<UserDataComponent> retrieveUsersData(UsersDataRetrievalRequest request) {
        DataRetrievalResponse<UserDataComponent> result = null;
        UserBean requestorData = sessionsManager.getUserData(request.getRequestorId());
        boolean isOperationAllowed = false;
        int userId = Utilities.Constants.INVALID_USER_ID;
        if (requestorData != null) {
            if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_MANAGER) {
                isOperationAllowed = true;
                userId = request.getResourceId();
            } else if (request.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE && request.getRequestorId() == request.getResourceId()) {
                isOperationAllowed = true;
                userId = request.getRequestorId();
            } else ;
        } else ;
        if (isOperationAllowed) {
            switch(request.getResourceRequestType()) {
                case SINGLE_RESOURCE:
                    {
                        UserBean userData = databaseManager.getUser(userId);
                        if (userData != null && userData.isValid()) {
                            ArrayList<PermissionsBean> permissions = databaseManager.getAllPermissionsForUser(userId);
                            UserDataComponent data = new UserDataComponent(userData, sessionsManager.getUserData(userId) != null, permissions);
                            result = new DataRetrievalResponse<UserDataComponent>(data);
                        } else {
                            result = new DataRetrievalResponse<UserDataComponent>(new ApplicationException(ExceptionMessages.APP_E_USER_RETRIEVAL_FAILED, "(" + userId + ")"));
                        }
                    }
                    break;
                case MULTIPLE_RESOURCES:
                    {
                        ArrayList<UserDataComponent> data = new ArrayList<UserDataComponent>();
                        ArrayList<UserBean> usersData = databaseManager.getUsers();
                        for (UserBean userBean : usersData) data.add(new UserDataComponent(userBean));
                        result = new DataRetrievalResponse<UserDataComponent>(data);
                    }
                    break;
                default:
                    {
                        result = new DataRetrievalResponse<UserDataComponent>(new ApplicationException(ExceptionMessages.APP_E_RESOURCES_RETRIEVAL_FAILED));
                    }
                    break;
            }
        } else result = new DataRetrievalResponse<UserDataComponent>(new ApplicationException(ExceptionMessages.APP_E_OPERATION_NOT_ALLOWED));
        return result;
    }

    /**
     * Retrieves server(s) data.
     *
     * @param request server(s) data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<ServerDataComponent> retrieveServersData(ServersDataRetrievalRequest request) {
        DataRetrievalResponse<ServerDataComponent> result = null;
        UserBean requestorData = sessionsManager.getUserData(request.getRequestorId());
        if (requestorData == null) return new DataRetrievalResponse<ServerDataComponent>(new ApplicationException(ExceptionMessages.APP_E_REQUESTOR_AUTHENTICATION_FAILED)); else ;
        if (request.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) {
            boolean isOperationAllowed = false;
            if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) isOperationAllowed = true; else {
                PermissionsBean serverPermissions = databaseManager.getPermissions(request.getRequestorId(), request.getResourceId());
                if (serverPermissions != null) isOperationAllowed = true; else ;
            }
            if (isOperationAllowed) {
                ServerBean serverData = databaseManager.getServer(request.getResourceId());
                if (serverData != null && serverData.isValid()) {
                    result = new DataRetrievalResponse<ServerDataComponent>(new ServerDataComponent(serverData, !(requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER)));
                } else {
                    result = new DataRetrievalResponse<ServerDataComponent>(new ApplicationException(ExceptionMessages.APP_E_SERVER_RETRIEVAL_FAILED, "(" + request.getResourceId() + ")"));
                }
            } else {
                result = new DataRetrievalResponse<ServerDataComponent>(new ApplicationException(ExceptionMessages.APP_E_OPERATION_NOT_ALLOWED));
            }
        } else {
            ArrayList<ServerDataComponent> data = new ArrayList<ServerDataComponent>();
            if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) {
                ArrayList<ServerBean> serversData = databaseManager.getServers();
                for (ServerBean serverBean : serversData) data.add(new ServerDataComponent(serverBean, false));
            } else {
                ArrayList<PermissionsBean> requestorPermissions = databaseManager.getAllPermissionsForUser(request.getRequestorId());
                if (requestorPermissions != null) {
                    for (PermissionsBean serverPermissions : requestorPermissions) {
                        ServerBean serverData = databaseManager.getServer(serverPermissions.getServerId());
                        if (serverData != null) data.add(new ServerDataComponent(serverData, true)); else ;
                    }
                } else ;
            }
            result = new DataRetrievalResponse<ServerDataComponent>(data);
        }
        return result;
    }

    /**
     * Retrieves logs data.
     *
     * @param request logs data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<LogDataComponent> retrieveLogsData(LogsDataRetrievalRequest request) {
        DataRetrievalResponse<LogDataComponent> result = null;
        UserBean requestorData = sessionsManager.getUserData(request.getRequestorId());
        ArrayList<LogBean> logs = null;
        switch(request.getLimit()) {
            case NO_LIMIT:
                {
                    if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) logs = databaseManager.getLogs(); else logs = databaseManager.getLogsByInitiatorId(request.getRequestorId());
                }
                break;
            case INITIATOR_ID:
                {
                    if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) logs = databaseManager.getLogsByInitiatorId(request.getLimitingResourceId()); else if (request.getRequestorId() == request.getLimitingResourceId()) logs = databaseManager.getLogsByInitiatorId(request.getRequestorId()); else ;
                }
                break;
            case SERVER_ID:
                {
                    if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) logs = databaseManager.getLogsByServerId(request.getLimitingResourceId()); else {
                        PermissionsBean permissions = databaseManager.getPermissions(request.getRequestorId(), request.getLimitingResourceId());
                        if (Functions.isUserServerManager(request.getRequestorId(), permissions)) logs = databaseManager.getLogsByServerId(request.getLimitingResourceId()); else ;
                    }
                }
                break;
            case SEVERITY:
                {
                    if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) logs = databaseManager.getLogsBySeverity(request.getSeverity()); else ;
                }
                break;
            default:
                ;
                break;
        }
        if (logs != null) {
            ArrayList<LogDataComponent> data = new ArrayList<LogDataComponent>(logs.size());
            for (LogBean logBean : logs) data.add(new LogDataComponent(logBean));
            result = new DataRetrievalResponse<LogDataComponent>(data);
        } else {
            result = new DataRetrievalResponse<LogDataComponent>(new ApplicationException(ExceptionMessages.APP_E_LOGS_RETRIEVAL_FAILED, "(" + request.getLimit() + "/" + request.getLimitingResourceId() + ")"));
        }
        return result;
    }

    /**
     * Retrieves system data.
     *
     * @param request system data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<SystemDataComponent> retrieveSystemData(SystemDataRetrievalRequest request) {
        DataRetrievalResponse<SystemDataComponent> result = null;
        UserBean requestorData = sessionsManager.getUserData(request.getRequestorId());
        if (requestorData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER) {
            IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getResourceId(), request.getRequestorId());
            if (vbox != null) {
                SystemVirtualBoxData vboxData = new SystemVirtualBoxData(vbox);
                SystemHostData hostData = new SystemHostData(vbox.getHost());
                result = new DataRetrievalResponse<SystemDataComponent>(new SystemDataComponent(request.getResourceId(), vboxData, hostData));
            } else result = new DataRetrievalResponse<SystemDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE));
        } else result = new DataRetrievalResponse<SystemDataComponent>(new ApplicationException(ExceptionMessages.APP_E_OPERATION_NOT_ALLOWED));
        return result;
    }

    /**
     * Retrieves machine(s) data.
     *
     * @param request machine(s) data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<MachineDataComponent> retrieveMachinesData(MachinesDataRetrievalRequest request) {
        DataRetrievalResponse<MachineDataComponent> result = null;
        IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getServerId(), request.getRequestorId());
        if (vbox == null) return new DataRetrievalResponse<MachineDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE)); else ;
        if (request.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) {
            try {
                IMachine vboxData = vbox.getMachine(request.getResourceId());
                VirtualResourceBean databaseData = databaseManager.getVirtualResource(request.getResourceId(), request.getServerId(), VirtualResourceType.MACHINE);
                AudioSettings audioSetup = new AudioSettings(vboxData.getAudioAdapter().getEnabled(), vboxData.getAudioAdapter().getAudioController(), vboxData.getAudioAdapter().getAudioDriver());
                IBIOSSettings bios = vboxData.getBIOSSettings();
                BIOSSettings biosSetup = new BIOSSettings(vboxData.getFirmwareType(), vboxData.getHpetEnabled(), vboxData.getRTCUseUTC(), bios.getBootMenuMode(), bios.getACPIEnabled(), bios.getIOAPICEnabled(), bios.getTimeOffset());
                int maxBootDevices = vbox.getSystemProperties().getMaxBootPosition().intValue();
                DeviceType[] bootDevices = new DeviceType[maxBootDevices];
                for (int i = 1; i <= maxBootDevices; i++) bootDevices[i - 1] = vboxData.getBootOrder(new Long(i));
                BootSettings bootSetup = new BootSettings(bootDevices);
                CPUSettings cpuSetup = new CPUSettings(vboxData.getCPUHotPlugEnabled(), vboxData.getCPUCount().intValue());
                IOSettings ioSetup = new IOSettings(vboxData.getIoCacheEnabled(), vboxData.getIoCacheSize().intValue(), vboxData.getIoBandwidthMax().intValue());
                MemorySettings memorySetup = new MemorySettings(vboxData.getMemorySize().intValue(), vboxData.getMemoryBalloonSize().intValue(), vboxData.getPageFusionEnabled());
                int maxNetworkAdapters = vbox.getSystemProperties().getNetworkAdapterCount().intValue();
                NetworkAdapterSettings[] networksSetup = new NetworkAdapterSettings[maxNetworkAdapters];
                for (int i = 0; i < maxNetworkAdapters; i++) {
                    INetworkAdapter currentAdapter = vboxData.getNetworkAdapter(new Long(i));
                    String networkName = null, externalName = null;
                    switch(currentAdapter.getAttachmentType()) {
                        case HOST_ONLY:
                        case BRIDGED:
                            networkName = currentAdapter.getHostInterface();
                            break;
                        case INTERNAL:
                            {
                                networkName = currentAdapter.getInternalNetwork();
                                InternalNetworkBean internalNetwork = databaseManager.getInternalNetwork(networkName, request.getServerId());
                                if (internalNetwork != null) externalName = internalNetwork.getNetworkName(); else ;
                            }
                            break;
                        case NAT:
                            networkName = currentAdapter.getNATNetwork();
                            break;
                        case VDE:
                            networkName = currentAdapter.getVDENetwork();
                            break;
                        default:
                            ;
                            break;
                    }
                    NetworkAdapterSettings currentAdapterSetup = new NetworkAdapterSettings(currentAdapter.getAdapterType(), currentAdapter.getAttachmentType(), currentAdapter.getEnabled(), currentAdapter.getMACAddress(), networkName, externalName, currentAdapter.getCableConnected(), currentAdapter.getLineSpeed().intValue());
                    currentAdapterSetup.setBootPriority(currentAdapter.getBootPriority().intValue());
                    if (currentAdapter.getAttachmentType() == NetworkAttachmentType.NAT) {
                        NATEngineSettings natSetup = new NATEngineSettings();
                        INATEngine natEngine = currentAdapter.getNatDriver();
                        Holder<Long> holderMTU = new Holder<Long>();
                        Holder<Long> holderSockSnd = new Holder<Long>();
                        Holder<Long> holderSockRcv = new Holder<Long>();
                        Holder<Long> holderWndSnd = new Holder<Long>();
                        Holder<Long> holderWNdRcv = new Holder<Long>();
                        natEngine.getNetworkSettings(holderMTU, holderSockSnd, holderSockRcv, holderWndSnd, holderWNdRcv);
                        natSetup.setEngineConfiguration(holderMTU.value, holderSockSnd.value, holderSockRcv.value, holderWndSnd.value, holderWNdRcv.value, natEngine.getNetwork(), natEngine.getHostIP());
                        natSetup.setTFTPConfiguration(natEngine.getTftpPrefix(), natEngine.getTftpBootFile(), natEngine.getTftpNextServer());
                        natSetup.setDNSConfiguration(natEngine.getDnsPassDomain(), natEngine.getDnsProxy(), natEngine.getDnsUseHostResolver());
                        natSetup.setAliasMode(natEngine.getAliasMode());
                        for (String currentRawRule : natEngine.getRedirects()) {
                            StringTokenizer tokenizer = new StringTokenizer(currentRawRule, ",");
                            NATEngineForwardingRule currentRule = new NATEngineForwardingRule(tokenizer.nextToken(), (tokenizer.nextToken().equals("1")) ? NATProtocol.TCP : NATProtocol.UDP, tokenizer.nextToken(), Integer.parseInt(tokenizer.nextToken()), tokenizer.nextToken(), Integer.parseInt(tokenizer.nextToken()));
                            natSetup.addNewForwardingRule(currentRule);
                        }
                        currentAdapterSetup.setNatEngineSetup(natSetup);
                    } else ;
                    networksSetup[i] = currentAdapterSetup;
                }
                ISharedFolder[] sharedFolders = vboxData.getSharedFolders().toArray(new ISharedFolder[vboxData.getSharedFolders().size()]);
                SharedFolderSettings[] sharedFoldersSetup = new SharedFolderSettings[sharedFolders.length];
                String sharedFoldersRoot = databaseManager.getServerSharedFoldersRoot(request.getServerId());
                for (int i = 0; i < sharedFolders.length; i++) {
                    ISharedFolder currentFolder = sharedFolders[i];
                    String folderPath = currentFolder.getHostPath().substring(sharedFoldersRoot.length());
                    SharedFolderSettings folderSetup = new SharedFolderSettings(currentFolder.getName(), "{root}" + folderPath, currentFolder.isWritable(), currentFolder.isAccessible(), currentFolder.getLastAccessError());
                    sharedFoldersSetup[i] = folderSetup;
                }
                int numberOfControllers = vboxData.getStorageControllers().size();
                int numberOfAttachments = vboxData.getMediumAttachments().size();
                IStorageController[] controllers = vboxData.getStorageControllers().toArray(new IStorageController[numberOfControllers]);
                IMediumAttachment[] attachments = vboxData.getMediumAttachments().toArray(new IMediumAttachment[numberOfAttachments]);
                MachineStorageData storageSetup = new MachineStorageData(controllers, attachments);
                IVRDPServer vrdpServer = vboxData.getVRDPServer();
                VRDPSettings vrdpSetup = new VRDPSettings(vrdpServer.getEnabled(), vrdpServer.getPorts(), vrdpServer.getNetAddress(), vrdpServer.getAuthType(), vrdpServer.getAuthTimeout().intValue(), vrdpServer.getAllowMultiConnection(), vrdpServer.getReuseSingleConnection(), vrdpServer.getVideoChannel(), vrdpServer.getVideoChannelQuality().intValue());
                VideoSettings videoSetup = new VideoSettings(vboxData.getVRAMSize().intValue(), vboxData.getAccelerate3DEnabled(), vboxData.getAccelerate2DVideoEnabled());
                MachineDataComponent data = new MachineDataComponent(vboxData.getName(), vboxData.getOSTypeId(), vboxData.getDescription(), vboxData.getState(), vboxData.getSnapshotCount().intValue(), databaseData, audioSetup, biosSetup, bootSetup, cpuSetup, ioSetup, memorySetup, networksSetup, sharedFoldersSetup, storageSetup, vrdpSetup, videoSetup);
                result = new DataRetrievalResponse<MachineDataComponent>(data);
            } catch (WebServiceException e) {
                result = new DataRetrievalResponse<MachineDataComponent>(Functions.parseVirtualBoxException(e));
            }
        } else {
            ArrayList<VirtualResourceBean> machinesData = null;
            UserBean userData = sessionsManager.getUserData(request.getRequestorId());
            PermissionsBean userPermissions = databaseManager.getPermissions(request.getRequestorId(), request.getServerId());
            switch(request.getLimit()) {
                case NO_LIMIT:
                    {
                        machinesData = databaseManager.getVirtualResourcesByServer(request.getServerId(), VirtualResourceType.MACHINE);
                    }
                    break;
                case OWNER_ID:
                    {
                        if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || request.getRequestorId() == request.getOwnerId() || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                            machinesData = databaseManager.getVirtualResourcesByOwner(request.getOwnerId(), VirtualResourceType.MACHINE);
                        } else ;
                    }
                    break;
                default:
                    ;
                    break;
            }
            if (machinesData != null) {
                ArrayList<MachineDataComponent> data = new ArrayList<MachineDataComponent>();
                if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                    IMachine currentMachine = null;
                    for (VirtualResourceBean currentResource : machinesData) {
                        try {
                            currentMachine = vbox.getMachine(currentResource.getResourceId());
                            data.add(new MachineDataComponent(currentMachine.getName(), currentMachine.getOSTypeId(), currentMachine.getDescription(), currentMachine.getState(), currentResource));
                        } catch (WebServiceException e) {
                        }
                    }
                } else {
                    boolean isRequestAuthorized = false;
                    AuthorizationRequest authRequest = null;
                    IMachine currentMachine = null;
                    for (VirtualResourceBean currentResource : machinesData) {
                        authRequest = new AuthorizationRequest(request.getRequestorId(), request.getServerId(), request.getRequestedAction(), VirtualResourceType.MACHINE, currentResource.getResourceId());
                        isRequestAuthorized = permissionsManager.isActionAuthorized(authRequest);
                        if (isRequestAuthorized) {
                            try {
                                currentMachine = vbox.getMachine(currentResource.getResourceId());
                                data.add(new MachineDataComponent(currentMachine.getName(), currentMachine.getOSTypeId(), currentMachine.getDescription(), currentMachine.getState(), currentResource));
                            } catch (WebServiceException e) {
                            }
                        } else ;
                    }
                }
                result = new DataRetrievalResponse<MachineDataComponent>(data);
            } else {
                result = new DataRetrievalResponse<MachineDataComponent>(new ApplicationException(ExceptionMessages.APP_E_MACHINES_RETRIEVAL_FAILED));
            }
        }
        return result;
    }

    /**
     * Retrieves snapshots data.
     *
     * @param request snapshots data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<MachineSnapshotDataComponent> retrieveSnapshotsData(SnapshotsDataRetrievalRequest request) {
        DataRetrievalResponse<MachineSnapshotDataComponent> result = null;
        IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getServerId(), request.getRequestorId());
        if (vbox == null) return new DataRetrievalResponse<MachineSnapshotDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE)); else ;
        try {
            IMachine vboxData = vbox.getMachine(request.getResourceId());
            MachineSnapshotDataComponent data = null;
            try {
                data = new MachineSnapshotDataComponent(vboxData.getSnapshot(null));
                data.setCurrentSnapshot(vboxData.getCurrentSnapshot().getId());
            } catch (WebServiceException e) {
            }
            result = new DataRetrievalResponse<MachineSnapshotDataComponent>(data);
        } catch (WebServiceException e) {
            result = new DataRetrievalResponse<MachineSnapshotDataComponent>(Functions.parseVirtualBoxException(e));
        }
        return result;
    }

    /**
     * Retrieves medium/media data.
     *
     * @param request medium/media data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<MediumDataComponent> retrieveMediaData(MediaDataRetrievalRequest request) {
        DataRetrievalResponse<MediumDataComponent> result = null;
        IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getServerId(), request.getRequestorId());
        if (vbox == null) return new DataRetrievalResponse<MediumDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE)); else ;
        if (request.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) {
            try {
                IMedium vboxData = null;
                switch(request.getMediumDeviceType()) {
                    case HARD_DISK:
                        vboxData = vbox.getHardDisk(request.getResourceId());
                        break;
                    case DVD:
                        vboxData = vbox.getDVDImage(request.getResourceId());
                        break;
                    case FLOPPY:
                        vboxData = vbox.getFloppyImage(request.getResourceId());
                        break;
                    default:
                        ;
                        break;
                }
                if (vboxData != null) {
                    VirtualResourceBean databaseData = databaseManager.getVirtualResource(request.getResourceId(), request.getServerId(), VirtualResourceType.MEDIUM);
                    if (databaseData != null && databaseData.isValid()) {
                        vboxData.refreshState();
                        result = new DataRetrievalResponse<MediumDataComponent>(new MediumDataComponent(databaseData, vboxData));
                    } else {
                        result = new DataRetrievalResponse<MediumDataComponent>(new ApplicationException(ExceptionMessages.APP_E_MEDIUM_RETRIEVAL_FAILED));
                    }
                } else {
                    result = new DataRetrievalResponse<MediumDataComponent>(new ApplicationException(ExceptionMessages.APP_E_MEDIUM_RETRIEVAL_FAILED));
                }
            } catch (WebServiceException e) {
                result = new DataRetrievalResponse<MediumDataComponent>(Functions.parseVirtualBoxException(e));
            }
        } else {
            ArrayList<VirtualResourceBean> mediaData = null;
            UserBean userData = sessionsManager.getUserData(request.getRequestorId());
            PermissionsBean userPermissions = databaseManager.getPermissions(request.getRequestorId(), request.getServerId());
            switch(request.getLimit()) {
                case NO_LIMIT:
                    {
                        mediaData = databaseManager.getVirtualResourcesByServer(request.getServerId(), VirtualResourceType.MEDIUM);
                    }
                    break;
                case OWNER_ID:
                    {
                        if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || request.getRequestorId() == request.getOwnerId() || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                            mediaData = databaseManager.getVirtualResourcesByOwner(request.getOwnerId(), VirtualResourceType.MEDIUM);
                        } else ;
                    }
                    break;
                default:
                    ;
                    break;
            }
            if (mediaData != null) {
                ArrayList<MediumDataComponent> data = new ArrayList<MediumDataComponent>();
                if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                    IMedium currentMedium = null;
                    for (VirtualResourceBean currentResource : mediaData) {
                        try {
                            switch(request.getMediumDeviceType()) {
                                case HARD_DISK:
                                    currentMedium = vbox.getHardDisk(currentResource.getResourceId());
                                    break;
                                case DVD:
                                    currentMedium = vbox.getDVDImage(currentResource.getResourceId());
                                    break;
                                case FLOPPY:
                                    currentMedium = vbox.getFloppyImage(currentResource.getResourceId());
                                    break;
                                default:
                                    ;
                                    break;
                            }
                            if (currentMedium != null) {
                                data.add(new MediumDataComponent(currentResource, currentMedium.getName(), currentMedium.getDescription(), currentMedium.getState()));
                            } else ;
                        } catch (WebServiceException e) {
                        }
                        currentMedium = null;
                    }
                } else {
                    boolean isRequestAuthorized = false;
                    AuthorizationRequest authRequest = null;
                    IMedium currentMedium = null;
                    for (VirtualResourceBean currentResource : mediaData) {
                        authRequest = new AuthorizationRequest(request.getRequestorId(), request.getServerId(), request.getRequestedAction(), VirtualResourceType.MEDIUM, currentResource.getResourceId());
                        isRequestAuthorized = permissionsManager.isActionAuthorized(authRequest);
                        if (isRequestAuthorized) {
                            try {
                                switch(request.getMediumDeviceType()) {
                                    case HARD_DISK:
                                        currentMedium = vbox.getHardDisk(currentResource.getResourceId());
                                        break;
                                    case DVD:
                                        currentMedium = vbox.getDVDImage(currentResource.getResourceId());
                                        break;
                                    case FLOPPY:
                                        currentMedium = vbox.getFloppyImage(currentResource.getResourceId());
                                        break;
                                    default:
                                        ;
                                        break;
                                }
                                if (currentMedium != null) {
                                    data.add(new MediumDataComponent(currentResource, currentMedium.getName(), currentMedium.getDescription(), currentMedium.getState()));
                                } else ;
                            } catch (WebServiceException e) {
                            }
                        } else ;
                        currentMedium = null;
                    }
                }
                result = new DataRetrievalResponse<MediumDataComponent>(data);
            } else {
                result = new DataRetrievalResponse<MediumDataComponent>(new ApplicationException(ExceptionMessages.APP_E_MEDIA_RETRIEVAL_FAILED));
            }
        }
        return result;
    }

    /**
     * Retrieves network(s) data.
     *
     * @param request network(s) data retrieval request
     * @return a response to the data retrieval request
     */
    private DataRetrievalResponse<NetworkDataComponent> retrieveNetworksData(NetworksDataRetrievalRequest request) {
        DataRetrievalResponse<NetworkDataComponent> result = null;
        IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getServerId(), request.getRequestorId());
        if (vbox == null) return new DataRetrievalResponse<NetworkDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE)); else ;
        if (request.getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) {
            try {
                VirtualResourceBean databaseData = databaseManager.getVirtualResource(request.getResourceId(), request.getServerId(), VirtualResourceType.NETWORK);
                switch(request.getNetworkType()) {
                    case HOST_ONLY:
                        {
                            IHostNetworkInterface networkInterface = vbox.getHost().findHostNetworkInterfaceById(request.getResourceId());
                            DHCPServerConfiguration dhcpConfig = null;
                            try {
                                IDHCPServer dhcpServer = vbox.findDHCPServerByNetworkName(networkInterface.getNetworkName());
                                dhcpConfig = new DHCPServerConfiguration(dhcpServer.getIPAddress(), dhcpServer.getNetworkMask(), dhcpServer.getLowerIP(), dhcpServer.getUpperIP(), dhcpServer.getEnabled());
                            } catch (WebServiceException e) {
                            }
                            HostNetworkConfiguration networkConfig = new HostNetworkConfiguration(networkInterface, dhcpConfig, databaseData);
                            result = new DataRetrievalResponse<NetworkDataComponent>(new NetworkDataComponent(networkConfig));
                        }
                        break;
                    case INTERNAL:
                        {
                            InternalNetworkBean network = databaseManager.getInternalNetwork(request.getResourceId(), request.getServerId());
                            DHCPServerConfiguration dhcpConfig = null;
                            if (network.isDHCPAvailable()) {
                                try {
                                    IDHCPServer dhcpServer = vbox.findDHCPServerByNetworkName(network.getNetworkId());
                                    dhcpConfig = new DHCPServerConfiguration(dhcpServer.getIPAddress(), dhcpServer.getNetworkMask(), dhcpServer.getLowerIP(), dhcpServer.getUpperIP(), dhcpServer.getEnabled());
                                } catch (WebServiceException e) {
                                }
                            } else ;
                            InternalNetworkConfiguration networkConfig = new InternalNetworkConfiguration(network, dhcpConfig, databaseData);
                            result = new DataRetrievalResponse<NetworkDataComponent>(new NetworkDataComponent(networkConfig));
                        }
                        break;
                    default:
                        {
                            result = new DataRetrievalResponse<NetworkDataComponent>(new ApplicationException(ExceptionMessages.APP_E_NETWORK_TYPE_INVALID));
                        }
                        break;
                }
            } catch (WebServiceException e) {
                result = new DataRetrievalResponse<NetworkDataComponent>(Functions.parseVirtualBoxException(e));
            }
        } else {
            ArrayList<VirtualResourceBean> networksData = null;
            UserBean userData = sessionsManager.getUserData(request.getRequestorId());
            PermissionsBean userPermissions = databaseManager.getPermissions(request.getRequestorId(), request.getServerId());
            switch(request.getLimit()) {
                case NO_LIMIT:
                    {
                        networksData = databaseManager.getVirtualResourcesByServer(request.getServerId(), VirtualResourceType.NETWORK);
                    }
                    break;
                case OWNER_ID:
                    {
                        if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || request.getRequestorId() == request.getOwnerId() || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                            networksData = databaseManager.getVirtualResourcesByOwner(request.getOwnerId(), VirtualResourceType.NETWORK);
                        } else ;
                    }
                    break;
                default:
                    ;
                    break;
            }
            if (networksData != null) {
                ArrayList<NetworkDataComponent> data = new ArrayList<NetworkDataComponent>();
                if (userData.getAccountLevel() >= Constants.DB_USER_ACCOUNT_LEVEL_OWNER || Functions.isUserServerManager(request.getRequestorId(), userPermissions)) {
                    for (VirtualResourceBean currentResource : networksData) {
                        try {
                            switch(request.getNetworkType()) {
                                case HOST_ONLY:
                                    {
                                        IHostNetworkInterface network = vbox.getHost().findHostNetworkInterfaceById(currentResource.getResourceId());
                                        data.add(new NetworkDataComponent(network.getName(), request.getNetworkType(), currentResource, network.getInterfaceType()));
                                    }
                                    break;
                                case INTERNAL:
                                    {
                                        InternalNetworkBean network = databaseManager.getInternalNetwork(currentResource.getResourceId(), request.getServerId());
                                        if (network != null) {
                                            data.add(new NetworkDataComponent(network.getNetworkName(), request.getNetworkType(), currentResource, null));
                                        } else ;
                                    }
                                    break;
                                default:
                                    ;
                                    break;
                            }
                        } catch (WebServiceException e) {
                        }
                    }
                } else {
                    boolean isRequestAuthorized = false;
                    AuthorizationRequest authRequest = null;
                    for (VirtualResourceBean currentResource : networksData) {
                        authRequest = new AuthorizationRequest(request.getRequestorId(), request.getServerId(), request.getRequestedAction(), VirtualResourceType.NETWORK, currentResource.getResourceId());
                        isRequestAuthorized = permissionsManager.isActionAuthorized(authRequest);
                        if (isRequestAuthorized) {
                            switch(request.getNetworkType()) {
                                case HOST_ONLY:
                                    {
                                        try {
                                            IHostNetworkInterface network = vbox.getHost().findHostNetworkInterfaceById(currentResource.getResourceId());
                                            data.add(new NetworkDataComponent(network.getName(), request.getNetworkType(), currentResource, network.getInterfaceType()));
                                        } catch (WebServiceException e) {
                                        }
                                    }
                                    break;
                                case INTERNAL:
                                    {
                                        InternalNetworkBean network = databaseManager.getInternalNetwork(currentResource.getResourceId(), request.getServerId());
                                        if (network != null) {
                                            data.add(new NetworkDataComponent(network.getNetworkName(), request.getNetworkType(), currentResource, null));
                                        } else ;
                                    }
                                    break;
                                default:
                                    ;
                                    break;
                            }
                        } else ;
                    }
                }
                result = new DataRetrievalResponse<NetworkDataComponent>(data);
            } else {
                result = new DataRetrievalResponse<NetworkDataComponent>(new ApplicationException(ExceptionMessages.APP_E_NETWORKS_RETRIEVAL_FAILED));
            }
        }
        return result;
    }

    /**
     * Retrieves various data.
     *
     * @param request data retrieval request
     * @return a response to the retrieval request
     */
    private DataRetrievalResponse<MiscDataComponent> retrieveMiscellaneousData(MiscDataRetrievalRequest request) {
        DataRetrievalResponse<MiscDataComponent> result = null;
        VirtualResourceType resourceType = null;
        switch(request.getRequestType()) {
            case GUEST_OS_TYPES:
                resourceType = VirtualResourceType.MACHINE;
                break;
            case MEDIUM_FORMATS:
                resourceType = VirtualResourceType.MEDIUM;
                break;
            default:
                resourceType = VirtualResourceType.INVALID;
                break;
        }
        boolean isActionAuthorized = permissionsManager.isActionAuthorized(new SimpleAuthorizationRequest(request.getRequestorId(), request.getResourceId(), request.getRequestedAction(), resourceType));
        if (isActionAuthorized) {
            IVirtualBox vbox = connectionsManager.getVirtualBoxReference(request.getResourceId(), request.getRequestorId());
            if (vbox == null) return new DataRetrievalResponse<MiscDataComponent>(new ApplicationException(ExceptionMessages.APP_E_VBOX_REF_NOT_AVAILABLE)); else ;
            try {
                MiscDataComponent data = null;
                switch(request.getRequestType()) {
                    case GUEST_OS_TYPES:
                        {
                            List<IGuestOSType> osTypes = vbox.getGuestOSTypes();
                            data = new MiscDataComponent(osTypes.toArray(new IGuestOSType[osTypes.size()]));
                        }
                        break;
                    case MEDIUM_FORMATS:
                        {
                            List<IMediumFormat> mediumFormats = vbox.getSystemProperties().getMediumFormats();
                            data = new MiscDataComponent(mediumFormats.toArray(new IMediumFormat[mediumFormats.size()]));
                        }
                        break;
                    default:
                        ;
                        break;
                }
                if (data != null) result = new DataRetrievalResponse<MiscDataComponent>(data); else {
                    result = new DataRetrievalResponse<MiscDataComponent>(new ApplicationException(ExceptionMessages.APP_E_MISC_DATA_RETRIEVAL_FAILED));
                }
            } catch (WebServiceException e) {
                result = new DataRetrievalResponse<MiscDataComponent>(Functions.parseVirtualBoxException(e));
            }
        } else result = new DataRetrievalResponse<MiscDataComponent>(new ApplicationException(ExceptionMessages.APP_E_OPERATION_NOT_ALLOWED));
        return result;
    }
}
