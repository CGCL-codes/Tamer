package org.ourgrid.common.interfaces;

import org.ourgrid.common.LoginResult;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import br.edu.ufcg.lsd.commune.api.Remote;

@Remote
public interface LocalWorkerProviderClient {

    /**
	 * @param workerProvider
	 * @param result
	 * @param peerAccounting
	 */
    void loginSucceed(LocalWorkerProvider workerProvider, LoginResult result, AccountingAggregator peerAccounting);

    /**
	 * @param worker
	 * @param workerSpec
	 * @param requestSpec
	 */
    void hereIsWorker(Worker worker, WorkerSpec workerSpec, RequestSpec requestSpec);
}
