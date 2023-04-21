package org.adempierelbr.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.script.ScriptException;
import org.adempierelbr.model.MLBRNCMIVA;
import org.adempierelbr.model.MLBRTax;
import org.adempierelbr.model.X_LBR_TaxFormula;
import org.adempierelbr.model.X_LBR_TaxLine;
import org.adempierelbr.model.X_LBR_TaxName;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MProduct;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import bsh.EvalError;
import bsh.Interpreter;

/**
 * TaxBR
 *
 * TaxBR Utils
 *
 * @author Mario Grigioni
 * @contributor Fernando Lucktemberg (Faire, www.faire.com.br)
 * @contributor Fernando O. Moraes (Faire, www.faire.com.br)
 * @version $Id: TextBR.java, 26/01/2010 09:10:00 mgrigioni
 */
public abstract class TaxBR {

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(TaxBR.class);

    /** Scale           */
    public static final int SCALE = 2;

    public static final int ROUND_SCALE = 12;

    public static final RoundingMode ROUND = RoundingMode.HALF_UP;

    public static final MathContext MCROUND = new MathContext(ROUND_SCALE, ROUND);

    /** Produto */
    public static String taxType_Product = X_LBR_TaxName.LBR_TAXTYPE_Product;

    /** Serviço */
    public static String taxType_Service = X_LBR_TaxName.LBR_TAXTYPE_Service;

    /** Substituição Tributária */
    public static String taxType_Substitution = X_LBR_TaxName.LBR_TAXTYPE_Substitution;

    /**************************************************************************
	 *  calculateTaxes
	 *
	 *  Method that calculate Brazilian Taxes using Groovy
	 *
	 *  @param Integer Line_ID (C_OrderLine_ID, C_InvoiceLine_ID)
	 *  @param boolean isOrder (defines if this is an Order or Invoice)
	 *  @param String trx
	 *  @throws ScriptException
	 */
    public static void calculateTaxes(int Line_ID, boolean isOrder, String trx) throws EvalError {
        Properties ctx = Env.getCtx();
        X_LBR_TaxName[] taxesName = ImpostoBR.getLBR_TaxName(ctx, Line_ID, isOrder, trx);
        Map<String, ImpostoBR> lines = new HashMap<String, ImpostoBR>();
        lines = ImpostoBR.getImpostoBR(Line_ID, isOrder, trx);
        MProduct product = null;
        boolean isTaxIncluded = false;
        BigDecimal lineamt = Env.ZERO;
        BigDecimal ivaPrct = Env.ZERO;
        if (isOrder) {
            MOrderLine oLine = new MOrderLine(ctx, Line_ID, trx);
            MOrder order = new MOrder(ctx, oLine.getC_Order_ID(), trx);
            product = oLine.getProduct();
            isTaxIncluded = order.isTaxIncluded();
            lineamt = oLine.getPriceEntered().multiply(oLine.getQtyEntered());
            if (product != null) {
                ivaPrct = MLBRNCMIVA.getProfitPercentage(ctx, product.get_ValueAsInt("LBR_NCM_ID"), order.getAD_Org_ID(), order.getBill_Location(), order.isSOTrx());
            }
        } else {
            MInvoiceLine iLine = new MInvoiceLine(ctx, Line_ID, trx);
            MInvoice invoice = new MInvoice(ctx, iLine.getC_Invoice_ID(), trx);
            product = iLine.getProduct();
            isTaxIncluded = invoice.isTaxIncluded();
            lineamt = iLine.getPriceEntered().multiply(iLine.getQtyEntered());
            if (product != null) {
                ivaPrct = MLBRNCMIVA.getProfitPercentage(ctx, product.get_ValueAsInt("LBR_NCM_ID"), invoice.getAD_Org_ID(), invoice.getC_BPartner_Location(), invoice.isSOTrx());
            }
        }
        for (X_LBR_TaxName taxName : taxesName) {
            String name = taxName.getName().trim();
            ImpostoBR taxBR = lines.get(name);
            ImpostoBR s_taxBR = null;
            if (taxName.getlbr_TaxType().equals(TaxBR.taxType_Substitution)) {
                X_LBR_TaxName s_taxName = new X_LBR_TaxName(ctx, taxName.getLBR_TaxSubstitution_ID(), trx);
                String s_name = s_taxName.getName().trim();
                s_taxBR = lines.get(s_name);
                calculateTaxAmt(s_taxBR, null, product, s_taxName, lines, isTaxIncluded, lineamt, ivaPrct, trx);
                calculateTaxAmt(taxBR, s_taxBR, product, taxName, lines, isTaxIncluded, lineamt, ivaPrct, trx);
            } else calculateTaxAmt(taxBR, s_taxBR, product, taxName, lines, isTaxIncluded, lineamt, ivaPrct, trx);
        }
    }

