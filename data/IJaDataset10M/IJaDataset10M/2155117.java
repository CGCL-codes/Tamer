package oxygen.tool.wlfacade.plugins;

import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.tool.wlfacade.WLFacadeContextHelper;
import oxygen.tool.wlfacade.helpers.WLList;
import oxygen.util.OxyTable;

public class ListMBS extends FacadePluginAdapter {

    public OxyTable execute() throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        return WLList.listmbs(wlctx);
    }
}
