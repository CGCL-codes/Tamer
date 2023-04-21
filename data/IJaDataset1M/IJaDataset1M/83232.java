package uk.ac.ed.ph.qtitools.xmlutils;

import org.qtitools.qti.exception.QTIParseException;
import org.qtitools.qti.node.XmlObject;
import java.io.Serializable;

/**
 * FIXME: Document this type!
 *
 * @author  David McKain
 * @version $Revision: 2812 $
 */
public final class QTIReadResult<E extends XmlObject> implements Serializable {

    private static final long serialVersionUID = -6470500039269477402L;

    private final E jqtiObject;

    private final XMLParseResult xmlParseResult;

    private final QTIParseException qtiParseException;

    public QTIReadResult(E jqtiObject, XMLParseResult xmlParseResult, QTIParseException qtiParseException) {
        this.jqtiObject = jqtiObject;
        this.xmlParseResult = xmlParseResult;
        this.qtiParseException = qtiParseException;
    }

    public E getJQTIObject() {
        return jqtiObject;
    }

    public XMLParseResult getXMLParseResult() {
        return xmlParseResult;
    }

    public QTIParseException getQTIParseException() {
        return qtiParseException;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode() + "(jqtiObject=" + jqtiObject + ",xmlParseResult=" + xmlParseResult + ",qtiParseException=" + qtiParseException + ")";
    }
}
