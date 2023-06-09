package org.exist.storage.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;
import org.exist.dom.ExtArrayNodeSet;
import org.exist.dom.Match;
import org.exist.dom.NodeProxy;
import org.exist.dom.NodeSet;
import org.exist.dom.QName;
import org.exist.dom.persistent.StoredNodeProxy;
import org.exist.indexing.AbstractMatchListener;
import org.exist.numbering.NodeId;
import org.exist.stax.EmbeddedXMLStreamReader;
import org.exist.storage.NativeTextEngine;
import org.exist.util.FastQSort;
import org.exist.util.serializer.AttrList;
import org.xml.sax.SAXException;

/**
 * Implementation of {@link org.exist.indexing.MatchListener} for the fulltext index.
 * Right now, the serializer will directly plug this into the listener pipeline. This will
 * change once we move the fulltext index into its own module.
 */
public class FTMatchListener extends AbstractMatchListener {

    private static final Logger LOG = Logger.getLogger(FTMatchListener.class);

    private Match match;

    private Stack offsetStack = null;

    public FTMatchListener() {
    }

    public FTMatchListener(NodeProxy proxy) {
        reset(proxy);
    }

    public boolean hasMatches(NodeProxy proxy) {
        Match nextMatch = proxy.getMatches();
        while (nextMatch != null) {
            if (nextMatch.getIndexId() == NativeTextEngine.FT_MATCH_ID) {
                return true;
            }
            nextMatch = nextMatch.getNextMatch();
        }
        return false;
    }

    protected void reset(NodeProxy proxy) {
        this.match = proxy.getMatches();
        setNextInChain(null);
        NodeSet ancestors = null;
        Match nextMatch = this.match;
        while (nextMatch != null) {
            if (proxy.getNodeId().isDescendantOf(nextMatch.getNodeId())) {
                if (ancestors == null) ancestors = new ExtArrayNodeSet();
                ancestors.add(new StoredNodeProxy(proxy.getDocument(), nextMatch.getNodeId()));
            }
            nextMatch = nextMatch.getNextMatch();
        }
        if (ancestors != null && !ancestors.isEmpty()) {
            for (Iterator i = ancestors.iterator(); i.hasNext(); ) {
                NodeProxy p = (NodeProxy) i.next();
                int startOffset = 0;
                try {
                    XMLStreamReader reader = proxy.getDocument().getBroker().getXMLStreamReader(p, false);
                    while (reader.hasNext()) {
                        int ev = reader.next();
                        NodeId nodeId = (NodeId) reader.getProperty(EmbeddedXMLStreamReader.PROPERTY_NODE_ID);
                        if (nodeId.equals(proxy.getNodeId())) break;
                        if (ev == XMLStreamReader.CHARACTERS) startOffset += reader.getText().length();
                    }
                } catch (IOException e) {
                    LOG.warn("Problem found while serializing XML: " + e.getMessage(), e);
                } catch (XMLStreamException e) {
                    LOG.warn("Problem found while serializing XML: " + e.getMessage(), e);
                }
                if (offsetStack == null) offsetStack = new Stack();
                offsetStack.push(new NodeOffset(p.getNodeId(), startOffset));
            }
        }
    }

    public void startElement(QName qname, AttrList attribs) throws SAXException {
        Match nextMatch = match;
        while (nextMatch != null) {
            if (nextMatch.getNodeId().equals(getCurrentNode().getNodeId())) {
                if (offsetStack == null) offsetStack = new Stack();
                offsetStack.push(new NodeOffset(nextMatch.getNodeId()));
                break;
            }
            nextMatch = nextMatch.getNextMatch();
        }
        super.startElement(qname, attribs);
    }

    public void endElement(QName qname) throws SAXException {
        Match nextMatch = match;
        while (nextMatch != null) {
            if (nextMatch.getNodeId().equals(getCurrentNode().getNodeId())) {
                offsetStack.pop();
                break;
            }
            nextMatch = nextMatch.getNextMatch();
        }
        super.endElement(qname);
    }

    public void characters(CharSequence seq) throws SAXException {
        List offsets = null;
        if (offsetStack != null) {
            for (int i = 0; i < offsetStack.size(); i++) {
                NodeOffset no = (NodeOffset) offsetStack.get(i);
                int end = no.offset + seq.length();
                Match next = match;
                while (next != null) {
                    if (next.getIndexId() == NativeTextEngine.FT_MATCH_ID && next.getNodeId().equals(no.nodeId)) {
                        int freq = next.getFrequency();
                        for (int j = 0; j < freq; j++) {
                            Match.Offset offset = next.getOffset(j);
                            if (offset.getOffset() < end && offset.getOffset() + offset.getLength() > no.offset) {
                                if (offsets == null) {
                                    offsets = new ArrayList(4);
                                }
                                int start = offset.getOffset() - no.offset;
                                int len = offset.getLength();
                                if (start < 0) {
                                    len = len - Math.abs(start);
                                    start = 0;
                                }
                                if (start + len > seq.length()) len = seq.length() - start;
                                offsets.add(new Match.Offset(start, len));
                            }
                        }
                    }
                    next = next.getNextMatch();
                }
                no.offset = end;
            }
        }
        Match next = match;
        while (next != null) {
            if (next.getIndexId() == NativeTextEngine.FT_MATCH_ID && next.getNodeId().equals(getCurrentNode().getNodeId())) {
                if (offsets == null) offsets = new ArrayList();
                int freq = next.getFrequency();
                for (int i = 0; i < freq; i++) {
                    offsets.add(next.getOffset(i));
                }
            }
            next = next.getNextMatch();
        }
        if (offsets != null) {
            FastQSort.sort(offsets, 0, offsets.size() - 1);
            String s = seq.toString();
            int pos = 0;
            for (int i = 0; i < offsets.size(); i++) {
                Match.Offset offset = (Match.Offset) offsets.get(i);
                if (offset.getOffset() > pos) {
                    super.characters(s.substring(pos, pos + (offset.getOffset() - pos)));
                }
                super.startElement(MATCH_ELEMENT, null);
                super.characters(s.substring(offset.getOffset(), offset.getOffset() + offset.getLength()));
                super.endElement(MATCH_ELEMENT);
                pos = offset.getOffset() + offset.getLength();
            }
            if (pos < s.length()) {
                super.characters(s.substring(pos));
            }
        } else super.characters(seq);
    }

    private class NodeOffset {

        NodeId nodeId;

        int offset = 0;

        public NodeOffset(NodeId nodeId) {
            this.nodeId = nodeId;
        }

        public NodeOffset(NodeId nodeId, int offset) {
            this.nodeId = nodeId;
            this.offset = offset;
        }
    }
}
