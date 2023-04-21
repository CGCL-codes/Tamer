package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;

/**
 *  Process Instance Parameter Model
 *
 *  @author Jorg Janke
 *  @version $Id: MPInstancePara.java,v 1.3 2006/07/30 00:58:05 jjanke Exp $
 */
public class MPInstancePara extends X_AD_PInstance_Para {

    /**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trxName transaction
	 */
    public MPInstancePara(Properties ctx, int ignored, String trxName) {
        super(ctx, 0, trxName);
        if (ignored != 0) throw new IllegalArgumentException("Multi-Key");
    }

    /**
	 * 	Parent Constructor
	 *	@param ctx
	 *	@param AD_PInstance_ID id
	 *	@param SeqNo sequence
	 */
    public MPInstancePara(Properties ctx, int AD_PInstance_ID, int SeqNo) {
        super(ctx, 0, null);
        setAD_PInstance_ID(AD_PInstance_ID);
        setSeqNo(SeqNo);
    }

    /**
	 * 	Parent Constructor
	 *	@param instance instance
	 *	@param SeqNo sequence
	 */
    public MPInstancePara(MPInstance instance, int SeqNo) {
        super(instance.getCtx(), 0, instance.get_TrxName());
        setAD_PInstance_ID(instance.getAD_PInstance_ID());
        setSeqNo(SeqNo);
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MPInstancePara(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MPInstancePara[").append(get_ID()).append("-").append(getParameterName());
        if (getP_String() != null) {
            sb.append("(s)=").append(getP_String());
            if (getP_String_To() != null) sb.append(" - ").append(getP_String_To());
        }
        BigDecimal bd = (BigDecimal) get_Value("P_Number");
        if (bd != null) {
            sb.append("(n)=").append(bd);
            BigDecimal bd2 = (BigDecimal) get_Value("P_Number_To");
            if (bd2 != null) sb.append(" - ").append(bd2);
        }
        if (getP_Date() != null) {
            sb.append("(d)=").append(getP_Date());
            if (getP_Date_To() != null) sb.append(" - ").append(getP_Date_To());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
	 * 	Set P_Number
	 *	@param P_Number no
	 */
    public void setP_Number(int P_Number) {
        setP_Number(new BigDecimal(P_Number));
    }

    /**
	 * 	Set P_Number
	 *	@param P_Number no
	 */
    public void setP_Number(Integer P_Number) {
        if (P_Number == null) setP_Number(0); else setP_Number(((Integer) P_Number).intValue());
    }

    /**
	 * 	Set P_Number To
	 *	@param P_Number_To no to
	 */
    public void setP_Number_To(int P_Number_To) {
        setP_Number_To(new BigDecimal(P_Number_To));
    }

    /**
	 * 	Set P_Number To
	 *	@param P_Number_To no to
	 */
    public void setP_Number_To(Integer P_Number_To) {
        if (P_Number_To == null) setP_Number_To(0); else setP_Number_To(((Integer) P_Number_To).intValue());
    }

    /**
	 * 	Set String Parameter
	 *	@param parameterName name
	 *	@param stringParameter value
	 */
    public void setParameter(String parameterName, String stringParameter) {
        setParameterName(parameterName);
        setP_String(stringParameter);
    }

    /**
	 * 	Set Number Parameter
	 *	@param parameterName name
	 *	@param bdParameter value
	 */
    public void setParameter(String parameterName, BigDecimal bdParameter) {
        setParameterName(parameterName);
        setP_Number(bdParameter);
    }

    /**
	 * 	Set Number Parameter
	 *	@param parameterName name
	 *	@param iParameter value
	 */
    public void setParameter(String parameterName, int iParameter) {
        setParameterName(parameterName);
        setP_Number(new BigDecimal(iParameter));
    }
}
