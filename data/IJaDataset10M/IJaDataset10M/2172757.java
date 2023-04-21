package org.ourgrid.peer.controller.allocation;

import java.util.Collection;
import java.util.List;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.peer.to.AllocableWorker;
import org.ourgrid.peer.to.Consumer;
import org.ourgrid.peer.to.Request;
import org.ourgrid.reqtrace.Req;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;

/**
 * Interface for allocation algorithms 
 */
@Req("REQ011")
public interface Allocator {

    /**
	 * Select the workers that will be allocated for a local request
	 * 
	 * @param request The request that will be served
	 * @param allAllocableWorkers Collection containing all the current 
	 *         allocable workers in this peer
	 * @return A list with the workers to be allocated for this request
	 */
    @Req("REQ011")
    List<AllocableWorker> getAllocableWorkersForLocalRequest(Request request, Collection<AllocableWorker> allAllocableWorkers, ServiceManager serviceManager);

    /**
	 * Select the workers that will be allocated for a remote request
	 * 
	 * @param consumerPubKey Remote peer public key
	 * @param requestSpec Remote request specification
	 * @param allocableWorkers Collection containing all the current 
	 *         allocable workers in this peer
	 * @return A list with the workers to be allocated for this remote request
	 */
    @Req("REQ011")
    List<AllocableWorker> getAllocableWorkersForRemoteRequest(Consumer consumer, RequestSpec requestSpec, List<AllocableWorker> allocableWorkers, ServiceManager serviceManager);

    /**
	 * Determines a local request to be associated to a worker
	 * @param workerSpec Worker specification
	 * @return The request for which the worker will be allocated
	 */
    @Req("REQ018")
    Request getRequestForWorkerSpec(WorkerSpec workerSpec, ServiceManager serviceManager);
}
