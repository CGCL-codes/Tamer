package screen.tools.sbs.context.defaults;

import screen.tools.sbs.context.Context;
import screen.tools.sbs.objects.TinyPack;

public class TinyPackContext implements Context {

    private TinyPack pack;

    public TinyPackContext() {
        pack = null;
    }

    public void setPack(TinyPack pack) {
        this.pack = pack;
    }

    public boolean hasPack() {
        return pack != null;
    }

    public TinyPack getPack() {
        return pack;
    }
}
