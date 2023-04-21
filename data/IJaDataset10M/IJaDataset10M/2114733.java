package k2msg;

import kfmes.natelib.entity.NateGroup;

public class NateFriendGroupWrapper extends FriendGroup {

    private NateGroup g;

    public NateFriendGroupWrapper(NateGroup g) {
        this.g = g;
    }

    @Override
    public String getFormattedName() {
        return g.getName();
    }

    @Override
    public String getIndex() {
        return g.getNumber();
    }

    @Override
    public String getName() {
        return g.getName();
    }
}
