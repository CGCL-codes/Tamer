package org.qs.sapi.sockets.handlers.interfaces;

import java.io.IOException;
import java.net.UnknownHostException;
import org.qs.sapi.exception.GenericAtlasException;

public interface HandlerInterface {

    public void connect() throws UnknownHostException, IOException;

    public void close() throws IOException, GenericAtlasException;

    public void setStreams() throws IOException;

    public void closeStreams() throws IOException;

    public void preActivate();

    public void activate() throws UnknownHostException, IOException, GenericAtlasException;
}
