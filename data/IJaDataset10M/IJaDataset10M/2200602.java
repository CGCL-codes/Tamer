package gnu.xml.libxmlj.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

/**
 * A DOM character data node implemented in libxml2.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
abstract class GnomeCharacterData extends GnomeNode implements CharacterData {

    GnomeCharacterData(Object id) {
        super(id);
    }

    public String getData() throws DOMException {
        return getNodeValue();
    }

    public void setData(String data) throws DOMException {
        setNodeValue(data);
    }

    public int getLength() {
        return getData().length();
    }

    public String substringData(int offset, int count) throws DOMException {
        return getData().substring(offset, offset + count);
    }

    public void appendData(String arg) throws DOMException {
        setData(getData() + arg);
    }

    public void insertData(int offset, String arg) throws DOMException {
        String data = getData();
        setData(data.substring(0, offset) + arg + data.substring(offset));
    }

    public void deleteData(int offset, int count) throws DOMException {
        String data = getData();
        setData(data.substring(0, offset) + data.substring(offset + count));
    }

    public void replaceData(int offset, int count, String arg) {
        String data = getData();
        setData(data.substring(0, offset) + arg + data.substring(offset + count));
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append("[data=");
        buffer.append(getData());
        buffer.append("]");
        return buffer.toString();
    }
}
