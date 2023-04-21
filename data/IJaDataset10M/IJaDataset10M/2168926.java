package balmysundaycandy.more.low.level.operations.datastore.impl;

import java.util.concurrent.Future;
import balmysundaycandy.core.future.ProtocolMessageFuture;
import balmysundaycandy.core.operations.OperationNamespaces.datastore_v3;
import balmysundaycandy.more.low.level.operations.datastore.DatastoreOperation;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.DatastorePb.AllocateIdsRequest;
import com.google.apphosting.api.DatastorePb.AllocateIdsResponse;

/**
 * allocateids operation.
 * 
 * @author marblejenka
 */
public final class AllocateIdsOperation extends DatastoreOperation<AllocateIdsRequest, AllocateIdsResponse> {

    @Override
    public AllocateIdsResponse call(AllocateIdsRequest request) {
        AllocateIdsResponse allocateIdsResponse = new AllocateIdsResponse();
        allocateIdsResponse.mergeFrom(ApiProxy.makeSyncCall(datastore_v3.packageName, datastore_v3.methodName.AllocateIds, request.toByteArray()));
        return allocateIdsResponse;
    }

    @Override
    public Future<AllocateIdsResponse> callAsync(AllocateIdsRequest request, ApiConfig apiConfig) {
        AllocateIdsResponse response = new AllocateIdsResponse();
        return new ProtocolMessageFuture<AllocateIdsResponse>(ApiProxy.makeAsyncCall(datastore_v3.packageName, datastore_v3.methodName.AllocateIds, request.toByteArray(), apiConfig), response);
    }
}
