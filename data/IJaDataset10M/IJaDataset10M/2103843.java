package vdoclet.ejb;

import vdoclet.docinfo.*;
import java.util.*;

/**
 * Represents an EJB remote client-view
 */
public class EjbRemoteView extends EjbView {

    /**
     * Construct a new EjbRemoteInfo
     */
    public EjbRemoteView(EjbInfo ejbInfo) {
        super(ejbInfo);
    }

    String getViewSuffix() {
        return "";
    }

    String getFlagTag() {
        return EjbTags.REMOTE_FLAG;
    }

    String getInterfaceClassTag() {
        return EjbTags.REMOTE_CLASS;
    }

    String getInterfaceExtendsTag() {
        return EjbTags.REMOTE_EXTENDS;
    }

    String getHomeClassTag() {
        return EjbTags.REMOTE_HOME_CLASS;
    }

    String getHomeJndiTag() {
        return EjbTags.REMOTE_HOME_JNDI;
    }

    String getDefaultSuperInterface() {
        return "javax.ejb.EJBObject";
    }

    EjbView getSuperView(EjbInfo superEjb) {
        return superEjb.getRemoteView();
    }

    void addViewExceptions(MethodInfo viewMethod) {
        viewMethod.addException("java.rmi.RemoteException");
    }
}
