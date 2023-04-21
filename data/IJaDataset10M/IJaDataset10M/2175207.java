package stickyhand;

import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class NZBSegment {

    private int bytes;

    private int number;

    private String ID;

    public NZBSegment(Element e) throws DataConversionException {
        List attributeList = e.getAttributes();
        Iterator attributeIterator = attributeList.iterator();
        while (attributeIterator.hasNext()) {
            Attribute attribute = (Attribute) attributeIterator.next();
            if (attribute.getName().equals("bytes")) {
                bytes = attribute.getIntValue();
            } else if (attribute.getName().equals("number")) {
                number = attribute.getIntValue();
            }
        }
        ID = e.getTextNormalize();
    }

    /**
	 * @return the bytes
	 */
    public int getBytes() {
        return bytes;
    }

    /**
	 * @return the number
	 */
    public int getNumber() {
        return number;
    }

    /**
	 * @return the iD
	 */
    public String getID() {
        return ID;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("number: " + number + " bytes: " + bytes + " ID: " + ID);
        return result.toString();
    }
}
