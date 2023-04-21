package org.koossery.adempiere.core.backend.interfaces.dao.task;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.task.AD_TaskInstanceCriteria;
import org.koossery.adempiere.core.contract.dto.task.AD_TaskInstanceDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAD_TaskInstanceDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(AD_TaskInstanceCriteria criteria) throws KTAdempiereException;

    public boolean update(AD_TaskInstanceCriteria aD_TaskInstanceCriteria) throws KTAdempiereException;

    public ArrayList<AD_TaskInstanceDTO> getAD_TaskInstance(AD_TaskInstanceCriteria aD_TaskInstanceCriteria) throws KTAdempiereException;
}
