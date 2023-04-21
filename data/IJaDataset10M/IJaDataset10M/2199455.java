package relaxngcc.grammar;

import relaxngcc.parser.ParserRuntime;

/**
 * Represents &lt;cc:java> block.
 * This class derives Pattern so that it can be mixed with
 * other patterns. However, only GroupPattern is allowed to
 * have this Pattern as its child.
 *
 * @author
 *      Kohsuke Kawaguchi (kk@kohsuke.org)
 */
public class JavaBlock extends Pattern {

    public JavaBlock(ParserRuntime rt, String code_) {
        this.code = code_;
    }

    public Object apply(PatternFunction f) {
        return f.javaBlock(this);
    }

    /** code fragment. */
    public final String code;

    public boolean isPattern() {
        return false;
    }

    public Pattern asPattern() {
        return null;
    }

    public boolean isJavaBlock() {
        return true;
    }

    public JavaBlock asJavaBlock() {
        return this;
    }
}
