package org.jvc.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.InputStreamResource;
import simple.http.Request;
import simple.http.Response;
import simple.http.load.BasicService;
import simple.http.serve.Context;

/**
 * This class is loaded by the Simple HTTP server.  This class constructs the
 * true JvcService using the Spring Framework.  We have to do it like this because
 * we can't get Spring initialization through Simple [HTTP server].
 *
 * This class delegates the process() calls to the contained JvcService instance.
 *
 * @see org.jvc.server.JvcService
 */
public class JvcServiceWrapper extends BasicService {

    private JvcService jvcService;

    /**
    * The wrapper constructor that uses Spring Framework to obtain the true
    * JvcService instance.
    *
    * @param context this is the Simple HTTP service context
    */
    public JvcServiceWrapper(Context context) {
        super(context);
        try {
            String confDir = System.getProperty("jvc.home");
            confDir = (confDir != null ? confDir + "/conf" : "./conf");
            File jvcFile = new File(confDir + "/jvc-service.xml");
            if (!jvcFile.exists()) {
                System.err.println("Unable to find jvc-service.xml configuration file.");
                System.exit(2);
            }
            InputStream is = new FileInputStream(confDir + "/jvc-service.xml");
            XmlBeanFactory factory = new XmlBeanFactory(new InputStreamResource(is));
            Object[] params = { context };
            jvcService = (JvcService) factory.getBean("jvcService", params);
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }

    /**
    * Delegate to the real JvcService.
    */
    protected void process(Request req, Response resp) throws Exception {
        jvcService.process(req, resp);
    }
}
