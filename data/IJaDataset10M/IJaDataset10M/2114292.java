package org.antlr.runtime3_2_0.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime3_2_0.Token;
import org.antlr.runtime3_2_0.TokenStream;
import org.antlr.runtime3_2_0.misc.IntArray;

/** A buffered stream of tree nodes.  Nodes can be from a tree of ANY kind.
 *
 *  This node stream sucks all nodes out of the tree specified in
 *  the constructor during construction and makes pointers into
 *  the tree using an array of Object pointers. The stream necessarily
 *  includes pointers to DOWN and UP and EOF nodes.
 *
 *  This stream knows how to mark/release for backtracking.
 *
 *  This stream is most suitable for tree interpreters that need to
 *  jump around a lot or for tree parsers requiring speed (at cost of memory).
 *  There is some duplicated functionality here with UnBufferedTreeNodeStream
 *  but just in bookkeeping, not tree walking etc...
 *
 *  TARGET DEVELOPERS:
 *
 *  This is the old CommonTreeNodeStream that buffered up entire node stream.
 *  No need to implement really as new CommonTreeNodeStream is much better
 *  and covers what we need.
 *
 *  @see CommonTreeNodeStream
 */
public class BufferedTreeNodeStream implements TreeNodeStream {

    public static final int DEFAULT_INITIAL_BUFFER_SIZE = 100;

    public static final int INITIAL_CALL_STACK_SIZE = 10;

    protected class StreamIterator implements Iterator {

        int i = 0;

        public boolean hasNext() {
            return i < nodes.size();
        }

        public Object next() {
            int current = i;
            i++;
            if (current < nodes.size()) {
                return nodes.get(current);
            }
            return eof;
        }

        public void remove() {
            throw new RuntimeException("cannot remove nodes from stream");
        }
    }

    protected Object down;

    protected Object up;

    protected Object eof;

    /** The complete mapping from stream index to tree node.
	 *  This buffer includes pointers to DOWN, UP, and EOF nodes.
	 *  It is built upon ctor invocation.  The elements are type
	 *  Object as we don't what the trees look like.
	 *
	 *  Load upon first need of the buffer so we can set token types
	 *  of interest for reverseIndexing.  Slows us down a wee bit to
	 *  do all of the if p==-1 testing everywhere though.
	 */
    protected List nodes;

    /** Pull nodes from which tree? */
    protected Object root;

    /** IF this tree (root) was created from a token stream, track it. */
    protected TokenStream tokens;

    /** What tree adaptor was used to build these trees */
    TreeAdaptor adaptor;

    /** Reuse same DOWN, UP navigation nodes unless this is true */
    protected boolean uniqueNavigationNodes = false;

    /** The index into the nodes list of the current node (next node
	 *  to consume).  If -1, nodes array not filled yet.
	 */
    protected int p = -1;

    /** Track the last mark() call result value for use in rewind(). */
    protected int lastMarker;

    /** Stack of indexes used for push/pop calls */
    protected IntArray calls;

    public BufferedTreeNodeStream(Object tree) {
        this(new CommonTreeAdaptor(), tree);
    }

    public BufferedTreeNodeStream(TreeAdaptor adaptor, Object tree) {
        this(adaptor, tree, DEFAULT_INITIAL_BUFFER_SIZE);
    }

    public BufferedTreeNodeStream(TreeAdaptor adaptor, Object tree, int initialBufferSize) {
        this.root = tree;
        this.adaptor = adaptor;
        nodes = new ArrayList(initialBufferSize);
        down = adaptor.create(Token.DOWN, "DOWN");
        up = adaptor.create(Token.UP, "UP");
        eof = adaptor.create(Token.EOF, "EOF");
    }

    /** Walk tree with depth-first-search and fill nodes buffer.
	 *  Don't do DOWN, UP nodes if its a list (t is isNil).
	 */
    protected void fillBuffer() {
        fillBuffer(root);
        p = 0;
    }

    public void fillBuffer(Object t) {
        boolean nil = adaptor.isNil(t);
        if (!nil) {
            nodes.add(t);
        }
        int n = adaptor.getChildCount(t);
        if (!nil && n > 0) {
            addNavigationNode(Token.DOWN);
        }
        for (int c = 0; c < n; c++) {
            Object child = adaptor.getChild(t, c);
            fillBuffer(child);
        }
        if (!nil && n > 0) {
            addNavigationNode(Token.UP);
        }
    }

    /** What is the stream index for node? 0..n-1
	 *  Return -1 if node not found.
	 */
    protected int getNodeIndex(Object node) {
        if (p == -1) {
            fillBuffer();
        }
        for (int i = 0; i < nodes.size(); i++) {
            Object t = (Object) nodes.get(i);
            if (t == node) {
                return i;
            }
        }
        return -1;
    }

    /** As we flatten the tree, we use UP, DOWN nodes to represent
	 *  the tree structure.  When debugging we need unique nodes
	 *  so instantiate new ones when uniqueNavigationNodes is true.
	 */
    protected void addNavigationNode(final int ttype) {
        Object navNode = null;
        if (ttype == Token.DOWN) {
            if (hasUniqueNavigationNodes()) {
                navNode = adaptor.create(Token.DOWN, "DOWN");
            } else {
                navNode = down;
            }
        } else {
            if (hasUniqueNavigationNodes()) {
                navNode = adaptor.create(Token.UP, "UP");
            } else {
                navNode = up;
            }
        }
        nodes.add(navNode);
    }

