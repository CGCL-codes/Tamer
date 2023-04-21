package com.softwarementors.extjs.djn.router.processor.standard;

import com.softwarementors.extjs.djn.StringUtils;
import com.softwarementors.extjs.djn.api.RegisteredAction;
import com.softwarementors.extjs.djn.api.RegisteredStandardMethod;
import com.softwarementors.extjs.djn.api.Registry;
import com.softwarementors.extjs.djn.config.GlobalConfiguration;
import com.softwarementors.extjs.djn.router.dispatcher.Dispatcher;
import com.softwarementors.extjs.djn.router.processor.RequestException;
import com.softwarementors.extjs.djn.router.processor.RequestProcessorBase;
import com.softwarementors.extjs.djn.router.processor.RequestProcessorUtils;

public abstract class StandardRequestProcessorBase extends RequestProcessorBase {

    protected StandardErrorResponseData createJsonServerErrorResponse(StandardRequestData request, Throwable t) {
        assert request != null;
        assert t != null;
        Throwable reportedException = RequestProcessorUtils.getExceptionToReport(t);
        String message = RequestProcessorUtils.getExceptionMessage(reportedException);
        String where = RequestProcessorUtils.getExceptionWhere(reportedException, getDebug());
        StandardErrorResponseData response = new StandardErrorResponseData(request.getTid(), request.getAction(), request.getMethod());
        response.setMessageAndWhere(message, where);
        return response;
    }

    protected StandardRequestProcessorBase(Registry registry, Dispatcher dispatcher, GlobalConfiguration globalConfiguration) {
        super(registry, dispatcher, globalConfiguration);
    }

    protected RegisteredStandardMethod getStandardMethod(String actionName, String methodName) {
        assert !StringUtils.isEmpty(actionName);
        assert !StringUtils.isEmpty(methodName);
        RegisteredAction action = getRegistry().getAction(actionName);
        if (action == null) {
            throw RequestException.forActionNotFound(actionName);
        }
        RegisteredStandardMethod method = action.getStandardMethod(methodName);
        if (method == null) {
            throw RequestException.forActionMethodNotFound(action.getName(), methodName);
        }
        return method;
    }

    protected Object dispatchStandardMethod(String actionName, String methodName, Object[] parameters) {
        assert !StringUtils.isEmpty(actionName);
        assert !StringUtils.isEmpty(methodName);
        assert parameters != null;
        RegisteredStandardMethod method = getStandardMethod(actionName, methodName);
        Object result = getDispatcher().dispatch(method, parameters);
        return result;
    }
}
