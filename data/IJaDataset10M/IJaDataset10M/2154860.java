package com.dokumentarchiv.plugins.alfresco;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.xml.rpc.ServiceException;
import org.alfresco.util.ISO8601DateFormat;
import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.ActionFault;
import org.alfresco.webservice.action.ActionServiceLocator;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.Condition;
import org.alfresco.webservice.action.Rule;
import org.alfresco.webservice.authentication.AuthenticationResult;
import org.alfresco.webservice.authentication.AuthenticationService;
import org.alfresco.webservice.authentication.AuthenticationServiceLocator;
import org.alfresco.webservice.authentication.AuthenticationServiceSoapPort;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceLocator;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceLocator;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.encoding.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import com.dokumentarchiv.ServerControlClient;
import com.dokumentarchiv.plugins.IArchive;
import com.dokumentarchiv.plugins.PluginHelper;
import com.dokumentarchiv.search.ListEntry;
import com.dokumentarchiv.search.Search;
import com.dokumentarchiv.search.SearchExpression;
import com.dokumentarchiv.search.SearchFunction;
import de.inovox.AdvancedMimeMessage;

/**
 * Plugin for Alfresco support
 * 
 * @author Carsten Burghardt
 * @version $Id: AlfrescoPlugin.java 615 2008-03-12 21:51:55Z carsten $
 */
public class AlfrescoPlugin extends Plugin implements IArchive {

    /**
     * serial id
     */
    private static final long serialVersionUID = 2757413493262568253L;

    private static Log log = LogFactory.getLog(AlfrescoPlugin.class);

    private String baseUrl;

    /** current ticket */
    private String ticket;

    /** AuthenticationService */
    private AuthenticationServiceSoapPort authService;

    /** ContentService */
    private ContentServiceSoapBindingStub contentService;

    /** RepositoryService */
    private RepositoryServiceSoapBindingStub repositoryService;

    /** ActionService */
    private ActionServiceSoapBindingStub actionService;

    /** store and reference to the space */
    private Store store;

    private Reference rootFolder;

    private ParentReference parentReference;

    /** WS security information */
    public static final String WS_SECURITY_INFO = "<deployment xmlns='http://xml.apache.org/axis/wsdd/' xmlns:java='http://xml.apache.org/axis/wsdd/providers/java'>" + "   <transport name='http' pivot='java:org.apache.axis.transport.http.HTTPSender'/>" + "   <globalConfiguration >" + "      <requestFlow >" + "        <handler type='java:org.apache.ws.axis.security.WSDoAllSender' >" + "               <parameter name='action' value='UsernameToken Timestamp'/>" + "               <parameter name='user' value='ticket'/>" + "               <parameter name='passwordCallbackClass' value='com.dokumentarchiv.plugins.alfresco.SecurityHandler'/>" + "               <parameter name='passwordType' value='PasswordText'/>" + "         </handler>" + "         <handler name='cookieHandler' type='java:com.dokumentarchiv.plugins.alfresco.CookieHandler' />" + "       </requestFlow >" + "   </globalConfiguration>" + "</deployment>";

    /** Our namespace */
    private static final String INOVOX_NS = "{http://www.inovox.de/model/1.0}";

    /** Our content type */
    private static final String CONTENTTYPE = INOVOX_NS + "email";

    /** Default encoding */
    private static String ENCODING = "UTF-8";

    /** The name of our action */
    private static String ACTION_NAME = "decode-metadata";

    /** Configuration */
    private Configuration config;

    /** Folder name - Reference */
    private Map folderNameToReference;

    /** Create a folder for each TO recipient */
    private boolean createUserFolder;

    /** Consider all users */
    private boolean considerAllUsers;

    /** List of domains that are local */
    private List myUserDomains = new Vector();

    /** Fallback to the sender */
    private boolean useSender;

    private boolean base64encode;

    /**
     * Constructor
     */
    public AlfrescoPlugin() {
        super();
    }

