package org.compiere.model;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import org.compiere.util.*;
import org.compiere.print.*;

/**
 * 	Performance Color Schema
 *	
 *  @author Jorg Janke
 *  @version $Id: MColorSchema.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MColorSchema extends X_PA_ColorSchema {

    /**
	 * 	Get Color
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param target target value
	 *	@param actual actual value
	 *	@return color
	 */
    public static Color getColor(Properties ctx, int PA_ColorSchema_ID, BigDecimal target, BigDecimal actual) {
        int percent = 0;
        if (actual != null && actual.signum() != 0 && target != null && target.signum() != 0) {
            BigDecimal pp = actual.multiply(Env.ONEHUNDRED).divide(target, 0, BigDecimal.ROUND_HALF_UP);
            percent = pp.intValue();
        }
        return getColor(ctx, PA_ColorSchema_ID, percent);
    }

    /**
	 * 	Get Color
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param percent percent
	 *	@return color
	 */
    public static Color getColor(Properties ctx, int PA_ColorSchema_ID, int percent) {
        MColorSchema cs = get(ctx, PA_ColorSchema_ID);
        return cs.getColor(percent);
    }

    /**
	 * 	Get MColorSchema from Cache
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@return MColorSchema
	 */
    public static MColorSchema get(Properties ctx, int PA_ColorSchema_ID) {
        if (PA_ColorSchema_ID == 0) {
            MColorSchema retValue = new MColorSchema(ctx, 0, null);
            retValue.setDefault();
            return retValue;
        }
        Integer key = new Integer(PA_ColorSchema_ID);
        MColorSchema retValue = (MColorSchema) s_cache.get(key);
        if (retValue != null) return retValue;
        retValue = new MColorSchema(ctx, PA_ColorSchema_ID, null);
        if (retValue.get_ID() != 0) s_cache.put(key, retValue);
        return retValue;
    }

    /**	Cache						*/
    private static CCache<Integer, MColorSchema> s_cache = new CCache<Integer, MColorSchema>("PA_ColorSchema", 20);

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param trxName trx
	 */
    public MColorSchema(Properties ctx, int PA_ColorSchema_ID, String trxName) {
        super(ctx, PA_ColorSchema_ID, trxName);
        if (PA_ColorSchema_ID == 0) {
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
    public MColorSchema(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Set Default.
	 * 	Red (50) - Yellow (100) - Green
	 */
    public void setDefault() {
        setName("Default");
        setMark1Percent(50);
        setAD_PrintColor1_ID(102);
        setMark2Percent(100);
        setAD_PrintColor2_ID(113);
        setMark3Percent(9999);
        setAD_PrintColor3_ID(103);
    }

    /**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
    protected boolean beforeSave(boolean newRecord) {
        if (getMark1Percent() > getMark2Percent()) setMark1Percent(getMark2Percent());
        if (getMark2Percent() > getMark3Percent() && getMark3Percent() != 0) setMark2Percent(getMark3Percent());
        if (getMark3Percent() > getMark4Percent() && getMark4Percent() != 0) setMark4Percent(getMark4Percent());
        return true;
    }

    /**
	 * 	Get Color
	 *	@param percent percent
	 *	@return color
	 */
    public Color getColor(int percent) {
        int AD_PrintColor_ID = 0;
        if (percent <= getMark1Percent() || getMark2Percent() == 0) AD_PrintColor_ID = getAD_PrintColor1_ID(); else if (percent <= getMark2Percent() || getMark3Percent() == 0) AD_PrintColor_ID = getAD_PrintColor2_ID(); else if (percent <= getMark3Percent() || getMark4Percent() == 0) AD_PrintColor_ID = getAD_PrintColor3_ID(); else AD_PrintColor_ID = getAD_PrintColor4_ID();
        if (AD_PrintColor_ID == 0) {
            if (getAD_PrintColor3_ID() != 0) AD_PrintColor_ID = getAD_PrintColor3_ID(); else if (getAD_PrintColor2_ID() != 0) AD_PrintColor_ID = getAD_PrintColor2_ID(); else if (getAD_PrintColor1_ID() != 0) AD_PrintColor_ID = getAD_PrintColor1_ID();
        }
        if (AD_PrintColor_ID == 0) return Color.black;
        MPrintColor pc = MPrintColor.get(getCtx(), AD_PrintColor_ID);
        if (pc != null) return pc.getColor();
        return Color.black;
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MColorSchema[");
        sb.append(get_ID()).append("-").append(getName()).append("]");
        return sb.toString();
    }
}
