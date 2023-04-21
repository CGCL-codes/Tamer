package com.afp.medya.importer;

import java.io.File;
import java.io.IOException;
import org.afp.util.digester.DigesterConfigReader;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.xml.sax.SAXException;
import com.afp.medya.importer.config.SpringConfig;
import com.afp.medya.importer.interfaces.ICataloger;
import com.afp.medya.importer.process.Taskes;
import com.afp.medya.importer.util.FileHelper;

/**
 * @author fboltz
 * 
 */
public class Application {

    private String m_email;

    private String m_smtpHost;

    private boolean m_bDebug;

    private boolean mvar_RunningAsDeamon;

    private ICataloger mvar_Cataloguer;

    private Taskes mvar_Taskes;

    private String mvar_CatalogerBean;

    private String mvar_ApplicationConfig = FileHelper.MatchHomeDirectory("~/conf/app.config");

    private static String mvar_ImporterHome;

    private static Log4JLogger mvar_Logger;

    private static Application mvar_CurrentApplication;

    private static String mvar_RootSpringConfig = "com/afp/medya/importer/config/beans.xml";

    static boolean validPath(String path) {
        if (path == null || path.length() == 0) return false;
        return new File(path).exists();
    }

    static {
        Application.getLogger();
        mvar_ImporterHome = System.getProperty("newsml-importer.home");
        if (validPath(mvar_ImporterHome) == false) mvar_ImporterHome = System.getenv("NEWSML_IMPORTER_HOME");
        if (validPath(mvar_ImporterHome) == false) mvar_ImporterHome = System.getProperty("user.dir");
        if (validPath(mvar_ImporterHome) == false) mvar_ImporterHome = new File(".").getAbsolutePath();
    }

    public static Log4JLogger getLogger() {
        if (mvar_Logger == null) {
            mvar_Logger = new Log4JLogger(Application.class.getPackage().getName());
        }
        return mvar_Logger;
    }

    public static void main(String[] args) {
        Application process = null;
        try {
            process = (Application) SpringConfig.getBeanFactory(Application.getRootSpringConfig()).getBean("Application");
            process.Run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Application() throws IOException, SAXException {
        super();
        if (Application.mvar_CurrentApplication == null) {
            Application.mvar_CurrentApplication = this;
        } else {
            throw new RuntimeException("ProcessFilter instance already running");
        }
    }

    /**
	 * MÃ©thode appelÃ© par la servlet frontal de l'application<br>
	 * ExÃ©cutÃ© par un nouve au thread
	 * 
	 * @param nom
	 * @param email
	 * @throws Exception
	 *             setRunning
	 */
    public void Run() throws Exception {
        getTaskes().RunTaskes();
    }

    /**
	 * envoi un mail dans il se passe un Ã©vÃ©nement (success ou erreur)
	 * 
	 * @param stradrmail
	 * @param strname
	 * @param msg
	 */
    public void sendMail(String msg) {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(this.getSmtpHost());
        try {
            if (this.getEmail() == null) throw new IllegalArgumentException("m_email parameter can't be null");
            email.addTo(this.getEmail());
            email.setFrom("noreply@afp.com", "newsml-batch-filter");
            email.setSubject("Fin du process ...");
            email.setMsg(msg);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return m_email;
    }

    /**
	 * 
	 * @param email
	 */
    public void setEmail(String email) {
        m_email = email;
    }

    /**
	 * @return the m_smtpHost
	 */
    public String getSmtpHost() {
        return m_smtpHost;
    }

    /**
	 * @param host
	 *            the m_smtpHost to set
	 */
    public void setSmtpHost(String host) {
        m_smtpHost = host;
    }

    /**
	 * @return the m_bDebug
	 */
    public boolean isDebug() {
        return m_bDebug;
    }

    /**
	 * @param debug
	 *            the m_bDebug to set
	 */
    public void setDebug(boolean debug) {
        m_bDebug = debug;
    }

    /**
	 * @return the mvar_CurrentProcessFilter
	 */
    public static Application currentApplication() {
        return mvar_CurrentApplication;
    }

    /**
	 * @return the mvar_RunningAsDeamon
	 */
    public boolean isRunningAsDeamon() {
        return mvar_RunningAsDeamon;
    }

    /**
	 * @param mvar_RunningAsDeamon
	 *            the mvar_RunningAsDeamon to set
	 */
    public void setRunningAsDeamon(boolean mvar_RunningAsDeamon) {
        this.mvar_RunningAsDeamon = mvar_RunningAsDeamon;
    }

    /**
	 * @return the mvar_Cataloguer
	 */
    public ICataloger getCataloguer() {
        if (mvar_Cataloguer == null) mvar_Cataloguer = (ICataloger) SpringConfig.getBeanFactory(Application.getRootSpringConfig()).getBean(this.getCatalogerBean());
        return mvar_Cataloguer;
    }

    /**
	 * @return the mvar_Taskes
	 * @throws Exception
	 * @throws IOException
	 */
    public Taskes getTaskes() throws IOException, Exception {
        if (mvar_Taskes == null) {
            String appConfig = getAppConfig();
            System.err.println(String.format("Load taskes from file:%s", appConfig));
            mvar_Taskes = (Taskes) DigesterConfigReader.configure(new File(FileHelper.MatchHomeDirectory("~/conf/config-digester-rules.xml")), new File(appConfig), Thread.currentThread().getContextClassLoader());
        }
        return mvar_Taskes;
    }

    public static String getImporterHome() {
        return mvar_ImporterHome;
    }

    public String getCatalogerBean() {
        return mvar_CatalogerBean;
    }

    public void setCatalogerBean(String catalogerBean) {
        this.mvar_CatalogerBean = catalogerBean;
    }

    public String getAppConfig() {
        return mvar_ApplicationConfig;
    }

    public void setAppConfig(String applicationConfig) {
        this.mvar_ApplicationConfig = FileHelper.MatchHomeDirectory(applicationConfig);
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            System.err.println("Cannot access thread context ClassLoader - falling back to system class loader" + ex.toString());
        }
        if (cl == null) {
            cl = Application.class.getClassLoader();
        }
        return cl;
    }

    public static String getRootSpringConfig() {
        return mvar_RootSpringConfig;
    }
}