    protected void doStart() throws Exception {
        folderNameToReference = new HashMap();
        try {
            URL configUrl = getManager().getPathResolver().resolvePath(getDescriptor(), CONFIGNAME);
            this.config = new PropertiesConfiguration(configUrl);
        } catch (ConfigurationException e) {
            log.error("Can not read properties", e);
            getManager().disablePlugin(getDescriptor());
            return;
        }
        createUserFolder = config.getBoolean("createUserFolder", false);
        baseUrl = config.getString("baseurl");
        String storeName = config.getString("store");
        String spaceName = config.getString("space");
        base64encode = config.getBoolean("base64.encoding.enabled", true);
        AuthenticationService auth = new AuthenticationServiceLocator();
        URL authUrl = new URL(baseUrl + "AuthenticationService");
        authService = auth.getAuthenticationService(authUrl);
        try {
            login();
        } catch (Exception e) {
            log.error("Failed to authenticate please check username and password", e);
            getManager().disablePlugin(getDescriptor());
            return;
        }
        contentService = getContentWebService();
        repositoryService = getRepositoryWebService();
        actionService = getActionWebService();
        store = new Store(Constants.WORKSPACE_STORE, storeName);
        log.debug("Store:" + store.getAddress());
        String path = "/app:company_home/cm:" + spaceName;
        Reference ref = new Reference(store, null, path);
        Predicate predicate = new Predicate(new Reference[] { ref }, store, null);
        Node[] nodes = null;
        try {
            nodes = repositoryService.get(predicate);
        } catch (Exception e) {
            log.fatal("Can not find the given space:" + path);
            getManager().disablePlugin(getDescriptor());
            return;
        }
        if (nodes == null || nodes.length == 0) {
            log.fatal("Can not find the given space:" + path);
            getManager().disablePlugin(getDescriptor());
            return;
        }
        rootFolder = nodes[0].getReference();
        log.debug("Space:" + rootFolder.getUuid());
        if (createUserFolder) {
            log.debug("Fetching children of:" + spaceName);
            fetchFolders();
            myUserDomains = config.getList("createUserFolder.myUserDomains");
            if (myUserDomains != null) {
                if (myUserDomains.size() == 1) {
                    String first = (String) myUserDomains.get(0);
                    if (StringUtils.isEmpty(first)) {
                        myUserDomains.clear();
                    }
                }
                if (!myUserDomains.isEmpty()) {
                    log.debug("My domains:" + myUserDomains);
                }
            } else {
                myUserDomains = new Vector();
            }
            considerAllUsers = config.getBoolean("createUserFolder.considerAllUsers", true);
            useSender = config.getBoolean("createUserFolder.useSenderFirst", false);
        }
        parentReference = new ParentReference();
        parentReference.setAssociationType(Constants.ASSOC_CONTAINS);
        parentReference.setStore(store);
        parentReference.setUuid(rootFolder.getUuid());
        if (base64encode) {
            createRule(parentReference);
        }
        log.info("Connected to:" + baseUrl);
    }

    /**
     * Create a rule with our custom action for the specified reference
     * @throws RemoteException
     * @throws ActionFault
     */
    private void createRule(Reference reference) throws RemoteException, ActionFault {
        boolean createRule = true;
        Rule[] rules = actionService.getRules(reference, null);
        if (rules != null && rules.length > 0) {
            for (int i = 0; i < rules.length; ++i) {
                Rule rule = rules[i];
                if (rule.getAction().getActionName().equals(ACTION_NAME)) {
                    createRule = false;
                    break;
                }
            }
        }
        if (!createRule) {
            log.debug("Rule already present for " + reference.getUuid());
            return;
        }
        Condition condition = new Condition();
        condition.setId(UUID.randomUUID().toString());
        condition.setConditionName("is-subtype");
        condition.setInvertCondition(false);
        NamedValue content = new NamedValue("type", false, CONTENTTYPE, null);
        condition.setParameters(new NamedValue[] { content });
        Action metadataAction = new Action();
        metadataAction.setActionName(ACTION_NAME);
        metadataAction.setTitle("Decode metadata");
        metadataAction.setId(UUID.randomUUID().toString());
        metadataAction.setConditions(new Condition[] { condition });
        metadataAction.setParameters(new NamedValue[] { new NamedValue("dummy", false, "Dummy", null) });
        Rule rule = new Rule();
        rule.setRuleTypes(new String[] { "inbound" });
        rule.setTitle("Decode metadata");
        rule.setAction(metadataAction);
        actionService.saveRules(reference, new Rule[] { rule });
        log.debug("Created rule for folder " + reference.getUuid());
    }

