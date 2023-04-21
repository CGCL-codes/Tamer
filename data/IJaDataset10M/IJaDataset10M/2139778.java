package org.jmlspecs.jml6.boogie.translator.node;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.jmlspecs.jml6.boogie.ast.BoogieBlock;
import org.jmlspecs.jml6.boogie.translator.BoogieMissingDelegate;
import org.jmlspecs.jml6.boogie.translator.IBoogieInlineNodeTranslator;
import org.jmlspecs.jml6.boogie.translator.IBoogieRootTranslator;

public class BoogieSynchronizedStatementTranslator extends BoogieJavaNodeTranslator implements IBoogieInlineNodeTranslator<SynchronizedStatement, BoogieBlock> {

    public BoogieSynchronizedStatementTranslator(IBoogieRootTranslator root) {
        super(root, ASTNode.SYNCHRONIZED_STATEMENT);
    }

    @Override
    public void translate(SynchronizedStatement statement, BoogieBlock block) throws BoogieMissingDelegate {
    }
}
