package oxygen.tool.wlfacade.plugins;

import java.util.List;
import javax.management.ObjectName;
import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.tool.wlfacade.WLFacadeContextHelper;
import oxygen.tool.wlfacade.helpers.WLSecurity;

public class SecurityLister extends FacadePluginAdapter {

    public List execute(String cursor) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        return WLSecurity.securitylister(wlctx, cursor);
    }

    public List execute(String cursor, ObjectName cmo) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        return WLSecurity.securitylister(wlctx, cursor, cmo);
    }
}