    /**
     * Do the login of the configured user Username and password is taken from
     * the config
     * 
     * @throws Exception
     */
    private void login() throws Exception {
        String username = config.getString("username");
        String password = ServerControlClient.decrypt(config.getString("password"));
        log.debug("Starting session for user:" + username);
        AuthenticationResult authResult = authService.startSession(username, password);
        ticket = authResult.getTicket();
        CookieHandler.setSessionId(authResult.getSessionid());
        log.debug("Authenticated ticket:" + ticket);
        SecurityHandler.setTicket(ticket);
    }

    /**
     * Logout
     */
    private void logout() {
        if (authService != null) {
            try {
                authService.endSession(ticket);
                log.debug("Logged out ticket:" + ticket);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * @param spaceName
     * @throws RemoteException
     * @throws RepositoryFault
     */
    private void fetchFolders() throws RemoteException, RepositoryFault {
        folderNameToReference.clear();
        QueryResult results = repositoryService.queryChildren(rootFolder);
        ResultSet set = results.getResultSet();
        ResultSetRow[] rows = set.getRows();
        if (rows == null) {
            log.info("No children found");
        } else {
            for (int i = 0; i < rows.length; ++i) {
                ResultSetRow row = rows[i];
                ResultSetRowNode node = row.getNode();
                NamedValue[] columns = row.getColumns();
                String folderName = null;
                for (int j = 0; j < columns.length; ++j) {
                    NamedValue prop = columns[j];
                    if (prop.getName().equals(Constants.PROP_CONTENT)) {
                        folderName = null;
                        break;
                    }
                    if (prop.getName().equals(Constants.PROP_NAME)) {
                        folderName = prop.getValue();
                    }
                }
                if (folderName != null) {
                    ParentReference parent = new ParentReference();
                    parent.setAssociationType(Constants.ASSOC_CONTAINS);
                    parent.setStore(store);
                    parent.setUuid(node.getId());
                    folderNameToReference.put(folderName, parent);
                    log.debug("Registered folder:" + folderName);
                    if (base64encode) {
                        createRule(parent);
                    }
                }
            }
        }
    }

    /**
     * Get the respository web service.
     * 
     * @return the respository web service
     * @throws ServiceException
     *             Service Exception
     * @throws MalformedURLException 
     */
    private RepositoryServiceSoapBindingStub getRepositoryWebService() throws ServiceException, MalformedURLException {
        EngineConfiguration config = new FileProvider(new ByteArrayInputStream(WS_SECURITY_INFO.getBytes()));
        RepositoryServiceLocator repositoryServiceLocator = new RepositoryServiceLocator(config);
        URL url = new URL(baseUrl + repositoryServiceLocator.getRepositoryServiceWSDDServiceName());
        RepositoryServiceSoapBindingStub repositoryService = (RepositoryServiceSoapBindingStub) repositoryServiceLocator.getRepositoryService(url);
        repositoryService.setMaintainSession(true);
        return repositoryService;
    }

    /**
     * Get the content web service
     * 
     * @return content web service
     * @throws ServiceException
     * @throws MalformedURLException 
     */
    private ContentServiceSoapBindingStub getContentWebService() throws ServiceException, MalformedURLException {
        EngineConfiguration config = new FileProvider(new ByteArrayInputStream(WS_SECURITY_INFO.getBytes()));
        ContentServiceLocator contentServiceLocator = new ContentServiceLocator(config);
        URL url = new URL(baseUrl + contentServiceLocator.getContentServiceWSDDServiceName());
        ContentServiceSoapBindingStub contentService = (ContentServiceSoapBindingStub) contentServiceLocator.getContentService(url);
        contentService.setMaintainSession(true);
        return contentService;
    }

    private ActionServiceSoapBindingStub getActionWebService() throws ServiceException, MalformedURLException {
        EngineConfiguration config = new FileProvider(new ByteArrayInputStream(WS_SECURITY_INFO.getBytes()));
        ActionServiceLocator actionServiceLocator = new ActionServiceLocator(config);
        URL url = new URL(baseUrl + actionServiceLocator.getActionServiceWSDDServiceName());
        ActionServiceSoapBindingStub actionService = (ActionServiceSoapBindingStub) actionServiceLocator.getActionService(url);
        actionService.setMaintainSession(true);
        return actionService;
    }

    protected void doStop() throws Exception {
        logout();
    }

    public boolean archiveEMail(AdvancedMimeMessage msg) {
        try {
            String subject = decodeMimeString(msg.getHeader("Subject", null));
            byte[] bytes = msg.getBytes();
            StringBuffer buf = new StringBuffer();
            buf.append(subject);
            buf.append("-");
            buf.append(generateUid(bytes));
            String name = buf.toString();
            String contentType = msg.getContentType();
            String charset = ENCODING;
            if (contentType.indexOf("charset") != -1) {
                int start = contentType.indexOf("charset");
                if (contentType.indexOf("\"", start) != -1) {
                    start = contentType.indexOf("\"", start);
                    charset = contentType.substring(start + 1, contentType.lastIndexOf("\""));
                } else {
                    charset = contentType.substring(contentType.indexOf("=", start) + 1);
                }
                charset = charset.trim();
            }
            log.debug("Using charset:" + charset);
            NamedValue[] props = getProperties(msg, name);
            List parents = getParentFolders(msg);
            Iterator it = parents.iterator();
            while (it.hasNext()) {
                ParentReference parent = (ParentReference) it.next();
                parent.setChildName(name);
                CMLCreate create = new CMLCreate(name, parent, null, null, null, CONTENTTYPE, props);
                CML cml = new CML();
                cml.setCreate(new CMLCreate[] { create });
                UpdateResult[] result = repositoryService.update(cml);
                if (result.length > 0) {
                    Reference newContentNode = result[0].getDestination();
                    String mimeType = msg.getMimeType();
                    if (msg.isCompressData()) {
                        mimeType = "message/rfc822-zip";
                    }
                    ContentFormat format = new ContentFormat(mimeType, charset);
                    contentService.write(newContentNode, Constants.PROP_CONTENT, bytes, format);
                }
            }
        } catch (Exception e) {
            log.error("Error writing message", e);
            boolean success = false;
            if (recoverSessionIfNecessary()) {
                success = archiveEMail(msg);
            }
            return success;
        }
        return true;
    }

    /**
     * Checks if we have to renew the ticket and session
     * @return if a new session has been successfully established
     */
    private boolean recoverSessionIfNecessary() {
        boolean recover = false;
        try {
            Predicate predicate = new Predicate(new Reference[] { rootFolder }, store, null);
            repositoryService.get(predicate);
        } catch (Exception e) {
            recover = true;
        }
        if (recover) {
            log.debug("Trying to login again");
            try {
                login();
                log.info("Logged in again");
                return true;
            } catch (Exception e1) {
                log.info("Failed to login");
            }
        }
        return false;
    }

    /**
     * Get properties of the message
     * 
     * @param msg
     * @return
     * @throws MessagingException
     * @throws NoSuchAlgorithmException
     */
    private NamedValue[] getProperties(MimeMessage msg, String uid) throws MessagingException, NoSuchAlgorithmException {
        NamedValue[] props = new NamedValue[6];
        String value = msg.getHeader("Subject", null);
        value = decodeMimeString(value);
        props[0] = Utils.createNamedValue(INOVOX_NS + ISearchField.SUBJECT, value);
        log.debug("Subject:" + value);
        props[1] = Utils.createNamedValue(Constants.PROP_TITLE, value);
        value = PluginHelper.addressToString(msg.getFrom());
        value = decodeMimeString(value);
        props[2] = Utils.createNamedValue(INOVOX_NS + ISearchField.FROM, value);
        log.debug("From:" + value);
        String[] values = PluginHelper.adressToStringArray(msg.getRecipients(Message.RecipientType.TO));
        for (int i = 0; i < values.length; ++i) {
            values[i] = decodeMimeString(values[i]);
        }
        props[3] = Utils.createNamedValue(INOVOX_NS + ISearchField.TO, values);
        log.debug("To:" + StringUtils.join(values, ","));
        props[4] = Utils.createNamedValue(Constants.PROP_NAME, uid);
        log.debug("Name:" + uid);
        Date sent = msg.getSentDate();
        String sentStr = ISO8601DateFormat.format(sent);
        props[5] = Utils.createNamedValue(INOVOX_NS + ISearchField.DATE, sentStr);
        log.debug("Date:" + sentStr);
        return props;
    }

    /**
     * @param input
     * @return
     */
    private String decodeMimeString(String input) {
        if (!base64encode) {
            return input;
        }
        try {
            String decoded = MimeUtility.decodeText(input);
            decoded = Base64.encode(decoded.getBytes());
            return decoded;
        } catch (Exception e) {
            log.error("Failed to decode", e);
        }
        return input;
    }

    /**
     * Generate a new unique id as filename with extension .msg
     * 
     * @param bytes
     * @return unique id
     * @throws NoSuchAlgorithmException
     */
    private String generateUid(byte[] bytes) throws NoSuchAlgorithmException {
        String docid = null;
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] digest = sha.digest(bytes);
        docid = PluginHelper.hexEncode(digest);
        return docid;
    }

    private synchronized List getParentFolders(MimeMessage msg) throws MessagingException, RemoteException {
        List folders = new ArrayList();
        if (!createUserFolder) {
            folders.add(parentReference);
            return folders;
        }
        List addresses = new ArrayList();
        if (useSender) {
            Address[] senders = msg.getFrom();
            if (senders != null && senders.length > 0 && addressIsLocal(senders[0])) {
                addresses.add(senders[0]);
            } else {
                Address[] toList = msg.getRecipients(Message.RecipientType.TO);
                if (considerAllUsers) {
                    addresses.addAll(Arrays.asList(toList));
                } else {
                    addresses.add(toList[0]);
                }
            }
        } else {
            Address[] toList = msg.getRecipients(Message.RecipientType.TO);
            if (considerAllUsers) {
                addresses.addAll(Arrays.asList(toList));
            } else {
                addresses.add(toList[0]);
            }
        }
        for (int i = 0; i < addresses.size(); ++i) {
            Address address = (Address) addresses.get(i);
            String folderName = getAddress(address);
            if (!myUserDomains.isEmpty()) {
                String domain = getDomainFromAddress(address);
                if (domain != null) {
                    if (!myUserDomains.contains(domain)) {
                        if (!folders.contains(parentReference)) {
                            folders.add(parentReference);
                        }
                        continue;
                    }
                }
            }
            folderName = escapeName(folderName);
            folderName = folderName.trim();
            if (folderNameToReference.containsKey(folderName)) {
                ParentReference ref = (ParentReference) folderNameToReference.get(folderName);
                if (!folders.contains(ref)) {
                    folders.add(ref);
                }
            } else {
                log.debug("Creating folder:" + folderName);
                NamedValue[] properties = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME, folderName) };
                parentReference.setChildName(folderName);
                CMLCreate create = new CMLCreate("1", parentReference, null, null, null, Constants.TYPE_FOLDER, properties);
                CML cml = new CML();
                cml.setCreate(new CMLCreate[] { create });
                UpdateResult[] results;
                try {
                    results = repositoryService.update(cml);
                    if (results != null && results.length > 0) {
                        Reference folder = results[0].getDestination();
                        if (base64encode) {
                            createRule(folder);
                        }
                        ParentReference parent = new ParentReference();
                        parent.setAssociationType(Constants.ASSOC_CONTAINS);
                        parent.setStore(store);
                        parent.setUuid(folder.getUuid());
                        folderNameToReference.put(folderName, parent);
                        folders.add(parent);
                        log.debug("Creating folder done");
                    }
                } catch (RepositoryFault e) {
                    log.warn("Could not create folder, refreshing children");
                    fetchFolders();
                    if (folderNameToReference.containsKey(folderName)) {
                        folders.add(folderNameToReference.get(folderName));
                    }
                }
            }
        }
        return folders;
    }

