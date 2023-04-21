package com.vo.universalworker.service;

import com.vo.universalworker.common.exception.UniversalWorkerException;
import com.vo.universalworker.jobmanager.JobTaskManager;
import com.vo.universalworker.representation.JobsRepresentation;
import java.util.ArrayList;
import java.util.List;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.ParameterStyle;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.RequestInfo;
import org.restlet.ext.wadl.ResponseInfo;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

/**
 * Resource to handle jobs
 *
 * @author Jean-Christophe Malapert
 */
public class JobsResource extends BaseJobResource {

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        this.app = (UwsApplication) getApplication();
        setName("Jobs Resource");
        setDescription("This resource contains the whole list of job for which the current date is inferior to destruction date");
    }

    /**
     * Get jobs
     * @return Returns the JobSummary representation
     * @exception ResourceException Returns a HTTP Status 500 for an Internal Server Error
     */
    @Get("xml")
    public Representation getJobs() throws ResourceException {
        setStatus(Status.SUCCESS_OK);
        return new JobsRepresentation(getReference().getIdentifier(), JobTaskManager.getInstance().getJobTasks(), true);
    }

    /**
     * Create a Job
     * @param Representation Parameters sent by a user
     * @exception ResourceException Returns a HTTP Status 400 when form is not valid
     * @exception ResourceException Returns a HTTP Status 500 for an Internal Server Error
     */
    @Post("form")
    public void acceptJob(Representation entity) throws ResourceException {
        try {
            String jobTaskId = JobTaskManager.getInstance().createJobTask(entity);
            this.setRequestedJobId(jobTaskId);
            this.redirectToJobID();
        } catch (UniversalWorkerException ex) {
            throw new ResourceException(ex.getStatus(), ex.getMessage());
        }
    }

    @Override
    protected Representation describe() {
        setName("Jobs Resource");
        setDescription("This resource contains the whole list of job for which the current date is inferior to destruction date");
        return super.describe();
    }

    @Override
    protected void describeGet(MethodInfo info) {
        info.setName(Method.GET);
        info.setDocumentation("List all created jobs. The list may be empty if the UWS is idle");
        List<RepresentationInfo> repsInfo = new ArrayList<RepresentationInfo>();
        RepresentationInfo repInfo = new RepresentationInfo();
        repInfo.setXmlElement("uws:ShortJobDescription");
        repInfo.setMediaType(MediaType.TEXT_XML);
        DocumentationInfo docInfo = new DocumentationInfo();
        docInfo.setTitle("ShortJobDescription");
        docInfo.setTextContent("The representation of the Job List is a list of links to extant jobs. The list may be empty if the UWS is idle.");
        repInfo.setDocumentation(docInfo);
        repsInfo.add(repInfo);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.SUCCESS_OK);
        responseInfo.setRepresentations(repsInfo);
        info.getResponses().add(responseInfo);
        responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.SERVER_ERROR_INTERNAL);
        responseInfo.setDocumentation("Internal server error - Please contact the administrator");
        info.getResponses().add(responseInfo);
    }

    @Override
    protected void describePost(MethodInfo info) {
        info.setName(Method.POST);
        info.setDocumentation("POSTing a request to the Job List creates a new job (unless the service rejects the request)");
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.REDIRECTION_SEE_OTHER);
        responseInfo.setDocumentation("Redirects to /{job-id}");
        info.getResponses().add(responseInfo);
        responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.CLIENT_ERROR_BAD_REQUEST);
        responseInfo.setDocumentation("only RUN value is accepted for PHASE keyword. only service parameters are accepted");
        info.getResponses().add(responseInfo);
        responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.SERVER_ERROR_INTERNAL);
        info.getResponses().add(responseInfo);
        RequestInfo request = new RequestInfo();
        ParameterInfo param = new ParameterInfo();
        param.setName("PHASE");
        param.setStyle(ParameterStyle.QUERY);
        param.setFixed("RUN");
        param.setRequired(false);
        param.setType("xs:string");
        param.setDocumentation("Starting a job after its submission");
        request.getParameters().add(param);
        RepresentationInfo repInfo = new RepresentationInfo();
        repInfo.setMediaType(MediaType.MULTIPART_FORM_DATA);
        request.getRepresentations().add(repInfo);
        info.setRequest(request);
        super.describePost(info);
    }
}
