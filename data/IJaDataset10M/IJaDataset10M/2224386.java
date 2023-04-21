package org.tei.comparator.web.client.rpc;

import org.tei.comparator.web.client.Initialization;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Initialization")
public interface InitializationService extends RemoteService {

    public Initialization initializeTEIComparator();
}
