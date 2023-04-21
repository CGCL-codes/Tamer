package com.risertech.xdav.internal;

import java.net.URI;
import java.util.UUID;
import com.risertech.xdav.caldav.CalDAVMethodFactory;
import com.risertech.xdav.carddav.CardDAVMethodFactory;
import com.risertech.xdav.collection.ICollection;
import com.risertech.xdav.http.HeaderBlock;
import com.risertech.xdav.http.IResponse;
import com.risertech.xdav.http.header.ContentType;
import com.risertech.xdav.http.header.IfNoneMatch;
import com.risertech.xdav.http.method.IPutMethod;
import com.risertech.xdav.http.type.StatusLine;

public abstract class AbstractCollection implements ICollection {

    protected URI uri;

    protected ClientFactory clientFactory;

    public AbstractCollection(ClientFactory clientFactory, URI uri) {
        this.clientFactory = clientFactory;
        this.uri = uri;
    }

    public void delete() {
        StatusLine statusLine = clientFactory.executeMethod(CalDAVMethodFactory.getInstance().createDeleteMethod(uri)).getStatusLine();
        if (statusLine.getStatusCode() != 204) {
            throw new RuntimeException(statusLine.toString());
        }
    }

    protected URI put(String name, String entity, String contentType) {
        try {
            HeaderBlock headerBlock = new HeaderBlock();
            headerBlock.addHeader(new IfNoneMatch());
            headerBlock.addHeader(new ContentType(contentType));
            URI componentURI = new URI(uri.getPath() + name);
            IPutMethod method = CardDAVMethodFactory.getInstance().createPutMethod(componentURI, entity);
            IResponse response = clientFactory.executeMethod(headerBlock, method);
            System.out.println("Attempting to put entity at " + componentURI + " - " + response.getStatusLine());
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException(response.getStatusLine().toString());
            }
            if (true) {
                System.out.println(response.getStatusLine());
                System.out.println();
            }
            return componentURI;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String generateUUIDName(String extension) {
        return UUID.randomUUID().toString() + extension;
    }
}
