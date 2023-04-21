package org.jmlspecs.jml4.ast;

import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

public class JmlStoreRefInformalDescription extends JmlStoreRef {

    private final JmlInformalExpression desc;

    public JmlStoreRefInformalDescription(JmlInformalExpression desc) {
        this.desc = desc;
    }

    public void analyseCode(BlockScope scope, FlowContext methodContext, FlowInfo flowInfo) {
    }

    public void resolve(BlockScope scope) {
    }

    public StringBuffer print(int indent, StringBuffer output) {
        return this.desc.print(indent, output);
    }
}
