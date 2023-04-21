package ac.jp.u_tokyo.SyncLib.language3;

import java.util.Collection;
import java.util.LinkedList;
import ac.jp.u_tokyo.SyncLib.language.EvaluationFailedException;
import ac.jp.u_tokyo.SyncLib.language.GraphCombinatorBody;

public abstract class VarExpr extends LineConstruct {

    public VarExpr(int line) {
        super(line);
    }

    public abstract void addBody(Var var, boolean atLeft, GraphCombinatorBody body, Collection<String> genericParas) throws EvaluationFailedException;

    public abstract Collection<Var> getVarUsed(Collection<String> genericParas) throws EvaluationFailedException;
}
