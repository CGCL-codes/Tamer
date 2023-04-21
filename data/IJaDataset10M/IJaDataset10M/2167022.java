package org.tolven.app.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.tolven.app.MenuLocal;
import org.tolven.app.MirthOperationsLocal;
import org.tolven.app.MirthOperationsRemote;
import org.tolven.app.entity.MenuData;
import org.tolven.core.AccountDAOLocal;
import org.tolven.core.ActivationLocal;
import org.tolven.core.entity.Account;
import org.tolven.core.entity.AccountUser;
import org.tolven.core.entity.TolvenUser;
import org.tolven.doc.DocumentLocal;
import org.tolven.doc.XMLProtectedLocal;
import org.tolven.doc.entity.DocXML;
import org.tolven.logging.TolvenLogger;
import org.tolven.mirth.Message;
import org.tolven.mirth.Response;
import org.tolven.trim.Act;
import org.tolven.trim.ActRelationship;
import org.tolven.trim.ActRelationshipDirection;
import org.tolven.trim.II;
import org.tolven.trim.ex.ActEx;
import org.tolven.trim.ex.TrimEx;
import org.tolven.trim.ex.TrimFactory;

/**
 * This class handles the Mirth related operations
 * @author Syed
 * added on 6/3/2010
 */
@Stateless()
@Local(MirthOperationsLocal.class)
@Remote(MirthOperationsRemote.class)
public class MirthOperationsBean extends CreatorBean implements MirthOperationsLocal, MirthOperationsRemote {

    @EJB
    MenuLocal menuBean;

    @EJB
    DocumentLocal documentBean;

    @EJB
    XMLProtectedLocal xmlProtectedBean;

    @EJB
    ActivationLocal activationBean;

    @EJB
    AccountDAOLocal accountBean;

    private static TrimFactory factory;

    private static final char RETURN = 13;

    private static final char SOB = 11;

    private static final char EOR = 28;

    private static final String OUTBOUND = "OUT";

    public MirthOperationsBean() {
        factory = new TrimFactory();
    }

    /**
	 * This method sends the HL7 trim message to Mirth
	 * @author Syed
	 * @param trim
	 * @throws Exception 
	 * added on 6/4/2010
	 */
    public void sendMessage(TrimEx trim) throws Exception {
        boolean success = true;
        String host = propertyBean.getProperty("tolven.mirth.host");
        int port = Integer.parseInt(propertyBean.getProperty("tolven.mirth.port"));
        ByteArrayOutputStream trimXML = new ByteArrayOutputStream();
        ActRelationship dirRel = ((ActEx) trim.getAct()).getRelationship().get("transportStatus");
        List<ActRelationship> orderList = new ArrayList<ActRelationship>(((ActEx) trim.getAct()).getRelationshipsList().get("entry"));
        try {
            if (dirRel.getDirection().value().equals(OUTBOUND) && (!orderList.isEmpty())) {
                xmlBean.marshalTRIM(trim, trimXML);
                String trimXmlStr = trimXML.toString().split("\n", 2)[1].replaceAll("xsi:", "");
                Socket socket = establishConnection(host, port);
                sendMessage(trimXmlStr, socket);
                String response = getResponse(socket);
                TolvenLogger.info(response, this.getClass());
                closeConnection(socket);
            }
        } catch (Exception e) {
            success = false;
        } finally {
            if (success) TolvenLogger.info("Sent Lab orders to Mirth(" + host + ") on port " + port, this.getClass()); else throw (new Exception("Could not send Lab orders to Mirth(" + host + ") on port " + port));
        }
    }

    /**
	 * Closes the client socket
	 * @author Syed
	 * @return status
	 * added on 6/10/2010
	 */
    private void closeConnection(Socket socket) {
        try {
            socket.getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            TolvenLogger.info(e.getMessage(), this.getClass());
        }
    }

    /**
	 * Creates a stream socket and connects it to the specified port number on the named host.
	 * @author syed
	 * @param host
	 * @param port
	 * @throws Exception 
	 * added on 6/10/2010
	 */
    private Socket establishConnection(String host, int port) throws Exception {
        Socket socket = new Socket(host, port);
        return socket;
    }

    /**
	 * Wraps the message using a header and trailer and writes it to the outputstream of the Socket
	 * @author syed
	 * @param msg - the message string
	 * @param socket
	 * @throws IOException 
	 * added on 6/10/2010
	 */
    private void sendMessage(String msg, Socket socket) throws IOException {
        String outbound = SOB + msg + EOR + RETURN;
        OutputStream out = socket.getOutputStream();
        out.write(outbound.getBytes());
        out.flush();
    }

    /**
	 * This method reads the response from Mirth
	 * @author Syed
	 * @param socket
	 * @return response
	 * added on 6/10/2010
	 */
    private String getResponse(Socket socket) {
        StringBuffer inbound = new StringBuffer();
        try {
            char[] c = new char[1024];
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            while (inbound.indexOf(String.valueOf(EOR)) == -1) {
                int size = in.read(c);
                if (size == -1) {
                    if (inbound.length() == 0) {
                        return null;
                    } else {
                        String response = inbound.toString();
                        return response;
                    }
                }
                inbound.append(c, 0, size);
            }
            int pos = inbound.indexOf(String.valueOf(EOR));
            String retVal = inbound.substring(0, pos);
            return retVal;
        } catch (IOException e) {
            TolvenLogger.info(e.getMessage(), this.getClass());
            return null;
        }
    }

