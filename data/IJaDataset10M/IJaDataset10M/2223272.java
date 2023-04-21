package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CalloutGLJournal extends CalloutEngine {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     */
    public String period(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        String colName = mField.getColumnName();
        if ((value == null) || isCalloutActive()) {
            return "";
        }
        setCalloutActive(true);
        int AD_Client_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
        Timestamp DateAcct = null;
        if (colName.equals("DateAcct")) {
            DateAcct = (Timestamp) value;
        } else {
            DateAcct = (Timestamp) mTab.getValue("DateAcct");
        }
        int C_Period_ID = 0;
        if (colName.equals("C_Period_ID")) {
            C_Period_ID = ((Integer) value).intValue();
        }
        if (colName.equals("DateDoc")) {
            mTab.setValue("DateAcct", value);
        } else if (colName.equals("DateAcct")) {
            String sql = "SELECT C_Period_ID " + "FROM C_Period " + "WHERE C_Year_ID IN " + "     (SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID =" + "  (SELECT C_Calendar_ID FROM AD_ClientInfo WHERE AD_Client_ID=?))" + " AND ? BETWEEN StartDate AND EndDate" + " AND PeriodType='S'";
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql);
                pstmt.setInt(1, AD_Client_ID);
                pstmt.setTimestamp(2, DateAcct);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    C_Period_ID = rs.getInt(1);
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Journal_Period - DateAcct", e);
                setCalloutActive(false);
                return e.getLocalizedMessage();
            }
            if (C_Period_ID != 0) {
                mTab.setValue("C_Period_ID", new Integer(C_Period_ID));
            }
        } else {
            String sql = "SELECT PeriodType, StartDate, EndDate " + "FROM C_Period WHERE C_Period_ID=?";
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql);
                pstmt.setInt(1, C_Period_ID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String PeriodType = rs.getString(1);
                    Timestamp StartDate = rs.getTimestamp(2);
                    Timestamp EndDate = rs.getTimestamp(3);
                    if (PeriodType.equals("S")) {
                        if (DateAcct.before(StartDate) || DateAcct.after(EndDate)) {
                            mTab.setValue("DateAcct", EndDate);
                        }
                    }
                }
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Journal_Period - Period", e);
                setCalloutActive(false);
                return e.getLocalizedMessage();
            }
        }
        setCalloutActive(false);
        return "";
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     */
    public String rate(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if (value == null) {
            return "";
        }
        Integer Currency_ID = (Integer) mTab.getValue("C_Currency_ID");
        int C_Currency_ID = Currency_ID.intValue();
        Integer ConversionType_ID = (Integer) mTab.getValue("C_ConversionType_ID");
        int C_ConversionType_ID = ConversionType_ID.intValue();
        Timestamp DateAcct = (Timestamp) mTab.getValue("DateAcct");
        if (DateAcct == null) {
            DateAcct = new Timestamp(System.currentTimeMillis());
        }
        int C_AcctSchema_ID = Env.getContextAsInt(ctx, WindowNo, "C_AcctSchema_ID");
        MAcctSchema as = MAcctSchema.get(ctx, C_AcctSchema_ID);
        int AD_Client_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
        int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
        BigDecimal CurrencyRate = MConversionRate.getRate(C_Currency_ID, as.getC_Currency_ID(), DateAcct, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
        log.fine("rate = " + CurrencyRate);
        if (CurrencyRate == null) {
            CurrencyRate = Env.ZERO;
        }
        mTab.setValue("CurrencyRate", CurrencyRate);
        return "";
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     */
    public String amt(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if ((value == null) || isCalloutActive()) {
            return "";
        }
        setCalloutActive(true);
        int C_AcctSchema_ID = Env.getContextAsInt(ctx, WindowNo, "C_AcctSchema_ID");
        MAcctSchema as = MAcctSchema.get(ctx, C_AcctSchema_ID);
        int Precision = as.getStdPrecision();
        BigDecimal CurrencyRate = (BigDecimal) mTab.getValue("CurrencyRate");
        if (CurrencyRate == null) {
            CurrencyRate = Env.ONE;
            mTab.setValue("CurrencyRate", CurrencyRate);
        }
        BigDecimal AmtSourceDr = (BigDecimal) mTab.getValue("AmtSourceDr");
        if (AmtSourceDr == null) {
            AmtSourceDr = Env.ZERO;
        }
        BigDecimal AmtSourceCr = (BigDecimal) mTab.getValue("AmtSourceCr");
        if (AmtSourceCr == null) {
            AmtSourceCr = Env.ZERO;
        }
        BigDecimal AmtAcctDr = AmtSourceDr.multiply(CurrencyRate);
        AmtAcctDr = AmtAcctDr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
        mTab.setValue("AmtAcctDr", AmtAcctDr);
        BigDecimal AmtAcctCr = AmtSourceCr.multiply(CurrencyRate);
        AmtAcctCr = AmtAcctCr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
        mTab.setValue("AmtAcctCr", AmtAcctCr);
        setCalloutActive(false);
        return "";
    }
}
