package org.koossery.adempiere.core.backend.interfaces.sisv.workflow;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.workflow.AD_WF_EventAuditCriteria;
import org.koossery.adempiere.core.contract.dto.workflow.AD_WF_EventAuditDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IAD_WF_EventAuditSISV extends IKTADempiereSimpleService {

    public int createAD_WF_EventAudit(Properties ctx, AD_WF_EventAuditDTO aD_WF_EventAuditDTO, String trxname) throws KTAdempiereAppException;

    public AD_WF_EventAuditDTO getAD_WF_EventAudit(Properties ctx, int aD_WF_EventAuditID, String trxname) throws KTAdempiereAppException;

    public ArrayList<AD_WF_EventAuditDTO> findAD_WF_EventAudit(Properties ctx, AD_WF_EventAuditCriteria aD_WF_EventAuditCriteria) throws KTAdempiereAppException;

    public void updateAD_WF_EventAudit(Properties ctx, AD_WF_EventAuditDTO aD_WF_EventAuditDTO) throws KTAdempiereAppException;

    public boolean deleteAD_WF_EventAudit(Properties ctx, AD_WF_EventAuditCriteria aD_WF_EventAuditCriteria) throws KTAdempiereAppException;
}
