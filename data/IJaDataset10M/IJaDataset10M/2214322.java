package org.linkedgeodata.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import com.hp.hpl.jena.sparql.engine.http.HttpParams;

public class MessageBodyWriterHtml extends AbstractModelMessageReaderWriterProvider {

    public MessageBodyWriterHtml() {
        super("HTML");
    }
}
