package org.icenigrid.gridsam.bes.webservice.axis;

import java.rmi.RemoteException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import javax.security.auth.Subject;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.DOMReader;
import org.icenigrid.gridsam.bes.webservice.common.BesFactoryPort;
import org.icenigrid.gridsam.bes.webservice.common.BesManagementPort;
import org.icenigrid.schema.bes.factory.y2006.m08.CreateActivityDocument;
import org.icenigrid.schema.bes.factory.y2006.m08.GetActivityDocumentsDocument;
import org.icenigrid.schema.bes.factory.y2006.m08.GetActivityStatusesDocument;
import org.icenigrid.schema.bes.factory.y2006.m08.GetFactoryAttributesDocumentDocument1;
import org.icenigrid.schema.bes.factory.y2006.m08.TerminateActivitiesDocument;
import org.icenigrid.schema.bes.management.y2006.m08.StartAcceptingNewActivitiesDocument;
import org.icenigrid.schema.bes.management.y2006.m08.StopAcceptingNewActivitiesDocument;
import uk.ac.soton.itinnovation.grid.gridservit.EScienceService;

/**
 *
 * @author Vesso A. Novov
 */
public class BasicExecutionServiceAxisImpl extends EScienceService implements ServiceLifecycle {

    /**
     * Private member variables.
     */
    private static final Log sLog = LogFactory.getLog(BasicExecutionServiceAxisImpl.class.getName());

    private ServletEndpointContext oContext;

    private BesFactoryPort oBesFactoryPort = null;

    private BesManagementPort oBesManagementPort = null;

    private X500Principal defaultPrincipal = null;

    /**
     * It overrides an inherited parent method.  Checks the validation of the 
     * context object.
     *
     * @param  pContext            the context received from the container
     * @throws ServiceException    if context object is invalid
     */
    public void init(Object pContext) throws ServiceException {
        sLog.debug("ENTER:init()");
        defaultPrincipal = new X500Principal("CN=gridsam");
        if ((pContext == null) || !(pContext instanceof ServletEndpointContext)) {
            throw new ServiceException("Invalid Context Object Received!");
        }
        final ServletEndpointContext fContext = (ServletEndpointContext) pContext;
        oContext = new ServletEndpointContext() {

            public MessageContext getMessageContext() {
                return fContext.getMessageContext();
            }

            public Principal getUserPrincipal() {
                Principal xPrincipal = null;
                try {
                    xPrincipal = getServiceContext().getEScienceContext().getSecurityContext().getAuthenticatedSubjectPrincipal();
                    if (xPrincipal != null) {
                        sLog.debug("principal obtained from WS-Security subsystem - " + xPrincipal.getName());
                    }
                } catch (Exception xEx) {
                    sLog.debug("WS-Security context is not available. WS-Security might be turned off");
                }
                if (xPrincipal == null) {
                    xPrincipal = fContext.getUserPrincipal();
                    if (xPrincipal != null) {
                        sLog.debug("principal obtained from MessageContext - " + xPrincipal.getName());
                    }
                }
                if (xPrincipal == null) {
                    ServletRequest xRequest = (ServletRequest) getMessageContext().getProperty("transport.http.servletRequest");
                    if (xRequest != null) {
                        X509Certificate[] xCerts = (X509Certificate[]) xRequest.getAttribute("javax.servlet.request.X509Certificate");
                        if (xCerts != null && xCerts.length > 0) {
                            xPrincipal = xCerts[0].getSubjectDN();
                        }
                        if (xPrincipal != null) {
                            sLog.debug("principal obtained from Axis transport - " + xPrincipal.getName());
                        }
                    }
                }
                return xPrincipal;
            }

            public HttpSession getHttpSession() {
                return fContext.getHttpSession();
            }

            public ServletContext getServletContext() {
                return fContext.getServletContext();
            }

            public boolean isUserInRole(String s) {
                return fContext.isUserInRole(s);
            }
        };
        oBesFactoryPort = new BesFactoryPort();
        oBesFactoryPort.init(oContext);
        oBesManagementPort = new BesManagementPort();
        oBesManagementPort.init(oContext);
        sLog.debug("EXIT:init()");
    }

    /**
     * It overrides an inherited parent method.
     */
    public void destroy() {
        sLog.debug("ENTER:destroy()");
        oBesFactoryPort.destroy();
        sLog.debug("EXIT:destroy()");
    }

    /**
     * loads in the Subject, depending on various system settings
     */
    public Subject loadSubject() {
        Subject xSubject = new Subject();
        if (oContext.getUserPrincipal() != null) xSubject.getPrincipals().add(oContext.getUserPrincipal()); else xSubject.getPrincipals().add(defaultPrincipal);
        return xSubject;
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document CreateActivity(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:CreateActivity()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesFactoryPort.createActivity(CreateActivityDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:CreateActivity()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document GetActivityStatuses(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:GetActivityStatuses()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesFactoryPort.getActivityStatuses(GetActivityStatusesDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:GetActivityStatuses()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document TerminateActivities(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:TerminateActivities()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesFactoryPort.terminateActivities(TerminateActivitiesDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:TerminateActivities()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document GetActivityDocuments(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:GetActivityDocuments()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesFactoryPort.getActivityDocuments(GetActivityDocumentsDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:GetActivityDocuments()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document GetFactoryAttributesDocument(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:GetFactoryAttributesDocument()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesFactoryPort.getFactoryAttributesDocument(GetFactoryAttributesDocumentDocument1.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:GetFactoryAttributesDocument()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document StopAcceptingNewActivities(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:StopAcceptingNewActivities()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesManagementPort.stopAcceptingNewActivities(StopAcceptingNewActivitiesDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:StopAcceptingNewActivities()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }

    /**
     * 
     * @param pInput the input message
     * @return Document the output message
     * @throws RemoteException
     */
    public org.w3c.dom.Document StartAcceptingNewActivities(final org.w3c.dom.Document pInput) throws RemoteException {
        if (sLog.isDebugEnabled()) {
            sLog.debug("ENTER:StartAcceptingNewActivities()");
            sLog.debug((new DOMReader()).read(pInput).asXML());
        }
        Subject xSubject = loadSubject();
        try {
            return (org.w3c.dom.Document) Subject.doAs(xSubject, new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    org.w3c.dom.Document xResult = ((org.w3c.dom.Document) oBesManagementPort.startAcceptingNewActivities(StartAcceptingNewActivitiesDocument.Factory.parse(pInput)).getDomNode());
                    if (sLog.isDebugEnabled()) {
                        sLog.debug((new DOMReader()).read(xResult).asXML());
                        sLog.debug("EXIT:StartAcceptingNewActivities()");
                    }
                    return xResult;
                }
            });
        } catch (PrivilegedActionException xEx) {
            if (xEx.getCause() instanceof RemoteException) {
                throw (RemoteException) xEx.getCause();
            } else {
                throw new RemoteException(xEx.getCause().getMessage(), xEx.getCause());
            }
        }
    }
}
