package com.peterhi.server.command;

/**
 *
 * @author YUN TAO
 */
public class CommandResult {

    private String response;

    private int statusCode;

    public CommandResult() {
    }

    public CommandResult(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
