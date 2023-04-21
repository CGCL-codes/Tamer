package org.koossery.adempiere.core.backend.interfaces.dao.org;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.client.AD_ClientInfoCriteria;
import org.koossery.adempiere.core.contract.criteria.org.AD_OrgInfoCriteria;
import org.koossery.adempiere.core.contract.dto.org.AD_OrgInfoDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAD_OrgInfoDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(AD_OrgInfoCriteria criteria) throws KTAdempiereException;

    public boolean update(AD_OrgInfoCriteria aD_OrgInfoCriteria) throws KTAdempiereException;

    public ArrayList<AD_OrgInfoDTO> getAD_OrgInfo(AD_OrgInfoCriteria aD_OrgInfoCriteria) throws KTAdempiereException;

    public boolean delete(AD_OrgInfoCriteria aD_OrgInfoCriteria) throws KTAdempiereException;
}
