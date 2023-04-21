package uk.gov.dti.og.fox.command;

import java.net.InetAddress;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import uk.gov.dti.og.fox.App;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.XThread;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ContextUElem;
import uk.gov.dti.og.fox.Mod;
import uk.gov.dti.og.fox.ContextUCon;
import uk.gov.dti.og.fox.Fox;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.io.LDAP;
import uk.gov.dti.og.fox.security.AuthenticatedInfo;
import uk.gov.dti.og.fox.security.AuthenticationDescriptor;

public class UserLoginCommand extends AbstractCommand {

    /** An XPATH expression for the Web User Account ID of the user to log in. */
    private String wuaLoginIdExpr;

    /** An XPATH expression for the password of the account to log in. */
    private String passwordExpr;

    /** An XPATH expression for the status code to be assigned to */
    private String statusCodeExpr;

    /** An XPATH expression for the status message to be assigned to */
    private String statusMsgExpr;

    /** Authentication method */
    private String authMethodExpr;

    private String authDomainExpr;

    private static final String AUTH_METHOD_LDAP = "LDAP";

    /**
   * Contructs the command from the XML element specified.
   *
   * @param commandElement the element from which the command will
   *        be constructed.
   */
    public UserLoginCommand(Mod pMod, DOM commandElement) throws ExInternal {
        super(commandElement);
    }

    /**
   * Validates the user login command syntax.
   *
   * @param module the module where the component resides
   * @param commandElement the XML element that comprises the command
   * @throws ExInternal if the component syntax is invalid.
   */
    protected void validate(Mod module, DOM commandElement) throws ExInternal {
        wuaLoginIdExpr = commandElement.getAttrOrNull("wua-login-id");
        passwordExpr = commandElement.getAttrOrNull("password");
        statusCodeExpr = commandElement.getAttrOrNull("status-code");
        statusMsgExpr = commandElement.getAttrOrNull("status-message");
        authMethodExpr = commandElement.getAttrOrNull("authentication-method");
        authDomainExpr = commandElement.getAttrOrNull("authentication-domain");
        if (wuaLoginIdExpr == null) {
            throw new ExInternal("The " + getName() + " command has been used incorrectly in application \"" + module.getApp().getApplicationName() + "\" and module \"" + module.getName() + ": the command is missing the \"wua-login-id\" attribute that specifies an XPath expression for the Web User Account (WUA) Login Id of the user to login.");
        }
        if (passwordExpr == null) {
            throw new ExInternal("The " + getName() + " command has been used incorrectly in application \"" + module.getApp().getApplicationName() + "\" and module \"" + module.getName() + ": the command is missing the \"password\" attribute specifying an XPath expression for the password of the Web User Account (WUA) of the user to login.");
        }
        if ((statusCodeExpr != null && statusMsgExpr == null)) {
            throw new ExInternal("The " + getName() + " command has been used incorrectly in application \"" + module.getApp().getApplicationName() + "\" and module \"" + module.getName() + ": the command is missing the \"status-message\" attribute, but status-code is specified. You must declare status-message or remove status-code");
        }
        if ((statusMsgExpr != null && statusCodeExpr == null)) {
            throw new ExInternal("The " + getName() + " command has been used incorrectly in application \"" + module.getApp().getApplicationName() + "\" and module \"" + module.getName() + ": the command is missing the \"status-code\" attribute, but status-message is specified. You must declare status-code or remove status-message");
        }
    }

