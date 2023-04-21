package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg definitions parse tree node
 * 
 * @author: Dennis
 */
public class CoXtgDefsParseNode extends CoXtgParseNode {

    private CoXtgDefParseNode m_defNode;

    private CoXtgDefsParseNode m_nextNode;

    /**
 * CoXtgRootParseNode constructor comment.
 */
    public CoXtgDefsParseNode() {
        super();
    }

    public void createXtg(PrintStream s) {
        s.println();
        if (m_defNode != null) m_defNode.createXtg(s);
        if (m_nextNode != null) m_nextNode.createXtg(s);
    }

    public void dump(String indent) {
        System.err.println(indent + this);
        if (m_defNode != null) m_defNode.dump(indent + "  ");
        if (m_nextNode != null) m_nextNode.dump(indent + "  ");
    }

    public void extract(com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l) {
        if (m_defNode != null) m_defNode.extract(r, l);
        if (m_nextNode != null) m_nextNode.extract(r, l);
    }

    public void extract(com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger) {
    }

    boolean parse(CoXtgParser p, CoXtgLogger l) throws CoXtgParseException {
        if ((p.getToken() instanceof CoXtgNameToken) && (p.getNextToken() instanceof CoXtgEqualsToken)) {
            m_defNode = new CoXtgDefParseNode();
            if (m_defNode.parse(p, l)) {
                m_nextNode = new CoXtgDefsParseNode();
                if (!m_nextNode.parse(p, l)) m_nextNode = null;
            } else {
                m_defNode = null;
            }
            return true;
        }
        return false;
    }
}
