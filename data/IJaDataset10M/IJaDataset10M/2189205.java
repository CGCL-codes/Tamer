package org.monet.frontservice.control;

import java.util.UUID;
import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import org.monet.frontservice.ApplicationFrontservice;
import org.monet.frontservice.control.actions.ActionProcessServiceResponse;
import org.monet.frontservice.control.actions.ActionsFactory;
import org.monet.frontservice.control.constants.Actions;
import org.monet.frontservice.control.constants.SystemUser;
import org.monet.frontservice.control.exceptions.DataError;
import org.monet.frontservice.control.exceptions.SessionError;
import org.monet.frontservice.control.exceptions.SystemError;
import org.monet.kernel.agents.AgentLogger;
import org.monet.kernel.agents.AgentSession;
import org.monet.kernel.components.ComponentFrontAccountsManager;
import org.monet.kernel.constants.ApplicationInterface;
import org.monet.kernel.constants.Database;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.exceptions.SessionException;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Context;
import org.monet.kernel.model.User;
import com.sun.xml.ws.transport.http.servlet.WSServlet;

@WebService(name = "Callback", serviceName = "Callback", targetNamespace = "urn:callbackservice.services.monet.org")
public class CallbackController extends WSServlet {

    private static final long serialVersionUID = 1L;

    private Context oContext;

    private String idTransportSession;

    private AgentLogger agentException = AgentLogger.getInstance();

    private void checkBusinessUnitStarted() throws SystemError {
        if ((!BusinessUnit.started()) || (!ApplicationFrontservice.started())) {
            this.throwSystemException(null);
        }
    }

    private void updateContext() {
        long idCurrentThread = Thread.currentThread().getId();
        oContext.setApplication(idCurrentThread, "ip remota", ApplicationFrontservice.NAME, ApplicationInterface.APPLICATION);
        oContext.setSessionId(idCurrentThread, this.idTransportSession);
        oContext.setDatabaseConnectionType(idCurrentThread, Database.ConnectionTypes.AUTO_COMMIT);
    }

    private void throwSessionException(Exception original) throws SessionError {
        if (original != null) this.agentException.error(original);
        throw new SessionError();
    }

    private void throwSystemException(Exception original) throws SystemError {
        String id = UUID.randomUUID().toString();
        if (original != null) this.agentException.error("Error id: " + id, original);
        throw new SystemError(id);
    }

    private void throwDataException(Exception original) throws DataError {
        String id = UUID.randomUUID().toString();
        if (original != null) this.agentException.error("Error id: " + id, original);
        throw new DataError(id);
    }

    public CallbackController() throws SystemError {
        try {
            this.oContext = Context.getInstance();
        } catch (Exception oException) {
            this.throwSystemException(oException);
        }
    }

    private User login(String username, String password) throws SystemError, SessionError {
        if ((username == null) || (password == null)) {
            this.throwSessionException(null);
        }
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            return ComponentFrontAccountsManager.getInstance().login(username, password);
        } catch (SessionException oException) {
            this.throwSessionException(oException);
        } catch (Exception oException) {
            this.throwSystemException(oException);
        }
        return null;
    }

    @WebMethod
    public void responseCallback(@WebParam(name = "serviceRequestId") String serviceRequestId, @WebParam(name = "responseDocument") @XmlMimeType("application/octet-stream") DataHandler responseDocument) throws SystemError, SessionError, DataError {
        ActionsFactory actionsFactory;
        ActionProcessServiceResponse action;
        AgentSession agentSession = AgentSession.getInstance();
        this.idTransportSession = UUID.randomUUID().toString();
        agentSession.add(this.idTransportSession);
        try {
            this.checkBusinessUnitStarted();
            this.updateContext();
            if (this.login(SystemUser.USERNAME, SystemUser.PASSWORD) == null) {
                this.throwSessionException(null);
            }
            actionsFactory = ActionsFactory.getInstance();
            action = (ActionProcessServiceResponse) actionsFactory.get(Actions.PROCESS_SERVICE_RESPONSE);
            action.setServiceRequestId(serviceRequestId);
            action.setResponseDocument(responseDocument.getInputStream());
            action.execute();
        } catch (DataException oException) {
            this.throwDataException(oException);
        } catch (Exception oException) {
            this.throwSystemException(oException);
        } finally {
            agentSession.remove(this.idTransportSession);
        }
    }
}
