package com.vo.universalworker.service;

import com.vo.universalworker.common.exception.UniversalWorkerException;
import com.vo.universalworker.jobmanager.JobTaskManager;
import com.vo.universalworker.representation.JobParameterNameRepresentation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import net.ivoa.xml.uws.v1.ExecutionPhase;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.RequestInfo;
import org.restlet.ext.wadl.ResponseInfo;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

/**
 * Resource to handle ParameterName
 * @author Jean-Chirstophe Malapert
 */
public class ParameterNameResource extends BaseJobResource {

    private String parameterNameId = null;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        this.parameterNameId = (String) getRequestAttributes().get("parameter-name");
        CopyOnWriteArraySet<Method> allowedMethods = new CopyOnWriteArraySet<Method>();
        allowedMethods.add(Method.GET);
        if (((UwsApplication) getApplication()).isAllowedParameterNamePutMethod()) {
            allowedMethods.add(Method.PUT);
        }
        setAllowedMethods(allowedMethods);
        setName("ParameterName Resource");
        setDescription("This resource handles parameter value");
    }

    @Get("plain")
    public Representation getParameterValue() {
        return new JobParameterNameRepresentation(getJobTask(), getParameterNameId(), true);
    }

    /**
     * Set a value for a specific parameter
     * @exception ResourceException Returns a HTTP Status 400 when the key is wrong
     * @exception ResourceException Returns a HTTP Status 403 when the operation is not allowed
     * @exception ResourceException Returns a HTTP Status 404 when the jobId or parameter is not found
     * @exception ResourceException Returns a HTTP Status 500 for an Internal Server Error
     */
    @Put("plain")
    public void updateParameterValue(String parameterValue) {
        if (isValidAction()) {
            try {
                JobTaskManager.getInstance().setParameter(getJobTask(), getParameterNameId(), parameterValue);
            } catch (UniversalWorkerException ex) {
                throw new ResourceException(ex.getStatus(), ex.getMessage(), ex.getCause());
            }
        } else {
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "A parameter value cannot be set while the job is running");
        }
        this.redirectToJobID();
    }

    protected final String getParameterNameId() {
        return this.parameterNameId;
    }

    protected final boolean isValidAction() {
        try {
            ExecutionPhase phase = JobTaskManager.getInstance().getStatus(getJobTask());
            return (phase.equals(phase.EXECUTING)) ? false : true;
        } catch (UniversalWorkerException ex) {
            throw new ResourceException(ex.getStatus(), ex.getMessage(), ex.getCause());
        }
    }

    @Override
    protected Representation describe() {
        setName("ParameterName Resource");
        setDescription("This resource handles parameter value");
        return super.describe();
    }

    @Override
    protected void describeGet(MethodInfo info) {
        info.setName(Method.GET);
        info.setDocumentation("Get a parameter name");
        ResponseInfo responseInfo = new ResponseInfo();
        List<RepresentationInfo> repsInfo = new ArrayList<RepresentationInfo>();
        RepresentationInfo repInfo = new RepresentationInfo();
        repInfo.setXmlElement("xs:string");
        repInfo.setMediaType(MediaType.TEXT_PLAIN);
        repsInfo.add(repInfo);
        responseInfo.setRepresentations(repsInfo);
        responseInfo.getStatuses().add(Status.SUCCESS_OK);
        info.getResponses().add(responseInfo);
        responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.CLIENT_ERROR_NOT_FOUND);
        responseInfo.setDocumentation("job does not exist");
        info.getResponses().add(responseInfo);
        responseInfo = new ResponseInfo();
        responseInfo.getStatuses().add(Status.SERVER_ERROR_INTERNAL);
        info.getResponses().add(responseInfo);
    }

    @Override
    protected void describePut(MethodInfo info) {
        info.setName(Method.PUT);
        info.setDocumentation("Change a parameter value");
        ResponseInfo responseInfo = null;
        if (((UwsApplication) getApplication()).isAllowedParameterNamePutMethod()) {
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.REDIRECTION_SEE_OTHER);
            responseInfo.setDocumentation("Redirects to /{job-id}");
            info.getResponses().add(responseInfo);
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.CLIENT_ERROR_BAD_REQUEST);
            responseInfo.setDocumentation("The key does not exist");
            info.getResponses().add(responseInfo);
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.CLIENT_ERROR_NOT_FOUND);
            responseInfo.setDocumentation("Job does not exist");
            info.getResponses().add(responseInfo);
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.CLIENT_ERROR_FORBIDDEN);
            responseInfo.setDocumentation("A parameter value cannot be set while the job is running");
            info.getResponses().add(responseInfo);
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.SERVER_ERROR_INTERNAL);
            info.getResponses().add(responseInfo);
            RequestInfo request = new RequestInfo();
            request.setDocumentation("Value to set");
            RepresentationInfo repInfo = new RepresentationInfo();
            repInfo.setMediaType(MediaType.TEXT_PLAIN);
            request.getRepresentations().add(repInfo);
            info.setRequest(request);
        } else {
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.CLIENT_ERROR_FORBIDDEN);
            responseInfo.setDocumentation("The client is not allowed to change the parameter value");
            info.getResponses().add(responseInfo);
            responseInfo = new ResponseInfo();
            responseInfo.getStatuses().add(Status.SERVER_ERROR_INTERNAL);
            info.getResponses().add(responseInfo);
        }
    }
}
