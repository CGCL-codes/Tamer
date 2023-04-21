package oxygen.tool.facade.plugins;

import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.util.ObjectWrapper;

public class Help extends FacadePluginAdapter {

    public Object execute() {
        return execute("help");
    }

    public Object execute(String s) {
        return new ObjectWrapper(ctx.getFacadeHelpHelper().getDetailedHelp(s));
    }
}
