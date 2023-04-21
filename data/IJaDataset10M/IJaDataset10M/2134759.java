package net.sf.drawbridge.dwr;

import java.util.List;
import net.sf.drawbridge.domain.DrawbridgeService;
import net.sf.drawbridge.vo.RunAsAccount;

public class WrapperDwrDrawbridgeFacade implements DwrDrawbridgeFacade {

    private DrawbridgeService drawbridgeService;

    public WrapperDwrDrawbridgeFacade(DrawbridgeService drawbridgeService) {
        this.drawbridgeService = drawbridgeService;
    }

    public List<RunAsAccount> listRunAsAccounts(Integer databaseId) {
        List<RunAsAccount> accounts = drawbridgeService.listRunAsAccounts(databaseId);
        for (RunAsAccount account : accounts) {
            account.setPassword("********");
        }
        return accounts;
    }
}
