package org.travelfusion.xmlclient.util;

import static org.travelfusion.xmlclient.util.TfXAPIUtil.notNull;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.travelfusion.xmlclient.cache.CacheProvider;
import org.travelfusion.xmlclient.cache.HasCacheKey;
import org.travelfusion.xmlclient.exception.CommandExecutionFailureException;
import org.travelfusion.xmlclient.exception.CommandResponseMissingException;
import org.travelfusion.xmlclient.exception.DataValidationFailureException;
import org.travelfusion.xmlclient.exception.InvalidResponseException;
import org.travelfusion.xmlclient.exception.TfXClientException;
import org.travelfusion.xmlclient.exception.UnrecognisedResponseException;
import org.travelfusion.xmlclient.login.Login;
import org.travelfusion.xmlclient.xobject.XError;
import org.travelfusion.xmlclient.xobject.XRequest;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.mycila.xmltool.CallBack;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLDocumentException;
import com.mycila.xmltool.XMLTag;

/**
 * A collection of utility methods and constants aimed at those writing SPI component implementations.
 * 
 * @author Jesse McLaughlin (nzjess@gmail.com)
 */
public class TfXSPIUtil {

    /**
   * This constant has the value: <?xml version="1.0" encoding="UTF-8"?>
   */
    public static final String XML_PREAMBLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final Pattern OPEN_TAG_PATTERN = Pattern.compile("(<.*?>)");

