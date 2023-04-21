package org.adempierelbr.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	MLBRApuracaoIPI
 *
 *	Model for X_LBR_ApuracaoIPI
 *
 *	@author Mario Grigioni
 *	@version $Id: MLBRApuracaoIPI.java, 21/03/2011 15:11:00 mgrigioni
 */
public class MLBRApuracaoIPI extends X_LBR_ApuracaoIPI {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /**************************************************************************
	 *  Default Constructor
	 *  @param Properties ctx
	 *  @param int ID (0 create new)
	 *  @param String trx
	 */
    public MLBRApuracaoIPI(Properties ctx, int ID, String trx) {
        super(ctx, ID, trx);
    }

    /**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
    public MLBRApuracaoIPI(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MLBRApuracaoIPI get(Properties ctx, int C_Period_ID, int AD_Org_ID) {
        String sql = "SELECT MAX(LBR_ApuracaoIPI_ID) FROM LBR_ApuracaoIPI " + "WHERE C_Period_ID = ? AND AD_Org_ID = ?";
        int LBR_ApuracaoIPI_ID = DB.getSQLValue(null, sql, new Object[] { C_Period_ID, AD_Org_ID });
        return new MLBRApuracaoIPI(ctx, LBR_ApuracaoIPI_ID, null);
    }

    /**
	 * Retorna SALDO do PERIODO ANTERIOR
	 * @param ctx
	 * @param C_Period_ID
	 * @return BigDecimal CumulatedAmt
	 */
    public static BigDecimal getCumulatedAmt(Properties ctx, int C_Period_ID) {
        BigDecimal amt = null;
        String sql = "SELECT TotalAmt FROM LBR_ApuracaoIPI ai " + "WHERE ai.C_Period_ID = " + "(SELECT p1.C_Period_ID FROM C_Period p1 " + "WHERE p1.StartDate = " + "(SELECT TRUNC(p2.StartDate-1,'MM') FROM C_Period p2 " + "WHERE p2.C_Period_ID = ?) AND p1.AD_Client_ID = ?)";
        amt = DB.getSQLValueBD(null, sql, new Object[] { C_Period_ID, Env.getAD_Client_ID(ctx) });
        if (amt == null || amt.signum() != 1) amt = Env.ZERO;
        return amt;
    }

    /**
	 * Retorna Soma das Linhas por Tipo de Ajuste
	 * @return BigDecimal amt
	 */
    public BigDecimal getAmtByType(String type) {
        BigDecimal amt = null;
        String sql = "SELECT SUM(al.Amt) FROM LBR_ApuracaoIPILine al " + "WHERE al.LBR_ApuracaoIPI_ID = ? AND al.Type = ?";
        amt = DB.getSQLValueBD(null, sql, new Object[] { get_ID(), type });
        if (amt == null) amt = Env.ZERO;
        return amt;
    }

    /**************************************************************************
	 *  getLines
	 *  @return X_LBR_ApuracaoIPILine[] lines
	 */
    public X_LBR_ApuracaoIPILine[] getLines() {
        String whereClause = "LBR_ApuracaoIPI_ID=?";
        MTable table = MTable.get(getCtx(), X_LBR_ApuracaoIPILine.Table_Name);
        Query query = new Query(getCtx(), table, whereClause, get_TrxName());
        query.setParameters(get_ID());
        List<X_LBR_ApuracaoIPILine> list = query.list();
        return list.toArray(new X_LBR_ApuracaoIPILine[list.size()]);
    }
}
