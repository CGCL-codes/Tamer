package com.picturehistory.core.jersey.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response {

    public static final String FAILED = "FAILED";

    public static final String OK = "OK";

    private String result;

    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
