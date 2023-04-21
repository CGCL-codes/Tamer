package gov.ornl.nice.nicecore.iNiCECore;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * @author bkj
 * 
 */
@Produces("text/plain")
@Provider
public class StringCollectionMessageBodyWriter implements MessageBodyWriter<ArrayList<String>> {

    /**
	 * Determine whether or not this writer can write the class in question.
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
	 *      java.lang.reflect.Type, java.lang.annotation.Annotation[],
	 *      javax.ws.rs.core.MediaType)
	 */
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] notes, MediaType mediaType) {
        return ArrayList.class.isAssignableFrom(type);
    }

    /**
	 * Get the Content-Length of the ArrayList - not immediately sure what this
	 * should equal, so just give it negative one and force it to count on its
	 * own. (non-Javadoc)
	 * 
	 * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
	 *      java.lang.Class, java.lang.reflect.Type,
	 *      java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
    public long getSize(ArrayList<String> collection, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
	 * Write to the output stream (non-Javadoc)
	 * 
	 * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
	 *      java.lang.Class, java.lang.reflect.Type,
	 *      java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType,
	 *      javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
	 */
    public void writeTo(ArrayList<String> collection, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
        for (String string : collection) outputStream.write(string.getBytes());
    }
}
