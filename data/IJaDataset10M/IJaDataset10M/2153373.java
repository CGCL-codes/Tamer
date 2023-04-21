package javassist.compiler;

import javassist.bytecode.Bytecode;
import javassist.compiler.ast.ASTList;

/**
 * An interface to an object for implementing $proceed().
 *
 * @see javassist.compiler.JvstCodeGen#setProceedHandler(ProceedHandler, String)
 * @see javassist.compiler.JvstCodeGen#atMethodCall(Expr)
 */
public interface ProceedHandler {

    void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError;

    void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError;
}
