package org.jquery4jsf.resource.stream.processor;

import java.io.InputStream;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;

public interface JSProcessor {

    public void processor(InputStream in, Writer out, HttpServletRequest request) throws Exception;
}
