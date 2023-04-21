package prop4j.parser;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.Type;
import java.util.ArrayList;
import java.util.List;
import static prop4j.Util.empty;

/**
 *
 * @author mike
 */
public class GetterNode extends AccessorNode {

    protected GetterNode(MethodDeclaration method) {
        super(method);
    }

    protected GetterNode(String propertyName, String fieldName, Type propertyType) {
        super(propertyName, fieldName, propertyType);
    }

    @Override
    protected MethodDeclaration buildMethodDeclaration(String propertyName, String fieldName, Type propertyType) {
        if (fieldName == null) throw new IllegalArgumentException();
        if (propertyType == null) throw new IllegalArgumentException();
        if (empty(propertyName)) propertyName = fieldName;
        String methodName = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        NameExpr ne = new NameExpr(1, 1, fieldName);
        ReturnStmt rs = new ReturnStmt(1, 1, ne);
        List<Statement> stat = new ArrayList<Statement>();
        stat.add(rs);
        BlockStmt block = new BlockStmt(1, 1, stat);
        return new MethodDeclaration(1, 1, ModifierSet.PUBLIC, null, null, propertyType, methodName, null, 0, null, block);
    }
}
