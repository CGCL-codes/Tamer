package it.ilz.hostingjava.util;

import it.ilz.hostingjava.model.paypal.Richieste;
import it.ilz.hostingjava.model.paypal.Risposte;
import it.ilz.hostingjava.util.file.GestioneFile;
import java.io.File;
import java.util.Properties;
import javax.servlet.ServletException;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *
 * @author  luigi
 */
public class Util {

    private static Util util = new Util();

    static {
        try {
            Properties props = new Properties();
            props.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
            props.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());
            Velocity.init(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *crea il nome del contesto dalla username
     * @param username 
     * @return 
     */
    public static String toContext(String username) {
        return "/-" + username;
    }

    /**
     *singleton
     * @return 
     * @throws javax.servlet.ServletException 
     */
    public static synchronized Util getUtil() throws ServletException {
        return util;
    }

    public static boolean elimnaContext(String ctxbase, String webappbase, String username) {
        boolean rt = true;
        try {
            String userxml = ctxbase + File.separatorChar + "-" + username + ".xml";
            File f = new File(userxml);
            rt = f.delete();
            if (!rt) return rt;
            String userapps = webappbase + File.separatorChar + "-" + username;
            rt = GestioneFile.cancellaCartella(userapps);
        } catch (Exception e) {
            rt = false;
            logger.error("\n" + e.getMessage(), e);
        }
        return rt;
    }

    public static String getEmail(String userid) {
        String email = "";
        Session session = null;
        try {
            session = it.ilz.hostingjava.util.HibernateHelper.apriSessione();
            email = (String) session.createQuery("select u.email from Userdata u where u.userid='" + userid + "'  ").setReadOnly(true).uniqueResult();
        } catch (Exception e) {
            logger.error("\n" + e.getMessage(), e);
        } finally {
            it.ilz.hostingjava.util.HibernateHelper.chiudiSessione(session);
        }
        return email;
    }

    public static Risposte getRichiesta(String transactionID) {
        Risposte rt = null;
        Session session = null;
        try {
            session = it.ilz.hostingjava.util.HibernateHelper.apriSessione();
            rt = (Risposte) session.createQuery("from Risposte r where r.transactionID='" + transactionID + "'  ").setReadOnly(true).uniqueResult();
        } catch (Exception e) {
            logger.error("\n" + e.getMessage(), e);
        } finally {
            it.ilz.hostingjava.util.HibernateHelper.chiudiSessione(session);
        }
        return rt;
    }

    private static Log logger = LogFactory.getLog(Util.class);
}
