package deesel.dsl.syntax;

import deesel.dsl.syntax.parser.*;
import deesel.util.Convert;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Author: <a href="mailto:troy@gprogramming.org">Troy Heninger</a>
 *
 * @version $ Revision: $
 */
public class Atom extends Element {

    public static final int IGNORED = 1;

    private Collection literals;

    private boolean inclusion;

    public Atom() {
        this(null);
    }

    public Atom(String name) {
        super(name);
        inclusion = true;
    }

    public Atom(String name, char arg) {
        this(name, Convert.toObject(arg), true);
    }

    public Atom(String name, char arg, boolean inclusion) {
        this(name, Convert.toObject(arg), inclusion);
    }

    public Atom(String name, double arg) {
        this(name, Convert.toObject(arg), true);
    }

    public Atom(String name, double arg, boolean inclusion) {
        this(name, Convert.toObject(arg), inclusion);
    }

    public Atom(String name, float arg) {
        this(name, Convert.toObject(arg), true);
    }

    public Atom(String name, float arg, boolean inclusion) {
        this(name, Convert.toObject(arg), inclusion);
    }

    public Atom(String name, int arg) {
        this(name, Convert.toObject(arg), true);
    }

    public Atom(String name, int arg, boolean inclusion) {
        this(name, Convert.toObject(arg), inclusion);
    }

    public Atom(String name, long arg) {
        this(name, Convert.toObject(arg), true);
    }

    public Atom(String name, long arg, boolean inclusion) {
        this(name, Convert.toObject(arg), inclusion);
    }

    public Atom(String name, Collection arg) {
        this(name, arg, true);
    }

    public Atom(String name, Collection arg, boolean inclusion) {
        super(name);
        this.literals = arg;
        this.inclusion = inclusion;
    }

    public Atom(String name, Object arg) {
        this(name, arg, true);
    }

    public Atom(String name, Object arg, boolean inclusion) {
        super(name);
        addLiteral(arg);
        this.inclusion = inclusion;
    }

    public Atom(String name, Object[] arg) {
        this(name, arg, true);
    }

    public Atom(String name, Object[] arg, boolean inclusion) {
        this(name, Arrays.asList(arg), inclusion);
    }

    public boolean isInclusion() {
        return inclusion;
    }

    public void setInclusion(boolean inclusion) {
        this.inclusion = inclusion;
    }

    public Object[] getLiterals() {
        return literals == null ? null : literals.toArray();
    }

    public void addLiteral(Object value) {
        if (literals == null) literals = new ArrayList();
        literals.add(value);
    }

    public void collectAll(Set all) {
    }

    public Element compress(Parser parser) {
        if ((literals == null || literals.size() == 0) && !parser.getTokenizer().isToken(getName())) {
            return new Reference(parser, getName());
        }
        return this;
    }

    private void debug(INode node) {
        while (node.getParent() != null) {
            node = node.getParent();
        }
        System.out.println("Matching " + node.getType());
    }

    public boolean match(Tokenizer tokenizer, INode parent) {
        tokenizer.mark();
        if (tokenizer.isDebug()) debug(parent);
        Token token = tokenizer.nextToken();
        if (token == null) return false;
        boolean matched = getName().equals(token.getType());
        if (matched) {
            matched = matchArguments(token);
            if (!inclusion) matched = !matched;
        }
        if (matched) {
            tokenizer.consume();
            parent.addChild(token);
            return true;
        }
        tokenizer.backTrack();
        return false;
    }

    private boolean matchArguments(Token token) {
        boolean matchedOne = true;
        if (literals != null) {
            matchedOne = false;
            Iterator it = literals.iterator();
            while (it.hasNext()) {
                Object condition = it.next();
                if (condition instanceof Pattern) {
                    matchedOne = ((Pattern) condition).matcher(token.getText()).matches();
                } else if (condition instanceof Character) {
                    String text = token.getText();
                    matchedOne = text.length() == 1 && ((Character) condition).charValue() == text.charAt(0);
                } else {
                    matchedOne = condition.equals(token.getValue());
                }
                if (matchedOne) break;
            }
        }
        return matchedOne;
    }

    public String toString() {
        return String.valueOf(literals);
    }

    private String unesc(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        try {
            for (int ix = 0, ixz = str.length(); ix < ixz; ix++) {
                char c = str.charAt(ix);
                if (c == '\\') {
                    c = str.charAt(++ix);
                    switch(c) {
                        case '\\':
                            c = '\\';
                            break;
                        case 'n':
                            c = '\n';
                            break;
                        case 'r':
                            c = '\r';
                            break;
                        case 't':
                            c = '\t';
                            break;
                    }
                }
                buf.append(c);
            }
        } catch (Exception e) {
        }
        return buf.toString();
    }

    public void validate() {
    }
}
