package cn.ekuma.epos.online.bean.erp.org;

import cn.ekuma.epos.online.def.I_ClientOrgBean;
import com.openbravo.bean.erp.Order;

public class ClientOrgOrder extends Order implements I_ClientOrgBean {

    String clientId;

    String orgId;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }

    @Override
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
