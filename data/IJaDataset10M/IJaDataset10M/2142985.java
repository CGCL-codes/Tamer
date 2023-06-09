package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;
import javax.xml.transform.OutputKeys;
import org.apache.xml.serializer.utils.MsgKey;
import org.apache.xml.serializer.utils.Utils;
import org.xml.sax.ContentHandler;

/**
 * This class is a public API, it is a factory for creating serializers.
   * 
   * The properties object passed to the getSerializer() method should be created by
   * the OutputPropertiesFactory. Although the properties object
   * used to create a serializer does not need to be obtained 
   * from OutputPropertiesFactory,
   * using this factory ensures that the default key/value properties
   * are set for the given output "method".
   * 
   * <p>
   * The standard property keys supported are: "method", "version", "encoding",
   * "omit-xml-declaration", "standalone", doctype-public",
   * "doctype-system", "cdata-section-elements", "indent", "media-type". 
   * These property keys and their values are described in the XSLT recommendation,
   * see {@link <a href="http://www.w3.org/TR/1999/REC-xslt-19991116"> XSLT 1.0 recommendation</a>}
   * 
   * <p>
   * The value of the "cdata-section-elements" property key is a whitespace
   * separated list of elements. If the element is in a namespace then 
   * value is passed in this format: {uri}localName 
   *
   * <p>
   * The non-standard property keys supported are defined in {@link OutputPropertiesFactory}.
   *
   * @see OutputPropertiesFactory
   * @see Method
   * @see Serializer
   */
public final class SerializerFactory {

    /**
   * This constructor is private just to prevent the creation of such an object.
   */
    private SerializerFactory() {
    }

    /**
   * Associates output methods to default output formats.
   */
    private static Hashtable m_formats = new Hashtable();

    /**
   * Returns a serializer for the specified output method. The output method
   * is specified by the value of the property associated with the "method" key.
   * If no implementation exists that supports the specified output method
   * an exception of some type will be thrown.
   * For a list of the output "method" key values see {@link Method}.
   *
   * @param format The output format, minimally the "method" property must be set.
   * @return A suitable serializer.
   * @throws IllegalArgumentException if method is
   * null or an appropriate serializer can't be found
   * @throws Exception if the class for the serializer is found but does not
   * implement ContentHandler.
   * @throws WrappedRuntimeException if an exception is thrown while trying to find serializer
   */
    public static Serializer getSerializer(Properties format) {
        Serializer ser;
        try {
            String method = format.getProperty(OutputKeys.METHOD);
            if (method == null) {
                String msg = Utils.messages.createMessage(MsgKey.ER_FACTORY_PROPERTY_MISSING, new Object[] { OutputKeys.METHOD });
                throw new IllegalArgumentException(msg);
            }
            String className = format.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);
            if (null == className) {
                Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
                className = methodDefaults.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);
                if (null == className) {
                    String msg = Utils.messages.createMessage(MsgKey.ER_FACTORY_PROPERTY_MISSING, new Object[] { OutputPropertiesFactory.S_KEY_CONTENT_HANDLER });
                    throw new IllegalArgumentException(msg);
                }
            }
            ClassLoader loader = ObjectFactory.findClassLoader();
            Class cls = ObjectFactory.findProviderClass(className, loader, true);
            Object obj = cls.newInstance();
            if (obj instanceof SerializationHandler) {
                ser = (Serializer) cls.newInstance();
                ser.setOutputFormat(format);
            } else {
                if (obj instanceof ContentHandler) {
                    className = SerializerConstants.DEFAULT_SAX_SERIALIZER;
                    cls = ObjectFactory.findProviderClass(className, loader, true);
                    SerializationHandler sh = (SerializationHandler) cls.newInstance();
                    sh.setContentHandler((ContentHandler) obj);
                    sh.setOutputFormat(format);
                    ser = sh;
                } else {
                    throw new Exception(Utils.messages.createMessage(MsgKey.ER_SERIALIZER_NOT_CONTENTHANDLER, new Object[] { className }));
                }
            }
        } catch (Exception e) {
            throw new org.apache.xml.serializer.utils.WrappedRuntimeException(e);
        }
        return ser;
    }
}