    public Object get(int i) {
        if (p == -1) {
            fillBuffer();
        }
        return nodes.get(i);
    }

    public Object LT(int k) {
        if (p == -1) {
            fillBuffer();
        }
        if (k == 0) {
            return null;
        }
        if (k < 0) {
            return LB(-k);
        }
        if ((p + k - 1) >= nodes.size()) {
            return eof;
        }
        return nodes.get(p + k - 1);
    }

    public Object getCurrentSymbol() {
        return LT(1);
    }

    /** Look backwards k nodes */
    protected Object LB(int k) {
        if (k == 0) {
            return null;
        }
        if ((p - k) < 0) {
            return null;
        }
        return nodes.get(p - k);
    }

    public Object getTreeSource() {
        return root;
    }

    public String getSourceName() {
        return getTokenStream().getSourceName();
    }

    public TokenStream getTokenStream() {
        return tokens;
    }

    public void setTokenStream(TokenStream tokens) {
        this.tokens = tokens;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public boolean hasUniqueNavigationNodes() {
        return uniqueNavigationNodes;
    }

    public void setUniqueNavigationNodes(boolean uniqueNavigationNodes) {
        this.uniqueNavigationNodes = uniqueNavigationNodes;
    }

    public void consume() {
        if (p == -1) {
            fillBuffer();
        }
        p++;
    }

    public int LA(int i) {
        return adaptor.getType(LT(i));
    }

    public int mark() {
        if (p == -1) {
            fillBuffer();
        }
        lastMarker = index();
        return lastMarker;
    }

    public void release(int marker) {
    }

    public int index() {
        return p;
    }

    public void rewind(int marker) {
        seek(marker);
    }

    public void rewind() {
        seek(lastMarker);
    }

    public void seek(int index) {
        if (p == -1) {
            fillBuffer();
        }
        p = index;
    }

    /** Make stream jump to a new location, saving old location.
	 *  Switch back with pop().
	 */
    public void push(int index) {
        if (calls == null) {
            calls = new IntArray();
        }
        calls.push(p);
        seek(index);
    }

    /** Seek back to previous index saved during last push() call.
	 *  Return top of stack (return index).
	 */
    public int pop() {
        int ret = calls.pop();
        seek(ret);
        return ret;
    }

    public void reset() {
        p = 0;
        lastMarker = 0;
        if (calls != null) {
            calls.clear();
        }
    }

    public int size() {
        if (p == -1) {
            fillBuffer();
        }
        return nodes.size();
    }

    public Iterator iterator() {
        if (p == -1) {
            fillBuffer();
        }
        return new StreamIterator();
    }

    public void replaceChildren(Object parent, int startChildIndex, int stopChildIndex, Object t) {
        if (parent != null) {
            adaptor.replaceChildren(parent, startChildIndex, stopChildIndex, t);
        }
    }

    /** Used for testing, just return the token type stream */
    public String toTokenTypeString() {
        if (p == -1) {
            fillBuffer();
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < nodes.size(); i++) {
            Object t = (Object) nodes.get(i);
            buf.append(" ");
            buf.append(adaptor.getType(t));
        }
        return buf.toString();
    }

    /** Debugging */
    public String toTokenString(int start, int stop) {
        if (p == -1) {
            fillBuffer();
        }
        StringBuffer buf = new StringBuffer();
        for (int i = start; i < nodes.size() && i <= stop; i++) {
            Object t = (Object) nodes.get(i);
            buf.append(" ");
            buf.append(adaptor.getToken(t));
        }
        return buf.toString();
    }

    public String toString(Object start, Object stop) {
        System.out.println("toString");
        if (start == null || stop == null) {
            return null;
        }
        if (p == -1) {
            fillBuffer();
        }
        if (start instanceof CommonTree) System.out.print("toString: " + ((CommonTree) start).getToken() + ", "); else System.out.println(start);
        if (stop instanceof CommonTree) System.out.println(((CommonTree) stop).getToken()); else System.out.println(stop);
        if (tokens != null) {
            int beginTokenIndex = adaptor.getTokenStartIndex(start);
            int endTokenIndex = adaptor.getTokenStopIndex(stop);
            if (adaptor.getType(stop) == Token.UP) {
                endTokenIndex = adaptor.getTokenStopIndex(start);
            } else if (adaptor.getType(stop) == Token.EOF) {
                endTokenIndex = size() - 2;
            }
            return tokens.toString(beginTokenIndex, endTokenIndex);
        }
        Object t = null;
        int i = 0;
        for (; i < nodes.size(); i++) {
            t = nodes.get(i);
            if (t == start) {
                break;
            }
        }
        StringBuffer buf = new StringBuffer();
        t = nodes.get(i);
        while (t != stop) {
            String text = adaptor.getText(t);
            if (text == null) {
                text = " " + String.valueOf(adaptor.getType(t));
            }
            buf.append(text);
            i++;
            t = nodes.get(i);
        }
        String text = adaptor.getText(stop);
        if (text == null) {
            text = " " + String.valueOf(adaptor.getType(stop));
        }
        buf.append(text);
        return buf.toString();
    }
}
