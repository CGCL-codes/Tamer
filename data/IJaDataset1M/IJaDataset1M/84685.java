package addressbook.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import rogatkin.ResourceException;
import rogatkin.ResourceManager;
import rogatkin.servlet.BaseFormProcessor;
import rogatkin.servlet.Dispatcher;
import rogatkin.servlet.Substitutor;
import rogatkin.servlet.TreeViewHelper;
import addressbook.servlet.model.AbstractOperations;
import addressbook.servlet.model.AttachmentOperations;
import addressbook.servlet.model.AttributeStorage;
import addressbook.servlet.model.CipherOperations;
import addressbook.servlet.model.ContactOperations;
import addressbook.servlet.model.EmailValidatorOperations;
import addressbook.servlet.model.FolderOperations;
import addressbook.servlet.model.LogOperations;
import addressbook.servlet.model.PersonalFolderOperations;
import addressbook.servlet.model.UserOperations;

public abstract class AddressBookProcessor extends BaseFormProcessor {

    public static final String P_FOLDER = "folder";

    public static final String P_HASH = "hash";

    public static final String P_NODE = "nodeid";

    public static final String P_DELETE = "delete";

    public static final String P_SHARE = "share";

    public static final String P_GET_THEM = "getthem";

    public static final String P_SEARCH = "search";

    static final String P_MODE = "mode";

    public static final String V_CONTACT = "contact";

    public static final String V_TYPE = "type";

    public static final String V_INDEX = "index";

    public static final String V_READONLY = "readonly";

    public static final String USERRECORD = "addressbook.servlet.user_records";

    public static final String EMAILVALIDATOR = "addressbook.servlet.email_validator";

    public static final String CONTACTOPER = "addressbook.servlet.contact_operations";

    public static final String FOLDEROPER = "addressbook.servlet.folder_operations";

    public static final String CIPHEROPER = "addressbook.servlet.cipher_operations";

    public static final String LOGOPER = "addressbook.servlet.log_operations";

    public static final String ATTR_SESSION_FOLDER_OPER = FOLDEROPER;

    /** used to store in session last login
	 * 
	 */
    public static final String ATTR_LASTLOGIN = "lastlogin";

    public static final String ABANDONED_AFTER = "ABANDONED_AFTER";

    protected UserOperations userOperationsCache;

    protected EmailValidatorOperations emailValidatorOperations;

    protected ContactOperations contactOperationsCache;

    protected FolderOperations folderOperationscache;

    protected CipherOperations cipherOperationsCache;

    protected LogOperations logOperationsCache;

    public HttpSession getSession() {
        return super.getSession();
    }

    protected UserOperations getUPOperations() {
        if (userOperationsCache != null) return userOperationsCache;
        userOperationsCache = getOperations(USERRECORD, UserOperations.class);
        return userOperationsCache;
    }

    protected EmailValidatorOperations getEmailValidatorOperations() {
        if (emailValidatorOperations != null) return emailValidatorOperations;
        emailValidatorOperations = getOperations(EMAILVALIDATOR, EmailValidatorOperations.class);
        return emailValidatorOperations;
    }

    protected ContactOperations getContactOperations() {
        if (contactOperationsCache != null) return contactOperationsCache;
        contactOperationsCache = getOperations(CONTACTOPER, ContactOperations.class);
        return contactOperationsCache;
    }

    public FolderOperations getFolderOperations() {
        HttpSession s = getSession();
        if (s == null) return getFolderOperations(null);
        return getFolderOperations((String) s.getAttribute(HV_USER_ID));
    }

    public FolderOperations getFolderOperations(String areaId) {
        if (folderOperationscache == null) folderOperationscache = getOperations(FOLDEROPER, FolderOperations.class);
        if (areaId != null) return new PersonalFolderOperations(areaId, folderOperationscache); else return folderOperationscache;
    }

    public AttachmentOperations getAttachmentOperations() {
        AttachmentOperations result = new AttachmentOperations();
        result.init(this);
        return result;
    }

    public CipherOperations getCipherOperations() {
        if (cipherOperationsCache != null) return cipherOperationsCache;
        cipherOperationsCache = getOperations(CIPHEROPER, CipherOperations.class);
        return cipherOperationsCache;
    }

