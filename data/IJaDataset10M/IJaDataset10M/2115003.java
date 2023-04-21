package org.koossery.adempiere.sisv.impl.org.cash;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_C_CashBook_Acct;
import org.koossery.adempiere.core.backend.interfaces.dao.org.cash.IC_CashBook_AcctDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.org.cash.IC_CashBook_AcctSISV;
import org.koossery.adempiere.core.contract.criteria.org.cash.C_CashBook_AcctCriteria;
import org.koossery.adempiere.core.contract.dto.org.cash.C_CashBook_AcctDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_CashBook_AcctSISVImpl extends AbstractCommonSISV implements IC_CashBook_AcctSISV, InitializingBean {

    private IC_CashBook_AcctDAO ccashbookacctDAOImpl;

    private static Logger logger = Logger.getLogger(C_CashBook_AcctSISVImpl.class);

    public C_CashBook_AcctSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.ccashbookacctDAOImpl = (IC_CashBook_AcctDAO) this.getDaoController().get("DAO/C_CashBook_Acct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_CashBook_Acct(Properties ctx, C_CashBook_AcctDTO c_CashBook_AcctDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_CashBook_Acct model = new X_C_CashBook_Acct(ctx, 0, trxname);
            C_CashBook_AcctCriteria criteria = new C_CashBook_AcctCriteria();
            if (ccashbookacctDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setC_AcctSchema_ID(c_CashBook_AcctDTO.getC_AcctSchema_ID());
                model.setC_CashBook_ID(c_CashBook_AcctDTO.getC_CashBook_ID());
                model.setCB_Asset_Acct(c_CashBook_AcctDTO.getCb_Asset_Acct());
                model.setCB_CashTransfer_Acct(c_CashBook_AcctDTO.getCb_CashTransfer_Acct());
                model.setCB_Differences_Acct(c_CashBook_AcctDTO.getCb_Differences_Acct());
                model.setCB_Expense_Acct(c_CashBook_AcctDTO.getCb_Expense_Acct());
                model.setCB_Receipt_Acct(c_CashBook_AcctDTO.getCb_Receipt_Acct());
                model.setIsActive(c_CashBook_AcctDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_CashBook_AcctDTO getC_CashBook_Acct(Properties ctx, int c_CashBookID, int c_AcctSchemaID, String trxname) throws KTAdempiereAppException {
        try {
            C_CashBook_AcctCriteria criteria = new C_CashBook_AcctCriteria();
            criteria.setC_CashBook_ID(c_CashBookID);
            criteria.setC_AcctSchema_ID(c_AcctSchemaID);
            ArrayList<C_CashBook_AcctDTO> list = new ArrayList<C_CashBook_AcctDTO>();
            list = ccashbookacctDAOImpl.getC_CashBook_Acct(criteria);
            return ((list != null) && (list.size() > 0)) ? list.get(0) : null;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_CashBook_AcctDTO> findC_CashBook_Acct(Properties ctx, C_CashBook_AcctCriteria c_CashBook_AcctCriteria) throws KTAdempiereAppException {
        try {
            ArrayList<C_CashBook_AcctDTO> list = new ArrayList<C_CashBook_AcctDTO>();
            list = ccashbookacctDAOImpl.getC_CashBook_Acct(c_CashBook_AcctCriteria);
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_CashBook_Acct(Properties ctx, C_CashBook_AcctDTO c_CashBook_AcctDTO) throws KTAdempiereAppException {
        try {
            C_CashBook_AcctCriteria criteria = new C_CashBook_AcctCriteria();
            criteria.setC_AcctSchema_ID(c_CashBook_AcctDTO.getC_AcctSchema_ID());
            criteria.setC_CashBook_ID(c_CashBook_AcctDTO.getC_CashBook_ID());
            criteria.setCb_Asset_Acct(c_CashBook_AcctDTO.getCb_Asset_Acct());
            criteria.setCb_CashTransfer_Acct(c_CashBook_AcctDTO.getCb_CashTransfer_Acct());
            criteria.setCb_Differences_Acct(c_CashBook_AcctDTO.getCb_Differences_Acct());
            criteria.setCb_Expense_Acct(c_CashBook_AcctDTO.getCb_Expense_Acct());
            criteria.setCb_Receipt_Acct(c_CashBook_AcctDTO.getCb_Receipt_Acct());
            if (ccashbookacctDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_000");
            ccashbookacctDAOImpl.update(criteria);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteC_CashBook_Acct(Properties ctx, C_CashBook_AcctCriteria c_CashBook_AcctCriteria) throws KTAdempiereAppException {
        try {
            X_C_CashBook_Acct model = new X_C_CashBook_Acct(ctx, 0, null);
            model.setC_CashBook_ID(c_CashBook_AcctCriteria.getC_CashBook_ID());
            model.setC_AcctSchema_ID(c_CashBook_AcctCriteria.getC_AcctSchema_ID());
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHBOOK_ACCT_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_CashBook_AcctCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getC_AcctSchema_ID() > 0) temp.append(" AND (C_ACCTSCHEMA_ID=" + criteria.getC_AcctSchema_ID() + ")");
        if (criteria.getC_CashBook_ID() > 0) temp.append(" AND (C_CASHBOOK_ID=" + criteria.getC_CashBook_ID() + ")");
        if (criteria.getCb_Asset_Acct() > 0) temp.append(" AND (CB_ASSET_ACCT=" + criteria.getCb_Asset_Acct() + ")");
        if (criteria.getCb_CashTransfer_Acct() > 0) temp.append(" AND (CB_CASHTRANSFER_ACCT=" + criteria.getCb_CashTransfer_Acct() + ")");
        if (criteria.getCb_Differences_Acct() > 0) temp.append(" AND (CB_DIFFERENCES_ACCT=" + criteria.getCb_Differences_Acct() + ")");
        if (criteria.getCb_Expense_Acct() > 0) temp.append(" AND (CB_EXPENSE_ACCT=" + criteria.getCb_Expense_Acct() + ")");
        if (criteria.getCb_Receipt_Acct() > 0) temp.append(" AND (CB_RECEIPT_ACCT=" + criteria.getCb_Receipt_Acct() + ")");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_CashBook_AcctDTO convertModelToDTO(X_C_CashBook_Acct model) {
        C_CashBook_AcctDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_CashBook_AcctDTO();
            if ((obj = model.get_Value("C_ACCTSCHEMA_ID")) != null) dto.setC_AcctSchema_ID((Integer) obj);
            if ((obj = model.get_Value("C_CASHBOOK_ID")) != null) dto.setC_CashBook_ID((Integer) obj);
            if ((obj = model.get_Value("CB_ASSET_ACCT")) != null) dto.setCb_Asset_Acct((Integer) obj);
            if ((obj = model.get_Value("CB_CASHTRANSFER_ACCT")) != null) dto.setCb_CashTransfer_Acct((Integer) obj);
            if ((obj = model.get_Value("CB_DIFFERENCES_ACCT")) != null) dto.setCb_Differences_Acct((Integer) obj);
            if ((obj = model.get_Value("CB_EXPENSE_ACCT")) != null) dto.setCb_Expense_Acct((Integer) obj);
            if ((obj = model.get_Value("CB_RECEIPT_ACCT")) != null) dto.setCb_Receipt_Acct((Integer) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
