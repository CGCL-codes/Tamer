package org.sourceforge.zlang.model;

import java.util.*;
import org.sourceforge.zlang.model.parser.SimpleNode;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * break
 * 
 * @author <a href="Tim.Lebedkov@web.de">Tim Lebedkov</a>
 * @version $Id: ZBreak.java,v 1.2 2002/12/04 22:45:47 hilt2 Exp $
 */
public class ZBreak extends ZStatement {

    static final long serialVersionUID = 3549163557427353221L;

    /**
     * Constructor 
     *
     * @parent parent of this node
     */
    public ZBreak(ZElement parent) {
        super(parent);
    }

    public void printXML(XMLCreator h) throws SAXException {
        throw new UnsupportedOperationException();
    }

    public void printJava(IndentedWriter w) {
        throw new UnsupportedOperationException();
    }

    public void load(Element el) {
        throw new UnsupportedOperationException();
    }

    public void load(SimpleNode el) {
        throw new UnsupportedOperationException();
    }
}
