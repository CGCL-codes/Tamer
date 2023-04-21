package de.schlund.pfixcore.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author mleidig@schlund.de
 */
public interface ServiceRequest {

    public String getServiceName();

    public Object getUnderlyingRequest();

    public String getParameter(String name);

    public String getCharacterEncoding();

    public String getMessage() throws IOException;

    public Reader getMessageReader() throws IOException;

    public InputStream getMessageStream() throws IOException;

    public String dump();
}
