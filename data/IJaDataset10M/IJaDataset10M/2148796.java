package org.tju.ebs.domain.manager;

import org.tju.ebs.domain.dao.OrgBankDAO;

public class OrgBankManager extends BaseManager {

    private OrgBankDAO orgBankDAO;

    public void setOrgBankDAO(OrgBankDAO orgBankDAO) {
        this.orgBankDAO = orgBankDAO;
    }

    public OrgBankDAO getOrgBankDAO() {
        return orgBankDAO;
    }
}
