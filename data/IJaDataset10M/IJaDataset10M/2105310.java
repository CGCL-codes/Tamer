package org.hydracache.server.httpd.handler;

import java.io.IOException;
import org.apache.commons.lang.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hydracache.io.Buffer;
import org.hydracache.protocol.data.codec.ProtocolDecoder;
import org.hydracache.protocol.data.message.BlobDataMessage;
import org.hydracache.protocol.data.message.DataMessage;
import org.hydracache.server.data.storage.Data;
import org.hydracache.server.data.storage.DataBank;
import org.hydracache.server.data.storage.DataStorageException;

/**
 * Put http method request handler
 * 
 * @author nzhu
 * 
 */
public class HttpPutMethodHandler extends BaseHttpMethodHandler {

    private static Logger log = Logger.getLogger(HttpPutMethodHandler.class);

    private ProtocolDecoder<DataMessage> decoder;

    /**
     * Constructor
     */
    public HttpPutMethodHandler(DataBank dataBank, ProtocolDecoder<DataMessage> decoder) {
        super(dataBank);
        this.decoder = decoder;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (isEmpty(request)) {
            log.warn("Empty request[" + request + "] received and ignored");
            return;
        }
        HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
        BlobDataMessage dataMessage = decodeProtocolMessage(entity);
        Long dataKey = extractDataKeyHash(request);
        verifyKeyHashConsistency(dataMessage, dataKey);
        int statusCode = createStatusCode(dataKey);
        doPut(response, dataMessage);
        response.setStatusCode(statusCode);
    }

    int createStatusCode(Long dataKey) {
        int returnStatusCode = HttpStatus.SC_OK;
        if (dataBank.get(dataKey) == null) returnStatusCode = HttpStatus.SC_CREATED;
        return returnStatusCode;
    }

    private void doPut(HttpResponse response, BlobDataMessage dataMessage) {
        try {
            dataBank.put(new Data(dataMessage.getKeyHash(), dataMessage.getVersion(), dataMessage.getBlob()));
        } catch (DataStorageException ex) {
            log.error("Failed to put:", ex);
            response.setStatusCode(HttpStatus.SC_METHOD_FAILURE);
        }
    }

    void verifyKeyHashConsistency(BlobDataMessage dataMessage, Long dataKey) {
        Validate.isTrue(dataKey.equals(dataMessage.getKeyHash()), "Data key hash does not match the hash specified in the URL");
    }

    BlobDataMessage decodeProtocolMessage(HttpEntity entity) throws IOException {
        byte[] entityContent = EntityUtils.toByteArray(entity);
        if (log.isDebugEnabled()) {
            log.debug("Incoming entity content (bytes): " + entityContent.length);
        }
        DataMessage dataMessage = decoder.decode(Buffer.wrap(entityContent).asDataInputStream());
        verifyMessageType(dataMessage);
        return (BlobDataMessage) dataMessage;
    }

    void verifyMessageType(DataMessage dataMessage) {
        Validate.isTrue(dataMessage instanceof BlobDataMessage, "Unsupported protocol message[" + dataMessage + "] received");
    }

    private boolean isEmpty(HttpRequest request) {
        return !(request instanceof HttpEntityEnclosingRequest);
    }
}
