package org.owasp.orizon.mirage.c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.owasp.orizon.core.Cons;
import org.owasp.orizon.mirage.Collector;
import org.owasp.orizon.mirage.Call;
import org.owasp.orizon.mirage.c.parser.CLexer;
import org.owasp.orizon.mirage.c.parser.CParser;
import org.owasp.orizon.mirage.c.parser.CompoundStatement;
import org.owasp.orizon.mirage.c.parser.DirectDeclarator;
import org.owasp.orizon.mirage.c.parser.ExpressionStatement;
import org.owasp.orizon.mirage.c.parser.IDENTIFIER;
import org.owasp.orizon.mirage.c.parser.JavaCharStream;
import org.owasp.orizon.mirage.c.parser.Node;
import org.owasp.orizon.mirage.c.parser.Nodes;
import org.owasp.orizon.mirage.c.parser.PostfixExpression;
import org.owasp.orizon.mirage.c.parser.StatementList;
import org.owasp.orizon.report.Reportable;

/**
 * @author thesp0nge
 *
 */
public class CCollector extends Collector {

    private CParser parser;

    private CLexer lexer;

    private Node root;

    private List<PostfixExpression> post_exp_raw_list = null;

    private List<IDENTIFIER> idents_raw = null;

    public CCollector(String name) {
        this(new File(name));
    }

    public CCollector(File f) {
        super();
        this.filename = f.getAbsolutePath();
        if (f.exists() && f.isFile()) {
            try {
                lexer = new CLexer(new JavaCharStream(new InputStreamReader(new FileInputStream(f))));
                parser = new CParser(lexer);
                status &= Cons.O_MS_READY;
            } catch (FileNotFoundException e) {
                status = Cons.O_MS_ERROR;
            }
        } else status = Cons.O_MS_ERROR;
        calls = new Vector<Call>();
    }

    @Override
    public boolean crawl(Vector<String> s) {
        return false;
    }

    @Override
    public boolean dump(String what) {
        return false;
    }

    @Override
    public Vector<String> getFileIncluded() {
        return null;
    }

    @Override
    public int getFileIncludedCount() {
        return 0;
    }

    @Override
    public int getIdentifiersCount() {
        return calls.size();
    }

    @Override
    public int getVariablesCount() {
        return 0;
    }

    private void extractIdentifiers(Node c) {
        if (post_exp_raw_list == null) post_exp_raw_list = Nodes.childrenOfType(c, PostfixExpression.class); else post_exp_raw_list.addAll(Nodes.childrenOfType(c, PostfixExpression.class));
        for (PostfixExpression p : post_exp_raw_list) {
            trace(" PostfixExpression --> " + p.toString() + " " + p.getChildCount());
            if (idents_raw == null) idents_raw = Nodes.childrenOfType(p, IDENTIFIER.class); else idents_raw.addAll(Nodes.childrenOfType(p, IDENTIFIER.class));
        }
        return;
    }

    @Override
    public boolean inspect() {
        boolean ret = true;
        if ((status &= Cons.O_MS_PARSED) != Cons.O_MS_PARSED) {
            trace(getName() + ": inspect() has not been parsed yet. Call parse() first");
            return false;
        }
        for (Iterator<Node> it = Nodes.iterator(root); it.hasNext(); ) {
            Node child = it.next();
            if (child instanceof DirectDeclarator) {
                Iterator<Node> foo = Nodes.iterator(child);
                for (; foo.hasNext(); ) {
                    Node c = foo.next();
                    if (c instanceof IDENTIFIER) trace(" IDENTIFIER " + c.toString()); else trace(" DirectDeclarator --> " + c.getClass().getName());
                }
            }
            if (child instanceof CompoundStatement) {
                Iterator<Node> f00 = Nodes.iterator(child);
                for (; f00.hasNext(); ) {
                    Node c = f00.next();
                    if (c instanceof StatementList) {
                        for (int i = 0; i < c.getChildCount(); i++) {
                            extractIdentifiers(c.getChild(i));
                        }
                    }
                    if (c instanceof ExpressionStatement) {
                        extractIdentifiers(c);
                        Iterator<Node> f00c0d3 = Nodes.iterator(c);
                        for (; f00c0d3.hasNext(); ) {
                            Node c0 = f00c0d3.next();
                            trace(" ExpressionStatement -->  " + c0.getClass().getName());
                        }
                    } else trace(" CompundStatement --> " + c.getClass().getName());
                }
            } else trace("CCollector.inspect(): " + child.getClass().getName());
        }
        if (idents_raw != null) {
            for (IDENTIFIER i : idents_raw) {
                trace("found () " + i.toString());
                calls.add(new Call(i.getEndLine(), "", i.toString(), filename));
            }
        }
        status &= Cons.O_MS_SPIDER;
        return ret;
    }

    @Override
    public boolean parse() {
        boolean ret = false;
        if (!canParse()) {
            System.err.println("can't parse the file due to initialization issues");
            return false;
        }
        trace(getName() + ": parse() --- BEGIN ---");
        try {
            parser.TranslationUnit();
            root = parser.rootNode();
            trace(getName() + ": parse() --- END ---");
            status |= Cons.O_MS_PARSED;
            ret = true;
        } catch (Exception e) {
            trace(getName() + ": parse() a parser error occured: " + e.getMessage());
            setError(e.getMessage());
            status = Cons.O_MS_ERROR;
            ret = false;
        } catch (Error e) {
            trace(getName() + ": parse() a serious parser error occured: " + e.getMessage());
            setError(e.getMessage());
            status = Cons.O_MS_ERROR;
            ret = false;
        }
        return ret;
    }

    @Override
    public boolean show(String name) {
        return false;
    }

    @Override
    public boolean spider() {
        return false;
    }

    @Override
    public Vector<Reportable> getAuxFaults() {
        return null;
    }

    @Override
    public boolean hasAuxFaults() {
        return false;
    }
}
