package org.koossery.adempiere.sisv.impl.task;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_ASP_Task;
import org.koossery.adempiere.core.backend.interfaces.dao.task.IASP_TaskDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.task.IASP_TaskSISV;
import org.koossery.adempiere.core.contract.criteria.task.ASP_TaskCriteria;
import org.koossery.adempiere.core.contract.dto.task.ASP_TaskDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class ASP_TaskSISVImpl extends AbstractCommonSISV implements IASP_TaskSISV, InitializingBean {

    private IASP_TaskDAO asptaskDAOImpl;

    private static Logger logger = Logger.getLogger(ASP_TaskSISVImpl.class);

    public ASP_TaskSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.asptaskDAOImpl = (IASP_TaskDAO) this.getDaoController().get("DAO/ASP_Task");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createASP_Task(Properties ctx, ASP_TaskDTO aSP_TaskDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_ASP_Task model = new X_ASP_Task(ctx, 0, trxname);
            ASP_TaskCriteria criteria = new ASP_TaskCriteria();
            if (asptaskDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_Task_ID(aSP_TaskDTO.getAd_Task_ID());
                model.setASP_Level_ID(aSP_TaskDTO.getAsP_Level_ID());
                model.setASP_Status(aSP_TaskDTO.getAsP_Status());
                model.setIsActive(aSP_TaskDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ASP_TaskDTO getASP_Task(Properties ctx, int aD_Task_ID, int aSP_Level_ID) throws KTAdempiereAppException {
        try {
            ASP_TaskCriteria criteria = new ASP_TaskCriteria();
            criteria.setAd_Task_ID(aD_Task_ID);
            criteria.setAsP_Level_ID(aSP_Level_ID);
            ArrayList<ASP_TaskDTO> list = new ArrayList<ASP_TaskDTO>();
            list = asptaskDAOImpl.getASP_Task(criteria);
            return ((list != null) && (list.size() > 0)) ? list.get(0) : null;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<ASP_TaskDTO> findASP_Task(Properties ctx, ASP_TaskCriteria aSP_TaskCriteria) throws KTAdempiereAppException {
        try {
            ArrayList<ASP_TaskDTO> list = new ArrayList<ASP_TaskDTO>();
            list = asptaskDAOImpl.getASP_Task(aSP_TaskCriteria);
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateASP_Task(Properties ctx, ASP_TaskDTO aSP_TaskDTO) throws KTAdempiereAppException {
        try {
            ASP_TaskCriteria criteria = new ASP_TaskCriteria();
            criteria.setAd_Task_ID(aSP_TaskDTO.getAd_Task_ID());
            criteria.setAsP_Level_ID(aSP_TaskDTO.getAsP_Level_ID());
            criteria.setAd_Client_ID(aSP_TaskDTO.getAd_Client_ID());
            criteria.setAd_Org_ID(aSP_TaskDTO.getAd_Org_ID());
            criteria.setIsActive(aSP_TaskDTO.getIsActive());
            criteria.setAsP_Status(aSP_TaskDTO.getAsP_Status());
            if (asptaskDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_FORM_ACCESS_SISV_000");
            asptaskDAOImpl.update(criteria);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteASP_Task(Properties ctx, ASP_TaskCriteria aSP_TaskCriteria) throws KTAdempiereAppException {
        try {
            asptaskDAOImpl.delete(aSP_TaskCriteria);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "ASP_TASK_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, ASP_TaskCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_Task_ID() > 0) temp.append(" AND (AD_TASK_ID=" + criteria.getAd_Task_ID() + ")");
        if (criteria.getAsP_Level_ID() > 0) temp.append(" AND (ASP_LEVEL_ID=" + criteria.getAsP_Level_ID() + ")");
        if (criteria.getAsP_Status() != null) temp.append(" AND ( ASP_STATUS LIKE '%" + criteria.getAsP_Status() + "%')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected ASP_TaskDTO convertModelToDTO(X_ASP_Task model) {
        ASP_TaskDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new ASP_TaskDTO();
            if ((obj = model.get_Value("AD_TASK_ID")) != null) dto.setAd_Task_ID((Integer) obj);
            if ((obj = model.get_Value("ASP_LEVEL_ID")) != null) dto.setAsP_Level_ID((Integer) obj);
            if ((obj = model.get_Value("ASP_STATUS")) != null) dto.setAsP_Status((String) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
