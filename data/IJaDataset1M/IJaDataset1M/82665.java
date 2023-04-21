package net.sf.etl.parsers.xml;

import net.sf.etl.parsers.Terms;
import net.sf.etl.parsers.TextPos;

/**
 * Structural XMI2-like output. XMI2 specification is available at
 * http://www.omg.org/cgi-bin/doc?formal/03-05-02.
 * 
 * @author const
 */
public class StructuralOutput extends XMLOutput {

    /** a logger */
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(StructuralOutput.class.getName());

    /**
	 * xmi namespace
	 */
    static final String xmins = "http://www.omg.org/XMI";

    /**
	 * schema instance namespace
	 */
    static final String xsins = "http://www.w3.org/2001/XMLSchema-instance";

    /**
	 * a constructor
	 */
    public StructuralOutput() {
        super();
    }

    /**
	 * @see net.sf.etl.parsers.xml.XMLOutput#process()
	 */
    @Override
    protected void process() throws Exception {
        suggestPrefix("xmi", xmins);
        suggestPrefix("xsi", xsins);
        startElement(xmins, "xmi");
        getPrefixForAuxilaryNamespace(xsins);
        attribute(xmins, "version", "2.0");
        parser.advance();
        loop: while (true) {
            switch(parser.current().kind()) {
                case OBJECT_START:
                    processObject(null);
                    break;
                case EOF:
                    break loop;
                default:
                    parser.advance();
            }
        }
        endElement();
        out.flush();
    }

    /**
	 * process object
	 * 
	 * @param property
	 *            current property
	 * @throws Exception
	 *             in case of IO problem
	 */
    void processObject(String property) throws Exception {
        if (property == null) {
            startElement(parser.current().objectName().namespace(), parser.current().objectName().name());
        } else {
            startElement(property);
            final String pfx = getPrefixForAuxilaryNamespace(parser.current().objectName().namespace());
            attribute(xsins, "type", pfx + ":" + parser.current().objectName().name());
        }
        final TextPos start = parser.current().start();
        attribute("startLine", Integer.toString(start.line()));
        attribute("startColumn", Integer.toString(start.column()));
        attribute("startOffset", Long.toString(start.offset()));
        parser.advance();
        boolean hadObjects = false;
        int extraStarts = 0;
        loop: while (true) {
            switch(parser.current().kind()) {
                case OBJECT_START:
                    log.severe("Uexprected Object Start Event in " + parser.getSystemId() + " (Grammar BUG): " + parser.current());
                    extraStarts++;
                    parser.advance();
                    break;
                case EOF:
                    return;
                case OBJECT_END:
                    if (extraStarts > 0) {
                        parser.advance();
                        extraStarts--;
                        break;
                    } else {
                        break loop;
                    }
                case PROPERTY_START:
                case LIST_PROPERTY_START:
                    hadObjects = processProperty(hadObjects);
                    break;
                default:
                    parser.advance();
            }
        }
        if (hadObjects) {
            final TextPos end = parser.current().end();
            startElement("endLine");
            out.writeCharacters(Integer.toString(end.line()));
            endElement();
            startElement("endColumn");
            out.writeCharacters(Integer.toString(end.column()));
            endElement();
            startElement("endOffset");
            out.writeCharacters(Long.toString(end.offset()));
            endElement();
        } else {
            final TextPos end = parser.current().end();
            attribute("endLine", Integer.toString(end.line()));
            attribute("endColumn", Integer.toString(end.column()));
            attribute("endOffset", Long.toString(end.offset()));
        }
        if (property == null) {
            endElement();
        } else {
            endElement();
        }
        parser.advance();
    }

    /**
	 * process property of the object
	 * 
	 * @param hadElements
	 *            if true, there had been aready elements output for the current
	 *            object
	 * @return true if property had been output as element rather then xml
	 *         attribute.
	 * @throws Exception
	 *             in case of IO problem
	 */
    boolean processProperty(boolean hadElements) throws Exception {
        final String prop = parser.current().propertyName().name();
        final boolean isList = parser.current().kind() == Terms.LIST_PROPERTY_START;
        parser.advance();
        int extraStarts = 0;
        loop: while (true) {
            switch(parser.current().kind()) {
                case PROPERTY_START:
                case LIST_PROPERTY_START:
                    log.severe("Uexprected Property Start Event in " + parser.getSystemId() + " (Grammar BUG): " + parser.current());
                    extraStarts++;
                    parser.advance();
                    break;
                case EOF:
                    return hadElements;
                case OBJECT_START:
                    hadElements = true;
                    this.processObject(prop);
                    break;
                case VALUE:
                    if (isList) {
                        hadElements = true;
                    }
                    if (hadElements) {
                        startElement(prop);
                        out.writeCharacters(parser.current().token().token().text());
                        endElement();
                    } else {
                        attribute(prop, parser.current().token().token().text());
                    }
                    parser.advance();
                    break;
                case PROPERTY_END:
                case LIST_PROPERTY_END:
                    if (extraStarts > 0) {
                        extraStarts--;
                        parser.advance();
                        break;
                    } else {
                        break loop;
                    }
                default:
                    parser.advance();
            }
        }
        parser.advance();
        return hadElements;
    }
}
