package org.ourgrid.common.interfaces;

import java.util.List;
import br.edu.ufcg.lsd.commune.api.Remote;

@Remote
public interface DiscoveryServiceClient {

    void hereIsRemoteWorkerProviderList(List<String> workerProviders);

    void hereAreDiscoveryServices(List<String> discoveryServices);
}
