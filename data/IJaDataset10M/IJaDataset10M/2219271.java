package oxygen.tool.wlfacade.plugins;

import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.tool.wlfacade.WLFacadeContextHelper;
import oxygen.tool.wlfacade.helpers.WLServer;

public class Suspend extends FacadePluginAdapter {

    public Object execute(String name) throws Exception {
        return execute(name, false);
    }

    public Object execute(String name, boolean force) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        Object tbl = null;
        if (WLServer.isServer(wlctx, name)) {
            tbl = WLServer.suspendserver(wlctx, name, force);
        }
        return tbl;
    }
}