    private static void calculateTaxAmt(ImpostoBR taxBR, ImpostoBR s_taxBR, MProduct product, X_LBR_TaxName taxName, Map<String, ImpostoBR> lines, boolean isTaxIncluded, BigDecimal lineamt, BigDecimal ivaPrct, String trx) throws EvalError {
        if (taxBR == null) return;
        BigDecimal amt = Env.ZERO;
        BigDecimal factor = Env.ZERO;
        BigDecimal substamt = Env.ZERO;
        if (taxName.getlbr_TaxType().equalsIgnoreCase(TaxBR.taxType_Service)) factor = calculateTaxBase(taxBR.getServiceFactor(), lineamt, factor, lines);
        if (isTaxIncluded) {
            amt = calculateTaxBase(taxBR.getFormulaNetWorth(), lineamt, factor, lines);
        } else {
            amt = lineamt;
        }
        if (taxName.getlbr_TaxType().equalsIgnoreCase(TaxBR.taxType_Substitution)) {
            if (product != null) {
                BigDecimal subsBaseAmt = Env.ZERO;
                if (s_taxBR != null) {
                    X_LBR_TaxLine s_taxLine = new X_LBR_TaxLine(Env.getCtx(), s_taxBR.getLBR_TaxLine_ID(), trx);
                    substamt = s_taxLine.getlbr_TaxAmt().setScale(SCALE, ROUND);
                    subsBaseAmt = s_taxLine.getlbr_TaxBaseAmt().setScale(SCALE, ROUND);
                }
                if (!taxBR.getTransactionType().equals(X_LBR_TaxFormula.LBR_TRANSACTIONTYPE_Resale)) ivaPrct = Env.ZERO;
                if (isTaxIncluded) {
                    lineamt = lineamt.multiply((Env.ONE.add((ivaPrct.divide(Env.ONEHUNDRED, 12, RoundingMode.HALF_UP)))));
                    amt = calculateTaxBase(taxBR.getFormulaNetWorth(), lineamt, factor, lines);
                } else {
                    if (ivaPrct.signum() == 0) amt = lineamt; else {
                        lineamt = subsBaseAmt;
                        X_LBR_TaxLine ipiLine = taxBR.getLBR_Tax().getIPILine();
                        if (ipiLine != null) {
                            BigDecimal rateIPI = ipiLine.getlbr_TaxRate();
                            BigDecimal baseIPI = Env.ONE.subtract((ipiLine.getlbr_TaxBase().divide(Env.ONEHUNDRED, MCROUND)));
                            BigDecimal ipiAmt = lineamt.multiply(rateIPI.divide(Env.ONEHUNDRED, MCROUND));
                            ipiAmt = ipiAmt.multiply(baseIPI).stripTrailingZeros();
                            lineamt = lineamt.add(ipiAmt);
                        }
                        lineamt = lineamt.multiply((Env.ONE.add((ivaPrct.divide(Env.ONEHUNDRED, 12, RoundingMode.HALF_UP)))));
                        amt = calculateTaxBase(taxBR.getFormulaNetWorth(), lineamt, factor, lines);
                    }
                }
            }
        }
        BigDecimal taxbase = calculateTaxBase(taxBR.getFormula(), amt, factor, lines);
        taxbase = taxbase.multiply(taxBR.getTaxBase());
        BigDecimal taxamt = taxbase.multiply(taxBR.getTaxRate());
        if (taxBR.getTaxRate().signum() != 1) taxbase = Env.ZERO;
        if (substamt.signum() != 0) {
            taxamt = taxamt.subtract(substamt);
        }
        X_LBR_TaxLine taxLine = new X_LBR_TaxLine(Env.getCtx(), taxBR.getLBR_TaxLine_ID(), trx);
        taxLine.setlbr_TaxAmt(taxamt.stripTrailingZeros());
        taxLine.setlbr_TaxBaseAmt(taxbase.stripTrailingZeros());
        if (taxLine.getLBR_Tax_ID() != 0) taxLine.save(trx);
    }

