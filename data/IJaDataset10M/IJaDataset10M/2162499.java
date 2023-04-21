package org.koossery.adempiere.core.backend.interfaces.dao.workflow;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.workflow.AD_WF_ProcessDataCriteria;
import org.koossery.adempiere.core.contract.dto.workflow.AD_WF_ProcessDataDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAD_WF_ProcessDataDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(AD_WF_ProcessDataCriteria criteria) throws KTAdempiereException;

    public boolean update(AD_WF_ProcessDataCriteria aD_WF_ProcessDataCriteria) throws KTAdempiereException;

    public ArrayList<AD_WF_ProcessDataDTO> getAD_WF_ProcessData(AD_WF_ProcessDataCriteria aD_WF_ProcessDataCriteria) throws KTAdempiereException;
}
