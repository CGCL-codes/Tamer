package org.jmlspecs.jml6.boogie.translator.node;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.jmlspecs.jml6.boogie.ast.BoogieBlock;
import org.jmlspecs.jml6.boogie.translator.BoogieMissingDelegate;
import org.jmlspecs.jml6.boogie.translator.IBoogieInlineNodeTranslator;
import org.jmlspecs.jml6.boogie.translator.IBoogieRootTranslator;

public class BoogieSwitchCaseTranslator extends BoogieJavaNodeTranslator implements IBoogieInlineNodeTranslator<SwitchCase, BoogieBlock> {

    public BoogieSwitchCaseTranslator(IBoogieRootTranslator root) {
        super(root, ASTNode.SWITCH_CASE);
    }

    @Override
    public void translate(SwitchCase statement, BoogieBlock block) throws BoogieMissingDelegate {
    }
}
