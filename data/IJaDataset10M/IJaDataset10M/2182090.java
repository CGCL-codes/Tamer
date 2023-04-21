package org.jecars.tools.workflow.xml;

import java.io.IOException;
import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.value.BinaryImpl;
import org.jecars.client.BASE64Decoder;
import org.jecars.client.BASE64Encoder;
import org.w3c.dom.DOMException;

/**
 * Contains generic JCR methods as used by WF_XmlReader / WF_XmlWriter
 *
 * @author schulth
 */
public class WF_JcrHelper {

    /**
     * return true if type is a type that must be stored as a binary value
     *
     * @param type a PropertyType type
     * @return
     */
    public static boolean isBinaryValue(int type) {
        return type == PropertyType.BINARY;
    }

    /** encode contents of binary using {@link BASE64Encoder}
     * @param binary
     * @return
     * @throws RepositoryException
     * @throws WF_XmlException 
     */
    public static String encode(Binary binary) throws RepositoryException, WF_XmlException {
        StringBuilder data = new StringBuilder();
        byte[] bytes = new byte[8192];
        long size = binary.getSize();
        long totalRead = 0;
        while (totalRead < size) {
            try {
                int nread = binary.read(bytes, totalRead);
                if (nread > 0) {
                    data.append(BASE64Encoder.encodeBuffer(bytes, 0, nread));
                    totalRead = totalRead + nread;
                } else {
                    throw new WF_XmlException("Unexpected end of binary");
                }
            } catch (DOMException e) {
                throw new WF_XmlException(e);
            } catch (IOException e) {
                throw new WF_XmlException(e);
            }
        }
        return data.toString();
    }

    /** decode contents of a String that was encoded using {@link #encode(javax.jcr.Binary) }
     * @param binary
     * @return
     * @throws RepositoryException
     * @throws WF_XmlException 
     */
    public static Binary decode(String encoded) throws RepositoryException, WF_XmlException {
        Binary result = new BinaryImpl(BASE64Decoder.decodeBuffer(encoded));
        return result;
    }

    /** return true if property with name propertyName should be skipped
     * @param skipSystemProperty whether to skip system properties such as the jcr:* properties (e.g. jcr:CreatedBy)
     *    Note: some system properties cannot be skipped because we need the info in order to create the JeCARS nodes from the XML, such as jcr:mixinTypes.
     * @param propertyName property name including nameSpace, e.g. jcr:createdBy, or jecars:Modified
     * @return true if propertyName indicates a system property, and skipSystemProperty
     */
    public static boolean isSkipSystemProperty(boolean skipSystemProperty, String propertyName) {
        boolean isSystemProperty = propertyName != null && ((propertyName.startsWith("jcr:") && (!propertyName.equals("jcr:mixinTypes")) && (!propertyName.equals("jcr:data")) && (!propertyName.equals("jcr:mimeType"))) || propertyName.equals("jecars:Modified"));
        return skipSystemProperty && isSystemProperty;
    }
}
