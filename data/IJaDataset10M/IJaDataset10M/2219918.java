package org.isi.monet.applications.backserver.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.isi.monet.applications.backserver.ApplicationBackserver;
import org.isi.monet.applications.backserver.control.exception.DataError;
import org.isi.monet.applications.backserver.control.exception.NodeAccessError;
import org.isi.monet.applications.backserver.control.exception.SessionError;
import org.isi.monet.applications.backserver.control.exception.SystemError;
import org.isi.monet.applications.backserver.control.exception.TaskAccessError;
import org.isi.monet.applications.backserver.control.model.Account;
import org.isi.monet.applications.backserver.control.model.Node;
import org.isi.monet.applications.backserver.control.model.SearchFilter;
import org.isi.monet.applications.backserver.control.model.ServerConfiguration;
import org.isi.monet.applications.backserver.control.model.Session;
import org.isi.monet.applications.backserver.control.model.Task;
import org.isi.monet.applications.backserver.control.model.UserInfo;
import org.isi.monet.core.agents.AgentSession;
import org.isi.monet.core.components.ComponentBackAccountsManager;
import org.isi.monet.core.components.ComponentPersistence;
import org.isi.monet.core.constants.ApplicationInterface;
import org.isi.monet.core.constants.Database;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.DataException;
import org.isi.monet.core.exceptions.NodeAccessException;
import org.isi.monet.core.exceptions.SessionException;
import org.isi.monet.core.exceptions.TaskAccessException;
import org.isi.monet.core.model.BusinessUnit;
import org.isi.monet.core.model.Context;
import org.isi.monet.core.model.LogBookNode;
import org.isi.monet.core.model.LogHistory;
import org.isi.monet.core.model.SearchRequest;
import com.sun.xml.ws.transport.http.servlet.WSServlet;

@WebService
public class Controller extends WSServlet {

    private static final long serialVersionUID = 1L;

    private AgentSession oAgentSession;

    private Context oContext;

    private String idTransportSession;

    private void checkBusinessUnitStarted() throws SystemError {
        if ((!BusinessUnit.started()) || (!ApplicationBackserver.started())) {
            throw new SystemError();
        }
    }

