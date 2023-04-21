package au.edu.archer.metadata.mdsr.repository;

import static au.edu.archer.metadata.mdsr.config.RepositoryConstants.FEDORA_REPOSITORY_URL;
import static au.edu.archer.metadata.mdsr.config.RepositoryConstants.OBJECT_NAMESPACE;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import au.edu.archer.metadata.mdsr.utils.MDSRepositoryHelper;
import au.edu.archer.metadata.mdsr.utils.PropertyContainer;
import au.edu.archer.metadata.mdsr.utils.ServletValidationException;

/**
 * Servlet to lookup datastreams within a given digital object.
 *
 * @author alabri
 *
 */
public class LookupServlet extends AbstractRepositoryServlet {

    private static final long serialVersionUID = 1L;

    public LookupServlet() {
        super(Logger.getLogger(LookupServlet.class));
    }

    /**
     * This Method Handles GET request. It calls post method.
     *
     * @param request
     *            A HTTP servlet request
     * @param response
     *            A HTTP servlet response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * This Method Handles POST request to lookup datastreams within a given
     * digital object. Connections to this servlet should set the following
     * request properties: - label_identifier: The custom identifier for the
     * digital object - ds_id: The datastream id
     *
     * This servlet will return the datastream/file in the response OutputStream
     * with the same MIME type as of the datastream/file MIME type and then
     * returns HttpServletResponse.SC_OK (200). It returns a
     * HttpServletResponse.SC_BAD_REQUEST (400) if 'label_identifier' and
     * 'ds_id' request properties are not set. If the given digital object or
     * datastream are not found in the MDSR, HttpServletResponse.SC_NOT_FOUND
     * (404) will be returned. HttpServletResponse.SC_INTERNAL_SERVER_ERROR
     * (500) is returned if something wrong happened in Fedora repository while
     * trying to retrieve the datastream.
     *
     * @param request
     *            A HTTP servlet request
     * @param response
     *            A HTTP servlet response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String labelIdentifier = getHeaderOrParameter(request, "label_identifier");
        String dsIdentifier = getHeaderOrParameter(request, "ds_id");
        try {
            validParams(labelIdentifier, dsIdentifier);
            try {
                FedoraRepositoryManagmentAPI managementAPI = MDSRepositoryHelper.getManagementAPI();
                FedoraRepositoryAccessAPI accessAPI = MDSRepositoryHelper.getAccessAPI();
                String dsVersionID = getHeaderOrParameter(request, "ds_version_id");
                boolean found = accessAPI.foundPid(labelIdentifier);
                if (found) {
                    Map<String, Object> dsProfile = managementAPI.getDatastreamProfile(labelIdentifier, dsIdentifier, null);
                    if (!dsProfile.isEmpty()) {
                        String createDateTime = null;
                        if (dsVersionID != null) {
                            createDateTime = managementAPI.getCreateDateTime(labelIdentifier, dsIdentifier, dsVersionID);
                        }
                        String mimeType = dsProfile.get("MIMEType").toString();
                        response.setContentType(mimeType);
                        URL datastreamURL = null;
                        if (createDateTime == null) {
                            datastreamURL = new URL(PropertyContainer.instance().getProperty(FEDORA_REPOSITORY_URL) + "/get/" + OBJECT_NAMESPACE + labelIdentifier + "/" + dsIdentifier);
                        } else {
                            datastreamURL = new URL(PropertyContainer.instance().getProperty(FEDORA_REPOSITORY_URL) + "/get/" + OBJECT_NAMESPACE + labelIdentifier + "/" + dsIdentifier + "/" + createDateTime);
                        }
                        pipeStream(datastreamURL.openStream(), response.getOutputStream(), 2048);
                        logger.info("Representation " + dsIdentifier + " for schema " + labelIdentifier + " has been downloaded.");
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        logger.error("Representation " + dsIdentifier + " not found for schema " + labelIdentifier + ".");
                        MDSRepositoryHelper.sendResponse(HttpServletResponse.SC_NOT_FOUND, "Representation " + dsIdentifier + " not found for schema " + labelIdentifier + ".", response);
                    }
                } else {
                    logger.error("Schema " + labelIdentifier + " cannot be found.");
                    MDSRepositoryHelper.sendResponse(HttpServletResponse.SC_NOT_FOUND, "Schema " + labelIdentifier + " cannot be found.", response);
                }
            } catch (Exception ex) {
                logger.error("Unexpected exception", ex);
                MDSRepositoryHelper.sendResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error: see mdsr log file for details.", response);
            }
        } catch (ServletValidationException ex) {
            logger.error("Invalid request parameters", ex);
            MDSRepositoryHelper.sendResponse(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage(), response);
        }
    }

    /**
     * A method to validate the given parameters
     *
     * @param labelIdentifier
     *            Object identifier
     * @param dsIdentifier
     *            Datastream Identifier
     * @throws ServletValidationException
     */
    private void validParams(String labelIdentifier, String dsIdentifier) throws ServletValidationException {
        validateLabelIdentifier(labelIdentifier);
        validateDSIdentifier(dsIdentifier);
    }

    /**
     * Copies the contents of an InputStream to an OutputStream, then closes
     * both.
     *
     * @param in
     *            The source stream.
     * @param out
     *            The target stram.
     * @param bufSize
     *            Number of bytes to attempt to copy at a time.
     * @throws IOException
     *             If any sort of read/write error occurs on either stream.
     */
    public void pipeStream(InputStream in, OutputStream out, int bufSize) throws IOException {
        try {
            byte[] buf = new byte[bufSize];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.err.println("WARNING: Could not close stream.");
            }
        }
    }
}
