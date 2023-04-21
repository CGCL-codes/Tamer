package org.koossery.adempiere.sisv.impl.data.utility;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_Note;
import org.koossery.adempiere.core.backend.interfaces.dao.data.utility.IAD_NoteDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.data.utility.IAD_NoteSISV;
import org.koossery.adempiere.core.contract.criteria.data.utility.AD_NoteCriteria;
import org.koossery.adempiere.core.contract.dto.data.utility.AD_NoteDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class AD_NoteSISVImpl extends AbstractCommonSISV implements IAD_NoteSISV, InitializingBean {

    private IAD_NoteDAO adnoteDAOImpl;

    private static Logger logger = Logger.getLogger(AD_NoteSISVImpl.class);

    public AD_NoteSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adnoteDAOImpl = (IAD_NoteDAO) this.getDaoController().get("DAO/AD_Note");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_Note(Properties ctx, AD_NoteDTO aD_NoteDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_Note model = new X_AD_Note(ctx, 0, trxname);
            AD_NoteCriteria criteria = new AD_NoteCriteria();
            if (adnoteDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_Message_ID(aD_NoteDTO.getAd_Message_ID());
                model.setAD_Table_ID(aD_NoteDTO.getAd_Table_ID());
                model.setAD_User_ID(aD_NoteDTO.getAd_User_ID());
                model.setAD_WF_Activity_ID(aD_NoteDTO.getAd_WF_Activity_ID());
                model.setDescription(aD_NoteDTO.getDescription());
                model.setRecord_ID(aD_NoteDTO.getRecord_ID());
                model.setReference(aD_NoteDTO.getReference());
                model.setTextMsg(aD_NoteDTO.getTextMsg());
                model.setProcessed(aD_NoteDTO.getIsProcessed() == "Y" ? true : false);
                model.setProcessing(aD_NoteDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_NoteDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_NoteDTO.setAd_Note_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_NoteDTO getAD_Note(Properties ctx, int ad_Note_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (ad_Note_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_002");
            X_AD_Note mnote = new X_AD_Note(ctx, ad_Note_ID, trxname);
            return convertModelToDTO(mnote);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_NoteDTO> findAD_Note(Properties ctx, AD_NoteCriteria aD_NoteCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aD_NoteCriteria);
            int id[] = X_AD_Note.getAllIDs("AD_Note", whereclause, null);
            ArrayList<AD_NoteDTO> list = new ArrayList<AD_NoteDTO>();
            AD_NoteDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getAD_Note(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_Note(Properties ctx, AD_NoteDTO aD_NoteDTO) throws KTAdempiereAppException {
        try {
            X_AD_Note model = new X_AD_Note(ctx, aD_NoteDTO.getAd_Note_ID(), null);
            if (model != null) {
                AD_NoteCriteria criteria = new AD_NoteCriteria();
                if (adnoteDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setAD_Message_ID(aD_NoteDTO.getAd_Message_ID());
                model.setAD_Table_ID(aD_NoteDTO.getAd_Table_ID());
                model.setAD_User_ID(aD_NoteDTO.getAd_User_ID());
                model.setAD_WF_Activity_ID(aD_NoteDTO.getAd_WF_Activity_ID());
                model.setDescription(aD_NoteDTO.getDescription());
                model.setRecord_ID(aD_NoteDTO.getRecord_ID());
                model.setReference(aD_NoteDTO.getReference());
                model.setTextMsg(aD_NoteDTO.getTextMsg());
                model.setProcessed(aD_NoteDTO.getIsProcessed() == "Y" ? true : false);
                model.setProcessing(aD_NoteDTO.getIsProcessing() == "Y" ? true : false);
                model.setIsActive(aD_NoteDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_NoteDTO.setAd_Note_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteAD_Note(Properties ctx, AD_NoteCriteria aD_NoteCriteria) throws KTAdempiereAppException {
        try {
            int id = aD_NoteCriteria.getAd_Note_ID();
            X_AD_Note model = new X_AD_Note(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_NOTE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, AD_NoteCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Message_ID() > 0) temp.append(" AND (AD_MESSAGE_ID=" + criteria.getAd_Message_ID() + ")");
        if (criteria.getAd_Note_ID() > 0) temp.append(" AND (AD_NOTE_ID=" + criteria.getAd_Note_ID() + ")");
        if (criteria.getAd_Table_ID() > 0) temp.append(" AND (AD_TABLE_ID=" + criteria.getAd_Table_ID() + ")");
        if (criteria.getAd_User_ID() > 0) temp.append(" AND (AD_USER_ID=" + criteria.getAd_User_ID() + ")");
        if (criteria.getAd_WF_Activity_ID() > 0) temp.append(" AND (AD_WF_ACTIVITY_ID=" + criteria.getAd_WF_Activity_ID() + ")");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getRecord_ID() > 0) temp.append(" AND (RECORD_ID=" + criteria.getRecord_ID() + ")");
        if (criteria.getReference() != null) temp.append(" AND ( REFERENCE LIKE '%" + criteria.getReference() + "%')");
        if (criteria.getTextMsg() != null) temp.append(" AND ( TEXTMSG LIKE '%" + criteria.getTextMsg() + "%')");
        if (criteria.getIsProcessed() != null) temp.append(" AND (ISPROCESSED='" + criteria.getIsProcessed() + "')");
        if (criteria.getIsProcessing() != null) temp.append(" AND (ISPROCESSING='" + criteria.getIsProcessing() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected AD_NoteDTO convertModelToDTO(X_AD_Note model) {
        AD_NoteDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new AD_NoteDTO();
            if ((obj = model.get_Value("AD_MESSAGE_ID")) != null) dto.setAd_Message_ID((Integer) obj);
            if ((obj = model.get_Value("AD_NOTE_ID")) != null) dto.setAd_Note_ID((Integer) obj);
            if ((obj = model.get_Value("AD_TABLE_ID")) != null) dto.setAd_Table_ID((Integer) obj);
            if ((obj = model.get_Value("AD_USER_ID")) != null) dto.setAd_User_ID((Integer) obj);
            if ((obj = model.get_Value("AD_WF_ACTIVITY_ID")) != null) dto.setAd_WF_Activity_ID((Integer) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("RECORD_ID")) != null) dto.setRecord_ID((Integer) obj);
            if ((obj = model.get_Value("REFERENCE")) != null) dto.setReference((String) obj);
            if ((obj = model.get_Value("TEXTMSG")) != null) dto.setTextMsg((String) obj);
            if ((obj = model.get_Value("ISPROCESSED")) != null) dto.setIsProcessed(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISPROCESSING")) != null) dto.setIsProcessing(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
