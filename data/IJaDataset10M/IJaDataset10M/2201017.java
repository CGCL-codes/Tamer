package openfarm.transmission.ws.engine;

import java.io.IOException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import openfarm.analysiscomponents.Parameterize;
import openfarm.multithreading.machine.Job;
import openfarm.transmission.ws.jobmonitoring.CurrentState;
import openfarm.transmission.ws.jobmonitoring.InterpreterJobState;
import openfarmtools.interpreter.exceptions.AnalysisComponentNotFoundException;
import openfarmtools.interpreter.exceptions.InvalidParametersException;
import openfarmtools.interpreter.exceptions.JobException;
import openfarmtools.interpreter.exceptions.JobStateException;
import openfarmtools.interpreter.exceptions.MtMachineException;
import openfarmtools.interpreter.exceptions.VideoFileNotFoundException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface TemplateService {

    @WebMethod
    String getInterpreterId();

    @WebMethod
    String invokeAnalysis(String analysisComponent, String[] videos, Parameterize parameters) throws AnalysisComponentNotFoundException, VideoFileNotFoundException, IOException, InvalidParametersException, MtMachineException;

    @WebMethod
    void abortAll();

    @WebMethod
    void stopJobGroup(String jobGroupName) throws MtMachineException;

    @WebMethod
    void stopSingleJob(String jobId) throws MtMachineException;

    @WebMethod
    boolean isInterpreterJobGroupCompleted(String jobGroupId);

    @WebMethod
    boolean isInterpreterSingleJobRunning(String jobId);

    @WebMethod
    Job[] getInterpreterJobDetails(String jobGroupId) throws JobException;

    @WebMethod
    String analysisComponentsDescription();

    @WebMethod
    CurrentState jobCurrentInterpreterState(String jobId) throws JobStateException;

    @WebMethod
    InterpreterJobState interpreterStatus();
}
