package radar.admin;

import java.util.*;
import java.io.*;
import javax.servlet.http.*;

import org.apache.tomcat.core.Request;
import org.apache.tomcat.core.FacadeManager;
import org.apache.tomcat.core.Context;
import org.apache.tomcat.core.ContextManager;
import org.apache.tomcat.util.RequestUtil;

import radar.util.Debug;
import radar.config.webapp.WebApp;
import radar.config.webapp.WebAppConfigException;

/**
 * A context administration class. Contexts can be
 * viewed, added, and removed from the context manager.
 *
 */
public class ContextAdmin {

    private ContextManager cm;
    private Request realRequest;
   
    public void init(HttpServletRequest request) {
        // Taken from the 'admin' webapp example in tomcat 3.2
        FacadeManager facadeM=(FacadeManager)request.getAttribute(FacadeManager.FACADE_ATTRIBUTE);
        if (facadeM == null) {
            realRequest = null;
            cm = null;
            return;
        }
        
	    realRequest = facadeM.getRealRequest(request);
    	cm = realRequest.getContext().getContextManager();
    }

    public Enumeration getContextNames() {
        return (Enumeration) cm.getContextNames();
    }
    
    public Context getContext(String name) {
        return cm.getContext(name);
    }
    
    public Enumeration getContexts() {
        return cm.getContexts();
    }

    public void addContext(String contextPath, 
        String docBase, 
        int debug, 
        boolean reloadable, 
        boolean createFiles) 
            throws ContextAdminException {

        if ((contextPath == null) && (docBase == null)) {
            throw new ContextAdminException("Context values 'path' and 'docBase' cannot be null");
        }
        
        Context context = new Context();
        context.setContextManager(cm);
        context.setPath(contextPath);
        context.setDocBase(docBase);
        context.setDebug(debug);
        context.setReloadable(reloadable);

	    try {
            cm.addContext(context);
            cm.initContext(context);
	    }
	    catch(org.apache.tomcat.core.TomcatException e) {
            e.printStackTrace();
            throw new ContextAdminException(e.getMessage());
	    }

        if (createFiles) {
            Debug.println("Creating webapp files");
            String fs = System.getProperty("file.separator");
            
            String realDocBase = "";
            File docFile = new File(docBase);
            if (!docFile.isAbsolute()) {
                realDocBase = System.getProperty("tomcat.home") 
                    + fs
                    + docBase;
            }
            else {
                realDocBase = docBase;
            }
            
            docFile = new File(realDocBase + fs + "WEB-INF" + fs + "classes");

            // Create docBase directory
            if (!docFile.exists()) {
                if (!docFile.mkdirs()) {
                    throw new ContextAdminException("Unable to create webapp directory: " + docFile);
                }
                Debug.println("Mkdirs for " + docFile + " succeeded");
            }

            File webXMLFile = new File(realDocBase + fs + "WEB-INF" + fs + "web.xml");

            // Create web.xml
            if (!webXMLFile.exists()) {
                WebApp wa = new WebApp();
                wa.setContext(contextPath);
                wa.setDocBase(docBase);
                try {
                    wa.write();
                }
                catch (WebAppConfigException e) {
                    throw new ContextAdminException("Unable to create file: " + webXMLFile);
                }
                Debug.println("Creation of " + webXMLFile + " succeeded");
            }

            try {
                    
                // Create sample index.jsp
                PrintWriter out = new PrintWriter(
                    new FileWriter(realDocBase + fs + "index.jsp"));

                out.println("<html><head><title>Welcome</title></head>");
                out.println("<body>");
                out.print("<p><h2>Sample welcome JSP for context ");
                out.print(contextPath);
                out.println("</h2></p>");
                out.println("<p><h5>Created " + new Date() + " by Radar</h5></p>");
                out.println("</body>");
                out.println("</html>");
                out.flush();
                out.close();
            }
            catch (IOException e) {
                throw new ContextAdminException("Error creating sample index.jsp: " + e.getMessage());
            }
        }
    }
    
    public void removeContext(String context) throws ContextAdminException {
    
        if (context != null) {
            Enumeration enum = cm.getContextNames();
            try {
                while (enum.hasMoreElements()) {
                    String name = (String)enum.nextElement();
                    
                    Debug.println("ContextAdmin - Searching context: " + name);
                    
                    if (context.equals(name)) {
                        cm.removeContext(context);
                        return;
                    }
                }
            }
            catch (org.apache.tomcat.core.TomcatException ex) {
                throw new ContextAdminException("Unable to remove context: "
                    + context);
            }
            
            throw new ContextAdminException("Context Name: " 
                + context + " Not Found");
        }
        else throw new ContextAdminException("ERROR: Null Context Name");

    }

}