    private boolean addressIsLocal(Address address) {
        if (!myUserDomains.isEmpty()) {
            String domain = getDomainFromAddress(address);
            if (domain != null) {
                if (!myUserDomains.contains(domain)) {
                    log.debug(domain + " is not in myUserDomains");
                    return false;
                } else {
                    log.debug(domain + " is a local domain");
                    return true;
                }
            }
        }
        return false;
    }

    private String getDomainFromAddress(Address address) {
        String addressString = getAddress(address);
        int start = addressString.indexOf("@");
        if (start != -1) {
            String domain = addressString.substring(start + 1, addressString.length());
            if (StringUtils.isEmpty(domain)) {
                return null;
            }
            return domain;
        }
        return null;
    }

    private String getAddress(Address address) {
        String addressString = address.toString();
        if (addressString.indexOf("<") != -1) {
            addressString = StringUtils.substringAfter(addressString, "<");
        }
        if (addressString.indexOf(">") != -1) {
            addressString = StringUtils.substringBefore(addressString, ">");
        }
        return addressString;
    }

    public List findDocuments(Search search) {
        Vector result = new Vector();
        try {
            Query query = getQuery(search);
            QueryResult qresult = repositoryService.query(store, query, false);
            ResultSet set = qresult.getResultSet();
            log.debug("RowCount:" + set.getTotalRowCount());
            if (set.getTotalRowCount() == 0) {
                return result;
            }
            ResultSetRow[] rows = set.getRows();
            for (int i = 0; i < rows.length; ++i) {
                ResultSetRow row = rows[i];
                ListEntry entry = new ListEntry();
                entry.setId(row.getNode().getId());
                NamedValue[] values = row.getColumns();
                for (int j = 0; j < values.length; ++j) {
                    NamedValue properties = values[j];
                    String name = properties.getName();
                    if (name.equals(INOVOX_NS + ISearchField.SUBJECT)) {
                        entry.setSubject(properties.getValue());
                    } else if (name.equals(INOVOX_NS + ISearchField.FROM)) {
                        entry.setFrom(properties.getValue());
                    } else if (name.equals(INOVOX_NS + ISearchField.TO)) {
                        String joined = StringUtils.join(properties.getValues(), ",");
                        entry.setTo(joined);
                    } else if (name.equals(INOVOX_NS + ISearchField.DATE)) {
                        String value = properties.getValue();
                        Date date = ISO8601DateFormat.parse(value);
                        entry.setDate(date);
                    }
                }
                result.add(entry);
            }
        } catch (Exception e) {
            log.error("Error in search", e);
            if (recoverSessionIfNecessary()) {
                return findDocuments(search);
            }
        }
        return result;
    }

