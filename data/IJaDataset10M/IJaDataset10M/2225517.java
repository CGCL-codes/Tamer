package org.koossery.adempiere.sisv.impl.org.recurring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_C_Recurring_Run;
import org.koossery.adempiere.core.backend.interfaces.dao.org.recurring.IC_Recurring_RunDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.org.recurring.IC_Recurring_RunSISV;
import org.koossery.adempiere.core.contract.criteria.org.recurring.C_Recurring_RunCriteria;
import org.koossery.adempiere.core.contract.dto.org.recurring.C_Recurring_RunDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_Recurring_RunSISVImpl extends AbstractCommonSISV implements IC_Recurring_RunSISV, InitializingBean {

    private IC_Recurring_RunDAO crecurringrunDAOImpl;

    private static Logger logger = Logger.getLogger(C_Recurring_RunSISVImpl.class);

    public C_Recurring_RunSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.crecurringrunDAOImpl = (IC_Recurring_RunDAO) this.getDaoController().get("DAO/C_Recurring_Run");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_Recurring_Run(Properties ctx, C_Recurring_RunDTO c_Recurring_RunDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_Recurring_Run model = new X_C_Recurring_Run(ctx, 0, trxname);
            C_Recurring_RunCriteria criteria = new C_Recurring_RunCriteria();
            if (crecurringrunDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setC_Invoice_ID(c_Recurring_RunDTO.getC_Invoice_ID());
                model.setC_Order_ID(c_Recurring_RunDTO.getC_Order_ID());
                model.setC_Payment_ID(c_Recurring_RunDTO.getC_Payment_ID());
                model.setC_Project_ID(c_Recurring_RunDTO.getC_Project_ID());
                model.setC_Recurring_ID(c_Recurring_RunDTO.getC_Recurring_ID());
                model.setDateDoc(c_Recurring_RunDTO.getDateDoc());
                model.setGL_JournalBatch_ID(c_Recurring_RunDTO.getGl_JournalBatch_ID());
                model.setIsActive(c_Recurring_RunDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_Recurring_RunDTO.setC_Recurring_Run_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_Recurring_RunDTO getC_Recurring_Run(Properties ctx, int c_Recurring_Run_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (c_Recurring_Run_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_002");
            X_C_Recurring_Run mrecurringrun = new X_C_Recurring_Run(ctx, c_Recurring_Run_ID, trxname);
            return convertModelToDTO(mrecurringrun);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_Recurring_RunDTO> findC_Recurring_Run(Properties ctx, C_Recurring_RunCriteria c_Recurring_RunCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, c_Recurring_RunCriteria);
            int id[] = X_C_Recurring_Run.getAllIDs("C_Recurring_Run", whereclause, null);
            ArrayList<C_Recurring_RunDTO> list = new ArrayList<C_Recurring_RunDTO>();
            C_Recurring_RunDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getC_Recurring_Run(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_Recurring_Run(Properties ctx, C_Recurring_RunDTO c_Recurring_RunDTO) throws KTAdempiereAppException {
        try {
            X_C_Recurring_Run model = new X_C_Recurring_Run(ctx, c_Recurring_RunDTO.getC_Recurring_Run_ID(), null);
            if (model != null) {
                C_Recurring_RunCriteria criteria = new C_Recurring_RunCriteria();
                if (crecurringrunDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setC_Invoice_ID(c_Recurring_RunDTO.getC_Invoice_ID());
                model.setC_Order_ID(c_Recurring_RunDTO.getC_Order_ID());
                model.setC_Payment_ID(c_Recurring_RunDTO.getC_Payment_ID());
                model.setC_Project_ID(c_Recurring_RunDTO.getC_Project_ID());
                model.setC_Recurring_ID(c_Recurring_RunDTO.getC_Recurring_ID());
                model.setDateDoc(c_Recurring_RunDTO.getDateDoc());
                model.setGL_JournalBatch_ID(c_Recurring_RunDTO.getGl_JournalBatch_ID());
                model.setIsActive(c_Recurring_RunDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_Recurring_RunDTO.setC_Recurring_Run_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteC_Recurring_Run(Properties ctx, C_Recurring_RunCriteria c_Recurring_RunCriteria) throws KTAdempiereAppException {
        try {
            int id = c_Recurring_RunCriteria.getC_Recurring_Run_ID();
            X_C_Recurring_Run model = new X_C_Recurring_Run(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_RECURRING_RUN_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_Recurring_RunCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getC_Invoice_ID() > 0) temp.append(" AND (C_INVOICE_ID=" + criteria.getC_Invoice_ID() + ")");
        if (criteria.getC_Order_ID() > 0) temp.append(" AND (C_ORDER_ID=" + criteria.getC_Order_ID() + ")");
        if (criteria.getC_Payment_ID() > 0) temp.append(" AND (C_PAYMENT_ID=" + criteria.getC_Payment_ID() + ")");
        if (criteria.getC_Project_ID() > 0) temp.append(" AND (C_PROJECT_ID=" + criteria.getC_Project_ID() + ")");
        if (criteria.getC_Recurring_ID() > 0) temp.append(" AND (C_RECURRING_ID=" + criteria.getC_Recurring_ID() + ")");
        if (criteria.getC_Recurring_Run_ID() > 0) temp.append(" AND (C_RECURRING_RUN_ID=" + criteria.getC_Recurring_Run_ID() + ")");
        if (criteria.getDateDoc() != null) temp.append(" AND (DATEDOC=" + criteria.getDateDoc() + ")");
        if (criteria.getGl_JournalBatch_ID() > 0) temp.append(" AND (GL_JOURNALBATCH_ID=" + criteria.getGl_JournalBatch_ID() + ")");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_Recurring_RunDTO convertModelToDTO(X_C_Recurring_Run model) {
        C_Recurring_RunDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_Recurring_RunDTO();
            if ((obj = model.get_Value("C_INVOICE_ID")) != null) dto.setC_Invoice_ID((Integer) obj);
            if ((obj = model.get_Value("C_ORDER_ID")) != null) dto.setC_Order_ID((Integer) obj);
            if ((obj = model.get_Value("C_PAYMENT_ID")) != null) dto.setC_Payment_ID((Integer) obj);
            if ((obj = model.get_Value("C_PROJECT_ID")) != null) dto.setC_Project_ID((Integer) obj);
            if ((obj = model.get_Value("C_RECURRING_ID")) != null) dto.setC_Recurring_ID((Integer) obj);
            if ((obj = model.get_Value("C_RECURRING_RUN_ID")) != null) dto.setC_Recurring_Run_ID((Integer) obj);
            if ((obj = model.get_Value("DATEDOC")) != null) dto.setDateDoc((Timestamp) obj);
            if ((obj = model.get_Value("GL_JOURNALBATCH_ID")) != null) dto.setGl_JournalBatch_ID((Integer) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
