package syntelos.fxp;

/**
 * <p> Like others in the genre, this "Fast XPath" processor accepts
 * <a href="http://www.w3.org/TR/xpath#path-abbrev">a subset of
 * "Abbreviated XPath"</a> in favor of parsing and evaluating very
 * quickly and with low overhead.  </p>
 * 
 * <h3>MT SAFETY</h3>
 * 
 * <p> An instance of Fast XPath is multi- thread safe.  <ul><li>Some
 * features of evaluation (position) are stateful within the call
 * stack.</li> <li>Construction performs parsing within its special
 * "atomic" scope -- a reference to the object is not available until
 * parsing is complete.</li> <li>The subject (discussed here) of the
 * MT-SAFETY of Fast XPath is independent from the MT-SAFETY of the
 * DOM.  While this class may have no multithreading issues, the state
 * of the DOM should not change while in use here.</li> </ul> </p>
 * 
 * <p> As in all DOM usage, multiple threads should be excluded from
 * making concurrent changes to a DOM.  </p>
 * 
 * <h3>Language</h3>
 * 
 * <p> {@link FastXPath} selects a single node, or a list of nodes,
 * using two different evaluation methods.  In selecting a single
 * node, the first matching node is returned.  In selecting a list of
 * nodes, all matching nodes are returned. </p>
 * 
 * <p> The top level selectors {@link Element element-name}, {@link
 * Anyname star}, {@link Dotnode dot}, and {@link AttributeOf
 * attribute-name} may be qualified by {@link AttributeIn
 * attribute-name-and-value} and {@link Position node-position}.  </p>
 * 
 * <p> Note that some of the possible constructs in this language will
 * not yield a result.  For example, <code>"@n[@n=v]"</code> is a
 * valid but illogical statement because an attribute has no
 * attributes.  </p>
 * 
 * <h3> Specification by object hierarchy </h3>
 * <p>
 * <pre>
 * {@link FastXPath} := { {@link Document}, {@link Top} } { '/' {@link Top} }*
 * {@link Top} := { {@link Element}, {@link Anyname}, {@link Dotnode},
 *               {@link AttributeOf} } { {@link Qualifier} }*
 * </pre>
 * 
 * For production operator <code>':='</code>, list of alternatives
 * <code>'{'</code> <i>...</i> <code>','</code> <i>...</i>
 * <code>'}'</code>, sequence of concatenation <code>'{'</code>
 * <i>...</i> <code>' '</code> <i>...</i> <code>'}'</code>, and the
 * set of zero or more terms <code>'{'</code> <i>...</i>
 * <code>'}*'</code>. </p>
 * 
 * <h3> Features by example </h3>
 * <p>
 *  <dl>
 * 
 *   <dt><code>/</code></dt> <dd> Select the document node.</dd>
 * 
 *   <dt><code>/a/b</code></dt> <dd> Absolute path selecting an
 *   element named 'b', child of the document element named 'a'.</dd>
 * 
 *   <dt><code>a/b</code></dt> <dd> Relative path from the context
 *   node to a child element named 'a', and selecting a child element
 *   of 'a' named 'b'.</dd>
 * 
 *   <dt><code>/a/*</code></dt> <dd> Star for any element name, as in
 *   this example to select all children of the document element named
 *   'a'.</dd>
 * 
 *   <dt><code>/a/.</code></dt> <dd> Dot for any node, as in this
 *   example to select the first child of the document element named
 *   'a'.  May be an element, text or cdata node. </dd>
 * 
 *   <dt><code>/a/b[1]</code></dt> <dd> The first element named 'b',
 *   child of document element named 'a'.  Position counting from one,
 *   not zero. </dd>
 * 
 *   <dt><code>/a/b[@n=v]</code></dt> <dd> Element path with
 *   differentiating attribute name 'n' and value 'v'.  Multiple
 *   '[',']' sets can be used in concatenated sequence, for example
 *   <code>"/a/b[@n1=v1][@n2=v2]"</code> or
 *   <code>"/a/b[@n=v][1]"</code>. </dd>
 * 
 *   <dt><code>/a/b/@n</code></dt> <dd> Select attribute named 'n'
 *   from an element named 'b', which is the child of the document
 *   element named 'a'. </dd>
 * 
 *  </dl>
 * </p>
 * 
 * 
 * @author jdp
 * @since 1.5
 */
