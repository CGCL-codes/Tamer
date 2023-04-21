package ac.jp.u_tokyo.SyncLib.language;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import ac.jp.u_tokyo.SyncLib.util.Pair;

public class NativeSyncBody extends SyncBody {

    String _factory;

    int _paraCount;

    Collection<Pair<String, Const>> _options = new LinkedList<Pair<String, Const>>();

    public NativeSyncBody(int line, String factory, int paraCount) {
        super(line);
        _factory = factory;
        _paraCount = paraCount;
    }

    public void addOption(String name, Const c) {
        _options.add(new Pair<String, Const>(name, c));
    }

    @Override
    public String writeInitializeFactory(Appendable sb, VarNameGenerator generator, ConstMapper constMap) throws EvaluationFailedException, IOException {
        String varName = generator.getNextName();
        sb.append(_factory).append(" ").append(varName).append(" = new ").append(_factory).append("();\n");
        for (Pair<String, Const> p : _options) {
            sb.append(varName).append(".set").append(p.getValue1()).append("(");
            p.getValue2().write(sb, constMap);
            sb.append(");\n");
        }
        return varName;
    }

    @Override
    public int getParaCount() throws EvaluationFailedException {
        return _paraCount;
    }
}
