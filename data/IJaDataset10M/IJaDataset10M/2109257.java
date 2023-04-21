package com.vmix.simplemq.daemon.http;

import com.vmix.simplemq.daemon.*;
import com.vmix.simplemq.daemon.status.DaemonInfo;
import org.simpleframework.xml.*;

@Root(name = "mq")
public class ResponseXml {

    @Attribute(name = "status_code")
    public int statusCode = 200;

    @Attribute(name = "status")
    public String statusMessage = "OK";

    @Attribute(name = "error_message", required = false)
    public String errorMessage = "";

    @Element(name = "message", required = false)
    public Message message;

    @Element(name = "pong", required = false)
    public Ping ping;

    @Element(name = "status", required = false)
    public DaemonInfo daemonStatus;

    @Element(name = "registration", required = false)
    public Registration registration;

    public ResponseXml() {
    }
}