public final class FastXPath extends java.lang.Object implements alto.sys.Alive, alto.sys.Destroy {

    public static final String TrimFragment(String fragment) {
        if (null == fragment || 1 > fragment.length()) return null; else if ('#' == fragment.charAt(0)) return TrimFragment(fragment.substring(1)); else return fragment;
    }

    private java.lang.String string;

    private Top node;

    private boolean maybeIdExpression;

    private int evalSingleState;

    public FastXPath() {
        super();
    }

    public FastXPath(java.lang.String string) {
        super();
        this.parse(string);
    }

    public boolean alive() {
        return (null != this.string && null != this.node);
    }

    public synchronized void destroy() {
        this.string = null;
        this.node = null;
    }

    /**
     * @param node The node value is a FastXPath expression.  
     * @exception alto.sys.Error$Argument A parsing error, the
     * exception message is the input string.
     */
    public void parse(org.w3c.dom.Attr node) throws alto.sys.Error.Argument {
        if (null != node) {
            this.parse(node.getNodeValue());
        } else {
            this.destroy();
        }
    }

    /**
     * @param node The node value is a FastXPath expression.  
     * @exception alto.sys.Error$Argument A parsing error, the
     * exception message is the input string.
     */
    public void parse(org.w3c.dom.CharacterData node) throws alto.sys.Error.Argument {
        if (null != node) {
            this.parse(node.getNodeValue());
        } else {
            this.destroy();
        }
    }

    /**
     * @param string A {@link FastXPath} expression (will be trimmed
     * here).  A parse failure produces an error argument exception.
     * @exception alto.sys.Error$Argument A parsing error, the
     * exception message is the input string.
     */
    public synchronized void parse(java.lang.String string) throws alto.sys.Error.Argument {
        string = TrimFragment(string);
        if (null != string) {
            this.string = string.trim();
            char[] expr = this.string.toCharArray();
            Top node = null;
            try {
                node = new Document(expr);
            } catch (Jump jump) {
                try {
                    node = Node.Expression(expr);
                } catch (Jump jump2) {
                    throw new alto.sys.Error.Argument(string);
                }
            }
            this.node = node;
            this.maybeIdExpression = ((node instanceof Element) && 1 == node.getDepth());
        } else {
            this.destroy();
        }
    }

    /**
     * 
     */
    public boolean isAbsolute() {
        return (this.node instanceof Document);
    }

    public boolean isRelative() {
        return (!this.isAbsolute());
    }

    public org.w3c.dom.Node evalSingle(org.w3c.dom.Node cnode) {
        return this.evalSingle(null, cnode);
    }

    public org.w3c.dom.Node evalSingle(alto.sys.Xml cx, org.w3c.dom.Node cnode) {
        switch(this.evalSingleState) {
            case 0:
                org.w3c.dom.Node re = this.node.evalSingle(cx, cnode);
                if (null == re && this.maybeIdExpression && cnode instanceof org.w3c.dom.Document) {
                    re = ((org.w3c.dom.Document) cnode).getElementById(this.string);
                    if (null != re) this.evalSingleState = 2;
                } else {
                    this.evalSingleState = 1;
                }
                return re;
            case 1:
                return this.node.evalSingle(cx, cnode);
            case 2:
                return ((org.w3c.dom.Document) cnode).getElementById(this.string);
            default:
                throw new IllegalStateException();
        }
    }

    public org.w3c.dom.NodeList evalMultiple(org.w3c.dom.Node cnode) {
        NodeList list = new NodeList(this);
        return this.node.evalMultiple(null, cnode, list);
    }

    public org.w3c.dom.NodeList evalMultiple(alto.sys.Xml cx, org.w3c.dom.Node cnode) {
        NodeList list = new NodeList(this);
        return this.node.evalMultiple(cx, cnode, list);
    }

    public java.lang.String toString() {
        java.lang.String string = this.string;
        if (null == string) return ""; else return string;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(java.lang.Object ano) {
        if (this == ano) return true; else if (null == ano) return false; else if (ano instanceof String) return this.toString().equals(ano); else return this.toString().equals(ano.toString());
    }

    public void write(java.io.OutputStream target) throws java.io.IOException {
        Top node = this.node;
        if (null != node) {
            alto.io.u.Ios out = new alto.io.u.Ios(target);
            try {
                this.node.write(out);
            } finally {
                out.flush();
            }
        }
    }
}
