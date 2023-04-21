package balmysundaycandy.more.low.level.operations.user.impl;

import java.util.concurrent.Future;
import balmysundaycandy.core.future.ProtocolMessageFuture;
import balmysundaycandy.core.operations.OperationNamespaces.user;
import balmysundaycandy.more.low.level.operations.user.UserOperation;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.UserServicePb.CreateLogoutURLRequest;
import com.google.apphosting.api.UserServicePb.CreateLogoutURLResponse;

public final class CreateLogoutURLOperation extends UserOperation<CreateLogoutURLRequest, CreateLogoutURLResponse> {

    @Override
    public CreateLogoutURLResponse call(CreateLogoutURLRequest request) {
        CreateLogoutURLResponse response = new CreateLogoutURLResponse();
        response.mergeFrom(ApiProxy.makeSyncCall(user.packageName, user.methodName.CreateLogoutURL, request.toByteArray()));
        return response;
    }

    @Override
    public Future<CreateLogoutURLResponse> callAsync(CreateLogoutURLRequest request, ApiConfig apiConfig) {
        CreateLogoutURLResponse response = new CreateLogoutURLResponse();
        return new ProtocolMessageFuture<CreateLogoutURLResponse>(ApiProxy.makeAsyncCall(user.packageName, user.methodName.CreateLogoutURL, request.toByteArray(), apiConfig), response);
    }
}
