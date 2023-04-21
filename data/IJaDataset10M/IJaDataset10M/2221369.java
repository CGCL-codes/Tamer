package org.ourgrid.peer.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.ourgrid.common.interfaces.LocalWorkerProviderClient;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.peer.to.LocalConsumer;
import org.ourgrid.peer.to.Request;
import org.ourgrid.reqtrace.Req;
import br.edu.ufcg.lsd.commune.Application;
import br.edu.ufcg.lsd.commune.container.servicemanager.dao.DAO;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

/**
 * Stores requests
 */
public class RequestDAO extends DAO {

    private final Map<Long, Request> requests;

    private final Map<RequestSpec, Future<?>> scheduledAdverts;

    /**
	 * Create request manager.
	 */
    @Req("REQ010")
    public RequestDAO(Application application) {
        super(application);
        this.requests = new LinkedHashMap<Long, Request>();
        this.scheduledAdverts = new HashMap<RequestSpec, Future<?>>();
    }

    /**
	 * @param workerProviderClient
	 * @param requestSpec
	 * @return
	 */
    public Request createRequest(DeploymentID lwpDID, LocalWorkerProviderClient workerProviderClient, RequestSpec requestSpec, LocalConsumer localConsumer) {
        localConsumer.setConsumerStub(lwpDID, workerProviderClient);
        Request request = new Request(requestSpec);
        request.setConsumer(localConsumer);
        localConsumer.addRequest(request);
        long requestID = requestSpec.getRequestId();
        this.requests.put(requestID, request);
        return request;
    }

    /**
	 * @param requestID
	 * @return The <code>Request</code> if the request exists, null otherwise.
	 */
    public Request getRequest(Long requestID) {
        return this.requests.get(requestID);
    }

    /**
	 * Verify if a request is running (created and not finished) in this peer.
	 * @param requestSpec Request to be verified
	 * @return True if the request is running
	 */
    @Req("REQ027")
    public boolean isRunning(RequestSpec requestSpec) {
        if (requestSpec == null) {
            return false;
        }
        Request request = this.requests.get(requestSpec.getRequestId());
        return request != null;
    }

    /**
	 * Return a collection of Requests in a reverse order of creation
	 * @return
	 */
    @Req({ "REQ018" })
    public List<Request> getRunningRequests() {
        LinkedList<Request> reverseOrderRequests = new LinkedList<Request>();
        for (Request request : this.requests.values()) {
            reverseOrderRequests.addFirst(request);
        }
        return reverseOrderRequests;
    }

    /**
	 * Remove a <code>Request</code> by request identification
	 * @param requestID
	 */
    public void removeRequest(Long requestID) {
        Request request = this.requests.remove(requestID);
        request.pause();
    }

    /**
	 * 
	 * @return
	 */
    public Collection<Request> getScheduledRequests() {
        Collection<Request> scheduledRequests = new LinkedList<Request>();
        for (RequestSpec requestSpec : scheduledAdverts.keySet()) {
            Request request = requests.get(requestSpec.getRequestId());
            String error = "Error in the RequestDAO code. A scheduled request is not " + "mapped in the request structures.";
            if (request == null) {
                getLog().error(error);
            }
            scheduledRequests.add(request);
        }
        return scheduledRequests;
    }

    /**
	 * @param spec
	 * @return
	 */
    public boolean containsAScheduledRequest(RequestSpec spec) {
        return scheduledAdverts.containsKey(spec);
    }

    @Req("REQ011")
    public void putScheduledRequest(RequestSpec scheduledRequest, Future<?> advertFuture) {
        scheduledAdverts.put(scheduledRequest, advertFuture);
    }

    @Req("REQ116")
    public Future<?> removeFuture(RequestSpec requestSpec) {
        return scheduledAdverts.remove(requestSpec);
    }
}
