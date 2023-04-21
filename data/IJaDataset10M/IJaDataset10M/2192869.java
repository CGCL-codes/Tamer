package abc.om.visit;

import java.util.Stack;
import abc.aspectj.ast.AspectBody;
import abc.aspectj.ast.AspectDecl;
import abc.aspectj.visit.PCNode;
import abc.aspectj.visit.PCStructure;
import abc.om.ExtensionInfo;
import abc.om.ast.ModuleDecl;
import abc.om.ast.OpenModNodeFactory;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.ContextVisitor;
import polyglot.visit.NodeVisitor;

/**
 * Checks if a class has been included in more than one module.
 * @author Neil Ongkingco
 */
public class CheckDuplicateClassInclude extends ContextVisitor {

    private ExtensionInfo ext;

    public CheckDuplicateClassInclude(Job job, TypeSystem ts, OpenModNodeFactory nf, ExtensionInfo ext) {
        super(job, ts, nf);
        this.ext = ext;
    }

    protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
        if (n instanceof ClassBody && !(n instanceof AspectBody)) {
            PCNode node = PCStructure.v().getClass(context().currentClass());
            assert (node != null) : "Node is null";
            if (ext.moduleStruct.hasMultipleOwners(node)) {
                throw new SemanticException("Class " + node.toString() + " included in more than one module.", n.position());
            }
        }
        return super.enterCall(parent, n);
    }
}
