package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;

/**
 * Contract for providing "auxiliary" data to the client.
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/aux")
public interface IModelDataService extends RemoteService {

    /**
	 * Handles a request for "auxiliary" data.
	 * @param request The auxiliary data request
	 * @return Auxiliary payload
	 */
    ModelDataPayload handleModelDataRequest(ModelDataRequest request);
}
