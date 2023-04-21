package com.jvantage.ce.web.setup.port;

import com.jvantage.ce.presentation.html.HTML;
import com.jvantage.ce.web.setup.Controller;
import com.jvantage.ce.web.setup.SetupConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

/**
 * @author  Brent Clay
 */
public class PortSetup extends HttpServlet {

    public static final int stepNumber = 4;

    public static final String htmlTemplateName = Controller.getTemplatePathAndName(SetupConstants.sfSetupPage);

    private Controller controller = null;

    private ServletConfig myConfig = null;

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /** Initializes the servlet.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myConfig = config;
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(true);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResourceBundle bundle = ResourceBundle.getBundle(SetupConstants.sfResourceBundleClassName);
        controller = (Controller) httpSession.getAttribute(SetupConstants.sfSessionKey_Controller);
        if (controller == null) {
            response.sendRedirect(Controller.sfWizardHome);
            return;
        }
        controller.setCurrentStep(stepNumber);
        controller.initTemplate(myConfig.getServletContext().getResourceAsStream(htmlTemplateName));
        controller.setPageTitle("Host Server URL");
        controller.setInstructions(bundle.getString(SetupConstants.sfResourceBundleKey_PortInstruction));
        String webHostName = null;
        try {
            webHostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            webHostName = "";
        }
        String serverInfo = StringUtils.defaultString(myConfig.getServletContext().getServerInfo()).toLowerCase();
        String port = "";
        String host = "";
        String requestURL = StringUtils.defaultString(request.getRequestURL().toString()).toLowerCase();
        String hostOnward = StringUtils.substringAfter(requestURL, "http://");
        String hostAndPort = StringUtils.substringBefore(hostOnward, "/");
        if (StringUtils.contains(hostAndPort, webHostName + ":")) {
            host = StringUtils.substringBefore(hostAndPort, ":");
            port = StringUtils.substringAfter(hostAndPort, ":");
        } else {
            host = hostAndPort;
            port = "";
        }
        if (StringUtils.isBlank(host) || "localhost".equalsIgnoreCase(host)) {
            if (StringUtils.isNotBlank(webHostName)) {
                host = webHostName;
            }
        }
        String defaultUrl = "http://" + host + (StringUtils.isBlank(port) ? "" : (":" + port)) + "/";
        String urlEnteredByUser = (String) httpSession.getAttribute(SetupConstants.sfSessionKey_Port);
        if (urlEnteredByUser != null) {
            defaultUrl = urlEnteredByUser;
        }
        StringBuffer generatedContent = new StringBuffer();
        generatedContent.append(HTML.divBeg("Align=\"Center\"")).append(HTML.formBeg(SetupConstants.ServletAlias_SetupPortProcessor)).append(HTML.text("HTTP Host and Port Number ")).append(HTML.formInputTextWithDefaultValue(SetupConstants.sfFormInput_Port, 50, defaultUrl, null)).append(HTML.formInputSubmit()).append(HTML.formEnd()).append(HTML.divEnd());
        controller.setGeneratedContent(generatedContent.toString());
        out.write(controller.getContent());
    }
}
