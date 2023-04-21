package net.sourceforge.xmote.encoding;

import java.util.UUID;
import org.jdom.Element;

/**
 * Encoding for String class.
 * 
 * @author Jason Rush
 */
public class UuidEncoding extends DefaultEncoding {

    private static final Class<?> CLASS = UUID.class;

    private static final String NAME = "uuid";

    public UuidEncoding() {
        super(CLASS, NAME);
    }

    /**
   * @see net.sourceforge.xmote.encoding.DefaultEncoding#encode(java.lang.Object)
   */
    public Element encode(Object object) throws EncodingException {
        UUID value = (UUID) object;
        Element root = super.encode(object);
        if (value != null) {
            root.setText(value.toString());
        }
        return root;
    }

    /**
   * @see net.sourceforge.xmote.encoding.DefaultEncoding#decode(org.jdom.Element)
   */
    public Object decode(Element root) throws EncodingException {
        String nullValue = root.getAttributeValue("null");
        if (nullValue != null && Boolean.parseBoolean(nullValue)) {
            return null;
        }
        return UUID.fromString(root.getText());
    }
}
