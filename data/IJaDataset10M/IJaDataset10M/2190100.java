package org.koossery.adempiere.sisv.impl.client;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_ASP_ClientException;
import org.koossery.adempiere.core.backend.interfaces.dao.client.IASP_ClientExceptionDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.client.IASP_ClientExceptionSISV;
import org.koossery.adempiere.core.contract.criteria.client.ASP_ClientExceptionCriteria;
import org.koossery.adempiere.core.contract.dto.client.ASP_ClientExceptionDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class ASP_ClientExceptionSISVImpl extends AbstractCommonSISV implements IASP_ClientExceptionSISV, InitializingBean {

    private IASP_ClientExceptionDAO aspclientexceptionDAOImpl;

    private static Logger logger = Logger.getLogger(ASP_ClientExceptionSISVImpl.class);

    public ASP_ClientExceptionSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.aspclientexceptionDAOImpl = (IASP_ClientExceptionDAO) this.getDaoController().get("DAO/ASP_ClientException");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createASP_ClientException(Properties ctx, ASP_ClientExceptionDTO aSP_ClientExceptionDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_ASP_ClientException model = new X_ASP_ClientException(ctx, 0, trxname);
            ASP_ClientExceptionCriteria criteria = new ASP_ClientExceptionCriteria();
            if (aspclientexceptionDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_Field_ID(aSP_ClientExceptionDTO.getAd_Field_ID());
                model.setAD_Form_ID(aSP_ClientExceptionDTO.getAd_Form_ID());
                model.setAD_Process_ID(aSP_ClientExceptionDTO.getAd_Process_ID());
                model.setAD_Process_Para_ID(aSP_ClientExceptionDTO.getAd_Process_Para_ID());
                model.setAD_Tab_ID(aSP_ClientExceptionDTO.getAd_Tab_ID());
                model.setAD_Task_ID(aSP_ClientExceptionDTO.getAd_Task_ID());
                model.setAD_WF_Node_ID(aSP_ClientExceptionDTO.getAd_WF_Node_ID());
                model.setAD_Window_ID(aSP_ClientExceptionDTO.getAd_Window_ID());
                model.setAD_Workflow_ID(aSP_ClientExceptionDTO.getAd_Workflow_ID());
                model.setASP_Status(aSP_ClientExceptionDTO.getAsP_Status());
                model.setIsActive(aSP_ClientExceptionDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aSP_ClientExceptionDTO.setAsP_ClientException_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ASP_ClientExceptionDTO getASP_ClientException(Properties ctx, int asP_ClientException_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (asP_ClientException_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_002");
            X_ASP_ClientException mclientexception = new X_ASP_ClientException(ctx, asP_ClientException_ID, trxname);
            return convertModelToDTO(mclientexception);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<ASP_ClientExceptionDTO> findASP_ClientException(Properties ctx, ASP_ClientExceptionCriteria aSP_ClientExceptionCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, aSP_ClientExceptionCriteria);
            int id[] = X_ASP_ClientException.getAllIDs("ASP_ClientException", whereclause, null);
            ArrayList<ASP_ClientExceptionDTO> list = new ArrayList<ASP_ClientExceptionDTO>();
            ASP_ClientExceptionDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getASP_ClientException(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateASP_ClientException(Properties ctx, ASP_ClientExceptionDTO aSP_ClientExceptionDTO) throws KTAdempiereAppException {
        try {
            X_ASP_ClientException model = new X_ASP_ClientException(ctx, aSP_ClientExceptionDTO.getAsP_ClientException_ID(), null);
            System.out.println("NO duplicates names for modification");
            model.setAD_Field_ID(aSP_ClientExceptionDTO.getAd_Field_ID());
            model.setAD_Form_ID(aSP_ClientExceptionDTO.getAd_Form_ID());
            model.setAD_Process_ID(aSP_ClientExceptionDTO.getAd_Process_ID());
            model.setAD_Process_Para_ID(aSP_ClientExceptionDTO.getAd_Process_Para_ID());
            model.setAD_Tab_ID(aSP_ClientExceptionDTO.getAd_Tab_ID());
            model.setAD_Task_ID(aSP_ClientExceptionDTO.getAd_Task_ID());
            model.setAD_WF_Node_ID(aSP_ClientExceptionDTO.getAd_WF_Node_ID());
            model.setAD_Window_ID(aSP_ClientExceptionDTO.getAd_Window_ID());
            model.setAD_Workflow_ID(aSP_ClientExceptionDTO.getAd_Workflow_ID());
            model.setASP_Status(aSP_ClientExceptionDTO.getAsP_Status());
            model.setIsActive(aSP_ClientExceptionDTO.getIsActive() == "Y" ? true : false);
            this.setPO(model);
            this.save();
            aSP_ClientExceptionDTO.setAsP_ClientException_ID(model.get_ID());
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteASP_ClientException(Properties ctx, ASP_ClientExceptionCriteria aSP_ClientExceptionCriteria) throws KTAdempiereAppException {
        try {
            int id = aSP_ClientExceptionCriteria.getAsP_ClientException_ID();
            X_ASP_ClientException model = new X_ASP_ClientException(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_CLIENTEXCEPTION_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, ASP_ClientExceptionCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Field_ID() > 0) temp.append(" AND (AD_FIELD_ID=" + criteria.getAd_Field_ID() + ")");
        if (criteria.getAd_Form_ID() > 0) temp.append(" AND (AD_FORM_ID=" + criteria.getAd_Form_ID() + ")");
        if (criteria.getAd_Process_ID() > 0) temp.append(" AND (AD_PROCESS_ID=" + criteria.getAd_Process_ID() + ")");
        if (criteria.getAd_Process_Para_ID() > 0) temp.append(" AND (AD_PROCESS_PARA_ID=" + criteria.getAd_Process_Para_ID() + ")");
        if (criteria.getAd_Tab_ID() > 0) temp.append(" AND (AD_TAB_ID=" + criteria.getAd_Tab_ID() + ")");
        if (criteria.getAd_Task_ID() > 0) temp.append(" AND (AD_TASK_ID=" + criteria.getAd_Task_ID() + ")");
        if (criteria.getAd_WF_Node_ID() > 0) temp.append(" AND (AD_WF_NODE_ID=" + criteria.getAd_WF_Node_ID() + ")");
        if (criteria.getAd_Window_ID() > 0) temp.append(" AND (AD_WINDOW_ID=" + criteria.getAd_Window_ID() + ")");
        if (criteria.getAd_Workflow_ID() > 0) temp.append(" AND (AD_WORKFLOW_ID=" + criteria.getAd_Workflow_ID() + ")");
        if (criteria.getAsP_ClientException_ID() > 0) temp.append(" AND (ASP_CLIENTEXCEPTION_ID=" + criteria.getAsP_ClientException_ID() + ")");
        if (criteria.getAsP_Status() != null) temp.append(" AND ( ASP_STATUS LIKE '%" + criteria.getAsP_Status() + "%')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected ASP_ClientExceptionDTO convertModelToDTO(X_ASP_ClientException model) {
        ASP_ClientExceptionDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new ASP_ClientExceptionDTO();
            if ((obj = model.get_Value("AD_FIELD_ID")) != null) dto.setAd_Field_ID((Integer) obj);
            if ((obj = model.get_Value("AD_FORM_ID")) != null) dto.setAd_Form_ID((Integer) obj);
            if ((obj = model.get_Value("AD_PROCESS_ID")) != null) dto.setAd_Process_ID((Integer) obj);
            if ((obj = model.get_Value("AD_PROCESS_PARA_ID")) != null) dto.setAd_Process_Para_ID((Integer) obj);
            if ((obj = model.get_Value("AD_TAB_ID")) != null) dto.setAd_Tab_ID((Integer) obj);
            if ((obj = model.get_Value("AD_TASK_ID")) != null) dto.setAd_Task_ID((Integer) obj);
            if ((obj = model.get_Value("AD_WF_NODE_ID")) != null) dto.setAd_WF_Node_ID((Integer) obj);
            if ((obj = model.get_Value("AD_WINDOW_ID")) != null) dto.setAd_Window_ID((Integer) obj);
            if ((obj = model.get_Value("AD_WORKFLOW_ID")) != null) dto.setAd_Workflow_ID((Integer) obj);
            if ((obj = model.get_Value("ASP_CLIENTEXCEPTION_ID")) != null) dto.setAsP_ClientException_ID((Integer) obj);
            if ((obj = model.get_Value("ASP_STATUS")) != null) dto.setAsP_Status((String) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