    public static BigDecimal getTaxAmt(GridTab mTab, String trxType, boolean isTaxIncluded) throws EvalError {
        BigDecimal amt = Env.ZERO;
        BigDecimal rateIPI = Env.ZERO;
        BigDecimal baseIPI = Env.ZERO;
        BigDecimal factor = Env.ZERO;
        BigDecimal lineamt = Env.ZERO;
        BigDecimal totalamt = Env.ZERO;
        BigDecimal substamt = Env.ZERO;
        Properties ctx = Env.getCtx();
        if (!isTaxIncluded) lineamt = (BigDecimal) mTab.getValue("PriceEntered"); else lineamt = (BigDecimal) mTab.getValue("lbr_PriceEnteredBR");
        if (mTab.getValue("LBR_Tax_ID") == null || mTab.getValue("C_Tax_ID") == null) return Env.ZERO;
        Integer C_Tax_ID = (Integer) mTab.getValue("C_Tax_ID");
        if (C_Tax_ID == null) C_Tax_ID = 0;
        Integer LBR_Tax_ID = 0;
        if (mTab.getValue("LBR_Tax_ID") instanceof Integer) {
            LBR_Tax_ID = (Integer) mTab.getValue("LBR_Tax_ID");
        }
        MLBRTax tx = new MLBRTax(ctx, LBR_Tax_ID, null);
        X_LBR_TaxLine[] txLines = tx.getLines();
        Map<String, ImpostoBR> lines = new HashMap<String, ImpostoBR>();
        lines = ImpostoBR.getImpostoBR(C_Tax_ID, LBR_Tax_ID, trxType, null);
        if (isTaxIncluded) {
            for (X_LBR_TaxLine txLine : txLines) {
                X_LBR_TaxName taxName = new X_LBR_TaxName(ctx, txLine.getLBR_TaxName_ID(), null);
                if (taxName.getName().trim().indexOf("IPI") != -1) {
                    rateIPI = txLine.getlbr_TaxRate();
                    baseIPI = Env.ONE.subtract((txLine.getlbr_TaxBase().divide(Env.ONEHUNDRED, MCROUND)));
                    BigDecimal ipiAmt = lineamt.multiply(rateIPI.divide(Env.ONEHUNDRED, MCROUND));
                    ipiAmt = ipiAmt.multiply(baseIPI).stripTrailingZeros();
                    lineamt = lineamt.add(ipiAmt);
                }
            }
        }
        for (X_LBR_TaxLine txLine : txLines) {
            X_LBR_TaxName taxName = new X_LBR_TaxName(ctx, txLine.getLBR_TaxName_ID(), null);
            ImpostoBR taxBR = lines.get(taxName.getName().trim());
            if (taxBR == null) continue;
            if (taxName.getName().trim().indexOf("IPI") != -1 || taxName.isHasWithHold() || taxName.getlbr_TaxType().equals(X_LBR_TaxName.LBR_TAXTYPE_Substitution)) continue;
            if (taxName.getlbr_TaxType().equalsIgnoreCase(TaxBR.taxType_Service)) factor = calculateTaxBase(taxBR.getServiceFactor(), lineamt, factor, lines);
            if (isTaxIncluded) {
                amt = calculateTaxBase(taxBR.getFormulaNetWorth(), lineamt, factor, lines);
            } else {
                amt = lineamt;
            }
            BigDecimal taxbase = calculateTaxBase(taxBR.getFormula(), amt, factor, lines);
            taxbase = taxbase.multiply(taxBR.getTaxBase());
            BigDecimal taxamt = taxbase.multiply(taxBR.getTaxRate());
            if (substamt.signum() != 0) {
                taxamt = taxamt.subtract(substamt);
            }
            totalamt = totalamt.add(taxamt);
        }
        return totalamt.stripTrailingZeros();
    }