    /**
     * Construct a query from the search
     * 
     * @param search
     * @return
     * @throws ParseException
     */
    private Query getQuery(Search search) throws ParseException {
        Query query = new Query();
        StringBuffer buffer = new StringBuffer();
        Iterator it = search.getChildren().iterator();
        String operator = search.getOperator().equals(Search.OPAND) ? " AND " : " OR ";
        while (it.hasNext()) {
            SearchExpression ex = (SearchExpression) it.next();
            String field = ex.getField().trim();
            String ns = field.equals(ISearchField.CONTENT) ? Constants.NAMESPACE_CONTENT_MODEL : INOVOX_NS;
            if (field.equals(ISearchField.CONTENT)) {
                buffer.append("TEXT");
            } else {
                buffer.append("\\@");
                buffer.append(escapeQName(ns + field));
            }
            buffer.append(":");
            if (ex.getFunction() == SearchFunction.EQUALS) {
                buffer.append("\"");
            }
            if (field.equals(ISearchField.DATE)) {
                Date date = DateFormat.getDateInstance().parse(ex.getValue());
                buffer.append(ISO8601DateFormat.format(date));
            } else {
                buffer.append(ex.getValue());
            }
            if (ex.getFunction() == SearchFunction.CONTAINS) {
                buffer.append("*");
            } else {
                buffer.append("\"");
            }
            if (it.hasNext()) {
                buffer.append(operator);
            }
        }
        String queryString = buffer.toString();
        log.debug("Query:" + queryString);
        query.setStatement(queryString);
        query.setLanguage(Constants.QUERY_LANG_LUCENE);
        return query;
    }

