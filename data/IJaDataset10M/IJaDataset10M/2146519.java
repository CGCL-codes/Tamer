package org.ox.framework.web.responses;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author admin
 */
public class ErrorResponse implements Renderizable {

    private String code;

    private String message;

    private Throwable e;

    public ErrorResponse(String code, String message, Throwable e) {
        this.code = code;
        this.message = message;
        this.e = e;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the e
     */
    public Throwable getE() {
        return e;
    }

    /**
     * @param e the e to set
     */
    public void setE(Throwable e) {
        this.e = e;
    }

    public String renderize() {
        String str = "<oxmessage><exception><message><![CDATA[" + getMessage() + "]]></message><code><![CDATA[" + getCode() + "]]></code>";
        StringWriter sb = new StringWriter();
        PrintWriter w = new PrintWriter(sb);
        if (getE() != null) {
            getE().printStackTrace(w);
        }
        str = str + "<detail><![CDATA[" + sb + "]]></detail>";
        str = str + "</exception></oxmessage>";
        return str;
    }

    public String content() {
        return "plain/xml;charset=UTF-8";
    }
}
