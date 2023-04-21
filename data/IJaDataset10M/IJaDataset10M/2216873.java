package net.sf.beezle.sushi.fs.webdav.methods;

import net.sf.beezle.sushi.fs.webdav.MovedException;
import net.sf.beezle.sushi.fs.webdav.MultiStatus;
import net.sf.beezle.sushi.fs.webdav.Name;
import net.sf.beezle.sushi.fs.webdav.StatusException;
import net.sf.beezle.sushi.fs.webdav.WebdavConnection;
import net.sf.beezle.sushi.fs.webdav.WebdavNode;
import net.sf.beezle.sushi.xml.Builder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.w3c.dom.Document;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PropFind extends Method<List<MultiStatus>> {

    public PropFind(WebdavNode resource, Name name, int depth) throws IOException {
        super("PROPFIND", resource);
        Document document;
        Builder builder;
        setRequestHeader("Depth", String.valueOf(depth));
        builder = getXml().getBuilder();
        synchronized (builder) {
            document = builder.createDocument("propfind", DAV);
        }
        name.addXml(Builder.element(document.getDocumentElement(), XML_PROP, DAV));
        setRequestEntity(document);
    }

    @Override
    public List<MultiStatus> processResponse(WebdavConnection connection, HttpResponse response) throws IOException {
        switch(response.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_MULTI_STATUS:
                return MultiStatus.fromResponse(getXml(), response);
            case HttpStatus.SC_BAD_REQUEST:
            case HttpStatus.SC_MOVED_PERMANENTLY:
                throw new MovedException();
            case HttpStatus.SC_NOT_FOUND:
                throw new FileNotFoundException(getUri());
            default:
                throw new StatusException(response.getStatusLine());
        }
    }
}
