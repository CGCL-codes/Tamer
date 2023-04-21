package org.koossery.adempiere.sisv.impl.org.cash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_C_CashLine;
import org.koossery.adempiere.core.backend.interfaces.dao.org.cash.IC_CashLineDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.org.cash.IC_CashLineSISV;
import org.koossery.adempiere.core.contract.criteria.org.cash.C_CashLineCriteria;
import org.koossery.adempiere.core.contract.dto.org.cash.C_CashLineDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_CashLineSISVImpl extends AbstractCommonSISV implements IC_CashLineSISV, InitializingBean {

    private IC_CashLineDAO ccashlineDAOImpl;

    private static Logger logger = Logger.getLogger(C_CashLineSISVImpl.class);

    public C_CashLineSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.ccashlineDAOImpl = (IC_CashLineDAO) this.getDaoController().get("DAO/C_CashLine");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_CashLine(Properties ctx, C_CashLineDTO c_CashLineDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_CashLine model = new X_C_CashLine(ctx, 0, trxname);
            C_CashLineCriteria criteria = new C_CashLineCriteria();
            criteria.setC_Cash_ID(c_CashLineDTO.getC_Cash_ID());
            if (ccashlineDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAmount(c_CashLineDTO.getAmount());
                model.setC_BankAccount_ID(c_CashLineDTO.getC_BankAccount_ID());
                model.setC_Cash_ID(c_CashLineDTO.getC_Cash_ID());
                model.setC_Charge_ID(c_CashLineDTO.getC_Charge_ID());
                model.setC_Currency_ID(c_CashLineDTO.getC_Currency_ID());
                model.setC_Invoice_ID(c_CashLineDTO.getC_Invoice_ID());
                model.setC_Payment_ID(c_CashLineDTO.getC_Payment_ID());
                model.setCashType(c_CashLineDTO.getCashType());
                model.setDescription(c_CashLineDTO.getDescription());
                model.setDiscountAmt(c_CashLineDTO.getDiscountAmt());
                model.setLine(c_CashLineDTO.getLine());
                model.setWriteOffAmt(c_CashLineDTO.getWriteOffAmt());
                model.setIsGenerated(c_CashLineDTO.getIsGenerated() == "Y" ? true : false);
                model.setProcessed(c_CashLineDTO.getIsProcessed() == "Y" ? true : false);
                model.setIsActive(c_CashLineDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_CashLineDTO.setC_CashLine_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_CashLineDTO getC_CashLine(Properties ctx, int c_CashLine_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (c_CashLine_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_002");
            X_C_CashLine mcashline = new X_C_CashLine(ctx, c_CashLine_ID, trxname);
            return convertModelToDTO(mcashline);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_CashLineDTO> findC_CashLine(Properties ctx, C_CashLineCriteria c_CashLineCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, c_CashLineCriteria);
            int id[] = X_C_CashLine.getAllIDs("C_CashLine", whereclause, null);
            ArrayList<C_CashLineDTO> list = new ArrayList<C_CashLineDTO>();
            C_CashLineDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getC_CashLine(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_CashLine(Properties ctx, C_CashLineDTO c_CashLineDTO) throws KTAdempiereAppException {
        try {
            X_C_CashLine model = new X_C_CashLine(ctx, c_CashLineDTO.getC_CashLine_ID(), null);
            if (model != null) {
                int oldCCashID = model.getC_Cash_ID();
                int newCCashID = c_CashLineDTO.getC_Cash_ID();
                if (oldCCashID != newCCashID) {
                    C_CashLineCriteria criteria = new C_CashLineCriteria();
                    criteria.setC_Cash_ID(newCCashID);
                    if (ccashlineDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setAmount(c_CashLineDTO.getAmount());
                model.setC_BankAccount_ID(c_CashLineDTO.getC_BankAccount_ID());
                model.setC_Cash_ID(c_CashLineDTO.getC_Cash_ID());
                model.setC_Charge_ID(c_CashLineDTO.getC_Charge_ID());
                model.setC_Currency_ID(c_CashLineDTO.getC_Currency_ID());
                model.setC_Invoice_ID(c_CashLineDTO.getC_Invoice_ID());
                model.setC_Payment_ID(c_CashLineDTO.getC_Payment_ID());
                model.setCashType(c_CashLineDTO.getCashType());
                model.setDescription(c_CashLineDTO.getDescription());
                model.setDiscountAmt(c_CashLineDTO.getDiscountAmt());
                model.setLine(c_CashLineDTO.getLine());
                model.setWriteOffAmt(c_CashLineDTO.getWriteOffAmt());
                model.setIsGenerated(c_CashLineDTO.getIsGenerated() == "Y" ? true : false);
                model.setProcessed(c_CashLineDTO.getIsProcessed() == "Y" ? true : false);
                model.setIsActive(c_CashLineDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_CashLineDTO.setC_CashLine_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteC_CashLine(Properties ctx, C_CashLineCriteria c_CashLineCriteria) throws KTAdempiereAppException {
        try {
            int id = c_CashLineCriteria.getC_CashLine_ID();
            X_C_CashLine model = new X_C_CashLine(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CASHLINE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_CashLineCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAmount() != null) temp.append(" AND (AMOUNT=" + criteria.getAmount() + ")");
        if (criteria.getC_BankAccount_ID() > 0) temp.append(" AND (C_BANKACCOUNT_ID=" + criteria.getC_BankAccount_ID() + ")");
        if (criteria.getC_Cash_ID() > 0) temp.append(" AND (C_CASH_ID=" + criteria.getC_Cash_ID() + ")");
        if (criteria.getC_CashLine_ID() > 0) temp.append(" AND (C_CASHLINE_ID=" + criteria.getC_CashLine_ID() + ")");
        if (criteria.getC_Charge_ID() > 0) temp.append(" AND (C_CHARGE_ID=" + criteria.getC_Charge_ID() + ")");
        if (criteria.getC_Currency_ID() > 0) temp.append(" AND (C_CURRENCY_ID=" + criteria.getC_Currency_ID() + ")");
        if (criteria.getC_Invoice_ID() > 0) temp.append(" AND (C_INVOICE_ID=" + criteria.getC_Invoice_ID() + ")");
        if (criteria.getC_Payment_ID() > 0) temp.append(" AND (C_PAYMENT_ID=" + criteria.getC_Payment_ID() + ")");
        if (criteria.getCashType() != null) temp.append(" AND ( CASHTYPE LIKE '%" + criteria.getCashType() + "%')");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getDiscountAmt() != null) temp.append(" AND (DISCOUNTAMT=" + criteria.getDiscountAmt() + ")");
        if (criteria.getLine() > 0) temp.append(" AND (LINE=" + criteria.getLine() + ")");
        if (criteria.getWriteOffAmt() != null) temp.append(" AND (WRITEOFFAMT=" + criteria.getWriteOffAmt() + ")");
        if (criteria.getIsGenerated() != null) temp.append(" AND (ISGENERATED='" + criteria.getIsGenerated() + "')");
        if (criteria.getIsProcessed() != null) temp.append(" AND (PROCESSED='" + criteria.getIsProcessed() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_CashLineDTO convertModelToDTO(X_C_CashLine model) {
        C_CashLineDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_CashLineDTO();
            if ((obj = model.get_Value("AMOUNT")) != null) dto.setAmount((BigDecimal) obj);
            if ((obj = model.get_Value("C_BANKACCOUNT_ID")) != null) dto.setC_BankAccount_ID((Integer) obj);
            if ((obj = model.get_Value("C_CASH_ID")) != null) dto.setC_Cash_ID((Integer) obj);
            if ((obj = model.get_Value("C_CASHLINE_ID")) != null) dto.setC_CashLine_ID((Integer) obj);
            if ((obj = model.get_Value("C_CHARGE_ID")) != null) dto.setC_Charge_ID((Integer) obj);
            if ((obj = model.get_Value("C_CURRENCY_ID")) != null) dto.setC_Currency_ID((Integer) obj);
            if ((obj = model.get_Value("C_INVOICE_ID")) != null) dto.setC_Invoice_ID((Integer) obj);
            if ((obj = model.get_Value("C_PAYMENT_ID")) != null) dto.setC_Payment_ID((Integer) obj);
            if ((obj = model.get_Value("CASHTYPE")) != null) dto.setCashType((String) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("DISCOUNTAMT")) != null) dto.setDiscountAmt((BigDecimal) obj);
            if ((obj = model.get_Value("LINE")) != null) dto.setLine((Integer) obj);
            if ((obj = model.get_Value("WRITEOFFAMT")) != null) dto.setWriteOffAmt((BigDecimal) obj);
            if ((obj = model.get_Value("ISGENERATED")) != null) dto.setIsGenerated(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("PROCESSED")) != null) dto.setIsProcessed(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
