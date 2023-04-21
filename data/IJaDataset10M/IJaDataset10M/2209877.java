package lu.fisch.unimozer.visitors;

import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import lu.fisch.unimozer.Package;

/**
 *
 * @author robertfisch
 */
public class ExtendsVisitor extends VoidVisitorAdapter {

    private String name = "";

    private boolean stop = true;

    public String getExtends() {
        return name;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        if (n.getExtends() != null) {
            stop = false;
            name = "";
            n.getExtends().get(0).accept(this, arg);
            stop = true;
        }
    }

    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            if (!stop) name += ".";
        }
        if (!stop) name += n.getName();
    }

    @Override
    public void visit(NameExpr n, Object arg) {
        if (!stop) name += n.getName();
    }

    @Override
    public void visit(QualifiedNameExpr n, Object arg) {
        if (!stop) {
            n.getQualifier().accept(this, arg);
            name += "." + n.getName();
        }
    }
}
