package org.vrspace.vrmlclient;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import org.vrspace.util.*;

/**
This class represents a single request from the client. It extends Message
to adding request logging capabilites, and also adds Client
@see RequestLog
*/
public class Request extends Message implements Cloneable {

    protected String caller;

    protected String callerMethod;

    /**
  Creates new Request as specified by <b>request</b>
  
  @param Client client which issued request
  @param String request specification
   */
    public Request(String caller, String callerMethod, String request) {
        this.caller = caller;
        this.callerMethod = callerMethod;
        parseLine(request);
    }

    /**
  This constructor uses client and session info from passed request.
  Intended for usage in commands that need to provide feedback over a
  specific session.
  @param Request Request from which client & session info are copied
  @param String request spec
  */
    public Request(Request r, String request) {
        this.caller = r.caller;
        this.callerMethod = callerMethod;
        parseLine(request);
    }

    /**
  Returns the originating caller
  */
    public String getCaller() {
        return caller;
    }

    /**
  Returns the caller's method
  */
    public String getCallerMethod() {
        return callerMethod;
    }

    /**
  Returns request string suitable for logging
  */
    public String toLogEntry() {
        return getTime() + " " + getCaller() + " " + getCallerMethod() + " " + getClassName() + " " + getId() + " " + getEventName() + " " + getEventValue() + ";";
    }

    void setId(long id) {
        this.objectId = id;
    }

    void setClass(String cls) {
        this.className = cls;
    }

    /**
  */
    public Object clone() {
        Request ret = null;
        try {
            ret = (Request) super.clone();
        } catch (CloneNotSupportedException e) {
            Logger.logError("Request: can't clone me", e);
        }
        return ret;
    }
}
