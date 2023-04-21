package ca.huy.ximple;

import java.io.IOException;
import java.io.Writer;
import org.w3c.dom.Element;

public interface Xerializer {

    /**
     * Convert object to text.
     * 
     * @param o
     * @return text representation of Object.
     */
    public void marshal(Object o, Writer out) throws XerialException, IOException;

    /**
     * Convert text to Object.
     * 
     * @param content
     * @return Object represented by content.
     */
    public Object unmarshal(Element root) throws XerialException;
}
