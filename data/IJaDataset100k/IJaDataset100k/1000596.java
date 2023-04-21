package org.enerj.query.oql.ast;

import org.odmg.QueryException;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.query.oql.EvaluatorContext;

/**
 * The Import AST. <p/>
 * NOTE: We allow wildcards at the end of the import, e.g. "import java.util.*". <p/>
 * 
 * @version $Id: ImportAST.java,v 1.5 2006/02/21 02:37:47 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad </a>
 */
public class ImportAST extends BaseAST {

    /** LinkedList of QueryAST and/or DeclarationAST. */
    private QualifiedNameAST mQualifiedName;

    private String mAlias;

    /**
     * Construct a new ImportAST. 
     */
    public ImportAST(QualifiedNameAST aQualifiedName, String anAlias) {
        mQualifiedName = aQualifiedName;
        mAlias = anAlias;
    }

    /**
     * Gets the qualified name of this import.
     * 
     * @return a QualifiedNameAST.
     */
    public QualifiedNameAST getQualifiedNameAST() {
        return mQualifiedName;
    }

    /**
     * Gets the import alias, if defined.
     * 
     * @return the import alias name, or null if not defined.
     */
    public String getAlias() {
        return mAlias;
    }

    protected Class getType0() throws QueryException {
        return null;
    }

    protected UnaryFunctor resolve0() throws QueryException {
        EvaluatorContext.getContext().addImport(mQualifiedName.getQualifiedName(), mAlias);
        return null;
    }
}
