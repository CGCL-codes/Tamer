package com.google.web.bindery.requestfactory.shared.messages;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * The factory for creating RequestFactory wire messages.
 */
public interface MessageFactory extends AutoBeanFactory {

    AutoBean<ServerFailureMessage> failure();

    AutoBean<IdMessage> id();

    AutoBean<InvocationMessage> invocation();

    AutoBean<JsonRpcRequest> jsonRpcRequest();

    AutoBean<OperationMessage> operation();

    AutoBean<RequestMessage> request();

    AutoBean<ResponseMessage> response();

    AutoBean<ViolationMessage> violation();
}
