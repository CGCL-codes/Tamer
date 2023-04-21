package org.identifylife.key.engine.web.jersey.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.WeakHashMap;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.identifylife.key.engine.EngineException;
import org.identifylife.key.engine.response.Response;
import org.identifylife.key.engine.response.ResponseBuilder;
import org.identifylife.key.engine.web.Constants;
import org.springframework.stereotype.Component;

/**
 * @author dbarnier
 *
 */
@Component
@Provider
@Produces(Constants.APPLICATION_PROTOBUF)
@SuppressWarnings("unchecked")
public class ProtobufResponseWriter implements MessageBodyWriter<Response> {

    private Map<Response, byte[]> buffer = new WeakHashMap<Response, byte[]>();

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Response.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Response response, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        try {
            ResponseBuilder builder = ResponseBuilder.protobufBuilder();
            builder.build(response);
            byte[] bytes = builder.bytes();
            buffer.put(response, bytes);
            return bytes.length;
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public void writeTo(Response response, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        if (!buffer.containsKey(response)) {
            throw new EngineException("response not found in encode buffer: " + response);
        }
        entityStream.write(buffer.remove(response));
    }
}
