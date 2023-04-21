package org.gjt.sp.jedit.bsh;

class SimpleNode implements Node {

    public static SimpleNode JAVACODE = new SimpleNode(-1) {

        public String getSourceFile() {
            return "<Called from Java Code>";
        }

        public int getLineNumber() {
            return -1;
        }

        public String getText() {
            return "<Compiled Java Code>";
        }
    };

    protected Node parent;

    protected Node[] children;

    protected int id;

    Token firstToken, lastToken;

    /** the source of the text from which this was parsed */
    String sourceFile;

    public SimpleNode(int i) {
        id = i;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        parent = n;
    }

    public Node jjtGetParent() {
        return parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (children == null) children = new Node[i + 1]; else if (i >= children.length) {
            Node c[] = new Node[i + 1];
            System.arraycopy(children, 0, c, 0, children.length);
            children = c;
        }
        children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return children[i];
    }

    public SimpleNode getChild(int i) {
        return (SimpleNode) jjtGetChild(i);
    }

    public int jjtGetNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    public String toString() {
        return ParserTreeConstants.jjtNodeName[id];
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    /**
		Detach this node from its parent.
		This is primarily useful in node serialization.
		(see BSHMethodDeclaration)
	*/
    public void prune() {
        jjtSetParent(null);
    }

    /**
		This is the general signature for evaluation of a node.
	*/
    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        throw new InterpreterError("Unimplemented or inappropriate for " + getClass().getName());
    }

    /**
		Set the name of the source file (or more generally source) of
		the text from which this node was parsed.
	*/
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
		Get the name of the source file (or more generally source) of
		the text from which this node was parsed.
		This will recursively search up the chain of parent nodes until
		a source is found or return a string indicating that the source
		is unknown.
	*/
    public String getSourceFile() {
        if (sourceFile == null) if (parent != null) return ((SimpleNode) parent).getSourceFile(); else return "<unknown file>"; else return sourceFile;
    }

    /**
		Get the line number of the starting token
	*/
    public int getLineNumber() {
        return firstToken.beginLine;
    }

    /**
		Get the text of the tokens comprising this node.
	*/
    public String getText() {
        StringBuffer text = new StringBuffer();
        Token t = firstToken;
        while (t != null) {
            text.append(t.image);
            if (!t.image.equals(".")) text.append(" ");
            if (t == lastToken || t.image.equals("{") || t.image.equals(";")) break;
            t = t.next;
        }
        return text.toString();
    }
}
