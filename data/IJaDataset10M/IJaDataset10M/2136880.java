package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.search.ISearch;

public interface ICrudServiceAsync {

    void load(LoadRequest<? extends ISearch> request, AsyncCallback<ModelPayload> callback);

    void persist(PersistRequest request, AsyncCallback<ModelPayload> callback);

    void purge(PurgeRequest request, AsyncCallback<ModelPayload> callback);
}
