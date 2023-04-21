package org.koossery.adempiere.sisv.impl.server.alert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_AlertProcessor;
import org.koossery.adempiere.core.backend.interfaces.dao.server.alert.IAD_AlertProcessorDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.server.alert.IAD_AlertProcessorSISV;
import org.koossery.adempiere.core.contract.criteria.server.alert.AD_AlertProcessorCriteria;
import org.koossery.adempiere.core.contract.dto.server.alert.AD_AlertProcessorDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_AlertProcessorSISVImpl extends AbstractCommonSISV implements IAD_AlertProcessorSISV, InitializingBean {

    private IAD_AlertProcessorDAO adalertprocessorDAOImpl;

    private static Logger logger = Logger.getLogger(AD_AlertProcessorSISVImpl.class);

    public AD_AlertProcessorSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adalertprocessorDAOImpl = (IAD_AlertProcessorDAO) this.getDaoController().get("DAO/AD_AlertProcessor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_AlertProcessor(Properties ctx, AD_AlertProcessorDTO aD_AlertProcessorDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_AlertProcessor model = new X_AD_AlertProcessor(ctx, 0, trxname);
            AD_AlertProcessorCriteria criteria = new AD_AlertProcessorCriteria();
            if (adalertprocessorDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setDateLastRun(aD_AlertProcessorDTO.getDateLastRun());
                model.setDateNextRun(aD_AlertProcessorDTO.getDateNextRun());
                model.setDescription(aD_AlertProcessorDTO.getDescription());
                model.setFrequency(aD_AlertProcessorDTO.getFrequency());
                model.setFrequencyType(aD_AlertProcessorDTO.getFrequencyType());
                model.setKeepLogDays(aD_AlertProcessorDTO.getKeepLogDays());
                model.setName(aD_AlertProcessorDTO.getName());
                model.setSupervisor_ID(aD_AlertProcessorDTO.getSupervisor_ID());
                model.setProcessing(aD_AlertProcessorDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_AlertProcessorDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_AlertProcessorDTO.setAd_AlertProcessor_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_AlertProcessorDTO getAD_AlertProcessor(Properties ctx, int ad_AlertProcessor_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_AlertProcessor_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_002");
            X_AD_AlertProcessor malertprocessor = new X_AD_AlertProcessor(ctx, ad_AlertProcessor_ID, trxname);
            return convertModelToDTO(malertprocessor);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_AlertProcessorDTO> findAD_AlertProcessor(Properties ctx, AD_AlertProcessorCriteria aD_AlertProcessorCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_AlertProcessorCriteria);
            int id[] = X_AD_AlertProcessor.getAllIDs("AD_AlertProcessor", whereclause, null);
            ArrayList<AD_AlertProcessorDTO> list = new ArrayList<AD_AlertProcessorDTO>();
            AD_AlertProcessorDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getAD_AlertProcessor(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_AlertProcessor(Properties ctx, AD_AlertProcessorDTO aD_AlertProcessorDTO) throws KTAdempiereAppException {
        try {
            X_AD_AlertProcessor model = new X_AD_AlertProcessor(ctx, aD_AlertProcessorDTO.getAd_AlertProcessor_ID(), null);
            if (model != null) {
                AD_AlertProcessorCriteria criteria = new AD_AlertProcessorCriteria();
                if (adalertprocessorDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setDateLastRun(aD_AlertProcessorDTO.getDateLastRun());
                model.setDateNextRun(aD_AlertProcessorDTO.getDateNextRun());
                model.setDescription(aD_AlertProcessorDTO.getDescription());
                model.setFrequency(aD_AlertProcessorDTO.getFrequency());
                model.setFrequencyType(aD_AlertProcessorDTO.getFrequencyType());
                model.setKeepLogDays(aD_AlertProcessorDTO.getKeepLogDays());
                model.setName(aD_AlertProcessorDTO.getName());
                model.setSupervisor_ID(aD_AlertProcessorDTO.getSupervisor_ID());
                model.setProcessing(aD_AlertProcessorDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_AlertProcessorDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_AlertProcessorDTO.setAd_AlertProcessor_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_AlertProcessor(Properties ctx, AD_AlertProcessorCriteria aD_AlertProcessorCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_AlertProcessorCriteria.getAd_AlertProcessor_ID();
            X_AD_AlertProcessor model = new X_AD_AlertProcessor(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ALERTPROCESSOR_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_AlertProcessorCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_AlertProcessor_ID() > 0) temp.append(" AND (AD_ALERTPROCESSOR_ID=" + criteria.getAd_AlertProcessor_ID() + ")");
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

    protected AD_AlertProcessorDTO convertModelToDTO(X_AD_AlertProcessor model) {
        AD_AlertProcessorDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_AlertProcessorDTO();
            if ((obj = model.get_Value("AD_ALERTPROCESSOR_ID")) != null) dto.setAd_AlertProcessor_ID((Integer) obj);
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
