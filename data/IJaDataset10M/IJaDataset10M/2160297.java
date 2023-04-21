package org.derigs.japff;

import java.io.IOException;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author juergen
 */
public class AuthBean {

    private Properties conf = null;

    private HttpSession session = null;

    private String configPath = null;

    private String headline = "";

    private String passwd = "";

    private String passwdDsc = "";

    private String login = "";

    private String loginDsc = "";

    private String loginButton = "";

    private String status = "";

    private String stackTrace = "";

    private Locale locale = null;

    private ResourceBundle rb = null;

    private SqlManager sqlManager = null;

    private Utils utils = null;

    private boolean stacktrace = false;

    /** Creates a new instance of StartBean */
    public AuthBean() throws ClassNotFoundException, SQLException, IOException {
        FacesContext fcontext = FacesContext.getCurrentInstance();
        conf = new Properties();
        configPath = fcontext.getExternalContext().getInitParameter("config");
        ServletContext context = (ServletContext) fcontext.getExternalContext().getContext();
        conf.load(new FileInputStream(context.getRealPath(configPath)));
        session = (HttpSession) fcontext.getExternalContext().getSession(true);
        locale = new Locale(conf.getProperty("locale"));
        rb = ResourceBundle.getBundle("org.derigs.japff.locales.auth", locale);
        headline = rb.getString("headline");
        passwdDsc = rb.getString("passwdDsc");
        loginDsc = rb.getString("loginDsc");
        loginButton = rb.getString("loginButton");
        stacktrace = (conf.getProperty("stacktrace").equals("true")) ? true : false;
        sqlManager = new SqlManager(conf);
        utils = new Utils(sqlManager, locale);
    }

    public String getHeadline() {
        return headline;
    }

    public String getPasswdText() {
        return passwd;
    }

    public void setPasswdText(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswdDsc() {
        return passwdDsc;
    }

    public String getLoginText() {
        return login;
    }

    public void setLoginText(String login) {
        this.login = login;
    }

    public String getLoginDsc() {
        return loginDsc;
    }

    public String getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(String loginButton) {
        this.loginButton = loginButton;
    }

    public String login() {
        String authSuccess = "false";
        try {
            String errorMesg = "";
            int error;
            if ((error = utils.stringIsNull(passwd)) != -1) {
                errorMesg += rb.getString("passwdField") + ": " + utils.errors[error] + " ";
            } else if ((error = utils.stringIsEmpty(passwd)) != -1) {
                errorMesg += rb.getString("passwdField") + ": " + utils.errors[error] + " ";
            }
            if ((error = utils.stringIsNull(login)) != -1) {
                errorMesg += rb.getString("loginField") + ": " + utils.errors[error] + " ";
            } else if ((error = utils.stringIsEmpty(login)) != -1) {
                errorMesg += rb.getString("loginField") + ": " + utils.errors[error] + " ";
            }
            String[] admins = conf.getProperty("admin").split("\\s");
            boolean admin = false;
            for (int i = 0; i < admins.length; i++) {
                if (admins[i].equals(login)) {
                    admin = true;
                }
            }
            if (!admin) {
                errorMesg += rb.getString("noAdmin");
                session.invalidate();
            } else {
                session.setAttribute("conf", conf);
            }
            if (errorMesg.length() > 0) {
                status = errorMesg;
                System.out.println(status);
                FacesContext context = FacesContext.getCurrentInstance();
                context.renderResponse();
            } else {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                md5.update(passwd.getBytes());
                byte[] result = md5.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    String hex = Integer.toHexString(0xFF & result[i]);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                authSuccess = (sqlManager.getPassword(login).equals(hexString.toString())) ? "true" : "false";
                if (authSuccess.equals("false")) session.invalidate();
            }
        } catch (NoSuchAlgorithmException nsae) {
            utils.catchExp(nsae);
            status = utils.getStatus();
            if (stacktrace) {
                stackTrace = utils.getStackTrace();
            }
            FacesContext.getCurrentInstance().renderResponse();
        } catch (SQLException sqle) {
            utils.catchExp(sqle);
            status = utils.getStatus();
            if (stacktrace) {
                stackTrace = utils.getStackTrace();
            }
            FacesContext.getCurrentInstance().renderResponse();
        }
        return authSuccess;
    }

    public String getStatus() {
        return status;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
