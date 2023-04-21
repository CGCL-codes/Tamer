package com.icesoft.faces.webapp.command;

import java.io.IOException;
import java.io.StringWriter;

public abstract class AbstractCommand implements Command {

    public String toString() {
        StringWriter writer = new StringWriter();
        try {
            serializeTo(writer);
        } catch (IOException e) {
        } finally {
            writer.flush();
        }
        return writer.toString();
    }
}
