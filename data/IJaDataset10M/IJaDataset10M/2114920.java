package de.fhdarmstadt.fbi.dtree.xml;

import de.fhdarmstadt.fbi.dtree.model.Alphabet;
import de.fhdarmstadt.fbi.dtree.model.Classification;
import de.fhdarmstadt.fbi.dtree.model.ClassificationNode;
import de.fhdarmstadt.fbi.dtree.model.DTreeNode;
import de.fhdarmstadt.fbi.dtree.model.PatternNode;
import org.jfree.xml.AbstractElementDefinitionHandler;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public final class SubNodeHandler extends AbstractElementDefinitionHandler {

    private DTreeNode node;

    private Alphabet alphabet;

    public SubNodeHandler(final Parser parser, final Alphabet alphabet) {
        super(parser);
        this.alphabet = alphabet;
    }

    public final DTreeNode getNode() {
        return node;
    }

    public final void startElement(final String s, final Attributes attributes) throws SAXException {
        if (s.equals(XmlTags.CLASSIFY_TAG)) {
            final String value = attributes.getValue("value");
            if (value == null) {
                throw new ElementDefinitionException("Value is not set");
            }
            if (value.equals("true")) {
                node = new ClassificationNode(Classification.POSITIVE);
            } else if (value.equals("false")) {
                node = new ClassificationNode(Classification.NEGATIVE);
            } else {
                throw new ElementDefinitionException("Value must be one of 'true' or 'false'");
            }
        } else if (s.equals(XmlTags.NODE_TAG)) {
            final PatternNode pnode = new PatternNode();
            final NodeHandler nodeHandler = new NodeHandler(getParser(), pnode, alphabet);
            getParser().pushFactory(nodeHandler);
            node = pnode;
        }
    }

    public final void endElement(final String s) throws SAXException {
        if (s.equals(XmlTags.CLASSIFY_TAG)) {
        } else if (s.equals(XmlTags.NODE_TAG)) {
        } else if (s.equals(XmlTags.ON_FALSE_TAG) || s.equals(XmlTags.ON_TRUE_TAG)) {
            getParser().popFactory().endElement(s);
        }
    }
}
