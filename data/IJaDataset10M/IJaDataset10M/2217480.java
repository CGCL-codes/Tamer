package org.koossery.adempiere.sisv.impl.bank;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.X_C_BankStatementLine;
import org.koossery.adempiere.core.backend.interfaces.dao.bank.IC_BankStatementLineDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.bank.IC_BankStatementLineSISV;
import org.koossery.adempiere.core.contract.criteria.bank.C_BankStatementLineCriteria;
import org.koossery.adempiere.core.contract.dto.bank.C_BankStatementLineDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_BankStatementLineSISVImpl extends AbstractCommonSISV implements IC_BankStatementLineSISV, InitializingBean {

    private IC_BankStatementLineDAO cbankstatementlineDAOImpl;

    private static Logger logger = Logger.getLogger(C_BankStatementLineSISVImpl.class);

    public C_BankStatementLineSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.cbankstatementlineDAOImpl = (IC_BankStatementLineDAO) this.getDaoController().get("DAO/C_BankStatementLine");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_BankStatementLine(Properties ctx, C_BankStatementLineDTO c_BankStatementLineDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_BankStatementLine model = new X_C_BankStatementLine(ctx, 0, trxname);
            if (cbankstatementlineDAOImpl.isDuplicate(c_BankStatementLineDTO.getLine())) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setC_BankStatement_ID(c_BankStatementLineDTO.getC_BankStatement_ID());
                model.setC_BPartner_ID(c_BankStatementLineDTO.getC_BPartner_ID());
                model.setC_Charge_ID(c_BankStatementLineDTO.getC_Charge_ID());
                model.setC_Currency_ID(c_BankStatementLineDTO.getC_Currency_ID());
                model.setC_Invoice_ID(c_BankStatementLineDTO.getC_Invoice_ID());
                model.setC_Payment_ID(c_BankStatementLineDTO.getC_Payment_ID());
                model.setChargeAmt(c_BankStatementLineDTO.getChargeAmt());
                model.setCreatePayment(c_BankStatementLineDTO.getCreatePayment());
                model.setDateAcct(c_BankStatementLineDTO.getDateAcct());
                model.setDescription(c_BankStatementLineDTO.getDescription());
                model.setEftAmt(c_BankStatementLineDTO.getEftAmt());
                model.setEftCheckNo(c_BankStatementLineDTO.getEftCheckNo());
                model.setEftCurrency(c_BankStatementLineDTO.getEftCurrency());
                model.setEftMemo(c_BankStatementLineDTO.getEftMemo());
                model.setEftPayee(c_BankStatementLineDTO.getEftPayee());
                model.setEftPayeeAccount(c_BankStatementLineDTO.getEftPayeeAccount());
                model.setEftReference(c_BankStatementLineDTO.getEftReference());
                model.setEftStatementLineDate(c_BankStatementLineDTO.getEftStatementLineDate());
                model.setEftTrxID(c_BankStatementLineDTO.getEftTrxID());
                model.setEftTrxType(c_BankStatementLineDTO.getEftTrxType());
                model.setEftValutaDate(c_BankStatementLineDTO.getEftValutaDate());
                model.setInterestAmt(c_BankStatementLineDTO.getInterestAmt());
                model.setLine(c_BankStatementLineDTO.getLine());
                model.setMatchStatement(c_BankStatementLineDTO.getMatchStatement());
                model.setMemo(c_BankStatementLineDTO.getMemo());
                model.setReferenceNo(c_BankStatementLineDTO.getReferenceNo());
                model.setStatementLineDate(c_BankStatementLineDTO.getStatementLineDate());
                model.setStmtAmt(c_BankStatementLineDTO.getStmtAmt());
                model.setTrxAmt(c_BankStatementLineDTO.getTrxAmt());
                model.setValutaDate(c_BankStatementLineDTO.getValutaDate());
                model.setIsManual(c_BankStatementLineDTO.getIsManual() == "Y" ? true : false);
                model.setIsReversal(c_BankStatementLineDTO.getIsReversal() == "Y" ? true : false);
                model.setIsActive(c_BankStatementLineDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_BankStatementLineDTO.setC_BankStatementLine_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_BankStatementLineDTO getC_BankStatementLine(Properties ctx, int c_BankStatementLine_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (c_BankStatementLine_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_002");
            MBankStatementLine mbankstatementline = new MBankStatementLine(ctx, c_BankStatementLine_ID, trxname);
            return convertModelToDTO(mbankstatementline);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_BankStatementLineDTO> findC_BankStatementLine(Properties ctx, C_BankStatementLineCriteria c_BankStatementLineCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, c_BankStatementLineCriteria);
            int id[] = MBankStatementLine.getAllIDs("C_BankStatementLine", whereclause, null);
            ArrayList<C_BankStatementLineDTO> list = new ArrayList<C_BankStatementLineDTO>();
            C_BankStatementLineDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getC_BankStatementLine(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_BankStatementLine(Properties ctx, C_BankStatementLineDTO c_BankStatementLineDTO) throws KTAdempiereAppException {
        try {
            X_C_BankStatementLine model = new X_C_BankStatementLine(ctx, c_BankStatementLineDTO.getC_BankStatementLine_ID(), null);
            if (model != null) {
                int oldname = model.getLine();
                int newname = c_BankStatementLineDTO.getLine();
                if (oldname != newname) {
                    if (cbankstatementlineDAOImpl.isDuplicate(newname)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_000");
                } else {
                    System.out.println("NO duplicates names for modification");
                    model.setC_BankStatement_ID(c_BankStatementLineDTO.getC_BankStatement_ID());
                    model.setC_BPartner_ID(c_BankStatementLineDTO.getC_BPartner_ID());
                    model.setC_Charge_ID(c_BankStatementLineDTO.getC_Charge_ID());
                    model.setC_Currency_ID(c_BankStatementLineDTO.getC_Currency_ID());
                    model.setC_Invoice_ID(c_BankStatementLineDTO.getC_Invoice_ID());
                    model.setC_Payment_ID(c_BankStatementLineDTO.getC_Payment_ID());
                    model.setChargeAmt(c_BankStatementLineDTO.getChargeAmt());
                    model.setCreatePayment(c_BankStatementLineDTO.getCreatePayment());
                    model.setDateAcct(c_BankStatementLineDTO.getDateAcct());
                    model.setDescription(c_BankStatementLineDTO.getDescription());
                    model.setEftAmt(c_BankStatementLineDTO.getEftAmt());
                    model.setEftCheckNo(c_BankStatementLineDTO.getEftCheckNo());
                    model.setEftCurrency(c_BankStatementLineDTO.getEftCurrency());
                    model.setEftMemo(c_BankStatementLineDTO.getEftMemo());
                    model.setEftPayee(c_BankStatementLineDTO.getEftPayee());
                    model.setEftPayeeAccount(c_BankStatementLineDTO.getEftPayeeAccount());
                    model.setEftReference(c_BankStatementLineDTO.getEftReference());
                    model.setEftStatementLineDate(c_BankStatementLineDTO.getEftStatementLineDate());
                    model.setEftTrxID(c_BankStatementLineDTO.getEftTrxID());
                    model.setEftTrxType(c_BankStatementLineDTO.getEftTrxType());
                    model.setEftValutaDate(c_BankStatementLineDTO.getEftValutaDate());
                    model.setInterestAmt(c_BankStatementLineDTO.getInterestAmt());
                    model.setLine(c_BankStatementLineDTO.getLine());
                    model.setMatchStatement(c_BankStatementLineDTO.getMatchStatement());
                    model.setMemo(c_BankStatementLineDTO.getMemo());
                    model.setReferenceNo(c_BankStatementLineDTO.getReferenceNo());
                    model.setStatementLineDate(c_BankStatementLineDTO.getStatementLineDate());
                    model.setStmtAmt(c_BankStatementLineDTO.getStmtAmt());
                    model.setTrxAmt(c_BankStatementLineDTO.getTrxAmt());
                    model.setValutaDate(c_BankStatementLineDTO.getValutaDate());
                    model.setIsManual(c_BankStatementLineDTO.getIsManual() == "Y" ? true : false);
                    model.setIsReversal(c_BankStatementLineDTO.getIsReversal() == "Y" ? true : false);
                    model.setIsActive(c_BankStatementLineDTO.getIsActive() == "Y" ? true : false);
                    this.setPO(model);
                    this.save();
                    c_BankStatementLineDTO.setC_BankStatementLine_ID(model.get_ID());
                }
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean updateC_BankStatementLine(C_BankStatementLineCriteria c_BankStatementLineCriteria) throws KTAdempiereAppException {
        try {
            return cbankstatementlineDAOImpl.update(c_BankStatementLineCriteria);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_BANKSTATEMENTLINE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_BankStatementLineCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getC_BankStatement_ID() > 0) temp.append(" AND (C_BANKSTATEMENT_ID=" + criteria.getC_BankStatement_ID() + ")");
        if (criteria.getC_BankStatementLine_ID() > 0) temp.append(" AND (C_BANKSTATEMENTLINE_ID=" + criteria.getC_BankStatementLine_ID() + ")");
        if (criteria.getC_BPartner_ID() > 0) temp.append(" AND (C_BPARTNER_ID=" + criteria.getC_BPartner_ID() + ")");
        if (criteria.getC_Charge_ID() > 0) temp.append(" AND (C_CHARGE_ID=" + criteria.getC_Charge_ID() + ")");
        if (criteria.getC_Currency_ID() > 0) temp.append(" AND (C_CURRENCY_ID=" + criteria.getC_Currency_ID() + ")");
        if (criteria.getC_Invoice_ID() > 0) temp.append(" AND (C_INVOICE_ID=" + criteria.getC_Invoice_ID() + ")");
        if (criteria.getC_Payment_ID() > 0) temp.append(" AND (C_PAYMENT_ID=" + criteria.getC_Payment_ID() + ")");
        if (criteria.getChargeAmt() != null) temp.append(" AND (CHARGEAMT=" + criteria.getChargeAmt() + ")");
        if (criteria.getCreatePayment() != null) temp.append(" AND ( CREATEPAYMENT LIKE '%" + criteria.getCreatePayment() + "%')");
        if (criteria.getDateAcct() != null) temp.append(" AND (DATEACCT=" + criteria.getDateAcct() + ")");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getEftAmt() != null) temp.append(" AND (EFTAMT=" + criteria.getEftAmt() + ")");
        if (criteria.getEftCheckNo() != null) temp.append(" AND ( EFTCHECKNO LIKE '%" + criteria.getEftCheckNo() + "%')");
        if (criteria.getEftCurrency() != null) temp.append(" AND ( EFTCURRENCY LIKE '%" + criteria.getEftCurrency() + "%')");
        if (criteria.getEftMemo() != null) temp.append(" AND ( EFTMEMO LIKE '%" + criteria.getEftMemo() + "%')");
        if (criteria.getEftPayee() != null) temp.append(" AND ( EFTPAYEE LIKE '%" + criteria.getEftPayee() + "%')");
        if (criteria.getEftPayeeAccount() != null) temp.append(" AND ( EFTPAYEEACCOUNT LIKE '%" + criteria.getEftPayeeAccount() + "%')");
        if (criteria.getEftReference() != null) temp.append(" AND ( EFTREFERENCE LIKE '%" + criteria.getEftReference() + "%')");
        if (criteria.getEftStatementLineDate() != null) temp.append(" AND (EFTSTATEMENTLINEDATE=" + criteria.getEftStatementLineDate() + ")");
        if (criteria.getEftTrxID() != null) temp.append(" AND ( EFTTRXID LIKE '%" + criteria.getEftTrxID() + "%')");
        if (criteria.getEftTrxType() != null) temp.append(" AND ( EFTTRXTYPE LIKE '%" + criteria.getEftTrxType() + "%')");
        if (criteria.getEftValutaDate() != null) temp.append(" AND (EFTVALUTADATE=" + criteria.getEftValutaDate() + ")");
        if (criteria.getInterestAmt() != null) temp.append(" AND (INTERESTAMT=" + criteria.getInterestAmt() + ")");
        if (criteria.getLine() > 0) temp.append(" AND (LINE=" + criteria.getLine() + ")");
        if (criteria.getMatchStatement() != null) temp.append(" AND ( MATCHSTATEMENT LIKE '%" + criteria.getMatchStatement() + "%')");
        if (criteria.getMemo() != null) temp.append(" AND ( MEMO LIKE '%" + criteria.getMemo() + "%')");
        if (criteria.getReferenceNo() != null) temp.append(" AND ( REFERENCENO LIKE '%" + criteria.getReferenceNo() + "%')");
        if (criteria.getStatementLineDate() != null) temp.append(" AND (STATEMENTLINEDATE=" + criteria.getStatementLineDate() + ")");
        if (criteria.getStmtAmt() != null) temp.append(" AND (STMTAMT=" + criteria.getStmtAmt() + ")");
        if (criteria.getTrxAmt() != null) temp.append(" AND (TRXAMT=" + criteria.getTrxAmt() + ")");
        if (criteria.getValutaDate() != null) temp.append(" AND (VALUTADATE=" + criteria.getValutaDate() + ")");
        if (criteria.getIsManual() != null) temp.append(" AND (ISMANUAL='" + criteria.getIsManual() + "')");
        if (criteria.getIsProcessed() != null) temp.append(" AND (ISPROCESSED='" + criteria.getIsProcessed() + "')");
        if (criteria.getIsReversal() != null) temp.append(" AND (ISREVERSAL='" + criteria.getIsReversal() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_BankStatementLineDTO convertModelToDTO(X_C_BankStatementLine model) {
        C_BankStatementLineDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_BankStatementLineDTO();
            if ((obj = model.get_Value("C_BANKSTATEMENT_ID")) != null) dto.setC_BankStatement_ID((Integer) obj);
            if ((obj = model.get_Value("C_BANKSTATEMENTLINE_ID")) != null) dto.setC_BankStatementLine_ID((Integer) obj);
            if ((obj = model.get_Value("C_BPARTNER_ID")) != null) dto.setC_BPartner_ID((Integer) obj);
            if ((obj = model.get_Value("C_CHARGE_ID")) != null) dto.setC_Charge_ID((Integer) obj);
            if ((obj = model.get_Value("C_CURRENCY_ID")) != null) dto.setC_Currency_ID((Integer) obj);
            if ((obj = model.get_Value("C_INVOICE_ID")) != null) dto.setC_Invoice_ID((Integer) obj);
            if ((obj = model.get_Value("C_PAYMENT_ID")) != null) dto.setC_Payment_ID((Integer) obj);
            if ((obj = model.get_Value("CHARGEAMT")) != null) dto.setChargeAmt((BigDecimal) obj);
            if ((obj = model.get_Value("CREATEPAYMENT")) != null) dto.setCreatePayment((String) obj);
            if ((obj = model.get_Value("DATEACCT")) != null) dto.setDateAcct((Timestamp) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("EFTAMT")) != null) dto.setEftAmt((BigDecimal) obj);
            if ((obj = model.get_Value("EFTCHECKNO")) != null) dto.setEftCheckNo((String) obj);
            if ((obj = model.get_Value("EFTCURRENCY")) != null) dto.setEftCurrency((String) obj);
            if ((obj = model.get_Value("EFTMEMO")) != null) dto.setEftMemo((String) obj);
            if ((obj = model.get_Value("EFTPAYEE")) != null) dto.setEftPayee((String) obj);
            if ((obj = model.get_Value("EFTPAYEEACCOUNT")) != null) dto.setEftPayeeAccount((String) obj);
            if ((obj = model.get_Value("EFTREFERENCE")) != null) dto.setEftReference((String) obj);
            if ((obj = model.get_Value("EFTSTATEMENTLINEDATE")) != null) dto.setEftStatementLineDate((Timestamp) obj);
            if ((obj = model.get_Value("EFTTRXID")) != null) dto.setEftTrxID((String) obj);
            if ((obj = model.get_Value("EFTTRXTYPE")) != null) dto.setEftTrxType((String) obj);
            if ((obj = model.get_Value("EFTVALUTADATE")) != null) dto.setEftValutaDate((Timestamp) obj);
            if ((obj = model.get_Value("INTERESTAMT")) != null) dto.setInterestAmt((BigDecimal) obj);
            if ((obj = model.get_Value("LINE")) != null) dto.setLine((Integer) obj);
            if ((obj = model.get_Value("MATCHSTATEMENT")) != null) dto.setMatchStatement((String) obj);
            if ((obj = model.get_Value("MEMO")) != null) dto.setMemo((String) obj);
            if ((obj = model.get_Value("REFERENCENO")) != null) dto.setReferenceNo((String) obj);
            if ((obj = model.get_Value("STATEMENTLINEDATE")) != null) dto.setStatementLineDate((Timestamp) obj);
            if ((obj = model.get_Value("STMTAMT")) != null) dto.setStmtAmt((BigDecimal) obj);
            if ((obj = model.get_Value("TRXAMT")) != null) dto.setTrxAmt((BigDecimal) obj);
            if ((obj = model.get_Value("VALUTADATE")) != null) dto.setValutaDate((Timestamp) obj);
            if ((obj = model.get_Value("ISMANUAL")) != null) dto.setIsManual(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISPROCESSED")) != null) dto.setIsProcessed(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISREVERSAL")) != null) dto.setIsReversal(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
        }
        return dto;
    }
}
