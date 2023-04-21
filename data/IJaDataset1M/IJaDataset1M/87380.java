package org.dmp.chillout.sav.savrenderer;

import java.util.EventObject;

public class WebExceptionEvent extends EventObject {

    private Exception ex;

    private String msg;

    public WebExceptionEvent(Exception ex, String customermsg) {
        super(ex);
        this.ex = ex;
        this.msg = customermsg;
    }

    public Exception getException() {
        return ex;
    }

    public String getWebExceptionMsg() {
        return ex.getMessage();
    }

    public String getGracefulMsg() {
        return this.msg;
    }
}
