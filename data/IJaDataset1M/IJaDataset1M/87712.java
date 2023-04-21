package org.koossery.adempiere.core.backend.interfaces.dao.change;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.change.AD_ChangeLogCriteria;
import org.koossery.adempiere.core.contract.dto.change.AD_ChangeLogDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAD_ChangeLogDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(AD_ChangeLogCriteria criteria) throws KTAdempiereException;

    public boolean update(AD_ChangeLogCriteria aD_ChangeLogCriteria) throws KTAdempiereException;

    public ArrayList<AD_ChangeLogDTO> getAD_ChangeLog(AD_ChangeLogCriteria aD_ChangeLogCriteria) throws KTAdempiereException;
}