    /**
	 * Updates the Lab order trim with the response from Mirth
	 * @author Syed
	 * @param mdPlaceholder
	 * @param accountUser
	 * @param transitionName
	 * added on 6/24/2010
	 * @param eventPath 
	 * @param mapList 
	 */
    private void updateTrimData(AccountUser accountUser, String transitionName, List<Message> mapList, PrivateKey userPrivateKey) throws Exception {
        String docPath = mapList.get(0).getDocumentPath();
        Account account = accountUser.getAccount();
        MenuData mdPlaceholder = menuBean.findDefaultedMenuDataItem(account, docPath);
        Date now = new Date();
        if (mdPlaceholder == null) {
            throw new IllegalArgumentException("[" + this.getClass().getSimpleName() + "]Missing placeholder for transition");
        }
        String knownType = accountUser.getAccount().getAccountType().getKnownType();
        DocXML docXMLFrom = (DocXML) documentBean.findDocument(mdPlaceholder.getDocumentId());
        TrimEx trim = (TrimEx) xmlProtectedBean.unmarshal(docXMLFrom, accountUser, userPrivateKey);
        DocXML docXMLNew = documentBean.createXMLDocument(TRIM_NS, accountUser.getUser().getId(), accountUser.getAccount().getId());
        docXMLNew.setSignatureRequired(isSignatureRequired(trim, knownType));
        documentBean.copyAttachments(docXMLFrom, docXMLNew);
        logger.info("Document (event) created, id: " + docXMLNew.getId());
        String status = calculateTransition(trim, transitionName);
        if (trim.getTolvenId(accountUser.getAccount().getId()) != null) {
            trim.addTolvenId(createTolvenId(mdPlaceholder, null, now, status, accountUser.getUser().getLdapUID()));
        }
        Map<String, Object> variables = new HashMap<String, Object>(10);
        MenuPath contextPath = new MenuPath(mdPlaceholder.getPath());
        variables.putAll(contextPath.getNodeValues());
        {
            String assignedPath = accountUser.getProperty().get("assignedAccountUser");
            if (assignedPath != null) {
                MenuData assigned = menuBean.findMenuDataItem(accountUser.getAccount().getId(), assignedPath);
                variables.put("assignedAccountUser", assigned);
            }
        }
        variables.put("trim", trim);
        String instancePath = getInstancePath(trim, knownType);
        MenuData mdEvent = createEvent(accountUser.getAccount(), instancePath, trim, now, variables);
        mdEvent.setDocumentId(docXMLNew.getId());
        List<II> iiList;
        ActEx relAct;
        boolean completed = true;
        Map<String, ActEx> relMap = new HashMap<String, ActEx>();
        List<ActRelationship> orderList = new ArrayList<ActRelationship>(((ActEx) trim.getAct()).getRelationshipsList().get("entry"));
        for (ActRelationship actRel : orderList) {
            iiList = actRel.getAct().getId().getIIS();
            relAct = (ActEx) actRel.getAct();
            for (II ii : iiList) {
                relMap.put(ii.getExtension(), relAct);
            }
        }
        for (Message msg : mapList) {
            if (msg.isProcessed()) {
                relAct = relMap.get(msg.getEventPath());
                relAct.getRelationship().get("orderStatus").getLabel().setValue("Processed");
                relAct.setStatusCodeValue(status);
            }
        }
        for (ActRelationship actRel : orderList) {
            relAct = (ActEx) actRel.getAct();
            if (!relAct.getStatusCodeValue().equalsIgnoreCase(status)) completed = false;
        }
        if (completed) {
            mdEvent.setActStatus(status);
        }
        ActRelationship dirRel = ((ActEx) trim.getAct()).getRelationship().get("transportStatus");
        dirRel.setDirection(ActRelationshipDirection.IN);
        trim.addTolvenEventId(createTolvenId(mdEvent, null, now, status, accountUser.getUser().getLdapUID()));
        marshalToDocument(trim, docXMLNew);
        submit(mdEvent, accountUser, userPrivateKey);
    }

    /**
	 * Gets the user account specified in the response message 
	 * and updates the lab orders.
	 * @author Syed
	 * @param user - TolvenUser
	 * @param resp - the response from Mirth
	 * added on 6/24/2010
	 */
    public void updateDocument(TolvenUser user, Response resp, PrivateKey userPrivateKey) {
        List<AccountUser> accountUsers = activationBean.findUserAccounts(user);
        List<Message> msgList = resp.getMessages();
        List<Message> mapList;
        long accountId;
        Map<String, List<Message>> msgMap = new HashMap<String, List<Message>>();
        for (Message msg : msgList) {
            if (msgMap.containsKey(msg.getDocumentPath())) msgMap.get(msg.getDocumentPath()).add(msg); else {
                mapList = new ArrayList<Message>();
                mapList.add(msg);
                msgMap.put(msg.getDocumentPath(), mapList);
            }
        }
        for (Map.Entry<String, List<Message>> entry : msgMap.entrySet()) {
            boolean isAccountUser = false;
            mapList = entry.getValue();
            accountId = mapList.get(0).getAccountId();
            for (AccountUser accountUser : accountUsers) {
                if (accountUser.getAccount().getId() == accountId) {
                    isAccountUser = true;
                    try {
                        updateTrimData(accountUser, "completed", mapList, userPrivateKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if (!isAccountUser) TolvenLogger.info("Mirth accountuser not found in account " + accountId, this.getClass());
        }
    }
}
