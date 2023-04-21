package org.posterita.model;

import org.compiere.model.MUserOrgAccess;

public class UDIMUserOrgAccess extends UDIPO {

    public UDIMUserOrgAccess(MUserOrgAccess po) {
        super(po);
    }

    public MUserOrgAccess getMUserOrgAccess() {
        return (MUserOrgAccess) getPO();
    }
}