    private void updateContext() {
        long idCurrentThread = Thread.currentThread().getId();
        oContext.setApplication(idCurrentThread, "ip remota", ApplicationBackserver.NAME, ApplicationInterface.APPLICATION);
        oContext.setSessionId(idCurrentThread, this.idTransportSession);
        oContext.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.MANUAL_COMMIT);
    }

    private void checkIfLogged(Session oSession) throws SessionError {
        if (!ComponentBackAccountsManager.getInstance().isLogged()) {
            throw new SessionError();
        }
    }

    public Controller() throws SystemError {
        try {
            this.oAgentSession = AgentSession.getInstance();
            this.oContext = Context.getInstance();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public Session openSession() throws SystemError, SessionError {
        Session oSession = null;
        try {
            this.checkBusinessUnitStarted();
            do {
                this.idTransportSession = String.valueOf(Math.random());
            } while (oAgentSession.exists(this.idTransportSession));
            this.oAgentSession.add(this.idTransportSession);
            this.updateContext();
        } catch (Exception oException) {
            throw new SystemError();
        }
        if (this.idTransportSession == null) {
            throw new SessionError();
        }
        try {
            oSession = new Session();
            Converter.convert(this.oAgentSession.get(this.idTransportSession), oSession);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oSession;
    }

    @WebMethod
    public void closeSession(Session oSession) throws SystemError, SessionError {
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            if (!this.oAgentSession.exists(oSession.getId())) {
                throw new SessionError();
            }
            this.oAgentSession.remove(oSession.getId());
            this.oContext.clear(Thread.currentThread().getId());
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public void login(Session oSession, String code, String sPassword) throws SystemError, SessionError {
        if ((oSession == null) || (code == null) || (sPassword == null)) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            ComponentBackAccountsManager.getInstance().login(code, sPassword);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public void logout(Session oSession) throws SystemError, SessionError {
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentBackAccountsManager.getInstance().logout();
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public Account loadAccount(Session oSession, String codeAccount) throws DataError, SystemError, SessionError {
        Account oAccount;
        org.isi.monet.core.model.Account oMonetAccount;
        if (oSession == null) {
            throw new SessionError();
        }
        if (codeAccount == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oAccount = new Account();
            oMonetAccount = ComponentBackAccountsManager.getInstance().loadAccount(codeAccount);
            Converter.convert(oMonetAccount, oAccount);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oAccount;
    }

    @WebMethod
    public Boolean saveAccount(Session oSession, Account oAccount) throws DataError, SystemError, SessionError {
        org.isi.monet.core.model.Account oMonetAccount;
        if (oSession == null) {
            throw new SessionError();
        }
        if (oAccount == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oMonetAccount = new org.isi.monet.core.model.Account();
            Converter.convert(oAccount, oMonetAccount);
            ComponentBackAccountsManager.getInstance().saveAccount(oMonetAccount);
            Converter.convert(oMonetAccount, oAccount);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return true;
    }

    @WebMethod
    public Account createAccount(Session oSession, String code, String sPassword, String codeNodeType, UserInfo oUserInfo) throws DataError, SystemError, SessionError {
        Account oAccount = null;
        org.isi.monet.core.model.Account oMonetAccount;
        org.isi.monet.core.model.UserInfo oMonetUserInfo;
        if (oSession == null) {
            throw new SessionError();
        }
        if ((code == null) || (code.equals("")) || (sPassword == null) || (sPassword.equals("")) || (codeNodeType == null) || (codeNodeType.equals("")) || (oUserInfo == null)) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oAccount = new Account();
            oMonetUserInfo = new org.isi.monet.core.model.UserInfo();
            Converter.convert(oUserInfo, oMonetUserInfo);
            oMonetAccount = ComponentBackAccountsManager.getInstance().createAccount(code, sPassword, codeNodeType, oMonetUserInfo);
            Converter.convert(oMonetAccount, oAccount);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oAccount;
    }

    @WebMethod
    public Boolean removeAccount(Session oSession, String codeAccount) throws DataError, SystemError, SessionError {
        if (oSession == null) {
            throw new SessionError();
        }
        if (codeAccount == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentBackAccountsManager.getInstance().removeAccounts(codeAccount);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return true;
    }

    @WebMethod
    public void commit(Session oSession) throws SystemError, SessionError, DataError {
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            BusinessUnit.getInstance().commitChanges();
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public void emptyTrash(Session oSession) throws SystemError, SessionError {
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentPersistence.getInstance().emptyTrash();
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public Node getNode(Session oSession, String idNode, int iDepth) throws SystemError, SessionError, DataError, NodeAccessError {
        Node oNode = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oNode = new Node();
            Converter.convert(ComponentPersistence.getInstance().loadNode(idNode), oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public Node locateNode(Session oSession, String code, int iDepth) throws SystemError, SessionError, DataError, NodeAccessError {
        Node oNode = null;
        ComponentPersistence oComponentPersistence = ComponentPersistence.getInstance();
        String idNode;
        if (oSession == null) {
            throw new SessionError();
        }
        if (code == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oNode = new Node();
            idNode = oComponentPersistence.locateNodeId(code);
            if ((idNode != null) && (!idNode.equals(""))) Converter.convert(oComponentPersistence.loadNode(idNode), oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public Node getUserNode(Session oSession, String codeUser, int iDepth) throws SystemError, SessionError, DataError, NodeAccessError {
        Node oNode = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (codeUser == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oNode = new Node();
            Converter.convert(ComponentBackAccountsManager.getInstance().loadAccount(codeUser).getRootNode(), oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public Node getRootNode(Session oSession, int iDepth) throws SystemError, SessionError, DataError, NodeAccessError {
        Node oNode = null;
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oNode = new Node();
            Converter.convert(ComponentBackAccountsManager.getInstance().loadAccount().getRootNode(), oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public Node createNode(Session oSession, String type, String idParent) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        Node oNode = null;
        org.isi.monet.core.model.Node oMonetNode, oMonetParent;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idParent == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oNode = new Node();
            oMonetParent = null;
            if ((idParent != null) && (!idParent.equals("-1"))) oMonetParent = oComponentPersistence.loadNode(idParent);
            oMonetNode = oComponentPersistence.addNode(type, oMonetParent);
            Converter.convert(oMonetNode, oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public Node loadNode(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        Node oNode = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oNode = new Node();
            Converter.convert(ComponentPersistence.getInstance().loadNode(idNode), oNode);
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public void saveNode(Session oSession, Node oNode) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        org.isi.monet.core.model.Node oMonetNode;
        if (oSession == null) {
            throw new SessionError();
        }
        if (oNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oMonetNode = oComponentPersistence.loadNode(oNode.getId());
            oMonetNode.getAttributeList().clear();
            Converter.convert(oNode, oMonetNode);
            oComponentPersistence.saveNode(oMonetNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public void removeNode(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentPersistence.getInstance().deleteNode(idNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public Node recoverNode(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        Node oNode = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oComponentPersistence.recoverNodeFromTrash(idNode);
            oNode = new Node();
            Converter.convert(oComponentPersistence.loadNode(idNode), oNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oNode;
    }

    @WebMethod
    public void importNode(Session oSession, String idNode, String sData) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        if (oSession == null) {
            throw new SessionError();
        }
        if (sData == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oComponentPersistence.importNode(idNode, sData);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public String exportNode(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        String sData = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            sData = ComponentPersistence.getInstance().exportNode(idNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return sData;
    }

    @WebMethod
    public Collection<Node> searchNode(Session oSession, String idNode, SearchFilter oSearchFilter) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        Collection<Node> clResult = null;
        SearchRequest oSearchRequest;
        org.isi.monet.core.model.Node oMonetNode;
        if (oSession == null) {
            throw new SessionError();
        }
        if ((idNode == null) || (oSearchFilter == null)) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oSearchRequest = new SearchRequest();
            Converter.convert(oSearchFilter, oSearchRequest);
            clResult = new ArrayList<Node>();
            oMonetNode = oComponentPersistence.loadNode(idNode);
            Converter.convert(oComponentPersistence.search(oMonetNode, oSearchRequest), clResult);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return clResult;
    }

    @WebMethod
    public Boolean makeNodePublic(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        org.isi.monet.core.model.Node oMonetNode;
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oMonetNode = oComponentPersistence.loadNode(idNode);
            oComponentPersistence.makeNodePublic(oMonetNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return true;
    }

    @WebMethod
    public Boolean makeNodePrivate(Session oSession, String idNode) throws SystemError, SessionError, DataError, NodeAccessError {
        ComponentPersistence oComponentPersistence;
        org.isi.monet.core.model.Node oMonetNode;
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oMonetNode = oComponentPersistence.loadNode(idNode);
            oComponentPersistence.makeNodePrivate(oMonetNode);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (NodeAccessException oException) {
            throw new NodeAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return true;
    }

    @WebMethod
    public Task createTask(Session oSession) throws SystemError, SessionError, DataError, TaskAccessError {
        Task oTask = null;
        if (oSession == null) {
            throw new SessionError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oTask = new Task();
            Converter.convert(ComponentPersistence.getInstance().createTask(Strings.NONE), oTask);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oTask;
    }

    @WebMethod
    public Task openTask(Session oSession, String idTask) throws SystemError, SessionError, DataError, TaskAccessError {
        Task oTask = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idTask == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oTask = new Task();
            Converter.convert(ComponentPersistence.getInstance().loadTask(idTask), oTask);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return oTask;
    }

    @WebMethod
    public void saveTask(Session oSession, Task oTask) throws SystemError, SessionError, DataError, TaskAccessError {
        ComponentPersistence oComponentPersistence;
        org.isi.monet.core.model.Task oMonetTask;
        if (oSession == null) {
            throw new SessionError();
        }
        if (oTask == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oComponentPersistence = ComponentPersistence.getInstance();
            oMonetTask = oComponentPersistence.loadTask(oTask.getId());
            Converter.convert(oTask, oMonetTask);
            oComponentPersistence.saveTask(oMonetTask);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public void removeTask(Session oSession, String idTask) throws SystemError, SessionError, DataError, TaskAccessError {
        if (oSession == null) {
            throw new SessionError();
        }
        if (idTask == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentPersistence.getInstance().removeTask(idTask);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public Collection<Task> getUserTasks(Session oSession, String codeUser) throws SystemError, SessionError, DataError, TaskAccessError {
        Collection<Task> clTasks = null;
        if (oSession == null) {
            throw new SessionError();
        }
        if (codeUser == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            clTasks = new ArrayList<Task>();
            org.isi.monet.core.model.Account oAccount = ComponentBackAccountsManager.getInstance().loadAccount(codeUser);
            Converter.convert(ComponentPersistence.getInstance().loadTasks(oAccount, Strings.ALL, Strings.ALL), clTasks);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return clTasks;
    }

    @WebMethod
    public Collection<Task> getNodeTasks(Session oSession, String idNode) throws SystemError, SessionError, DataError, TaskAccessError {
        Collection<Task> clNodeTasks = null;
        ComponentPersistence oComponentPersistence;
        if (oSession == null) {
            throw new SessionError();
        }
        if (idNode == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            clNodeTasks = new ArrayList<Task>();
            oComponentPersistence = ComponentPersistence.getInstance();
            org.isi.monet.core.model.Node oNode = oComponentPersistence.loadNode(idNode);
            Converter.convert(oComponentPersistence.loadTasks(oNode, Strings.ALL, Strings.ALL), clNodeTasks);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return clNodeTasks;
    }

    @WebMethod
    public void recoverTask(Session oSession, String idTask) throws SystemError, SessionError, DataError, TaskAccessError {
        if (oSession == null) {
            throw new SessionError();
        }
        if (idTask == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            ComponentPersistence.getInstance().recoverTaskFromTrash(idTask);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (TaskAccessException oException) {
            throw new TaskAccessError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }

    @WebMethod
    public String searchEvent(Session oSession, int type, Date dtFrom, Date dtTo) throws SessionError, DataError, SystemError {
        String sResult = null;
        LogBookNode oLogBookNode;
        LogHistory oLogHistory;
        if (oSession == null) {
            throw new SessionError();
        }
        if ((dtFrom == null) || (dtTo == null)) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oLogBookNode = ComponentPersistence.getInstance().loadLogBookNode();
            oLogHistory = new LogHistory();
            oLogHistory.setEntryList(oLogBookNode.search(type, dtFrom, dtTo));
            sResult = oLogHistory.serializeToXML(true).toString();
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
        return sResult;
    }

    @WebMethod
    public void subscribe(Session oSession, ServerConfiguration oConfiguration, int type) throws SessionError, DataError, SystemError {
        org.isi.monet.core.model.ServerConfiguration oMonetServerConfiguration;
        if (oSession == null) {
            throw new SessionError();
        }
        if (oConfiguration == null) {
            throw new DataError();
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            this.checkIfLogged(oSession);
            oMonetServerConfiguration = new org.isi.monet.core.model.ServerConfiguration();
            Converter.convert(oConfiguration, oMonetServerConfiguration);
            ComponentPersistence.getInstance().addNodeSubscriber(oMonetServerConfiguration, type);
        } catch (SessionException oException) {
            throw new SessionError();
        } catch (DataException oException) {
            throw new DataError();
        } catch (Exception oException) {
            throw new SystemError();
        }
    }
}
