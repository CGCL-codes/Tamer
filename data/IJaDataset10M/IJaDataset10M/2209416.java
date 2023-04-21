package org.tridentproject.repository.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Resource that manages a job
 *
 */
public class JobResource extends BaseResource {

    private static Logger log = Logger.getLogger(JobResource.class);

    String strJobId;

    public JobResource(Context context, Request request, Response response) {
        super(context, request, response);
        this.strJobId = (String) getRequest().getAttributes().get("jobid");
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }

    /**
     * Returns a representation of a job.  GetJob API method.
     */
    @Override
    public Representation getRepresentation(Variant variant) {
        if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
            DomRepresentation representation = null;
            try {
                representation = new DomRepresentation(MediaType.TEXT_XML);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            String strJobsUrl = ((ResourceApplication) getApplication()).getJobsUrl();
            File jobsFile = new File(strJobsUrl + "/" + strJobId + "/status.xml");
            try {
                Document d = representation.getDocument();
                Element root = d.createElement("job");
                d.appendChild(root);
                root.setAttribute("id", strJobId);
                root.setAttribute("href", strRepoURI + "/jobs/" + strJobId);
                try {
                    SAXReader reader = new SAXReader();
                    org.dom4j.Document statusDoc = reader.read(jobsFile);
                    org.dom4j.Element rootNode = statusDoc.getRootElement();
                    String strJobType = rootNode.attributeValue("type");
                    root.setAttribute("type", strJobType);
                    root.setAttribute("user", rootNode.attributeValue("user"));
                    root.setAttribute("status", rootNode.attributeValue("status"));
                    root.setAttribute("startTime", rootNode.attributeValue("startTime"));
                    Element logElem = d.createElement("log");
                    logElem.setAttribute("href", strRepoURI + "/jobs/" + strJobId + "/log");
                    root.appendChild(logElem);
                    Element reportElem = d.createElement("report");
                    reportElem.setAttribute("href", strRepoURI + "/jobs/" + strJobId + "/report");
                    root.appendChild(reportElem);
                    root.setAttribute("endTime", rootNode.attributeValue("endTime"));
                    root.setAttribute("numItemsProcessed", rootNode.attributeValue("numItemsProcessed"));
                    root.setAttribute("numTotalItems", rootNode.attributeValue("numTotalItems"));
                    root.setAttribute("numErrors", rootNode.attributeValue("numErrors"));
                } catch (DocumentException e) {
                    String strErrMsg = "Unable to find job, " + strJobId + ": " + e.getMessage();
                    log.debug(strErrMsg);
                    Representation rep = SetRepositoryMessage(Status.CLIENT_ERROR_NOT_FOUND, null, "JobNotFound", strErrMsg, null);
                    return rep;
                } catch (NullPointerException e) {
                    String strErrMsg = "Unable to find job, " + strJobId + ": " + e.getMessage();
                    log.debug(strErrMsg);
                    Representation rep = SetRepositoryMessage(Status.CLIENT_ERROR_NOT_FOUND, null, "JobNotFound", strErrMsg, null);
                    return rep;
                }
                d.normalizeDocument();
                return representation;
            } catch (IOException e) {
                String strErrMsg = "Internal error retrieving job, " + strJobId + ": " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", strErrMsg, null);
                return rep;
            } catch (Exception e) {
                String strErrMsg = "Internal error retrieving job, " + strJobId + ": " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", strErrMsg, null);
                return rep;
            }
        }
        return null;
    }
}
