package com.jjpeople.diff.text;

/**
 * Class "node". The symbol table routines in this class all
 * understand the symbol table format, which is a binary tree.
 * The methods are: addSymbol, symbolIsUnique, showSymbol.
 */
class node {

    node pleft, pright;

    int linenum;

    static final int freshnode = 0, oldonce = 1, newonce = 2, bothonce = 3, other = 4;

    int linestate;

    String line;

    static node panchor = null;

    /**
     * Construct a new symbol table node and fill in its fields.
     * @param        string  A line of the text file
     */
    node(String pline) {
        pleft = pright = null;
        linestate = freshnode;
        line = pline;
    }

    /**
     * matchsymbol       Searches tree for a match to the line.
     * @param  String  pline, a line of text
     * If node's linestate == freshnode, then created the node.
     */
    static node matchsymbol(String pline) {
        int comparison;
        node pnode = panchor;
        if (panchor == null) return panchor = new node(pline);
        for (; ; ) {
            comparison = pnode.line.compareTo(pline);
            if (comparison == 0) return pnode;
            if (comparison < 0) {
                if (pnode.pleft == null) {
                    pnode.pleft = new node(pline);
                    return pnode.pleft;
                }
                pnode = pnode.pleft;
            }
            if (comparison > 0) {
                if (pnode.pright == null) {
                    pnode.pright = new node(pline);
                    return pnode.pright;
                }
                pnode = pnode.pright;
            }
        }
    }

    /**
     * addSymbol(String pline) - Saves line into the symbol table.
     * Returns a handle to the symtab entry for that unique line.
     * If inoldfile nonzero, then linenum is remembered.
     */
    static node addSymbol(String pline, boolean inoldfile, int linenum) {
        node pnode;
        pnode = matchsymbol(pline);
        if (pnode.linestate == freshnode) {
            pnode.linestate = inoldfile ? oldonce : newonce;
        } else {
            if ((pnode.linestate == oldonce && !inoldfile) || (pnode.linestate == newonce && inoldfile)) pnode.linestate = bothonce; else pnode.linestate = other;
        }
        if (inoldfile) pnode.linenum = linenum;
        return pnode;
    }

    /**
     * symbolIsUnique    Arg is a ptr previously returned by addSymbol.
     * --------------    Returns true if the line was added to the
     *                   symbol table exactly once with inoldfile true,
     *                   and exactly once with inoldfile false.
     */
    boolean symbolIsUnique() {
        return (linestate == bothonce);
    }

    /**
     * showSymbol        Prints the line to stdout.
     */
    void showSymbol() {
        System.out.println(line);
    }
}