    /**
     * Escape a QName value so it can be used in lucene search strings
     * 
     * @param qName
     *            QName to escape
     * 
     * @return escaped value
     */
    private String escapeQName(String original) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            if ((c == '{') || (c == '}') || (c == ':') || (c == '-')) {
                buf.append('\\');
            }
            buf.append(c);
        }
        return buf.toString();
    }

    private String escapeName(String orig) {
        if (orig == null || orig.length() == 0) {
            return "";
        }
        String escaped = orig.replaceAll(":", "_");
        escaped = escaped.replaceAll("/", "_");
        escaped = escaped.replaceAll("\\?", "_");
        escaped = escaped.replaceAll("\\|", "_");
        escaped = escaped.replaceAll("<", "_");
        escaped = escaped.replaceAll(">", "_");
        escaped = escaped.replaceAll("\"", "_");
        escaped = escaped.replaceAll("\\*", "_");
        escaped = escaped.replaceAll(";", "_");
        escaped = escaped.replaceAll("%", "_");
        escaped = escaped.replaceAll("&", "_");
        escaped = escaped.replaceAll("\\+", "_");
        return escaped;
    }

    public InputStream getDocumentByID(String id) {
        Reference ref = new Reference(store, id, null);
        Predicate predicate = new Predicate(new Reference[] { ref }, store, null);
        try {
            Content[] result = contentService.read(predicate, Constants.PROP_CONTENT);
            Content content = result[0];
            String target = content.getUrl() + "?ticket=" + ticket;
            URL url = new URL(target);
            log.debug("Retrieving:" + url.getPath());
            return url.openConnection().getInputStream();
        } catch (Exception e) {
            log.error("Error reading content", e);
            if (recoverSessionIfNecessary()) {
                return getDocumentByID(id);
            }
        }
        return null;
    }

    public HashMap getSupportedFunctions() {
        HashMap map = new HashMap();
        Vector functions = new Vector();
        functions.add(SearchFunction.getStringForFunction(SearchFunction.CONTAINS));
        map.put(ISearchField.CONTENT, functions);
        functions = new Vector();
        functions.add(SearchFunction.getStringForFunction(SearchFunction.EQUALS));
        functions.add(SearchFunction.getStringForFunction(SearchFunction.CONTAINS));
        map.put(ISearchField.FROM, functions);
        map.put(ISearchField.SUBJECT, functions);
        map.put(ISearchField.TO, functions);
        functions = new Vector();
        functions.add(SearchFunction.getStringForFunction(SearchFunction.EQUALS));
        map.put(ISearchField.DATE, functions);
        return map;
    }
}
