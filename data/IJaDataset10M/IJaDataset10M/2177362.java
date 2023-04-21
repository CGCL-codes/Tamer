package de.ui.sushi.fs.webdav.methods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import de.ui.sushi.fs.webdav.MovedException;
import de.ui.sushi.fs.webdav.MultiStatus;
import de.ui.sushi.fs.webdav.Name;
import de.ui.sushi.fs.webdav.StatusException;
import de.ui.sushi.fs.webdav.WebdavConnection;
import de.ui.sushi.fs.webdav.WebdavRoot;
import de.ui.sushi.xml.Builder;
import org.w3c.dom.Document;

public class PropFindMethod extends WebdavMethod<List<MultiStatus>> {

    public PropFindMethod(WebdavRoot root, String path, Name name, int depth) throws IOException {
        super(root, "PROPFIND", path);
        Document document;
        setRequestHeader("Depth", String.valueOf(depth));
        document = getXml().builder.createDocument("propfind", DAV);
        name.addXml(Builder.element(document.getDocumentElement(), XML_PROP, DAV));
        setRequestEntity(document);
    }

    @Override
    public List<MultiStatus> processResponse(WebdavConnection conection, HttpResponse response) throws IOException {
        switch(response.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_MULTI_STATUS:
                return MultiStatus.fromResponse(getXml(), response);
            case HttpStatus.SC_MOVED_PERMANENTLY:
                throw new MovedException();
            case HttpStatus.SC_NOT_FOUND:
                throw new FileNotFoundException(getUri());
            default:
                throw new StatusException(response.getStatusLine());
        }
    }
}
