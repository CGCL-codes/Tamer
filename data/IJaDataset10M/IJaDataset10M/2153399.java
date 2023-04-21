package Galaxy.Tree.Tool.Output;

import Galaxy.Visitor.DFSToolVisitor.ToolImplVisitor;
import Galaxy.Visitor.DFSWorkflowVisitor.StepVisitor;
import Galaxy.Visitor.util.LoniContext;

public class Output {

    public Object accept(ToolImplVisitor toolVisitor, LoniContext context) {
        return toolVisitor.visit(this, context);
    }
}
