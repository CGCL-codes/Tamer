package ac.jp.u_tokyo.SyncLib.language3;

import java.util.LinkedList;
import java.util.List;

public abstract class BracketSync extends SyncConstruct {

    protected List<SyncConstruct> _inners = new LinkedList<SyncConstruct>();

    public BracketSync(int line) {
        super(line);
    }

    public void addInner(SyncConstruct body) {
        _inners.add(body);
    }
}
