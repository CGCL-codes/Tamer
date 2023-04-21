package japatest;

/**
 * @author saizhang@google.com (Your Name Here)
 *
 */
public class InvokeRandoop {

    public static void main(String[] args) {
        args = new String[] { "gentests", "--testclass=japa.parser.ast.type.ReferenceType", "--testclass=japa.parser.ast.stmt.ReturnStmt", "--testclass=japa.parser.ast.body.ClassOrInterfaceDeclaration", "--testclass=japa.parser.ast.CompilationUnit", "--testclass=japa.parser.JavaCharStream", "--testclass=japa.parser.ast.body.VariableDeclaratorId", "--testclass=japa.parser.ast.stmt.ExpressionStmt", "--testclass=japa.parser.ast.body.Parameter", "--testclass=japa.parser.ast.expr.VariableDeclarationExpr", "--testclass=japa.parser.ast.type.Type", "--testclass=japa.parser.Token", "--testclass=japa.parser.ast.body.TypeDeclaration", "--testclass=japa.parser.ast.type.ClassOrInterfaceType", "--testclass=japa.parser.ast.PackageDeclaration", "--testclass=japa.parser.ast.body.VariableDeclarator", "--testclass=japa.parser.ast.ImportDeclaration", "--testclass=japa.parser.ast.expr.Expression", "--testclass=japa.parser.ast.body.MethodDeclaration", "--testclass=japa.parser.ast.type.PrimitiveType", "--testclass=japa.parser.ast.stmt.Statement", "--testclass=japa.parser.ast.stmt.TryStmt", "--testclass=japa.parser.ast.body.BodyDeclaration", "--testclass=japa.parser.ast.expr.NameExpr", "--testclass=japa.parser.ast.LineComment", "--testclass=japa.parser.ASTParser", "--testclass=japa.parser.ast.stmt.BlockStmt", "--junit-output-dir=./tests", "--junit-package-name=tests", "--timelimit=60" };
        randoop.main.Main.main(args);
    }
}
