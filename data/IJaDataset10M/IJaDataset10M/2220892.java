package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg paragraphs parse tree node
 * 
 * @author: Dennis
 */
public class CoXtgParagraphsParseNode extends CoXtgParseNode {

    private CoXtgParagraphParseNode m_paragraphNode;

    private CoXtgParagraphsParseNode m_nextNode;

    /**
 * CoXtgRootParseNode constructor comment.
 */
    public CoXtgParagraphsParseNode() {
        super();
    }

    public void createXtg(PrintStream s) {
        s.println();
        if (m_paragraphNode != null) m_paragraphNode.createXtg(s);
        if (m_nextNode != null) m_nextNode.createXtg(s);
    }

    public void dump(String indent) {
        System.err.println(indent + this);
        if (m_paragraphNode != null) m_paragraphNode.dump(indent + "  ");
        if (m_nextNode != null) m_nextNode.dump(indent + "  ");
    }

    public void extract(com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l) {
    }

    public void extract(com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l) {
        if (m_paragraphNode != null) m_paragraphNode.extract(d, l);
        if (m_nextNode != null) m_nextNode.extract(d, l);
    }

    boolean parse(CoXtgParser p, CoXtgLogger l) throws CoXtgParseException {
        m_paragraphNode = new CoXtgParagraphParseNode();
        if (m_paragraphNode.parse(p, l)) {
            m_nextNode = new CoXtgParagraphsParseNode();
            if (!m_nextNode.parse(p, l)) m_nextNode = null;
        } else {
            m_paragraphNode = null;
        }
        return true;
    }
}
