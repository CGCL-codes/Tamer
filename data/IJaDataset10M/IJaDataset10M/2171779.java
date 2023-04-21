package com.producteev4j.model.request;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrey
 * Date: 14/05/11
 * TimeImpl: 18:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProducteevRequest {

    private ProducteevParameters params = new ProducteevParameters();

    private ProducteevRequestMethod method;

    private Class<? extends Object> responseClass;

    private String endpoint;

    public ProducteevParameters getParams() {
        return params;
    }

    protected void setMethod(ProducteevRequestMethod method) {
        this.method = method;
    }

    public ProducteevRequestMethod getMethod() {
        return method;
    }

    public void setResponseClass(Class<? extends Object> responseClass) {
        this.responseClass = responseClass;
    }

    public Class<? extends Object> getResponseClass() {
        return responseClass;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
