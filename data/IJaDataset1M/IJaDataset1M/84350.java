package com.leclercb.taskunifier.api.synchronizer.exc;

public class SynchronizerApiException extends SynchronizerException {

    private String apiId;

    private String code;

    public SynchronizerApiException(boolean expected, String apiId, String code, String message) {
        this(expected, apiId, code, message, null);
    }

    public SynchronizerApiException(boolean expected, String apiId, String code, String message, Throwable throwable) {
        super(expected, message, throwable);
        this.apiId = apiId;
        this.code = code;
    }

    public String getApiId() {
        return this.apiId;
    }

    public String getCode() {
        return this.code;
    }
}
