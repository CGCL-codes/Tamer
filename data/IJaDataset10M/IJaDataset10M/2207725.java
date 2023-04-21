package fi.hip.gb.gridlib.idff12.servlet;

import fi.hip.gb.gridlib.idff12.crypto.DelegationUtil;
import fi.hip.gb.gridlib.idff12.gridcheque.xml.GridChequeRequestType;
import fi.hip.gb.gridlib.idff12.gridcheque.xml.GridChequeRequestDocument;
import org.glite.security.delegation.GrDPX509Util;
import org.bouncycastle.jce.provider.*;
import org.sourceid.idff12.adapter.sp.AdapterFactory;
import org.sourceid.idff12.adapter.AuthnAdapter;
import org.sourceid.idff12.adapter.FederationStoreAdapter;
import org.sourceid.idff12.adapter.SessionInfo;
import org.sourceid.idff12.adapter.SessionStoreAdapter;
import org.sourceid.idff12.adapter.FederationInfo;
import org.sourceid.idff12.adapter.Warrant;
import org.sourceid.common.IDGenerator;
import org.sourceid.common.Util;
import org.sourceid.idff12.config.IdffRoleEnum;
import org.sourceid.idff12.config.ProfileEnum;
import org.sourceid.idff12.config.ProviderDirectory;
import org.sourceid.idff12.config.ProviderDirectoryFactory;
import org.sourceid.idff12.workflow.ProcessInfo;
import org.sourceid.workflow.WorkflowMediator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.security.*;
import java.security.cert.*;

/**
 * The servlet for initiating the Proxy Delegation process.
 * @author Henri Mikkonen
 * @version $Id: PDInitiateServlet.java,v 1.6 2006/05/22 12:16:02 mikkonen Exp $
 */
public class PDInitiateServlet extends HttpServlet {

    /**
     * The preferred profile.
     */
    public static final String PARAM_USE_PROFILE = "UseProfile";

    /**
     * The preferred GridCheque value (in integration with GridBank).
     */
    public static final String PARAM_CHEQUE_VALUE = "GridChequeValue";

    /**
     * The preferred GridCheque currency (in integration with GridBank).
     */
    public static final String PARAM_CHEQUE_CURRENCY = "GridChequeCurrency";

    /**
     * The URL where the user is redirected after a successful proxy delegation
     * process.
     */
    public static final String PARAM_SUCCESS_URL = "SuccessURL";

    /**
     * The URL where the user is redirected after an unsuccesful proxy
     * delegation process.
     */
    public static final String PARAM_FAILURE_URL = "FailureURL";

    /**
     * Process the HTTP Get method.
     * @param request The servlet request.
     * @param response The servlet response.
     * @throws ServletException If something goes wrong.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        AuthnAdapter authnAdapter = AdapterFactory.getAuthnAdapter();
        SessionStoreAdapter sessionStore = AdapterFactory.getSessionStoreAdapter();
        FederationStoreAdapter fedStore = AdapterFactory.getFederationStoreAdapter();
        String sessionId = authnAdapter.getSessionId(request, response);
        if (sessionId == null) {
            throw new ServletException("No session ID available to " + "initiate Proxy Delegation Protocol.");
        }
        Collection warrants = sessionStore.getWarrants(sessionId);
        if (warrants.size() != 1) {
            throw new ServletException("Too many or too few (" + warrants.size() + ") warrants found for this session!");
        }
        Warrant w = (Warrant) warrants.iterator().next();
        SessionInfo sinfo = sessionStore.getSession(sessionId);
        FederationInfo fedInfo = fedStore.getFederationInfo(sinfo.getPrincipalId(), w.getProviderId());
        String pdProfile = request.getParameter(PARAM_USE_PROFILE);
        if (Util.isEmpty(pdProfile)) {
            ProviderDirectory directory = ProviderDirectoryFactory.getDirectory();
            pdProfile = directory.getPreferredProfile(w.getProviderId(), ProfileEnum.PD_PROTOCOL, IdffRoleEnum.IDP);
        }
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyGen = null;
        KeyPair keys = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            keys = keyGen.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DelegationUtil deleg = new DelegationUtil();
        String proxyCertRequest = "";
        String proxyPrivateKey = "";
        try {
            proxyCertRequest = deleg.delegationRequest(fedInfo.getCertSubjects()[0]);
            proxyPrivateKey = deleg.getPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap params = new HashMap();
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_ROLE, IdffRoleEnum.SP);
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_PROFILE, pdProfile);
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_PROXY_CERT_REQUEST, proxyCertRequest);
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_IDP_NAMEID, fedInfo.getIdpNameId());
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_PROVIDER_ID, w.getProviderId());
        params.put(ProcessInfo.PROC_PD_INIT.ATTR_PROXY_PRIVATE_KEY, proxyPrivateKey);
        if (request.getParameter(PARAM_SUCCESS_URL) != null) {
            params.put(ProcessInfo.PROC_PD_INIT.ATTR_PD_SUCCESS_URI, request.getParameter(PARAM_SUCCESS_URL));
        }
        if (request.getParameter(PARAM_FAILURE_URL) != null) {
            params.put(ProcessInfo.PROC_PD_INIT.ATTR_PD_FAILURE_URI, request.getParameter(PARAM_FAILURE_URL));
        }
        if (request.getParameter(PARAM_CHEQUE_VALUE) != null && request.getParameter(PARAM_CHEQUE_CURRENCY) != null) {
            GridChequeRequestDocument chequeDoc = GridChequeRequestDocument.Factory.newInstance();
            GridChequeRequestType cheque = chequeDoc.addNewGridChequeRequest();
            cheque.setValue(request.getParameter(PARAM_CHEQUE_VALUE));
            cheque.setCurrency(request.getParameter(PARAM_CHEQUE_CURRENCY));
            params.put(ProcessInfo.PROC_PD_INIT.ATTR_PROXY_REQUEST_EXTENSION, chequeDoc.toString());
        }
        try {
            WorkflowMediator mediator = WorkflowMediator.getInstance();
            mediator.startProcessInstance(ProcessInfo.PROC_PD_INIT.getName(), params, request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
