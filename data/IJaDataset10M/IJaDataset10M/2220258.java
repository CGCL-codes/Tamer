package japatest;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MethodChanger {

    public static void main(String[] args) throws Exception {
        String file = "C:\\projects\\testdir\\experiment\\VectorTest.java";
        FileInputStream in = new FileInputStream(file);
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        changeMethods(cu);
        System.out.println(cu.toString());
    }

    private static void changeMethods(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            System.out.println(type.getName());
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    System.out.println("   " + method.getName());
                    changeMethod(method);
                }
            }
        }
    }

    private static void changeMethod(MethodDeclaration n) {
        if (!n.getName().equals("testLocal2")) {
            return;
        }
        ClassOrInterfaceType t = new ClassOrInterfaceType("Object");
        List<VariableDeclarator> vars = new ArrayList<VariableDeclarator>();
        VariableDeclarator decl = new VariableDeclarator();
        decl.setId(new VariableDeclaratorId("obj"));
        NameExpr readClass = new NameExpr("instrumenter.state.xstream.ObjectReader");
        MethodCallExpr readCall = new MethodCallExpr(readClass, "readObject");
        ASTHelper.addArgument(readCall, new StringLiteralExpr(".\\\\VectorTest_test4__VectorTest__java_util_Vector_vec_.xml"));
        ASTHelper.addArgument(readCall, new NameExpr("java.util.Vector.class"));
        decl.setInit(readCall);
        vars.add(decl);
        VariableDeclarationExpr declare = new VariableDeclarationExpr(t, vars);
        NameExpr setClass = new NameExpr("instrumenter.state.xstream.ClassState");
        MethodCallExpr setCall = new MethodCallExpr(setClass, "setClassField");
        ASTHelper.addArgument(setCall, new StringLiteralExpr("VectorTest"));
        ASTHelper.addArgument(setCall, new StringLiteralExpr("vec"));
        ASTHelper.addArgument(setCall, new NameExpr("obj"));
        BlockStmt block = new BlockStmt();
        ASTHelper.addStmt(block, declare);
        ASTHelper.addStmt(block, setCall);
        BlockStmt original = n.getBody();
        ASTHelper.addStmt(block, original);
        n.setBody(block);
    }
}
