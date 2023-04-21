package org.exist.http.servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.xml.DOMConfigurator;
import org.exist.util.Configuration;
import org.exist.util.ConfigurationHelper;
import org.exist.util.DatabaseConfigurationException;
import org.exist.external.org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * Helper servlet for initializing the log4j framework in a webcontainer.
 */
public class Log4jInit extends HttpServlet {

    private static final long serialVersionUID = 2358611360404852183L;

    private String getTimestamp() {
        return new Date().toString();
    }

    private void convertLogFile(File srcConfig, File destConfig, File logDir) {
        String srcDoc = "not initialized";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream is = new FileInputStream(srcConfig);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            is.close();
            baos.close();
            srcDoc = new String(baos.toByteArray());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String destDoc = srcDoc.replaceAll("loggerdir", logDir.getAbsolutePath().replaceAll("\\\\", "/"));
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(destDoc.getBytes());
            FileOutputStream fos = new FileOutputStream(destConfig);
            byte[] buf = new byte[1024];
            int len;
            while ((len = bais.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.close();
            bais.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initialize servlet for log4j purposes in servlet container (war file).
     */
    @Override
    public void init() throws ServletException {
        if (!isInWarFile()) {
            System.out.println("Logging already initialized. Skipping...");
            return;
        }
        System.out.println("============= eXist Initialization =============");
        String file = getInitParameter("log4j-init-file");
        String logdir = getInitParameter("log4j-log-dir");
        if (logdir == null) {
            logdir = "WEB-INF/logs";
        }
        String existDir = getServletContext().getRealPath("/");
        File logsdir = new File(existDir, logdir);
        logsdir.mkdirs();
        System.out.println(getTimestamp() + " - eXist logs dir=" + logsdir.getAbsolutePath());
        File srcConfigFile = new File(existDir, file);
        File log4jConfigFile = new File(existDir, "WEB-INF/TMPfile.xml");
        convertLogFile(srcConfigFile, log4jConfigFile, logsdir);
        System.out.println(getTimestamp() + " - eXist log4j configuration=" + log4jConfigFile.getAbsolutePath());
        DOMConfigurator.configure(log4jConfigFile.getAbsolutePath());
        File eXistConfigFile = new File(existDir, "WEB-INF/conf.xml");
        System.out.println(getTimestamp() + " - eXist-DB configuration=" + eXistConfigFile.getAbsolutePath());
        try {
            new Configuration(eXistConfigFile.getAbsolutePath());
        } catch (DatabaseConfigurationException ex) {
            ex.printStackTrace();
        }
        System.out.println("================================================");
    }

    /**
     *  Check wether exist runs in Servlet container (as war file).
     * @return TRUE if exist runs in servlet container.
     */
    public boolean isInWarFile() {
        boolean retVal = true;
        if (new File(ConfigurationHelper.getExistHome(), "lib/core").isDirectory()) {
            retVal = false;
        }
        return retVal;
    }

    /**
     *  Empty method.
     *
     * @param req HTTP Request object
     * @param res HTTP Response object
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
    }
}
