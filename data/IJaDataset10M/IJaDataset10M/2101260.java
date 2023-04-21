package org.exist.indexing.facet;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.PhraseQuery;
import org.exist.dom.*;
import org.exist.indexing.AbstractMatchListener;
import org.exist.numbering.NodeId;
import org.exist.stax.EmbeddedXMLStreamReader;
import org.exist.storage.DBBroker;
import org.exist.storage.IndexSpec;
import org.exist.storage.NodePath;
import org.exist.util.serializer.AttrList;
import org.xml.sax.SAXException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class FacetMatchListener extends AbstractMatchListener {

    private static final Logger LOG = Logger.getLogger(FacetMatchListener.class);

    private Match match;

    private Map<String, Query> termMap;

    private Map<NodeId, Offset> nodesWithMatch;

    private FacetIndex index;

    private FacetConfig config;

    private DBBroker broker;

    public FacetMatchListener(FacetIndex index, DBBroker broker, NodeProxy proxy) {
        this.index = index;
        reset(broker, proxy);
    }

    public boolean hasMatches(NodeProxy proxy) {
        Match nextMatch = proxy.getMatches();
        while (nextMatch != null) {
            if (nextMatch.getIndexId() == FacetIndex.ID) {
                return true;
            }
            nextMatch = nextMatch.getNextMatch();
        }
        return false;
    }

    protected void reset(DBBroker broker, NodeProxy proxy) {
        this.broker = broker;
        this.match = proxy.getMatches();
        setNextInChain(null);
        IndexSpec indexConf = proxy.getDocument().getCollection().getIndexConfiguration(broker);
        if (indexConf != null) config = (FacetConfig) indexConf.getCustomIndexSpec(FacetIndex.ID);
        getTerms();
        nodesWithMatch = new TreeMap<NodeId, Offset>();
        NodeSet ancestors = null;
        Match nextMatch = this.match;
        while (nextMatch != null) {
            if (proxy.getNodeId().isDescendantOf(nextMatch.getNodeId())) {
                if (ancestors == null) ancestors = new NewArrayNodeSet();
                ancestors.add(new NodeProxy(proxy.getDocument(), nextMatch.getNodeId()));
            }
            nextMatch = nextMatch.getNextMatch();
        }
        if (ancestors != null && !ancestors.isEmpty()) {
            for (NodeProxy p : ancestors) {
                scanMatches(p);
            }
        }
    }

    @Override
    public void startElement(QName qname, AttrList attribs) throws SAXException {
        Match nextMatch = match;
        while (nextMatch != null) {
            if (nextMatch.getNodeId().equals(getCurrentNode().getNodeId())) {
                scanMatches(new NodeProxy(getCurrentNode()));
                break;
            }
            nextMatch = nextMatch.getNextMatch();
        }
        super.startElement(qname, attribs);
    }

    @Override
    public void characters(CharSequence seq) throws SAXException {
        NodeId nodeId = getCurrentNode().getNodeId();
        Offset offset = nodesWithMatch.get(nodeId);
        if (offset == null) super.characters(seq); else {
            String s = seq.toString();
            int pos = 0;
            while (offset != null) {
                if (offset.startOffset > pos) {
                    if (offset.startOffset > seq.length()) throw new SAXException("start offset out of bounds");
                    super.characters(s.substring(pos, offset.startOffset));
                }
                int end = offset.endOffset;
                if (end > s.length()) end = s.length();
                super.startElement(MATCH_ELEMENT, null);
                super.characters(s.substring(offset.startOffset, end));
                super.endElement(MATCH_ELEMENT);
                pos = end;
                offset = offset.next;
            }
            if (pos < seq.length()) super.characters(s.substring(pos));
        }
    }

    private void scanMatches(NodeProxy p) {
        NodePath path = getPath(p);
        FacetIndexConfig idxConf = config.getConfig(path);
        TextExtractor extractor = new DefaultTextExtractor();
        extractor.configure(config, idxConf);
        OffsetList offsets = new OffsetList();
        int level = 0;
        int textOffset = 0;
        try {
            EmbeddedXMLStreamReader reader = broker.getXMLStreamReader(p, false);
            while (reader.hasNext()) {
                int ev = reader.next();
                switch(ev) {
                    case XMLStreamReader.END_ELEMENT:
                        if (--level < 0) break;
                        textOffset += extractor.endElement(reader.getQName());
                        break;
                    case XMLStreamReader.START_ELEMENT:
                        ++level;
                        textOffset += extractor.startElement(reader.getQName());
                        break;
                    case XMLStreamReader.CHARACTERS:
                        NodeId nodeId = (NodeId) reader.getProperty(EmbeddedXMLStreamReader.PROPERTY_NODE_ID);
                        offsets.add(textOffset, nodeId);
                        textOffset += extractor.characters(reader.getXMLText());
                        break;
                }
            }
        } catch (IOException e) {
            LOG.warn("Problem found while serializing XML: " + e.getMessage(), e);
        } catch (XMLStreamException e) {
            LOG.warn("Problem found while serializing XML: " + e.getMessage(), e);
        }
        Analyzer analyzer = idxConf.getAnalyzer();
        if (analyzer == null) {
            analyzer = index.getDefaultAnalyzer();
        }
        LOG.debug("Analyzer: " + analyzer + " for path: " + path);
        TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(extractor.getText().toString()));
        MarkableTokenFilter stream = new MarkableTokenFilter(tokenStream);
        Token token;
        try {
            while ((token = stream.next()) != null) {
                String text = token.term();
                Query query = termMap.get(text);
                if (query != null) {
                    if (query instanceof PhraseQuery) {
                        PhraseQuery phraseQuery = (PhraseQuery) query;
                        Term[] terms = phraseQuery.getTerms();
                        if (text.equals(terms[0].text())) {
                            stream.mark();
                            int t = 1;
                            List<Token> tokenList = new ArrayList<Token>(terms.length);
                            tokenList.add(token);
                            while ((token = stream.next()) != null && t < terms.length) {
                                text = token.term();
                                if (text.equals(terms[t].text())) {
                                    tokenList.add(token);
                                    if (++t == terms.length) {
                                        break;
                                    }
                                } else {
                                    stream.reset();
                                    break;
                                }
                            }
                            if (tokenList.size() == terms.length) {
                                int lastIdx = -1;
                                for (int i = 0; i < terms.length; i++) {
                                    Token nextToken = tokenList.get(i);
                                    int idx = offsets.getIndex(nextToken.startOffset());
                                    NodeId nodeId = offsets.ids[idx];
                                    Offset offset = nodesWithMatch.get(nodeId);
                                    if (offset != null) if (lastIdx == idx) offset.setEndOffset(nextToken.endOffset() - offsets.offsets[idx]); else offset.add(nextToken.startOffset() - offsets.offsets[idx], nextToken.endOffset() - offsets.offsets[idx]); else nodesWithMatch.put(nodeId, new Offset(nextToken.startOffset() - offsets.offsets[idx], nextToken.endOffset() - offsets.offsets[idx]));
                                    lastIdx = idx;
                                }
                            }
                        }
                    } else {
                        int idx = offsets.getIndex(token.startOffset());
                        NodeId nodeId = offsets.ids[idx];
                        Offset offset = nodesWithMatch.get(nodeId);
                        if (offset != null) offset.add(token.startOffset() - offsets.offsets[idx], token.endOffset() - offsets.offsets[idx]); else nodesWithMatch.put(nodeId, new Offset(token.startOffset() - offsets.offsets[idx], token.endOffset() - offsets.offsets[idx]));
                    }
                }
            }
        } catch (IOException e) {
            LOG.warn("Problem found while serializing XML: " + e.getMessage(), e);
        }
    }

    private NodePath getPath(NodeProxy proxy) {
        NodePath path = new NodePath();
        StoredNode node = (StoredNode) proxy.getNode();
        walkAncestor(node, path);
        return path;
    }

    private void walkAncestor(StoredNode node, NodePath path) {
        if (node == null) return;
        StoredNode parent = node.getParentStoredNode();
        walkAncestor(parent, path);
        path.addComponent(node.getQName());
    }

    /**
     * Get all query terms from the original queries.
     */
    private void getTerms() {
        Set<Query> queries = new HashSet<Query>();
        termMap = new TreeMap<String, Query>();
        Match nextMatch = this.match;
        while (nextMatch != null) {
            if (nextMatch.getIndexId() == FacetIndex.ID) {
                Query query = ((FacetIndexWorker.FacetMatch) nextMatch).getQuery();
                if (!queries.contains(query)) {
                    queries.add(query);
                    IndexReader reader = null;
                    try {
                        reader = index.getReader();
                        FacetUtil.extractTerms(query, termMap, reader);
                    } catch (IOException e) {
                        LOG.warn("Error while highlighting lucene query matches: " + e.getMessage(), e);
                    } finally {
                        index.releaseReader(reader);
                    }
                }
            }
            nextMatch = nextMatch.getNextMatch();
        }
    }

    private class OffsetList {

        int[] offsets = new int[16];

        NodeId[] ids = new NodeId[16];

        int len = 0;

        void add(int offset, NodeId nodeId) {
            if (len == offsets.length) {
                int[] tempOffsets = new int[len * 2];
                System.arraycopy(offsets, 0, tempOffsets, 0, len);
                offsets = tempOffsets;
                NodeId[] tempIds = new NodeId[len * 2];
                System.arraycopy(ids, 0, tempIds, 0, len);
                ids = tempIds;
            }
            offsets[len] = offset;
            ids[len++] = nodeId;
        }

        int getIndex(int offset) {
            for (int i = 0; i < len; i++) {
                if (offsets[i] <= offset && (i + 1 == len || offsets[i + 1] > offset)) {
                    return i;
                }
            }
            return -1;
        }
    }

    private class Offset {

        int startOffset;

        int endOffset;

        Offset next = null;

        Offset(int startOffset, int endOffset) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        void add(int offset, int endOffset) {
            if (startOffset == offset) return;
            getLast().next = new Offset(offset, endOffset);
        }

        private Offset getLast() {
            Offset next = this;
            while (next.next != null) {
                next = next.next;
            }
            return next;
        }

        void setEndOffset(int offset) {
            getLast().endOffset = offset;
        }
    }
}
