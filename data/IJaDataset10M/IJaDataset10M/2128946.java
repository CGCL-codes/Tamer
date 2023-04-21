package gov.lanl.objectdb.openurl;

import gov.lanl.util.HttpDate;
import gov.lanl.util.properties.PropertiesUtil;
import info.openurl.oom.ContextObject;
import info.openurl.oom.OpenURLRequest;
import info.openurl.oom.OpenURLRequestProcessor;
import info.openurl.oom.OpenURLResponse;
import info.openurl.oom.Service;
import info.openurl.oom.config.ClassConfig;
import info.openurl.oom.config.OpenURLConfig;
import info.openurl.oom.entities.ServiceType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

/**
 * The OpenURLaDORe1 OpenURL Service
 * 
 * @author Ryan Chute
 */
public class OpenURLaDORe1Service implements Service {

    private ResolverConnectionHandler db;

    private static final String svc = "info:lanl-repo/svc/getDIDL";

    /**
     * Construct an openurl-aDORe1 web service class.
     * 
     * @param openURLConfig
     * @param classConfig
     * @throws ResolverException 
     */
    public OpenURLaDORe1Service(OpenURLConfig openURLConfig, ClassConfig classConfig) throws ResolverException {
        String propFile;
        try {
            propFile = classConfig.getArg("props");
            this.db = new ResolverConnectionHandler(PropertiesUtil.loadConfigByClasspath(propFile));
        } catch (TransformerException e) {
            throw new ResolverException("Error attempting to obtain props file from classpath, disabling " + svc + " : " + e.getMessage());
        } catch (IOException e) {
            throw new ResolverException("Error attempting to open props file from classpath, disabling " + svc + " : " + e.getMessage());
        } catch (Exception e) {
            throw new ResolverException("Unknown error occurred, disabling " + svc + " : " + e.getMessage());
        }
    }

    public URI getServiceID() throws URISyntaxException {
        return new URI(svc);
    }

    public OpenURLResponse resolve(ServiceType serviceType, ContextObject contextObject, OpenURLRequest openURLRequest, OpenURLRequestProcessor processor) throws UnsupportedEncodingException {
        String responseFormat = null;
        byte[] response = null;
        Object rft = contextObject.getReferent().getDescriptors()[0];
        String id = ((URI) rft).toASCIIString();
        responseFormat = "text/xml; charset=utf-8";
        try {
            response = getRecord(id);
        } catch (IdentifierNotFoundException ex) {
            response = (" id doesn't exist " + ex.getMessage()).getBytes("UTF-8");
            responseFormat = "text/plain";
        } catch (ResolverException ex) {
            response = (" service exception " + ex.getMessage()).getBytes("UTF-8");
            responseFormat = "text/plain";
        }
        HashMap header_map = new HashMap();
        header_map.put("Content-Length", response.length + "");
        header_map.put("Date", HttpDate.getHttpDate());
        return new OpenURLResponse(HttpServletResponse.SC_OK, responseFormat, response, header_map);
    }

    public byte[] getRecord(String referent) throws ResolverException, IdentifierNotFoundException {
        String id = referent;
        byte[] bytes = null;
        try {
            bytes = db.getRecord(referent).getBytes();
        } catch (ResolverException e) {
            throw new ResolverException("Error attempting to obtain record: " + e.getMessage());
        }
        if (bytes == null) throw new IdentifierNotFoundException("Unable to resolve identifier: " + id);
        return bytes;
    }
}
