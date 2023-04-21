package oxygen.tool.facade.plugins;

import java.util.Collection;
import java.util.Set;
import oxygen.tool.facade.FacadePluginAdapter;

public class SetVars extends FacadePluginAdapter {

    public Set execute() {
        return ctx.getVars();
    }

    public Set execute(Collection c) {
        ctx.setVars(c);
        return ctx.getVars();
    }

    public Set execute(String s) {
        ctx.setVar(s);
        return ctx.getVars();
    }
}
