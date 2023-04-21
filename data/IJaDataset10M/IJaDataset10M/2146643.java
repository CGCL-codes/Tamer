package fr.insa.rennes.pelias.pexecutor.webservices;

import fr.insa.rennes.pelias.framework.ChainCall;
import fr.insa.rennes.pelias.framework.Execution;
import fr.insa.rennes.pelias.framework.ServiceCall;
import fr.insa.rennes.pelias.pexecutor.ApplicationBean1;
import fr.insa.rennes.pelias.pexecutor.Viewexecution;
import fr.insa.rennes.pelias.pexecutor.exceptions.InvalidCrendentialsException;
import fr.insa.rennes.pelias.pexecutor.login.LoginBean;
import fr.insa.rennes.pelias.platform.IRepository;
import fr.insa.rennes.pelias.platform.PObjectNotFoundException;
import fr.insa.rennes.pelias.platform.PObjectReference;
import fr.insa.rennes.pelias.platform.PSxSObjectReference;
import java.util.UUID;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author 2bo
 */
@XmlSeeAlso({ ChainCall.class, ServiceCall.class, PSxSObjectReference.class })
@WebService()
public class ExecutionServices {

    private static IRepository<Execution> repository;

    private LoginBean myLoginBean;

    public ExecutionServices() {
        repository = ApplicationBean1.getExecutionIRepository();
        myLoginBean = new LoginBean();
    }

    private void updateLoginBean() {
        myLoginBean = new LoginBean();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "registerExecutionConsumer")
    public boolean registerExecutionConsumer(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "arg0") UUID arg0, @WebParam(name = "arg1") PObjectReference arg1) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.registerObjectConsumer(arg0, arg1);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "unRegisterExecutionConsumer")
    public boolean unRegisterExecutionConsumer(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "arg0") UUID arg0, @WebParam(name = "arg1") PObjectReference arg1) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.unregisterObjectConsumer(arg0, arg1);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "enumerateExecutions")
    public java.util.List enumerateExecutions(@WebParam(name = "login") String login, @WebParam(name = "password") String password) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.enumerateObjects();
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "executionExists")
    public boolean executionExists(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.objectExists(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "getExecution")
    public Execution getExecution(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.getObject(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "putExecution")
    public boolean putExecution(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "item") Execution item, @WebParam(name = "label") String label, @WebParam(name = "replace") boolean replace) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            item.setLabel(label);
            return repository.putObject(item, replace);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "removeExecution")
    public boolean removeExecution(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "item") UUID item) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.removeObject(item);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "clearExecutions")
    public void clearExecutions(@WebParam(name = "login") String login, @WebParam(name = "password") String password) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            repository.clearObjects();
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "getExecutionDeclaredDependencies")
    public java.util.List getExecutionDeclaredDependencies(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.getObjectDeclaredDependencies(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "getExecutionRegistredConsumers")
    public java.util.List getExecutionRegistredConsumers(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.getObjectDeclaredDependencies(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "getExecutionLabel")
    public String getExecutionLabel(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.getObjectLabel(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "getExecutionAttachment")
    public String getExecutionAttachment(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id, @WebParam(name = "attachement") UUID attachement) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.getObjectAttachment(id, attachement);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "enumerateExecutionAttachments")
    public java.util.List enumerateExecutionAttachments(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.enumerateObjectAttachments(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "removeExecutionAttachment")
    public boolean removeExecutionAttachment(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id, @WebParam(name = "attachment") UUID attachment) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.removeObjectAttachment(id, attachment);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "putExecutionAttachment")
    public boolean putExecutionAttachment(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id, @WebParam(name = "attachment") UUID attachment, @WebParam(name = "value") String value, @WebParam(name = "replace") boolean replace) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.putObjectAttachment(id, attachment, value, replace);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "executionAttachmentExists")
    public boolean executionAttachmentExists(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id, @WebParam(name = "attachement") UUID attachement) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            return repository.objectAttachmentExists(id, attachement);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "clearExecutionAttachments")
    public void clearExecutionAttachments(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "id") UUID id) throws InvalidCrendentialsException, PObjectNotFoundException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            repository.clearObjectAttachments(id);
        } else {
            throw new InvalidCrendentialsException();
        }
    }

    @WebMethod(operationName = "launchExecution")
    public void launchExecution(@WebParam(name = "login") String login, @WebParam(name = "password") String password, @WebParam(name = "uuid") UUID uuid) throws InvalidCrendentialsException {
        updateLoginBean();
        myLoginBean.setId(login);
        myLoginBean.setPassword(password);
        if (myLoginBean.validate()) {
            Viewexecution.launchExecution(repository.getObject(uuid), login, password);
        } else {
            throw new InvalidCrendentialsException();
        }
    }
}
