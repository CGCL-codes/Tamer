package com.hyper9.vangaea.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Query;
import org.hibernate.Session;
import com.hyper9.common.text.MD5;
import com.hyper9.uvapi.connectors.HyperVConnector;
import com.hyper9.uvapi.connectors.VMwareConnector;
import com.hyper9.uvapi.connectors.XenConnector;
import com.hyper9.uvapi.types.ConnectorBean;
import com.hyper9.vangaea.impls.beans.admin.ConnectorTypeBeanImpl;
import com.hyper9.vangaea.impls.beans.admin.RoleBeanImpl;
import com.hyper9.vangaea.impls.beans.admin.SettingBeanImpl;
import com.hyper9.vangaea.impls.beans.admin.UserBeanImpl;
import com.hyper9.vangaea.types.beans.admin.ConnectorTypeBean;
import com.hyper9.vangaea.types.beans.admin.RoleBean;
import com.hyper9.vangaea.types.beans.admin.SettingBean;
import com.hyper9.vangaea.types.beans.admin.UserBean;

/**
 * The servlet responsible for initializing the application.
 * 
 * @author akutz
 * 
 */
public class InitApplicationServlet extends HttpServlet implements Servlet {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -7757451998876424832L;

    private ServletContext cxt;

    private static Properties props;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.cxt = getServletContext();
        initJaas();
        try {
            loadPropertiesFile();
        } catch (Exception e) {
            this.cxt.log("error initializing properties", e);
        }
        try {
            initDB();
        } catch (Exception e) {
            this.cxt.log("error initializing database", e);
        }
        initSysProps();
    }

    private void initSysProps() {
        if (props == null) {
            return;
        }
        for (Object k : props.keySet()) {
            if (System.getProperties().contains(k)) {
                props.put(k, System.getProperties().get(k));
            }
        }
    }

    private void initDB() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query iq = session.createQuery("from SettingBeanImpl where skey='com.hyper9.vangaea.db.initialized' and sval='true'");
        if (iq.list().size() == 1) {
            initSettings(session);
            session.getTransaction().commit();
            return;
        }
        addSetting(session, "jaas.loginContext", "Vangaea");
        addSetting(session, "login.expires", "1800");
        addSetting(session, "db.initialized", "true");
        addSetting(session, "rest.params.prefix", "com.hyper9.vangaea.rest.params");
        addSetting(session, "rest.params.keys.authToken", "authtoken");
        addSetting(session, "rest.params.keys.userName", "username");
        addSetting(session, "rest.params.keys.password", "password");
        addSetting(session, "rest.params.keys.encDecKey", "encdeckey");
        addSetting(session, "uris.rest.rootPath", "r");
        addSetting(session, "uris.rest.rootPath.virtualization", "v");
        RoleBean roleAdmin = addRole(session, "admin");
        UserBean admin = addUser(session, "admin", "password", "decrypt");
        roleAdmin.setUsers(Arrays.asList(admin));
        addConnectorType(session, VMwareConnector.class);
        addConnectorType(session, HyperVConnector.class);
        addConnectorType(session, XenConnector.class);
        String path = getFilePath("importSql", "/src/main/config/import.sql");
        if (path == null) {
            session.getTransaction().commit();
            return;
        }
        File f = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(f.getAbsolutePath()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("--") || line.equals("")) {
                continue;
            }
            Query q = session.createSQLQuery(line);
            q.executeUpdate();
        }
        in.close();
        session.getTransaction().commit();
    }

    private void addSetting(Session session, String key, String value) {
        key = "com.hyper9.vangaea." + key;
        SettingBean sb = new SettingBeanImpl();
        sb.setKey(key);
        sb.setValue(value);
        session.save(sb);
        props.setProperty(key, value);
    }

    private void initSettings(Session session) {
        String q = "from " + SettingBeanImpl.class.getSimpleName();
        List<?> sbs = session.createQuery(q).list();
        for (Object o : sbs) {
            SettingBean sb = (SettingBean) o;
            props.setProperty(sb.getKey(), sb.getValue());
        }
    }

    private UserBean addUser(Session session, String userName, String passphrase, String decrypt) throws Exception {
        UserBean ub = new UserBeanImpl();
        ub.setUserName(userName);
        ub.setPassphrase("MD5:" + MD5.hash(passphrase));
        ub.setKeyHash(MD5.hash(decrypt));
        session.save(ub);
        return ub;
    }

    private RoleBean addRole(Session session, String roleName) {
        RoleBean rb = new RoleBeanImpl();
        rb.setRoleName(roleName);
        session.save(rb);
        return rb;
    }

    private <T extends ConnectorBean> void addConnectorType(Session session, Class<T> clazz) {
        ConnectorTypeBean ctb = new ConnectorTypeBeanImpl();
        ctb.setPackageAndClassName(clazz.getName());
        session.save(ctb);
    }

    private String getFilePath(String propName, String absPath) {
        String path = getInitParameter(propName);
        if (path == null) {
            path = new File("").getAbsolutePath() + absPath;
        }
        if (doesFileExist(path)) {
            this.cxt.log("using file=" + path);
        } else {
            this.cxt.log("cannot find file");
        }
        return path;
    }

    private void initJaas() {
        String path = getFilePath("securityConfigFile", "/src/main/config/security.config");
        System.getProperties().put("java.security.auth.login.config", path);
    }

    private void loadPropertiesFile() {
        String path = getFilePath("propertiesFile", "/src/main/config/vangaea.properties");
        File f = new File(path);
        this.cxt.log("configuring log4j logging");
        PropertyConfigurator.configure(f.getPath());
        this.cxt.log("configuring java logging");
        try {
            LogManager lm = java.util.logging.LogManager.getLogManager();
            FileInputStream vmmPropsFileFIS = new FileInputStream(f);
            lm.readConfiguration(vmmPropsFileFIS);
        } catch (final Exception e) {
            this.cxt.log(String.format("error configuring java logging; msg=%s", e.getMessage()));
        }
        try {
            this.cxt.log("loading properties");
            props = new Properties();
            props.load(new FileInputStream(f.getPath()));
        } catch (final Exception e) {
            this.cxt.log("error loading properties", e);
        }
    }

    /**
     * Gets the configured properties.
     * 
     * @return The configured properties.
     */
    public static Properties getProperties() {
        return props;
    }

    private boolean doesFileExist(String path) {
        File f = new File(path);
        return f.exists();
    }
}
