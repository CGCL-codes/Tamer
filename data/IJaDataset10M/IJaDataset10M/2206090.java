package gleam.gateservice.client;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Interface for access to a GaS in asynchronous mode from Java. To be
 * expanded as need for new operations arises.
 */
public interface GateServiceClient {

    /**
   * Get the list of required parameter names supported by this GaS.
   * 
   * @return an array of parameter names.
   */
    public String[] getRequiredParameterNames() throws GateServiceClientException;

    /**
   * Get the list of optional parameter names supported by this GaS.
   * 
   * @return an array of parameter names.
   */
    public String[] getOptionalParameterNames() throws GateServiceClientException;

    /**
   * Get the list of annotation set names required by this service as
   * input. Note that one of the entries in the array may be
   * <code>null</code> if the service takes input from the default
   * annotation set.
   * 
   * @return an array of annotation set names, one of which may be
   *         <code>null</code>.
   */
    public String[] getInputAnnotationSetNames() throws GateServiceClientException;

    /**
   * Get the list of annotation set names output by this service. Note
   * that one of the entries in the array may be <code>null</code> if
   * the service provides output to the default annotation set. Also
   * note that the same annotation set may be named as both an input and
   * an output set.
   * 
   * @return an array of annotation set names, one of which may be
   *         <code>null</code>.
   */
    public String[] getOutputAnnotationSetNames() throws GateServiceClientException;

    /**
   * Start a process running on this GaS.
   * 
   * @param taskID the task ID that will be returned to the executive
   *          callback when the task has completed or failed.
   * @param docServiceLocation the location of the doc service
   *          containing the document to be processed.
   * @param docId the ID of the document to be processed in this doc
   *          service.
   * @param asMappings mappings for the input and output annotation set
   *          names required by this service. The map keys are the
   *          annotation set names used by the GaS, the values are the
   *          annotation set names in the document service (so you can
   *          map more than one GaS annotation set to the same DS
   *          annotation set, but not the other way around).
   * @param parameterValues parameter values required by this service.
   * @throws GateServiceClientException if an error occurs when calling
   *           the service.
   */
    public void processRemoteDocument(String taskID, URI docServiceLocation, String docId, Map<String, String> asMappings, Map<String, String> parameterValues) throws GateServiceClientException;

    /**
   * Start a process running over a set of documents on this GaS. Note
   * that you should <b>not</b> use this method to run over a corpus -
   * instead you should call {@link #processRemoteDocument} once for
   * each document. This method forces a single worker to process the
   * set of documents in one go, and cannot take advantage of multiple
   * workers to distribute the load.
   * 
   * @param taskID the task ID that will be returned to the executive
   *          callback when the task has completed or failed.
   * @param docServiceLocation the location of the doc service
   *          containing the document to be processed.
   * @param tasks the set of tasks to process. Each task contains a
   *          docID and a Map of asMappings, see
   *          {@link #processRemoteDocument} for details.
   * @param parameterValues parameter values required by this service.
   * @throws GateServiceClientException if an error occurs when calling
   *           the service.
   */
    public void processRemoteDocuments(String taskID, URI docServiceLocation, List<AnnotationTask> tasks, Map<String, String> parameterValues) throws GateServiceClientException;
}
