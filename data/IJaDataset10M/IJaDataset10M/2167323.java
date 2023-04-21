package org.nakedobjects.plugins.remoting.command.shared.marshal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Marshaller {

    public void openStreams(InputStream input, OutputStream output) throws IOException;
}
