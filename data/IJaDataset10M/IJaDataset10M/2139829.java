package com.oneandone.sushi.fs.webdav.methods;

import com.oneandone.sushi.fs.webdav.StatusException;
import com.oneandone.sushi.fs.webdav.WebdavConnection;
import com.oneandone.sushi.fs.webdav.WebdavNode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import java.io.IOException;

public class Put extends Method<Void> {

    public Put(WebdavNode resource) {
        super("PUT", resource);
    }

    @Override
    protected void setContentHeader() {
        setRequestHeader(HTTP.TRANSFER_ENCODING, HTTP.CHUNK_CODING);
    }

    @Override
    public Void processResponse(WebdavConnection connection, HttpResponse response) throws IOException {
        int status;
        status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_NO_CONTENT && status != HttpStatus.SC_CREATED) {
            throw new StatusException(response.getStatusLine());
        }
        return null;
    }
}
