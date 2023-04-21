package org.nakedobjects.distribution.reflect;

import org.nakedobjects.distribution.ObjectProxy;
import org.nakedobjects.distribution.ObjectRequest;
import org.nakedobjects.distribution.RequestContext;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectManager;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.ObjectStoreException;
import org.nakedobjects.object.control.About;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.ActionSpecification;
import org.nakedobjects.object.reflect.ActionSpecification.Type;
import org.nakedobjects.object.security.Certificate;

public class AboutActionRequest extends ObjectRequest {

    private static final long serialVersionUID = 1L;

    private String actionName;

    private Certificate certificate;

    private ObjectProxy parameters[];

    private Type actionType;

    public AboutActionRequest(NakedObject object, Action action, SecurityContext context, NakedObject parameters[]) {
        super(object);
        this.actionName = action.getName();
        this.actionType = action.getActionType();
        this.certificate = context.getCertificate();
        this.parameters = new ObjectProxy[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getOid() == null) {
                throw new IllegalArgumentException("All parameters must have an OID to used in a message");
            }
            this.parameters[i] = new ObjectProxy(parameters[i]);
        }
    }

    public About about() throws ObjectStoreException {
        sendRequest();
        return (About) response;
    }

    protected void generateResponse(RequestContext server) {
        try {
            NakedObject object = getObject(server.getLoadedObjects());
            int parameterCount = parameters.length;
            NakedObject parameters[] = new NakedObject[parameterCount];
            NakedObjectSpecification parameterTypes[] = new NakedObjectSpecification[parameterCount];
            NakedObjectManager objectManager = server.getObjectManager();
            for (int i = 0; i < parameterCount; i++) {
                NakedObject parameter = objectManager.getObject(parameters[i]);
                parameters[i] = parameter;
                parameterTypes[i] = parameter.getSpecification();
            }
            ActionSpecification action = object.getSpecification().getObjectAction(actionType, actionName, parameterTypes);
            SecurityContext context = server.getSecurityContext(certificate);
            response = (action == null) ? null : object.getHint(context, action, parameters);
        } catch (ObjectStoreException e) {
            response = e;
        }
    }

    /**
     * 
     * @return java.lang.String
     */
    public String toString() {
        String params = "";
        for (int i = 0; i < parameters.length; i++) {
            params = (i > 0) ? "," : "" + parameters[i];
        }
        return "AboutActionRequest [" + externalOid + "." + actionName + "(" + params + ")]";
    }
}
