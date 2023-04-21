package ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldDeclarationCollector extends ASTVisitor {

    List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

    @Override
    public boolean visit(FieldDeclaration node) {
        fields.add(node);
        return false;
    }

    public List<FieldDeclaration> getFields() {
        return fields;
    }
}