    private static BigDecimal calculateTaxBase(String formula, BigDecimal amt, BigDecimal factor, Map<String, ImpostoBR> lines) throws EvalError {
        if (formula == null || formula.equals("")) {
            return Env.ZERO;
        }
        String[] variables = parseVariable(formula);
        Interpreter engine = new Interpreter();
        for (String variable : variables) {
            if (variable.equals("AMT")) {
                log.info("Amt = " + amt);
                engine.set(variable, amt.doubleValue());
            } else if (variable.equals("FACTOR")) {
                log.info("Factor = " + factor);
                engine.set(variable, factor.doubleValue());
            } else {
                ImpostoBR temptaxBR = lines.get(variable);
                if (temptaxBR != null) {
                    log.info(variable + " = " + temptaxBR.getTaxRate());
                    engine.set(variable, (temptaxBR.getTaxRate().multiply(temptaxBR.getTaxBase())).doubleValue());
                } else {
                    log.info(variable + " = " + 0);
                    engine.set(variable, 0.0);
                }
            }
        }
        formula = formula.replaceAll("@", "");
        log.info(formula);
        if (amt.signum() == 0) {
            return Env.ZERO;
        }
        BigDecimal base = new BigDecimal(((Double) engine.eval(formula)).toString());
        return base.stripTrailingZeros();
    }

    /**
	 * getTotalLinesAmt
	 * @param ID
	 * @param isOrder
	 * @param trx
	 * @return BigDecimal TotalLinesAmt
	 */
    public static BigDecimal getTotalLinesAmt(int ID, boolean isOrder, String trx) {
        String sql = "SELECT SUM(QtyEntered * PriceEntered) ";
        if (isOrder) {
            sql += "FROM C_OrderLine " + "WHERE C_Order_ID = ?";
        } else {
            sql += "FROM C_InvoiceLine " + "WHERE C_Invoice_ID = ?";
        }
        BigDecimal TotalLinesAmt = DB.getSQLValueBD(trx, sql, ID);
        if (TotalLinesAmt == null) TotalLinesAmt = Env.ZERO;
        return TotalLinesAmt.setScale(SCALE, ROUND);
    }

