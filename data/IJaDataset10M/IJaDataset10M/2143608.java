package gmusic.servlet;

import gmusic.thread.GMusicBGProcess;
import java.io.File;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import algutil.system.Systeme;

public class InitServlet extends HttpServlet {

    private GMusicBGProcess bg;

    private static final Logger log = Logger.getLogger(InitServlet.class);

    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        File log4jFilePath = new File(prefix + File.separator + file);
        if (log4jFilePath.exists()) {
            DOMConfigurator.configure(log4jFilePath.getPath());
            log.info("[GMUSIC_INIT] Le fichier " + log4jFilePath + " existe");
        } else {
            System.err.println("[GMUSIC_INIT] Le fichier " + log4jFilePath + " n'existe pas!");
        }
        if (Systeme.getOS() == Systeme.WINDOWS_OS) {
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxyHost", "172.16.1.138");
            systemProperties.setProperty("http.proxyPort", "3128");
        }
        bg = new GMusicBGProcess();
        bg.start();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }

    @Override
    public void destroy() {
        if (bg != null) {
            bg.stopThread();
        }
        super.destroy();
    }
}
