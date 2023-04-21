package ac.jp.u_tokyo.SyncLib.language;

import java.io.IOException;

public abstract class SyncBody {

    public abstract String writeInitializeFactory(Appendable sb, VarNameGenerator generator, ConstMapper constMap) throws EvaluationFailedException, IOException;

    public abstract int getParaCount() throws EvaluationFailedException;

    private int _line;

    public int getLine() {
        return _line;
    }

    public SyncBody(int line) {
        super();
        _line = line;
    }
}
