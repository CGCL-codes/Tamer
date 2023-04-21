package uk.ac.ncl.neresc.dynasoar.hostprovider;

import org.apache.log4j.Logger;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.ncl.neresc.dynasoar.constants.DynasoarConstants;
import uk.ac.ncl.neresc.dynasoar.messages.HostProviderRequest;
import uk.ac.ncl.neresc.dynasoar.messages.HostProviderResponse;
import uk.ac.ncl.neresc.dynasoar.messages.RegisterWith;
import uk.ac.ncl.neresc.dynasoar.messages.RegisterWithResponse;
import uk.ac.ncl.neresc.dynasoar.messages.types.Status;
import uk.ac.ncl.neresc.dynasoar.utils.DOMUtils;
import javax.xml.soap.SOAPElement;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: nam48
 * Date: 14-Mar-2006
 * Time: 12:02:01
 * To change this template use File | Settings | File Templates.
 */
public class HostProviderAdminServlet extends HttpServlet {

    private static Logger mLog = Logger.getLogger(HostProviderAdminServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ResourceBundle rb = ResourceBundle.getBundle("HPConfig", request.getLocale());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        String title = "Host Provider Management Interface";
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        String config = rb.getString("self.nodetype");
        if (config.equals("LEAF")) {
            out.println("<h1> Register this HostProvider with the ROOT/Broker/WSP </h1>");
        } else {
            if (config.equals("ROOT")) {
                out.println("<h1> Register this HostProvider with a Broker/WSP </h1>");
            } else if (config.equals("BROKER")) {
                out.println("<h1> Register this HostProvider with a Broker/WSP </h1>");
            }
        }
        out.println("<p>");
        out.println("<form method=POST>");
        String regWith = rb.getString("register.with");
        if (regWith == null) {
            out.println("Enter registration endpoint:");
            out.println("<br/>");
            out.println("<input name=registerWith type=text size=100>");
        } else {
            out.println("Current registration endpoint: <b>" + regWith + "</b>");
            out.println("<br/>");
            out.println("If you wish to change it, enter the new endpoint, or leave the default:");
            out.println("<br/>");
            out.println("<input name=registerWith type=text size=100 value=" + regWith + ">");
        }
        out.println("<p>");
        out.println("<b>");
        out.println("Do you have VMWare Server on this host?");
        out.println("</b>");
        out.println("<br/>");
        out.println("<input type='radio' name='vmware' value=\"YES\"/>" + " YES ");
        out.println("<br/>");
        out.println("<input type='radio' name='vmware' value=\"NO\" checked='checked'/>" + " NO ");
        out.println("<hr />");
        out.println("<br/>");
        out.println("<input type=submit name=action value=Register>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        ResourceBundle rb = ResourceBundle.getBundle("HPConfig", request.getLocale());
        Boolean regVal = false;
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + "Registration request received" + "</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<h1> Registration Page </h1>");
            out.println("<p>");
            String registerWith = request.getParameter("registerWith");
            String parentType = rb.getString("parent.nodetype");
            String selfType = rb.getString("self.nodetype");
            String selfURI = rb.getString("self.uri");
            String vmwareServer = request.getParameter("vmware");
            mLog.debug("VMWare Server availability: " + vmwareServer);
            boolean vmAvail = false;
            if (vmwareServer.equals("YES")) {
                vmAvail = true;
            }
            mLog.debug("Is there a VM on this host: " + vmAvail);
            if (registerWith != null) {
                out.println("Welcome! I am a <b>" + selfType + "</b>, at <b>" + selfURI + "</b>");
                out.println("<br/>");
                if (parentType.equals("ROOT")) {
                    mLog.debug("Registering with ROOT HostProvider at " + registerWith);
                    out.println("Registering with ROOT HostProvider at <b>" + registerWith + "</b>");
                } else if (parentType.equals("BROKER")) {
                    mLog.debug("Registering with BROKER at " + registerWith);
                    out.println("Registering with BROKER at <b>" + registerWith + "</b>");
                } else if (parentType.equals("WSP")) {
                    mLog.debug("Registering with WSP at " + registerWith);
                    out.println("Registering with WSP at <b>" + registerWith + "</b>");
                }
                regVal = register(registerWith, parentType, selfURI, selfType, vmAvail);
            }
            out.println("<p>");
            if (regVal) {
                mLog.debug("Registration successful");
                out.println("<h2> Registration Successful </h2>");
            } else {
                mLog.debug("Registration failed");
                out.println("<h2> Registration Failed </h2>");
            }
            out.println("<br/>");
            out.println("<form method=GET>");
            out.println("If you wish, you can register this HostProvider with others.");
            out.println("<br/>");
            out.println("<input type=submit value=\"Register\">");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean register(String regWith, String pType, String selfURI, String selfType, boolean hasVM) {
        boolean ret = false;
        mLog.debug("Self Type: " + selfType);
        mLog.debug("Self URI: " + selfURI);
        mLog.debug("Parent Type: " + pType);
        mLog.debug("Parent URI: " + regWith);
        mLog.debug("Is there a VM on this host: " + hasVM);
        HostProviderRequest hpReq = new HostProviderRequest();
        RegisterWith regW = new RegisterWith();
        regW.setParentURI(regWith);
        regW.setParentType(pType);
        regW.setHasVMHost(hasVM);
        hpReq.setRegisterWith(regW);
        try {
            StringWriter writer = new StringWriter();
            Marshaller.marshal(hpReq, writer);
            String writerOutput = writer.toString();
            String s1 = writerOutput.substring(DynasoarConstants.XML_HEADER.length() + 1);
            String newString = s1.replaceAll(DynasoarConstants.HPREQ_TO_REPLACE, DynasoarConstants.HP_REQUEST_NAME);
            Document outputDocument = org.apache.axis.utils.XMLUtils.newDocument(new ByteArrayInputStream(newString.getBytes()));
            SOAPEnvelope envelope = new SOAPEnvelope();
            SOAPBody bdy = (SOAPBody) envelope.getBody();
            SOAPBodyElement csReq = (SOAPBodyElement) bdy.addBodyElement(envelope.createName(DynasoarConstants.DYNASOAR_SERVICE_REQUEST));
            SOAPElement elem1 = DOMUtils.convertDOMToSOAPElement(envelope, outputDocument.getDocumentElement());
            csReq.addChildElement(elem1);
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(selfURI));
            call.setTimeout(Integer.MAX_VALUE);
            SOAPEnvelope csResp = call.invoke(envelope);
            SOAPBody respB = (SOAPBody) csResp.getBody();
            Iterator itr = respB.getChildElements();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    SOAPBodyElement el1 = (SOAPBodyElement) itr.next();
                    Iterator itr2 = el1.getChildElements(csResp.createName(DynasoarConstants.HP_RESPONSE_NAME));
                    if (!itr2.hasNext()) {
                        mLog.debug("Unable to process request, unknown message");
                    } else {
                        SOAPElement el2 = (SOAPElement) itr2.next();
                        Document doc = ((MessageElement) el2).getAsDocument();
                        String input = XMLUtils.DocumentToString(doc);
                        mLog.debug("Unmarshalling request document");
                        HostProviderResponse hpR = (HostProviderResponse) HostProviderResponse.unmarshal(new StringReader(input));
                        if (hpR.getRegisterWithResponse() != null) {
                            RegisterWithResponse aR = hpR.getRegisterWithResponse();
                            if (aR.getRegistrationState().getType() == Status.SUCCESS_TYPE) {
                                ret = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
