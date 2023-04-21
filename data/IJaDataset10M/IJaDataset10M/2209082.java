package com.google.code.bing.webservices.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.virtualearth.dev.webservices.v1.common.Credentials;
import net.virtualearth.dev.webservices.v1.common.ExecutionOptions;
import net.virtualearth.dev.webservices.v1.common.ObjectFactory;
import net.virtualearth.dev.webservices.v1.common.RequestBase;

public abstract class BaseBingMapsServiceClientImpl implements BingMapsWebServicesClient {

    /** The task executor. */
    protected ExecutorService taskExecutor = Executors.newCachedThreadPool();

    protected static final ObjectFactory COMMON_FACTORY = new ObjectFactory();

    protected abstract static class BaseRequestBuilderImpl<T extends RequestBase> implements RequestBuilder<T> {

        protected T result;

        protected BaseRequestBuilderImpl(T result) {
            this.result = result;
        }

        @Override
        public RequestBuilder<T> withCredentials(String applicationId, String token) {
            Credentials credentials = COMMON_FACTORY.createCredentials();
            credentials.setApplicationId(applicationId);
            credentials.setToken(token);
            result.setCredentials(credentials);
            return this;
        }

        @Override
        public RequestBuilder<T> withCulture(String culture) {
            result.setCulture(culture);
            return this;
        }

        @Override
        public RequestBuilder<T> withExecutionOptions(Boolean suppressFaults) {
            ExecutionOptions options = COMMON_FACTORY.createExecutionOptions();
            options.setSuppressFaults(suppressFaults);
            result.setExecutionOptions(options);
            return this;
        }

        public T getResult() {
            return result;
        }
    }

    @Override
    public void setTaskExecutor(ExecutorService taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
