package net.sourceforge.ondex.restful.resources.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Simply output plain Boolean java type.
 * 
 * @author taubertj
 * 
 */
@Provider
@Singleton
@Produces(MediaType.TEXT_PLAIN)
public class BooleanMessageBodyWriter implements MessageBodyWriter<Boolean> {

    @Override
    public long getSize(Boolean value, Class<?> clazz, Type type, Annotation[] anno, MediaType mediatype) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> clazz, Type type, Annotation[] anno, MediaType mediatype) {
        return Boolean.class.isAssignableFrom(clazz);
    }

    @Override
    public void writeTo(Boolean value, Class<?> clazz, Type type, Annotation[] anno, MediaType mediatype, MultivaluedMap<String, Object> map, OutputStream outstream) throws IOException, WebApplicationException {
        if (value == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        OutputStreamWriter writer = new OutputStreamWriter(outstream);
        writer.write(value.toString());
        writer.flush();
    }
}
