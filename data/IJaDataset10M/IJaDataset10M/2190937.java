package ac.jp.u_tokyo.SyncLib.GraphBasedCombinators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ac.jp.u_tokyo.SyncLib.Mod;
import ac.jp.u_tokyo.SyncLib.Sync;

public class SyncHolder implements Iterable<Variable> {

    private List<Variable> _connectedVars;

    private Sync _sync;

    public SyncHolder(Sync heldSync) {
        super();
        _sync = heldSync;
        _connectedVars = new ArrayList<Variable>(getVariableCount());
        for (int i = 0; i < getVariableCount(); i++) _connectedVars.add(null);
    }

    int getVariableCount() {
        return _sync.getParaCount();
    }

    public void connectVariable(int index, Variable v) {
        assert (index >= 0 && index < getVariableCount());
        _connectedVars.set(index, v);
        v.onConnectedToSyncHolder(this, index);
    }

    Variable getVariable(int index) {
        return _connectedVars.get(index);
    }

    public Sync getSync() {
        return _sync;
    }

    public Iterator<Variable> iterator() {
        return _connectedVars.iterator();
    }

    public List<Mod> getModListFromVars() {
        List<Mod> result = new ArrayList<Mod>(getVariableCount());
        for (Variable v : this) {
            result.add(v.getMod());
        }
        return result;
    }

    public List<Object> getValueListFromVars() {
        List<Object> result = new ArrayList<Object>(getVariableCount());
        for (Variable v : this) {
            result.add(v.getValue());
        }
        return result;
    }
}
