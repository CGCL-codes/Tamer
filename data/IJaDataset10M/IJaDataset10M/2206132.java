package com.rbnb.inds.exec.commands;

import org.xml.sax.Attributes;

/**
  * File delete command.
  */
public class Sleep extends com.rbnb.inds.exec.Command {

    public Sleep(Attributes attr) {
        super(attr);
        duration_ms = Integer.parseInt(attr.getValue("duration_ms"));
    }

    protected boolean doExecute() throws java.io.IOException {
        try {
            Thread.sleep(duration_ms);
        } catch (InterruptedException ie) {
        }
        return true;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public String getPrettyName() {
        return "Sleep (" + duration_ms + "ms)";
    }

    private final int duration_ms;
}
