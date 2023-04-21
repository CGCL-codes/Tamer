package org.globus.ftp.dc;

import org.globus.ftp.vanilla.Reply;

/**
   Local server communicate with client with a  simplified control channel.
   This is a local, minimal version of Reply, free of overhead
   caused by parsing during construction.
 **/
public class LocalReply extends Reply {

    private static final String MESSAGE = "this LocalReply does not have a message";

    public LocalReply(int code) {
        this.message = MESSAGE;
        this.isMultiline = false;
        this.code = code;
        this.category = code / 100;
    }

    public LocalReply(int code, String message) {
        this.message = message;
        this.isMultiline = false;
        this.code = code;
        this.category = code / 100;
    }
}
