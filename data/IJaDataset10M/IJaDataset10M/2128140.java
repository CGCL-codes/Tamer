package org.eclipse.jdt.internal.codeassist.complete;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class CompletionOnExplicitConstructorCall extends ExplicitConstructorCall {

    public CompletionOnExplicitConstructorCall(int accessMode) {
        super(accessMode);
    }

    public StringBuffer printStatement(int tab, StringBuffer output) {
        printIndent(tab, output);
        output.append("<CompleteOnExplicitConstructorCall:");
        if (this.qualification != null) this.qualification.printExpression(0, output).append('.');
        if (this.accessMode == This) {
            output.append("this(");
        } else {
            output.append("super(");
        }
        if (this.arguments != null) {
            for (int i = 0; i < this.arguments.length; i++) {
                if (i > 0) output.append(", ");
                this.arguments[i].printExpression(0, output);
            }
        }
        return output.append(")>;");
    }

    public void resolve(BlockScope scope) {
        ReferenceBinding receiverType = scope.enclosingSourceType();
        if (this.arguments != null) {
            int argsLength = this.arguments.length;
            for (int a = argsLength; --a >= 0; ) this.arguments[a].resolveType(scope);
        }
        if (this.accessMode != This && receiverType != null) {
            if (receiverType.isHierarchyInconsistent()) throw new CompletionNodeFound();
            receiverType = receiverType.superclass();
        }
        if (receiverType == null) throw new CompletionNodeFound(); else throw new CompletionNodeFound(this, receiverType, scope);
    }
}
