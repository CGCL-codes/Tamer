package de.molimo.session.actions;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import de.molimo.common.AbstractParameterable;
import de.molimo.common.IRemoteParameterable;
import de.molimo.common.exceptions.ActionException;
import de.molimo.common.exceptions.InvalidValueException;
import de.molimo.common.exceptions.SeriousActionException;
import de.molimo.server.ThingLocal;
import de.molimo.session.IUserSessionContainer;
import de.molimo.session.UserSessionLocal;

/**
 * This AbstractSessionBean is the superclass of every action running in the session tier. It handles parameter in and output. subclasses can access the parameters via protected methods.
 * @ejbHome <{de.molimo.session.actions.ActionSessionHome}>
 * @ejbRemote <{de.molimo.session.actions.ActionSession}>
 * @ejbStateful
 * @ejbDontSynchronizeNames 
 */
public abstract class AbstractAction extends AbstractParameterable implements IUserSessionContainer, SessionBean, IAction, IRemoteParameterable {

    protected SessionContext ctx;

    private String[] paramValues;

    private boolean[] dirtyFlags;

    private int numberOfParams;

    private ThingLocal thing;

    private String layoutIdentifier;

    private ActionContainerInfoLocal actionContainerInfo;

    private UserSessionLocal userSession;

    private Map initParameter;

    public void setSessionContext(SessionContext context) throws RemoteException, EJBException {
        ctx = context;
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbCreate(UserSessionLocal userSessionLocal) throws CreateException, EJBException {
        userSession = userSessionLocal;
        initParameter = new HashMap();
    }

    public void setThing(ThingLocal thing) {
        this.thing = thing;
    }

    protected ThingLocal getThing() {
        return this.thing;
    }

    public void setActionContainerInfo(ActionContainerInfoLocal actionContainerInfo) {
        this.actionContainerInfo = actionContainerInfo;
    }

    protected ActionContainerInfoLocal getActionContainerInfo() {
        return actionContainerInfo;
    }

    public String[] getPossibleValues(int index) {
        return null;
    }

    public int getSelectedValue(int index) {
        return -1;
    }

    public boolean isMultiLine(int index) {
        return false;
    }

    public abstract Object containerCall() throws ActionException, SeriousActionException;

    public abstract void containerPreUI() throws ActionException;

    public abstract void containerInit();

    public void containerPostUI() throws ActionException {
    }

    public abstract String getName();

    public boolean isRequiredParam(int index) {
        return true;
    }

    public String getParamValue(int index) {
        return paramValues[index];
    }

    public void setParamValue(int index, String paramValue) throws InvalidValueException {
        paramValues[index] = paramValue;
        dirtyFlags[index] = true;
    }

    protected void initParamValue(int index, String paramValue) {
        paramValues[index] = paramValue;
        dirtyFlags[index] = false;
    }

    public int getNumberOfParams() {
        return numberOfParams;
    }

    protected boolean isDirty(int nr) {
        return dirtyFlags[nr];
    }

    protected boolean isEmpty(int nr) {
        return paramValues[nr].trim().length() == 0;
    }

    protected void initNumberOfParams(int nr) {
        numberOfParams = nr;
        paramValues = new String[numberOfParams];
        dirtyFlags = new boolean[numberOfParams];
        for (int i = 0; i < nr; i++) {
            paramValues[i] = new String();
            dirtyFlags[i] = false;
        }
    }

    public String getLayoutIdentifier() {
        return this.layoutIdentifier;
    }

    public void setLayoutIdentifier(String layoutIdentifier) {
        this.layoutIdentifier = layoutIdentifier;
    }

    public UserSessionLocal getUserSession() {
        return userSession;
    }

    public void setInitParameter(String key, Object value) {
        super.setParameter(key, value);
        initParameter.put(key, Boolean.TRUE);
    }

    public void setParameter(String key, Object value) {
        if (initParameter.get(key) == null) super.setParameter(key, value);
    }

    public void copyParameters(ActionSession toAction) throws RemoteException {
        Set keys = getParamterKeys();
        for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            if (!initParameter.containsKey(key)) toAction.setParameter(key, getParameterAsObject(key));
        }
    }
}
