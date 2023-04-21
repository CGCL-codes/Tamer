package org.soapfabric.server.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soapfabric.HTTPConstants;
import org.soapfabric.SOAPException;
import org.soapfabric.SOAPFaultException;
import org.soapfabric.SOAPMessage;
import org.soapfabric.server.SOAPEngine;
import org.soapfabric.server.SOAPRequest;
import org.soapfabric.server.SOAPResponse;
import org.soapfabric.server.config.Service;
import org.soapfabric.server.config.ServiceArchive;
import org.soapfabric.server.impl.FaultHelper;
import org.soapfabric.server.impl.SOAPRequestImpl;
import org.soapfabric.server.impl.SOAPResponseImpl;
import org.soapfabric.server.impl.ServerRuntime;
import org.soapfabric.server.impl.ServerURI;
import org.soapfabric.server.install.SARConstants;
import org.soapfabric.server.tools.ServerInfoPage;
import org.soapfabric.server.tools.ServiceInfoPage;
import org.soapfabric.server.tools.WsdlGenerator;
import org.soapfabric.util.io.IOUtil;

/**
 * @author <a href="mailto:mbsanchez at users.sf.net">Matt Sanchez</a>
 * @version $Id: HttpSoapServlet.java,v 1.8 2004/09/10 15:12:42 mbsanchez Exp $
 */
public class HttpSoapServlet extends HttpServlet {

    private static final Log LOG = LogFactory.getLog(HttpSoapServlet.class);

    public void init(ServletConfig config) throws ServletException {
        ServerRuntime.initialize(config);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServerRuntime runtime = ServerRuntime.getRuntime();
        ServerURI uri = new ServerURI(req.getRequestURI());
        if (uri.getServicePart() == null) {
            FaultHelper.sendFault("Malformed request URI: " + req.getRequestURI() + "'!", res);
            return;
        }
        String archiveName = uri.getServiceArchivePart();
        String serviceName = uri.getServicePart();
        ServiceArchive sa = (ServiceArchive) runtime.getAchiveMap().get(archiveName);
        if (sa == null) {
            FaultHelper.sendFault("Unknown service archive '" + archiveName + "'!", res);
            return;
        }
        Service service = sa.getService(serviceName);
        if (service == null) {
            FaultHelper.sendFault("Unknown service '" + serviceName + "'!", res);
            return;
        }
        Map mimeHeaders = new HashMap();
        Enumeration headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            mimeHeaders.put(name.toLowerCase(), req.getHeader(name));
        }
        SOAPMessage request = null;
        SOAPMessage response = new SOAPMessage();
        try {
            request = new SOAPMessage(req.getInputStream(), mimeHeaders);
        } catch (Exception e) {
            FaultHelper.sendFault("Error parsing SOAP request message!", e, res);
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("SOAP request received");
            LOG.debug(request);
        }
        SOAPRequest.Context ctx = new SOAPRequest.Context();
        ctx.setContextPath(req.getContextPath());
        ctx.setHostName(req.getServerName());
        ctx.setRequestParameters(req.getParameterMap());
        ctx.setRequestScheme(req.getScheme());
        ctx.setRequestURI(req.getRequestURI());
        ctx.setServerPort(req.getServerPort());
        ctx.setServletContext(req.getSession().getServletContext());
        SOAPRequest soapReq = new SOAPRequestImpl(ctx, request);
        SOAPResponse soapRes = new SOAPResponseImpl(response);
        SOAPEngine engine = runtime.getEngine();
        try {
            int status = engine.invoke(soapReq, soapRes);
            if (status == -1) {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else if (status != 0) {
                res.setStatus(status);
            }
        } catch (SOAPFaultException e) {
            FaultHelper.sendFault(e.getFault(), res);
            return;
        }
        request = soapReq.getRequestMessage();
        response = soapRes.getResponseMessage();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            response.writeTo(baos);
            res.setContentType(response.getMimeHeader(HTTPConstants.CONTENT_TYPE));
            if (response.isFault()) {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            byte[] responseBytes = baos.toByteArray();
            res.setContentLength(responseBytes.length);
            IOUtil.copy(new ByteArrayInputStream(responseBytes), res.getOutputStream());
            if (LOG.isDebugEnabled()) {
                LOG.debug("SOAP response sent");
                LOG.debug(response);
            }
        } catch (SOAPException e) {
            FaultHelper.sendFault("Error writing SOAP response message!", e, res);
            return;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServerRuntime runtime = ServerRuntime.getRuntime();
        ServerURI uri = new ServerURI(req.getRequestURI());
        if (uri.getServiceArchivePart() == null) {
            try {
                res.setContentType(HTTPConstants.TEXT_HTML);
                ServerInfoPage page = new ServerInfoPage(req.getScheme(), req.getServerName(), req.getServerPort(), uri);
                page.generate(runtime.getAchiveMap(), runtime.getTemplatePath(), res.getOutputStream());
            } catch (Throwable t) {
                t.printStackTrace();
                throw new ServletException("Caught exception while generating Server Info page");
            }
            return;
        }
        String archiveName = uri.getServiceArchivePart();
        String serviceName = uri.getServicePart();
        ServiceArchive sa = (ServiceArchive) runtime.getAchiveMap().get(archiveName);
        if (sa == null) {
            String faultString = "Unknown service archive '" + archiveName + "'";
            throw new ServletException(faultString);
        }
        Service service = sa.getService(serviceName);
        if (service == null) {
            String faultString = "Unknown service '" + serviceName + "'";
            throw new ServletException(faultString);
        }
        boolean wsdl = (req.getParameter("wsdl") != null);
        if (wsdl) {
            try {
                res.setContentType(HTTPConstants.TEXT_XML);
                WsdlGenerator gen = new WsdlGenerator(req.getServerName(), req.getServerPort(), req.getRequestURI());
                gen.generate(service, (File) sa.getInstallProperties().get(SARConstants.INSTALL_WSDL), res.getOutputStream());
            } catch (Throwable t) {
                t.printStackTrace();
                throw new ServletException("Caught exception while generating WSDL");
            }
        } else {
            try {
                res.setContentType(HTTPConstants.TEXT_HTML);
                ServiceInfoPage page = new ServiceInfoPage(req.getScheme(), req.getServerName(), req.getServerPort(), req.getRequestURI());
                page.generate(service, runtime.getTemplatePath(), res.getOutputStream());
            } catch (Throwable t) {
                t.printStackTrace();
                throw new ServletException("Caught exception while generating Service Info page");
            }
        }
    }
}
