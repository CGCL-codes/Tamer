package jintgen.gen;

import jintgen.jigir.*;
import java.util.List;

/**
 * @author Ben L. Titzer
 */
public abstract class CodeProcessor<Env> extends StmtRebuilder<Env> {

    protected final VarRenamer renamer;

    public CodeProcessor(VarRenamer rn) {
        renamer = rn;
    }

    public Expr visit(VarExpr e, Env env) {
        return renamer.getVarExpr(e);
    }

    public Expr visit(ReadExpr e, Env env) {
        return renamer.getReadExpr(e);
    }

    public Stmt visit(DeclStmt s, Env env) {
        return renamer.getDecl((DeclStmt) super.visit(s, env));
    }

    public Stmt visit(WriteStmt s, Env env) {
        return renamer.getWrite((WriteStmt) super.visit(s, env));
    }

    public abstract List<Stmt> process(List<Stmt> l);

    public void renameVariable(String orig, String n) {
        renamer.addVariable(orig, n);
    }
}
