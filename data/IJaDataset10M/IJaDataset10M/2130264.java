package org.progeeks.wn;

import java.util.*;
import com.phoenixst.plexus.*;
import org.progeeks.graph.*;
import org.progeeks.util.*;
import net.didion.jwnl.*;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *  A graph implementation providing access to a WordNet
 *  database.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class WordNetGraph extends AbstractGraph {

    private static Set UNDIRECTED = new HashSet();

    private static Set FORWARD = new HashSet();

    private static Set REVERSE = new HashSet();

    static {
        UNDIRECTED.add(PointerType.ANTONYM);
        UNDIRECTED.add(PointerType.ATTRIBUTE);
        FORWARD.add(PointerType.CATEGORY);
        REVERSE.add(PointerType.CATEGORY_MEMBER);
        FORWARD.add(PointerType.CAUSE);
        FORWARD.add(PointerType.DERIVED);
        REVERSE.add(PointerType.ENTAILED_BY);
        FORWARD.add(PointerType.ENTAILMENT);
        FORWARD.add(PointerType.HYPERNYM);
        REVERSE.add(PointerType.HYPONYM);
        FORWARD.add(PointerType.INSTANCE_HYPERNYM);
        REVERSE.add(PointerType.INSTANCES_HYPONYM);
        REVERSE.add(PointerType.MEMBER_HOLONYM);
        FORWARD.add(PointerType.MEMBER_MERONYM);
        UNDIRECTED.add(PointerType.NOMINALIZATION);
        REVERSE.add(PointerType.PART_HOLONYM);
        FORWARD.add(PointerType.PART_MERONYM);
        REVERSE.add(PointerType.PARTICIPLE_OF);
        FORWARD.add(PointerType.PERTAINYM);
        FORWARD.add(PointerType.REGION);
        REVERSE.add(PointerType.REGION_MEMBER);
        UNDIRECTED.add(PointerType.SEE_ALSO);
        UNDIRECTED.add(PointerType.SIMILAR_TO);
        REVERSE.add(PointerType.SUBSTANCE_HOLONYM);
        FORWARD.add(PointerType.SUBSTANCE_MERONYM);
        REVERSE.add(PointerType.USAGE);
        FORWARD.add(PointerType.USAGE_MEMBER);
        UNDIRECTED.add(PointerType.VERB_GROUP);
    }

    private Dictionary dict;

    public WordNetGraph(Dictionary dict) {
        this.dict = dict;
    }

    public Collection nodes() {
        return (Collections.EMPTY_SET);
    }

    public Collection edges() {
        return (Collections.EMPTY_SET);
    }

    public boolean containsNode(Object node) {
        if (node instanceof String) {
            return (true);
        } else if (node instanceof Synset) {
            return (true);
        }
        return (false);
    }

    public boolean containsEdge(Graph.Edge edge) {
        return (true);
    }

    protected Traverser traverser(Object node) {
        if (node instanceof String) {
            try {
                IndexWordSet words = dict.lookupAllIndexWords((String) node);
                if (words == null || words.size() == 0) return (GraphUtils.EMPTY_TRAVERSER);
                return (new WordTraverser((String) node, words));
            } catch (JWNLException e) {
                throw new RuntimeException("Error accessing WordNet DB", e);
            }
        } else if (node instanceof Synset) {
            return (new SynsetTraverser((Synset) node));
        }
        return (GraphUtils.EMPTY_TRAVERSER);
    }

    /**
     *  Goes from Strings to synsets that contain them.
     */
    private class WordTraverser extends CompositeIterator implements Traverser {

        private String startWord;

        private Graph.Edge currentEdge;

        public WordTraverser(String startWord, IndexWordSet words) {
            super(words.getIndexWordCollection().iterator());
        }

        public Object next() {
            Object o = super.next();
            currentEdge = new ImmutableIdentityEdge("SENSE", startWord, o, true);
            return (o);
        }

        public Graph.Edge getEdge() {
            return (currentEdge);
        }

        public void removeEdge() {
            throw new UnsupportedOperationException("WordNet edges cannot be removed.");
        }

        protected Iterator getIterator(Object item) {
            try {
                IndexWord word = (IndexWord) item;
                return (Arrays.asList(word.getSenses()).iterator());
            } catch (JWNLException e) {
                throw new RuntimeException("Error accessing WordNet DB", e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("WordNet elements cannot be removed.");
        }
    }

    private class SynsetTraverser implements Traverser {

        private Synset start;

        private Iterator it;

        private Graph.Edge currentEdge;

        public SynsetTraverser(Synset start) {
            this.start = start;
            this.it = Arrays.asList(start.getPointers()).iterator();
        }

        public boolean hasNext() {
            return (it.hasNext());
        }

        public void remove() {
            throw new UnsupportedOperationException("WordNet elements cannot be removed.");
        }

        public Object next() {
            try {
                Pointer p = (Pointer) it.next();
                Synset n = p.getTargetSynset();
                PointerType type = p.getType();
                if (UNDIRECTED.contains(type)) {
                    if (start.getOffset() < n.getOffset()) {
                        currentEdge = new ImmutableIdentityEdge(type, start, n, false);
                    } else if (start.getOffset() > n.getOffset()) {
                        currentEdge = new ImmutableIdentityEdge(type, n, start, false);
                    } else {
                        if (start.getLexFileId() <= n.getLexFileId()) currentEdge = new ImmutableIdentityEdge(type, start, n, false); else currentEdge = new ImmutableIdentityEdge(type, n, start, false);
                    }
                } else if (FORWARD.contains(type)) {
                    currentEdge = new ImmutableIdentityEdge(type, start, n, true);
                } else if (REVERSE.contains(type)) {
                    currentEdge = new ImmutableIdentityEdge(type, n, start, true);
                }
                return (n);
            } catch (JWNLException e) {
                throw new RuntimeException("Error access WordNet DB", e);
            }
        }

        public Graph.Edge getEdge() {
            return (currentEdge);
        }

        public void removeEdge() {
            throw new UnsupportedOperationException("WordNet edges cannot be removed.");
        }
    }
}
