package uk.gov.dti.og.fox.ex;

import java.io.StringWriter;
import java.io.PrintWriter;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.TextElement;
import uk.gov.dti.og.fox.dom.TextXMLFragment;
import uk.gov.dti.og.fox.ex.ExServiceUnavailable;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.track.Track;

public class ExRuntimeRoot extends RuntimeExceptionWrapperException {

    public static final String TYPE = "Fox Software Runtime Error";

    /** The type of error. */
    private String t = TYPE;

    /** An XML DOM related to the error and giving some context to the error that has occurred. */
    private DOM x = null;

    /**
    * Constructs a <code>ExRuntimeRoot</code> with the specified
    * detail message.
    *
    * @param s the detail message
    */
    public ExRuntimeRoot(String s) {
        this(s, TYPE, null, null);
    }

    /**
    * Constructs a <code>ExRuntimeRoot</code> with the specified
    * detail message and nested exception.
    *
    * @param s the detail message
    * @param ex the nested exception
    */
    public ExRuntimeRoot(String s, Throwable ex) {
        this(s, TYPE, null, ex);
    }

    /**
    * Constructs a <code>ExRuntimeRoot</code> with the specified
    * detail message and nested exception.
    *
    * @param s the detail message
    * @param type a string containing a description of the type of error
    * @param xml an XML DOM related to the error
    * @param ex the nested exception
    */
    public ExRuntimeRoot(String msg, String type, DOM xml, Throwable ex) {
        super(msg, ex);
        x = xml;
        t = type;
        trackException();
    }

    /** Track exception */
    public void trackException() {
        TextElement lTrack = Track.trackPush("Exception");
        Track.trackSetTransient(false);
        try {
            lTrack.addChildElement("Message", getMessage());
            lTrack.addChildElement("Type", t);
            lTrack.addChildElement("Class", this.getClass().getName());
            Throwable lCause = getCause();
            if (lCause != null) {
                lTrack.addChildElement("NestedException", lCause.getMessage());
            }
            if (x != null) {
                TextXMLFragment lTextXMLFragment;
                try {
                    lTextXMLFragment = new TextXMLFragment(x.outputNodeToString());
                    lTrack.addChildElement("NestedXML", lTextXMLFragment);
                } catch (Throwable e) {
                    lTrack.addChildElement("NestedXML").setAttribute("serialiseError", e.getMessage());
                }
            }
            lTrack.addChildElement("Stack", XFUtil.getJavaStackTraceInfo(new Exception()));
        } finally {
            Track.trackPop("Exception");
        }
    }

    public String toString() {
        return t + ": " + getMessage();
    }

    public String getMessageStack() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        printStackTrace(pw);
        return sw.toString();
    }

    public String getXmlString() {
        if (x != null) {
            return x.outputNodeToStringNoExInternal();
        } else {
            return "";
        }
    }

    /** Convert standard exceptions to common ExInternal("Unexpected") form */
    public ExInternal toUnexpected() {
        return new ExInternal("Unexpected Error", this);
    }

    public ExInternal toUnexpected(String pMsg) {
        return new ExInternal("Unexpected Error: " + pMsg, this);
    }

    public ExServiceUnavailable toServiceUnavailable() {
        return new ExServiceUnavailable("Service Unavailable", this);
    }

    public ExServiceUnavailable toServiceUnavailable(String pMsg) {
        return new ExServiceUnavailable("Service Unavailable: " + pMsg, this);
    }
}