    public LogOperations getLogOperations() {
        if (logOperationsCache != null) return logOperationsCache;
        logOperationsCache = getOperations(LOGOPER, LogOperations.class);
        return logOperationsCache;
    }

    protected <T extends AbstractOperations> T getOperations(String opersName, Class<T> cls) {
        T resultCache = null;
        synchronized (dispatcher) {
            resultCache = (T) dispatcher.getServletContext().getAttribute(opersName);
            if (resultCache != null) return resultCache;
            try {
                resultCache = (T) cls.newInstance();
                resultCache.init(this);
                dispatcher.getServletContext().setAttribute(opersName, resultCache);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultCache;
    }

    protected PrintWriter producePage(HttpServletRequest _req, HttpServletResponse _resp, HttpSession _session, Map _pageData, String _templateName, final Locale _locale) throws IOException, java.security.AccessControlException {
        if (_pageData != null) try {
            if (_session != null) _pageData.put(Substitutor.SESSION_ENTRY, _session);
            String referer = req.getHeader(Dispatcher.REFERER);
            _pageData.put("back", referer != null ? referer : getCancelPage());
            _pageData.put("commonlabel", ResourceManager.getResourceManager(ResourceManager.RESOURCE_RES).getResource("commonlabels", new ResourceManager.LocalizedRequester() {

                public Locale getLocale() {
                    return _locale;
                }

                public String getEncoding() {
                    return getCharSet();
                }

                public TimeZone getTimeZone() {
                    return AddressBookProcessor.this.getTimeZone();
                }
            }));
        } catch (ResourceException e) {
            log("No common res found:" + e, null);
        }
        return super.producePage(_req, _resp, _session, _pageData, _templateName, _locale);
    }

    protected void fillWithForm(AttributeStorage as, String... strings) {
        for (String ffieldName : strings) as.setAttribute(ffieldName, getStringParameterValue(ffieldName, null, 0));
    }

    protected void fillWithFormFilled(AttributeStorage as, String... strings) {
        for (String ffieldName : strings) {
            String value = getStringParameterValue(ffieldName, "", 0);
            if (value.length() > 0) as.setAttribute(ffieldName, value);
        }
    }

    protected Map fillWithForm(Map data, String... strings) {
        for (String ffieldName : strings) data.put(ffieldName, getStringParameterValue(ffieldName, null, 0));
        return data;
    }

    protected String fillUrlWithForm(String... strings) {
        StringBuffer result = new StringBuffer();
        for (String ffieldName : strings) try {
            result.append(ffieldName).append('=').append(URLEncoder.encode(getStringParameterValue(ffieldName, "", 0), "UTF-8")).append('&');
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    protected Map createErrorMap(String errorId) {
        return createErrorMap(errorId, null);
    }

    protected Map fillMessage(Map map, String field, String msgId, Object[] details) {
        if (map == null) map = new HashMap(4);
        if (details == null) map.put(field, getResourceString(msgId, msgId, getLocale())); else map.put(field, MessageFormat.format(getResourceString(msgId, msgId, getLocale()), details));
        return map;
    }

    protected Map createErrorMap(String errorId, Object[] details) {
        return fillMessage(null, HV_ERROR, errorId, details);
    }

    protected String treeStateEncode(String url, String stateKeeperParamname, String treeId) {
        String[] states = getParameterValues(TreeViewHelper.HV_STATE);
        if (states != null) {
            String pn = "&state" + treeId + '=';
            for (String state : states) try {
                url += pn + URLEncoder.encode(state, "utf-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return url;
    }

    protected String getCharSet() {
        return "UTF-8";
    }

    protected String getExpiredPage() {
        return "Login";
    }

    protected String getTemplateName() {
        return getResourceName() + ".htm";
    }

    protected void log(String message, Throwable t) {
        if (t == null) dispatcher.log("[" + getClass().getName() + "] " + message); else dispatcher.log("[" + getClass().getName() + "] " + message, t);
    }

    public String getDataRoot() {
        String dataRoot = getProperty("DATAROOT");
        if (dataRoot == null || dataRoot.length() == 0) dataRoot = System.getProperty("user.home");
        return dataRoot;
    }
}
