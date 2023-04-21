package loci.formats.codec;

/**
 * An LZW-compression helper class for building a symbol table in tree format.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/loci/formats/codec/LZWTreeNode.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/loci/formats/codec/LZWTreeNode.java">SVN</a></dd></dl>
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 */
public class LZWTreeNode {

    /** List of up to 256 children. */
    protected LZWTreeNode[] children;

    /** Code corresponding to this node. */
    protected int theCode;

    /** Constructs a new LZW symbol tree node. */
    public LZWTreeNode(int code) {
        children = new LZWTreeNode[256];
        theCode = code;
    }

    /** Initializes this node as the root of the symbol table. */
    public void initialize() {
        for (int i = 0; i < 256; i++) children[i] = new LZWTreeNode(i);
    }

    /** Gets the code corresponding to this node. */
    public int getCode() {
        return theCode;
    }

    /** Gets this node's indexth child. */
    public LZWTreeNode getChild(byte index) {
        int ndx = index;
        if (ndx < 0) ndx += 256;
        return children[ndx];
    }

    /** Sets this node's indexth child to match the given node. */
    public void addChild(int index, LZWTreeNode node) {
        children[index] = node;
    }

    /** Gets the code for the given byte sequence, or -1 if none. */
    public int codeFromString(ByteVector string) {
        LZWTreeNode node = nodeFromString(string);
        return node == null ? -1 : node.theCode;
    }

    /** Gets the node for the given byte sequence, or null if none. */
    public LZWTreeNode nodeFromString(ByteVector string) {
        byte[] b = string.toByteArray();
        LZWTreeNode node = this;
        for (int i = 0; i < b.length; i++) {
            int q = (int) b[i];
            if (q < 0) q += 256;
            node = node.children[q];
            if (node == null) return null;
        }
        return node;
    }

    /** Adds the given code for the specified byte sequence. */
    public void addTableEntry(ByteVector string, int code) {
        byte[] b = string.toByteArray();
        LZWTreeNode node = this;
        for (int i = 0; i < b.length - 1; i++) {
            int q = b[i];
            if (q < 0) q += 256;
            node = node.children[q];
        }
        int q = b[b.length - 1];
        if (q < 0) q += 256;
        node.addChild(q, new LZWTreeNode(code));
    }
}
