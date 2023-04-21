package org.apache.axiom.om.ds;

import org.apache.axiom.om.OMDataSourceExt;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * ByteArrayDataSource is an example implementation of OMDataSourceExt.
 * Use it to insert a (byte[], encoding) into an OM Tree.
 * This data source is useful for placing bytes into an OM
 * tree, instead of having a deeply nested tree.
 */
public class ByteArrayDataSource extends OMDataSourceExtBase {

    private static final Log log = LogFactory.getLog(ByteArrayDataSource.class);

    private static boolean DEBUG_ENABLED = log.isDebugEnabled();

    ByteArray byteArray = null;

    /**
     * Constructor
     * @param bytes 
     * @param encoding
     */
    public ByteArrayDataSource(byte[] bytes, String encoding) {
        byteArray = new ByteArray();
        byteArray.bytes = bytes;
        byteArray.encoding = encoding;
    }

    public XMLStreamReader getReader() throws XMLStreamException {
        if (DEBUG_ENABLED) {
            log.debug("getReader");
        }
        return StAXUtils.createXMLStreamReader(new ByteArrayInputStream(byteArray.bytes), byteArray.encoding);
    }

    public Object getObject() {
        return byteArray;
    }

    public boolean isDestructiveRead() {
        return false;
    }

    public boolean isDestructiveWrite() {
        return false;
    }

    public byte[] getXMLBytes(String encoding) throws UnsupportedEncodingException {
        if (DEBUG_ENABLED) {
            log.debug("getXMLBytes encoding=" + encoding);
        }
        if (!byteArray.encoding.equalsIgnoreCase(encoding)) {
            String text = new String(byteArray.bytes, byteArray.encoding);
            byteArray.bytes = text.getBytes(encoding);
            byteArray.encoding = encoding;
        }
        return byteArray.bytes;
    }

    public void close() {
        byteArray = null;
    }

    public OMDataSourceExt copy() {
        return new ByteArrayDataSource(byteArray.bytes, byteArray.encoding);
    }

    /**
     * Object containing the byte[]/encoding pair
     */
    public class ByteArray {

        public byte[] bytes;

        public String encoding;
    }
}