    public static MOrderTax getMOrderTax(Properties ctx, int C_Order_ID, int C_Tax_ID, String trx) {
        MOrderTax retValue = null;
        String sql = "SELECT * FROM C_OrderTax WHERE C_Order_ID=? AND C_Tax_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, trx);
            pstmt.setInt(1, C_Order_ID);
            pstmt.setInt(2, C_Tax_ID);
            rs = pstmt.executeQuery();
            if (rs.next()) retValue = new MOrderTax(ctx, rs, trx);
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
        }
        if (retValue == null && C_Tax_ID > 0) {
            retValue = new MOrderTax(ctx, 0, trx);
            retValue.setC_Order_ID(C_Order_ID);
            retValue.setC_Tax_ID(C_Tax_ID);
        }
        return retValue;
    }

    public static BigDecimal getMOrderTaxAmt(Integer C_Order_ID, String trx) {
        if (C_Order_ID == null) return Env.ZERO;
        String sql = "SELECT SUM(TaxAmt) FROM C_OrderTax WHERE C_Order_ID=?";
        BigDecimal retValue = DB.getSQLValueBD(trx, sql, C_Order_ID);
        return retValue != null ? retValue : Env.ZERO;
    }

    public static MInvoiceTax getMInvoiceTax(Properties ctx, int C_Invoice_ID, int C_Tax_ID, String trx) {
        MInvoiceTax retValue = null;
        String sql = "SELECT * FROM C_InvoiceTax WHERE C_Invoice_ID=? AND C_Tax_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, trx);
            pstmt.setInt(1, C_Invoice_ID);
            pstmt.setInt(2, C_Tax_ID);
            rs = pstmt.executeQuery();
            if (rs.next()) retValue = new MInvoiceTax(ctx, rs, trx);
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
        }
        if (retValue == null && C_Tax_ID > 0) {
            retValue = new MInvoiceTax(ctx, 0, trx);
            retValue.setC_Invoice_ID(C_Invoice_ID);
            retValue.setC_Tax_ID(C_Tax_ID);
        }
        return retValue;
    }

    public static BigDecimal getMInvoiceTaxAmt(Integer C_Invoice_ID, String trx) {
        if (C_Invoice_ID == null) return Env.ZERO;
        String sql = "SELECT SUM(TaxAmt) FROM C_InvoiceTax WHERE C_Invoice_ID=?";
        BigDecimal retValue = DB.getSQLValueBD(trx, sql, C_Invoice_ID);
        return retValue != null ? retValue : Env.ZERO;
    }

    private static String[] parseVariable(String formula) {
        ArrayList<String> taxes = new ArrayList<String>();
        int pos = 0;
        while (pos < formula.length()) {
            int first = formula.indexOf('@', pos);
            if (first != -1) {
                int second = formula.indexOf('@', first + 1);
                if (second != -1) {
                    String tax = (formula.substring(first + 1, second)).toUpperCase();
                    if (!taxes.contains(tax)) {
                        taxes.add(tax);
                    }
                    pos = second + 1;
                } else {
                    pos = formula.length();
                }
            } else {
                pos = formula.length();
            }
        }
        String[] retValue = new String[taxes.size()];
        taxes.toArray(retValue);
        return retValue;
    }

    public static int deleteAllTax(int ID, boolean isOrder, String trx) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        if (isOrder) sql.append(MOrderTax.Table_Name); else sql.append(MInvoiceTax.Table_Name);
        sql.append(" WHERE (TaxAmt = 0 OR ");
        sql.append("C_Tax_ID IN (SELECT C_Tax_ID FROM C_Tax WHERE (IsSummary = 'Y' OR LBR_TaxName_ID IS NOT NULL) AND " + "IsActive = 'Y')) AND ");
        if (isOrder) sql.append(MOrder.Table_Name).append("_ID = "); else sql.append(MInvoice.Table_Name).append("_ID = ");
        sql.append(ID);
        return DB.executeUpdate(sql.toString(), trx);
    }

    public static int deleteSummaryTax(int ID, boolean isOrder, String trx) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        if (isOrder) sql.append(MOrderTax.Table_Name); else sql.append(MInvoiceTax.Table_Name);
        sql.append(" WHERE (TaxAmt = 0 OR ");
        sql.append("C_Tax_ID IN (SELECT C_Tax_ID FROM C_Tax WHERE IsSummary = 'Y' AND IsActive = 'Y')) AND ");
        if (isOrder) sql.append(MOrder.Table_Name).append("_ID = "); else sql.append(MInvoice.Table_Name).append("_ID = ");
        sql.append(ID);
        return DB.executeUpdate(sql.toString(), trx);
    }

    /**
	 * getTaxGroup_ID
	 * @param TaxGroupName
	 * @return LBR_TaxGroup_ID
	 */
    public static int getTaxGroup_ID(String TaxGroupName) {
        String sql = "SELECT LBR_TaxGroup_ID " + "FROM LBR_TaxGroup " + "WHERE Name = ?";
        int LBR_TaxGroup_ID = DB.getSQLValue(null, sql, TaxGroupName);
        return LBR_TaxGroup_ID;
    }
}