    /**
   * Recursively searches a class, its interfaces, its superclasses and superinterfaces, for a given annotation. Returns
   * the annotation instance if found, otherwise returns null.
   */
    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
        if (clazz == null || clazz == Object.class) return null;
        T annotation = clazz.getAnnotation(annotationClass);
        if (annotation != null) return annotation;
        for (Class<?> interfaze : clazz.getInterfaces()) {
            annotation = findAnnotation(interfaze, annotationClass);
            if (annotation != null) return annotation;
        }
        if (!clazz.isInterface()) {
            annotation = findAnnotation(clazz.getSuperclass(), annotationClass);
        }
        return annotation;
    }

    /**
   * Implements a simple classpath based search algorithm for locating and loading a resource that will be used to
   * initialize a template for a request handler.
   * 
   * @throws IllegalStateException if the resource could not be found
   */
    public static InputStream loadTemplateResourceAsStream(Class<?> handlerType, Class<?> requestType) throws IllegalStateException {
        InputStream stream;
        String resourceName1 = requestType.getSimpleName() + ".xml";
        stream = handlerType.getResourceAsStream(resourceName1);
        if (stream != null) return stream;
        String resourceName2 = handlerType.getSimpleName() + ".xml";
        stream = handlerType.getResourceAsStream(resourceName2);
        if (stream != null) return stream;
        stream = handlerType.getResourceAsStream("/" + resourceName1);
        if (stream != null) return stream;
        stream = handlerType.getResourceAsStream("/" + resourceName2);
        if (stream != null) return stream;
        throw new IllegalStateException("resource not found; tried: " + resourceName1 + ", " + resourceName2);
    }

    /**
   * If necessary, wraps a given {@link InputStream} so that it supports the {@link InputStream#mark(int)} API.
   */
    public static InputStream ensureStreamSupportsMark(InputStream stream) {
        if (stream instanceof BufferedInputStream) return stream;
        if (stream instanceof ByteArrayInputStream) return stream;
        return new BufferedInputStream(stream);
    }

    /**
   * Parses a (potentially partial) XML string to find the command tag. The TripPlannerXML spec accepts requests
   * <b>AND</b> returns responses in the form:
   * 
   * <pre>
   *   &lt;CommandList&gt;
   *     &lt;CommandTagName&gt;
   *       ...
   *     &lt;/CommandTagName&gt;
   *   &lt;/CommandList&gt;
   * </pre>
   * 
   * Where <code>&lt;CommandTagName&gt;</code> may be any XML tag. This method will return this tag as a string, given
   * enough of the XML to determine its value (the whole XML document does not need to be passed in).
   * <p>
   * Note: this method does not support XML entities.
   * 
   * @param string The string to search for the command tag,
   * 
   * @return The command tag as a string.
   * 
   * @throws TfXClientException If the command tag cannot be found in the passed in string.
   */
    public static String findCommandTag(String string) throws TfXClientException {
        Matcher matcher = OPEN_TAG_PATTERN.matcher(string);
        String tag = null;
        while (matcher.find()) {
            String found = matcher.group();
            if (found.startsWith("<?")) continue;
            if (found.equals("<CommandList>")) continue;
            tag = found;
            break;
        }
        if (tag != null) {
            int start = 1;
            int end = tag.indexOf(" ");
            if (end == -1) end = tag.length() - 1;
            return tag.substring(start, end);
        }
        throw new TfXClientException("command tag not found in: '" + string + "'");
    }

    /**
   * Reads and parses enough of the response stream to determine if the response is a successful one or not. If not, the
   * whole response is parsed and the information retrieved is thrown back within an exception. Otherwise the stream is
   * reset and processing continues as though this method had never been called.
   * <p>
   * The passed in {@link InputStream} <b>must</b> support the {@link InputStream#mark(int)} API.
   */
    public static void checkResponse(InputStream responseStream, String requestCommandTag, int maxReadAheadLength) throws TfXClientException, IOException {
        responseStream.mark(maxReadAheadLength);
        byte[] buffer = new byte[maxReadAheadLength];
        int length = responseStream.read(buffer);
        if (length <= 0) throw new TfXClientException("empty response stream received");
        String initialPart = new String(buffer, 0, length, "UTF-8");
        responseStream.reset();
        String responseCommandTag = findCommandTag(initialPart);
        if (!requestCommandTag.equals(responseCommandTag)) {
            handleUnsuccessfulResponse(responseStream, initialPart);
        }
    }

    /**
   * Parses a response stream that represents an unsuccessful response from the TRAVELfusion TripPlannerXML service.
   */
    private static void handleUnsuccessfulResponse(InputStream responseStream, String initialPart) throws IOException {
        XMLTag xml;
        try {
            xml = XMLDoc.from(responseStream, true);
        } catch (XMLDocumentException e) {
            throw new UnrecognisedResponseException(initialPart, e);
        }
        if (!xml.getCurrentTag().hasChildNodes()) {
            throw new CommandResponseMissingException(parseError(xml));
        }
        if (xml.hasTag("CommandExecutionFailure")) {
            throw new CommandExecutionFailureException(parseError(xml.gotoTag("CommandExecutionFailure").gotoChild()));
        }
        if (xml.hasTag("DataValidationFailure")) {
            final List<XError> errors = new ArrayList<XError>();
            CallBack callback = new CallBack() {

                public void execute(XMLTag xml) {
                    Element element = xml.getCurrentTag();
                    if (element.hasAttribute("ecode")) {
                        errors.add(parseError(xml));
                    }
                }
            };
            xml.forEach(callback, "//*");
            throw new DataValidationFailureException(errors);
        }
        throw new InvalidResponseException(xml.toString());
    }

    private static XError parseError(XMLTag xml) {
        Element element = xml.getCurrentTag();
        return new XError(buildXPath(element), element.getAttribute("ecode"), element.getAttribute("etext"), element.getAttribute("edetail"), TfXAPIUtil.parseDateOrNull(element.getAttribute("edate")));
    }

    private static String buildXPath(Element element) {
        StringBuilder builder = new StringBuilder();
        buildXPath(element, builder);
        return builder.toString();
    }

    private static void buildXPath(Node node, StringBuilder builder) {
        if (node instanceof Element) {
            buildXPath(node.getParentNode(), builder);
            builder.append("/").append(node.getNodeName());
        }
    }

    /**
   * Convience method that {@link CacheProvider} implementations can use to create suitable cache keys.
   */
    public static Object createCacheKey(Login login, XRequest request, boolean cachePerXmlLoginId) {
        Object cacheKey = "";
        String xmlLoginId = "";
        if (request instanceof HasCacheKey) {
            cacheKey = notNull(((HasCacheKey) request).getCacheKey(), "cache key cannot be null");
        }
        if (cachePerXmlLoginId) {
            xmlLoginId = notNull(login.getXmlLoginId(), "xml login id cannot be null for cache");
        }
        return new CacheKeyImpl(request.getClass(), cacheKey, xmlLoginId);
    }

    private static final class CacheKeyImpl {

        final Class<? extends XRequest> requestType;

        final Object cacheKey;

        final String xmlLoginId;

        final int hashCode;

        CacheKeyImpl(Class<? extends XRequest> requestType, Object cacheKey, String xmlLoginId) {
            this.requestType = requestType;
            this.cacheKey = cacheKey;
            this.xmlLoginId = xmlLoginId;
            this.hashCode = (requestType.hashCode() + (13 * cacheKey.hashCode()) + (31 * xmlLoginId.hashCode()));
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof CacheKeyImpl)) return false;
            CacheKeyImpl other = (CacheKeyImpl) o;
            if (this.requestType != other.requestType) return false;
            if (!this.cacheKey.equals(other.cacheKey)) return false;
            if (!this.xmlLoginId.equals(other.xmlLoginId)) return false;
            return true;
        }

        @Override
        public String toString() {
            return (requestType.getName() + "/" + cacheKey + "/" + xmlLoginId);
        }
    }
}
