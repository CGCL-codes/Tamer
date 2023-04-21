package org.koossery.adempiere.sisv.impl.server.accounting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_C_AcctProcessor;
import org.koossery.adempiere.core.backend.interfaces.dao.server.accounting.IC_AcctProcessorDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.server.accounting.IC_AcctProcessorSISV;
import org.koossery.adempiere.core.contract.criteria.server.accounting.C_AcctProcessorCriteria;
import org.koossery.adempiere.core.contract.dto.server.accounting.C_AcctProcessorDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_AcctProcessorSISVImpl extends AbstractCommonSISV implements IC_AcctProcessorSISV, InitializingBean {

    private IC_AcctProcessorDAO cacctprocessorDAOImpl;

    private static Logger logger = Logger.getLogger(C_AcctProcessorSISVImpl.class);

    public C_AcctProcessorSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.cacctprocessorDAOImpl = (IC_AcctProcessorDAO) this.getDaoController().get("DAO/C_AcctProcessor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_AcctProcessor(Properties ctx, C_AcctProcessorDTO c_AcctProcessorDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_AcctProcessor model = new X_C_AcctProcessor(ctx, 0, trxname);
            C_AcctProcessorCriteria criteria = new C_AcctProcessorCriteria();
            if (cacctprocessorDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_Table_ID(c_AcctProcessorDTO.getAd_Table_ID());
                model.setC_AcctSchema_ID(c_AcctProcessorDTO.getC_AcctSchema_ID());
                model.setDateLastRun(c_AcctProcessorDTO.getDateLastRun());
                model.setDateNextRun(c_AcctProcessorDTO.getDateNextRun());
                model.setDescription(c_AcctProcessorDTO.getDescription());
                model.setFrequency(c_AcctProcessorDTO.getFrequency());
                model.setFrequencyType(c_AcctProcessorDTO.getFrequencyType());
                model.setKeepLogDays(c_AcctProcessorDTO.getKeepLogDays());
                model.setName(c_AcctProcessorDTO.getName());
                model.setSupervisor_ID(c_AcctProcessorDTO.getSupervisor_ID());
                model.setProcessing(c_AcctProcessorDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(c_AcctProcessorDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_AcctProcessorDTO.setC_AcctProcessor_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_AcctProcessorDTO getC_AcctProcessor(Properties ctx, int c_AcctProcessor_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (c_AcctProcessor_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_002");
            X_C_AcctProcessor macctprocessor = new X_C_AcctProcessor(ctx, c_AcctProcessor_ID, trxname);
            return convertModelToDTO(macctprocessor);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_AcctProcessorDTO> findC_AcctProcessor(Properties ctx, C_AcctProcessorCriteria c_AcctProcessorCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, c_AcctProcessorCriteria);
            int id[] = X_C_AcctProcessor.getAllIDs("C_AcctProcessor", whereclause, null);
            ArrayList<C_AcctProcessorDTO> list = new ArrayList<C_AcctProcessorDTO>();
            C_AcctProcessorDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getC_AcctProcessor(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_AcctProcessor(Properties ctx, C_AcctProcessorDTO c_AcctProcessorDTO) throws KTAdempiereAppException {
        try {
            X_C_AcctProcessor model = new X_C_AcctProcessor(ctx, c_AcctProcessorDTO.getC_AcctProcessor_ID(), null);
            if (model != null) {
                C_AcctProcessorCriteria criteria = new C_AcctProcessorCriteria();
                if (cacctprocessorDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setAD_Table_ID(c_AcctProcessorDTO.getAd_Table_ID());
                model.setC_AcctSchema_ID(c_AcctProcessorDTO.getC_AcctSchema_ID());
                model.setDateLastRun(c_AcctProcessorDTO.getDateLastRun());
                model.setDateNextRun(c_AcctProcessorDTO.getDateNextRun());
                model.setDescription(c_AcctProcessorDTO.getDescription());
                model.setFrequency(c_AcctProcessorDTO.getFrequency());
                model.setFrequencyType(c_AcctProcessorDTO.getFrequencyType());
                model.setKeepLogDays(c_AcctProcessorDTO.getKeepLogDays());
                model.setName(c_AcctProcessorDTO.getName());
                model.setSupervisor_ID(c_AcctProcessorDTO.getSupervisor_ID());
                model.setProcessing(c_AcctProcessorDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(c_AcctProcessorDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_AcctProcessorDTO.setC_AcctProcessor_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteC_AcctProcessor(Properties ctx, C_AcctProcessorCriteria c_AcctProcessorCriteria) throws KTAdempiereAppException {
        try {
            int id = c_AcctProcessorCriteria.getC_AcctProcessor_ID();
            X_C_AcctProcessor model = new X_C_AcctProcessor(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_ACCTPROCESSOR_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_AcctProcessorCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Table_ID() > 0) temp.append(" AND (AD_TABLE_ID=" + criteria.getAd_Table_ID() + ")");
        if (criteria.getC_AcctProcessor_ID() > 0) temp.append(" AND (C_ACCTPROCESSOR_ID=" + criteria.getC_AcctProcessor_ID() + ")");
        if (criteria.getC_AcctSchema_ID() > 0) temp.append(" AND (C_ACCTSCHEMA_ID=" + criteria.getC_AcctSchema_ID() + ")");
        if (criteria.getDateLastRun() != null) temp.append(" AND (DATELASTRUN=" + criteria.getDateLastRun() + ")");
        if (criteria.getDateNextRun() != null) temp.append(" AND (DATENEXTRUN=" + criteria.getDateNextRun() + ")");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getFrequency() > 0) temp.append(" AND (FREQUENCY=" + criteria.getFrequency() + ")");
        if (criteria.getFrequencyType() != null) temp.append(" AND ( FREQUENCYTYPE LIKE '%" + criteria.getFrequencyType() + "%')");
        if (criteria.getKeepLogDays() > 0) temp.append(" AND (KEEPLOGDAYS=" + criteria.getKeepLogDays() + ")");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getSupervisor_ID() > 0) temp.append(" AND (SUPERVISOR_ID=" + criteria.getSupervisor_ID() + ")");
        if (criteria.getIsProcessing() != null) temp.append(" AND (PROCESSING='" + criteria.getIsProcessing() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_AcctProcessorDTO convertModelToDTO(X_C_AcctProcessor model) {
        C_AcctProcessorDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_AcctProcessorDTO();
            if ((obj = model.get_Value("AD_TABLE_ID")) != null) dto.setAd_Table_ID((Integer) obj);
            if ((obj = model.get_Value("C_ACCTPROCESSOR_ID")) != null) dto.setC_AcctProcessor_ID((Integer) obj);
            if ((obj = model.get_Value("C_ACCTSCHEMA_ID")) != null) dto.setC_AcctSchema_ID((Integer) obj);
            if ((obj = model.get_Value("DATELASTRUN")) != null) dto.setDateLastRun((Timestamp) obj);
            if ((obj = model.get_Value("DATENEXTRUN")) != null) dto.setDateNextRun((Timestamp) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("FREQUENCY")) != null) dto.setFrequency((Integer) obj);
            if ((obj = model.get_Value("FREQUENCYTYPE")) != null) dto.setFrequencyType((String) obj);
            if ((obj = model.get_Value("KEEPLOGDAYS")) != null) dto.setKeepLogDays((Integer) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            if ((obj = model.get_Value("SUPERVISOR_ID")) != null) dto.setSupervisor_ID((Integer) obj);
            if ((obj = model.get_Value("PROCESSING")) != null) dto.setIsProcessing(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
