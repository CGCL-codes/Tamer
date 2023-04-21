package cn.ekuma.epos.online.bean.org;

import cn.ekuma.epos.online.def.I_ClientOrgBean;
import com.openbravo.bean.Location;

public class ClientOrgLocation extends Location implements I_ClientOrgBean {

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
