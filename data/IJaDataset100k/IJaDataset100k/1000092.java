package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.TokenReference;
import org.multijava.util.compiler.PositionedError;

/** 
 * A class representing JML example specifications. An example
 * specification is a redundant specification
 * (<code>redundant-spec</code>).  The production rule for example
 * specifications, <code>example</code> is defined as follows.
 *
 * <pre>
 *  example ::= [ [ privacy ] ] "example" ] [ spec-var-decls ]
 *      [ spec-header ] simple-spec-body
 *    | [ privacy ] "exceptional_example" [ spec-var-decls ]
 *      spec-header [ exceptional-example-body ]
 *    | [ privacy ] "exceptional_example" [ spec-var-decls ]
 *      exceptional-example-body
 *    | [ privacy ] "normal_example" [ spec-var-decls ]
 *      spec-header [ normal-example-body ]
 *    | [ privacy ] "normal_example" [ spec-var-decls ]
 *      normal-example-body
 * </pre>
 *
 * @author Yoonsik Cheon
 * @version $Revision: 1.9 $
 */
public class JmlExample extends JmlNode {

    /**
     * Constructs a new instance with given arguments. The created
     * instance is an AST for a heavy-weighted example specification.
     *
     * <pre><jml>
     * requires privacy == 0 ||
     *   privacy == Constants.ACC_PUBLIC ||
     *   privacy == Constants.ACC_PROTECTED ||
     *   privacy == Constants.ACC_PRIVATE;
     * </jml></pre>
     */
    public JmlExample(TokenReference where, long privacy, JmlSpecVarDecl[] specVarDecls, JmlRequiresClause[] specHeader, JmlSpecBodyClause[] specBody) {
        super(where);
        this.lightWeighted = false;
        this.privacy = privacy;
        this.specVarDecls = specVarDecls;
        this.specHeader = specHeader;
        this.specBody = specBody;
    }

    /**
     * Constructs a new instance with given arguments. The created
     * instance is an AST for a light-weighted example specification.
     */
    public JmlExample(TokenReference where, JmlSpecVarDecl[] specVarDecls, JmlRequiresClause[] specHeader, JmlSpecBodyClause[] specBody) {
        this(where, 0, specVarDecls, specHeader, specBody);
        this.lightWeighted = true;
    }

    /**
     * Returns true if this example specification is light-weighted.
     *
     * <pre><jml>
     * ensures \result == lightWeighted;
     * </jml></pre>
     */
    public boolean isLightWeighted() {
        return lightWeighted;
    }

    public long privacy() {
        return privacy;
    }

    public JmlSpecVarDecl[] specVarDecls() {
        return specVarDecls;
    }

    public JmlRequiresClause[] specHeader() {
        return specHeader;
    }

    /** Deprecated */
    public JmlSpecBodyClause[] specClauses() {
        return specBody;
    }

    public JmlSpecBodyClause[] specBody() {
        return specBody;
    }

    /**
     * Typechecks the <code>example</code> the context in which it
     * appears. Mutates the context to record the information learned
     * during checking.
     *
     * @param context the context in which this appears
     * @exception PositionedError if any checks fail 
     */
    public void typecheck(CFlowControlContextType context) throws PositionedError {
        long privacy = lightWeighted ? privacy(context) : this.privacy;
        CFlowControlContextType newCtx = context;
        if (specVarDecls != null) {
            newCtx = context.createFlowControlContext(getTokenReference());
            for (int i = 0; i < specVarDecls.length; i++) {
                specVarDecls[i].typecheck(newCtx, privacy);
            }
        }
        if (specHeader != null) {
            for (int i = 0; i < specHeader.length; i++) {
                if (specHeader[i] != null) {
                    specHeader[i].typecheck(newCtx, privacy);
                }
            }
        }
        if (specBody != null) {
            for (int i = 0; i < specBody.length; i++) {
                if (specBody[i] != null) {
                    specBody[i].typecheck(newCtx, privacy);
                }
            }
        }
        if (specVarDecls != null) {
            newCtx.checkingComplete();
        }
    }

    /** Returns the privacy of this node. By default, it returns that
     * of the method being checked. If the method has either
     * SPEC_PUBLIC or SPEC_PROTECTED modifier, then that modifier
     * takes precedence over the JAVA access modifiers. */
    protected long privacy(CFlowControlContextType context) {
        long modifiers = context.getCMethod().modifiers();
        long privacy = modifiers & (ACC_SPEC_PUBLIC | ACC_SPEC_PROTECTED);
        if (privacy == 0) {
            privacy = modifiers & (ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE);
        }
        return privacy;
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlExample(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    private long privacy;

    private JmlSpecVarDecl[] specVarDecls;

    private JmlRequiresClause[] specHeader;

    private JmlSpecBodyClause[] specBody;

    private boolean lightWeighted;
}
