package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.DB;

/**
 *  Depreciation Workfile Model
 *
 *
 */
public class MDepreciationWorkfile extends X_A_Depreciation_Workfile {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9075233803956474274L;

    /**
	 * 	Default Constructor X_A_Depreciation_Workfile
	 *	@param ctx context
	 *	@param M_InventoryLine_ID line
	 */
    public MDepreciationWorkfile(Properties ctx, int A_Depreciation_Workfile_ID, String trxName) {
        super(ctx, A_Depreciation_Workfile_ID, trxName);
        if (A_Depreciation_Workfile_ID == 0) {
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
    public MDepreciationWorkfile(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    protected boolean afterSave(boolean newRecord) {
        log.info("beforeSave");
        int p_wkasset_ID = 0;
        p_wkasset_ID = getA_Depreciation_Workfile_ID();
        StringBuffer sqlB = new StringBuffer("UPDATE A_Depreciation_Workfile " + "SET Processing = 'Y'" + " WHERE A_Depreciation_Workfile_ID = " + p_wkasset_ID);
        int no = DB.executeUpdate(sqlB.toString(), null);
        if (no == -1) log.info("Update to Deprecaition Workfile failed");
        return true;
    }

    /**
	 * 	after Save
	 *	@param newRecord new
	 *	@return true
	 */
    protected boolean beforeSave(boolean newRecord) {
        log.info("beforeSave");
        int p_A_Asset_ID = 0;
        p_A_Asset_ID = getA_Asset_ID();
        log.info("afterSave");
        X_A_Asset asset = new X_A_Asset(getCtx(), p_A_Asset_ID, null);
        asset.setA_QTY_Current(getA_QTY_Current());
        asset.save();
        if (getA_Accumulated_Depr().equals(null)) setA_Accumulated_Depr(new BigDecimal(0.0));
        if (new BigDecimal(getA_Period_Posted()).equals(null)) setA_Period_Posted(0);
        MAssetChange change = new MAssetChange(getCtx(), 0, null);
        log.info("0");
        String sql2 = "SELECT COUNT(*) FROM A_Depreciation_Workfile WHERE A_Asset_ID=? AND PostingType = ?";
        if (DB.getSQLValue(null, sql2, p_A_Asset_ID, getPostingType()) != 0) {
            change.setA_Asset_ID(p_A_Asset_ID);
            change.setChangeType("BAL");
            change.setTextDetails(MRefList.getListDescription(getCtx(), "A_Update_Type", "BAL"));
            change.setPostingType(getPostingType());
            change.setAssetValueAmt(getA_Asset_Cost());
            change.setA_QTY_Current(getA_QTY_Current());
            change.setA_QTY_Original(getA_QTY_Current());
            change.setAssetAccumDepreciationAmt(getA_Accumulated_Depr());
            change.save();
        }
        return true;
    }
}
