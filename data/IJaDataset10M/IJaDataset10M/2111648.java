package com.peterhi.server;

/**
 *
 * @author YUN TAO
 */
public class CommandResult {

    private String response;

    private byte statusCode;

    public CommandResult() {
    }

    public CommandResult(String response, byte statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public byte getCode() {
        return statusCode;
    }

    public void setStatusCode(byte statusCode) {
        this.statusCode = statusCode;
    }
}
