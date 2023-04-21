package com.hand.test;

import java.util.Iterator;
import java.util.List;
import com.hand.model.TAccountAdm;
import com.hand.model.TAccountMappingInfo;
import com.hand.model.TSalesmanInfo;
import com.hand.service.IAccountMappService;
import com.hand.service.IAgentService;
import com.hand.service.ISalesManService;

public class TestService extends TestBase {

    private IAccountMappService accountMappService;

    private ISalesManService salesManService;

    private IAgentService agentService;

    public void testHttp() {
        try {
            List<TAccountMappingInfo> accountLsit = accountMappService.getAccountMappAll();
            Iterator iter = accountLsit.iterator();
            while (iter.hasNext()) {
                TAccountMappingInfo account = (TAccountMappingInfo) iter.next();
                TAccountAdm accountAdm = agentService.getAgentByUserId(account.getUserId());
                if (null == accountAdm || null == accountAdm.getCellularPhoneC() || "".equals(accountAdm.getCellularPhoneC())) {
                    continue;
                }
                TSalesmanInfo sale = salesManService.getSalesManByPhone(accountAdm.getCellularPhoneC());
                if (null == sale) {
                    System.out.println(accountAdm.getCellularPhoneC());
                    continue;
                }
                account.setHiAccountId(sale.getSalesmanId());
                accountMappService.updateAccountMapInfo(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setComplete();
    }

    public void setAccountMappService(IAccountMappService accountMappService) {
        this.accountMappService = accountMappService;
    }

    public void setSalesManService(ISalesManService salesManService) {
        this.salesManService = salesManService;
    }

    public void setAgentService(IAgentService agentService) {
        this.agentService = agentService;
    }
}
