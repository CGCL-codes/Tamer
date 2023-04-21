package csiebug.web.webapp;

import java.io.UnsupportedEncodingException;
import javax.naming.NamingException;
import csiebug.service.ServiceLockException;
import csiebug.util.InvalidInputException;
import csiebug.util.WebUtility;

/**
 * @author George_Tsai
 * @version 2010/2/11
 */
public abstract class BasicAction extends WebUtility {

    private static final long serialVersionUID = 1L;

    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String SUSPEND = "suspend";

    public static final String INPUT = "input";

    public static final String LOGIN = "login";

    public static final String NONE = "none";

    public static final String FORMLOAD = "formload";

    public static final String NOPERMISSION = "nopermission";

    public static final String REDIRECT = "redirect";

    public static final String ADD = "add";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    public static final String VIEW = "view";

    public static final String EDIT = "edit";

    public static final String READONLY = "readonly";

    public String execute() {
        try {
            resetPageLoadMessage();
            resetPageLoadScript();
            if (validateInput()) {
                makePrePageURL();
                return main();
            } else {
                throw new InvalidInputException("Invalid input data!!");
            }
        } catch (InvalidInputException iiex) {
            writeErrorLog(iiex);
            try {
                addPageLoadWarningDialog(getMessage("common.error.InvalidInput"));
                return FORMLOAD;
            } catch (Exception ex) {
                writeErrorLog(ex);
                return ERROR;
            }
        } catch (java.lang.reflect.UndeclaredThrowableException ex) {
            Throwable throwable = ex.getCause();
            while (throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            if (throwable.getClass().equals(ServiceLockException.class)) {
                return SUSPEND;
            } else {
                ex.printStackTrace();
                writeErrorLog(ex);
                return ERROR;
            }
        } catch (ServiceLockException slex) {
            return SUSPEND;
        } catch (Exception ex) {
            ex.printStackTrace();
            writeErrorLog(ex);
            return ERROR;
        }
    }

    /**
	 * 無權限,導回登入頁,並提示訊息
	 * @return
	 * @throws Exception
	 * @author George_Tsai
	 * @version 2009/3/12
	 * @throws UnsupportedEncodingException 
	 */
    public String getNoPermissionForward() throws NamingException, UnsupportedEncodingException {
        return getNoPermissionForward(getMessage("common.NoPermissionForwardToLogin"));
    }

    /**
	 * 無權限,導回前頁,並提示訊息
	 * @return
	 * @throws Exception
	 * @author George_Tsai
	 * @version 2009/11/25
	 * @throws UnsupportedEncodingException 
	 */
    public String getNoPermissionForwardPrePage(String prePageURL) throws NamingException, UnsupportedEncodingException {
        return getNoPermissionForward(getMessage("common.NoPermission"), prePageURL);
    }

    /**
	 * TimeOut,導回登入頁,並提示訊息
	 * @return
	 * @throws Exception
	 * @author George_Tsai
	 * @version 2009/3/12
	 * @throws UnsupportedEncodingException 
	 */
    public String getSessionTimeOutForward() throws NamingException, UnsupportedEncodingException {
        return getNoPermissionForward(getMessage("common.SessionTimeOut"));
    }

    /**
	 * 無權限,導回登入頁,並提示自訂訊息
	 * @param msg
	 * @return
	 * @throws Exception
	 * @author George_Tsai
	 * @version 2009/2/17
	 * @throws NamingException 
	 */
    public String getNoPermissionForward(String msg) throws NamingException {
        addPageLoadMessage(msg);
        addPageLoadScript("parent.parent.parent.location.href = \"" + getSysURL() + "\";");
        return NOPERMISSION;
    }

    /**
	 * 無權限,導回前一頁,並提示自訂訊息
	 * @param msg
	 * @param prePageURL
	 * @return
	 * @throws Exception
	 * @author George_Tsai
	 * @version 2009/11/25
	 * @throws NamingException 
	 */
    public String getNoPermissionForward(String msg, String prePageURL) throws NamingException {
        addPageLoadMessage(msg);
        addPageLoadScript("parent.parent.parent.location.href = \"" + prePageURL + "\";");
        return NOPERMISSION;
    }

    public abstract String main() throws Exception;
}