    /**
   * Runs the command with the specified user thread and session.
   *
   * @param userThread the user thread context of the command
   * @return userSession the user's session context
   */
    public void run(XThread userThread, ContextUElem contextUElem, ContextUCon contextUCon) throws ExInternal, ExActionFailed {
        try {
            String wuaLoginIdExprResult = contextUElem.extendedXPathResult(contextUElem.getUElem(ContextUElem.ATTACH), wuaLoginIdExpr).asString();
            String passwordExprResult = contextUElem.extendedXPathResult(contextUElem.getUElem(ContextUElem.ATTACH), passwordExpr).asString();
            DOM statusCodeDOM = null;
            DOM statusMsgDOM = null;
            String statusCode = null;
            String statusMsg = null;
            String authMethod = null;
            String authDomain = null;
            if (!XFUtil.isNull(authMethodExpr)) {
                authMethod = contextUElem.extendedStringOrXPathString(contextUElem.attachDOM(), authMethodExpr);
            }
            if (!XFUtil.isNull(authDomainExpr)) {
                authDomain = contextUElem.extendedStringOrXPathString(contextUElem.attachDOM(), authDomainExpr);
            }
            if (statusCodeExpr != null && statusMsgExpr != null) {
                statusCodeDOM = contextUElem.getCreateXPath1E(statusCodeExpr, ContextUElem.ATTACH);
                statusMsgDOM = contextUElem.getCreateXPath1E(statusMsgExpr, ContextUElem.ATTACH);
            }
            String clientInfo = "IP=" + InetAddress.getLocalHost().getHostAddress() + ", REMOTE-ADDR=" + userThread.getFoxRequest().getHttpRequest().getRemoteAddr();
            AuthenticationDescriptor loginDescriptor = null;
            if (!XFUtil.isNull(authMethod)) {
                if (authMethod.equals(AUTH_METHOD_LDAP)) {
                    if (!App.mLDAPConnectionMap.containsKey(authDomain)) {
                        throw new ExInternal("No LDAP connection with name \"" + authDomain + "\" was found.");
                    }
                    AuthenticatedInfo lInfo = (AuthenticatedInfo) App.mLDAPConnectionMap.get(authDomain);
                    String lUserDN = (String) lInfo.mLDAPConnectionAttrs.get("user-dn-attr") + "=" + wuaLoginIdExprResult + "," + (String) lInfo.mLDAPConnectionAttrs.get("base-dn");
                    try {
                        String lHost = (String) lInfo.mLDAPConnectionAttrs.get("host");
                        lHost = lHost.indexOf("ldap://") == -1 ? "ldap://" + lHost : lHost;
                        lHost += ":" + XFUtil.nvl((String) lInfo.mLDAPConnectionAttrs.get("port"), "389");
                        LDAP lLDAPConnection = new LDAP(lHost, lUserDN, passwordExprResult);
                        String lSourceDN = (String) lInfo.mLDAPConnectionAttrs.get("auth-source-attr") + "=" + wuaLoginIdExprResult + "," + (String) lInfo.mLDAPConnectionAttrs.get("auth-source-base-dn");
                        DOM lLDAPResults = lLDAPConnection.getAttributesXMLFromDN(lSourceDN, null);
                        lInfo.processLDAPResult(lLDAPResults);
                        loginDescriptor = Fox.getAuthProvider().authenticateSSO(contextUCon.getUCon(), clientInfo, lInfo.getLoginId(), lInfo, authMethod, authDomain);
                        statusCode = loginDescriptor.getCode();
                        statusMsg = loginDescriptor.getMessage();
                    } catch (AuthenticationException ae) {
                        statusCode = "INVALID";
                        if (ae.getMessage().indexOf("Invalid Credentials") != -1) {
                            statusMsg = "Invalid username or password";
                        } else {
                            statusMsg = "An unexpected error has occurred.";
                        }
                    } catch (NamingException ne) {
                        statusCode = "INVALID";
                        statusMsg = "An unexpected error has occurred.";
                    }
                } else {
                    throw new ExInternal("Invalid authentication method, \"" + authMethod + "\" specified");
                }
            } else {
                loginDescriptor = Fox.getAuthProvider().login(contextUCon.getUCon(), clientInfo, wuaLoginIdExprResult, passwordExprResult);
                statusCode = loginDescriptor.getCode();
                statusMsg = loginDescriptor.getMessage();
            }
            if (loginDescriptor != null && loginDescriptor.getSessionId() != null) {
                String sessionId = loginDescriptor.getSessionId();
                userThread.getFoxRequest().addCookie("p_dti_session_id", sessionId, false);
                userThread.setUserPortalSessionId(sessionId);
                userThread.mForceReauthentication = true;
                userThread.reauthenticateAndAuthoriseUser(contextUCon.getUCon());
            }
            if (statusCodeDOM != null && statusMsgDOM != null) {
                statusCodeDOM.setText(statusCode);
                statusMsgDOM.setText(statusMsg);
            } else if (!"VALID".equals(statusCode)) {
                throw new ExActionFailed(statusCode, statusMsg);
            }
        } catch (ExActionFailed af) {
            throw af;
        } catch (Throwable th) {
            throw new ExInternal("Unable to log in user: ", th);
        }
    }

    public boolean isCallTransition() {
        return false;
    }
}
