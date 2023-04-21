package org.openXpertya.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.KeyNamePair;
import org.openXpertya.util.TimeUtil;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MAttributeSetInstance extends X_M_AttributeSetInstance {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param M_AttributeSetInstance_ID
     * @param M_Product_ID
     *
     * @return
     */
    public static MAttributeSetInstance get(Properties ctx, int M_AttributeSetInstance_ID, int M_Product_ID) {
        MAttributeSetInstance retValue = null;
        if (M_AttributeSetInstance_ID != 0) {
            s_log.fine("From M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
            return new MAttributeSetInstance(ctx, M_AttributeSetInstance_ID, null);
        }
        s_log.fine("From M_Product_ID=" + M_Product_ID);
        if (M_Product_ID == 0) {
            return null;
        }
        String sql = "SELECT M_AttributeSet_ID, M_AttributeSetInstance_ID " + "FROM M_Product " + "WHERE M_Product_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, M_Product_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int M_AttributeSet_ID = rs.getInt(1);
                retValue = new MAttributeSetInstance(ctx, 0, M_AttributeSet_ID, null);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (SQLException ex) {
            s_log.log(Level.SEVERE, "get", ex);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }
        pstmt = null;
        return retValue;
    }

    /** Descripción de Campos */
    private static CLogger s_log = CLogger.getCLogger(MAttributeSetInstance.class);

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param M_AttributeSetInstance_ID
     * @param trxName
     */
    public MAttributeSetInstance(Properties ctx, int M_AttributeSetInstance_ID, String trxName) {
        super(ctx, M_AttributeSetInstance_ID, trxName);
        if (M_AttributeSetInstance_ID == 0) {
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MAttributeSetInstance(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param M_AttributeSetInstance_ID
     * @param M_AttributeSet_ID
     * @param trxName
     */
    public MAttributeSetInstance(Properties ctx, int M_AttributeSetInstance_ID, int M_AttributeSet_ID, String trxName) {
        this(ctx, M_AttributeSetInstance_ID, trxName);
        setM_AttributeSet_ID(M_AttributeSet_ID);
    }

    /** Descripción de Campos */
    private MAttributeSet m_mas = null;

    /** Descripción de Campos */
    private DateFormat m_dateFormat = DisplayType.getDateFormat(DisplayType.Date);

    /**
     * Descripción de Método
     *
     *
     * @param mas
     */
    public void setMAttributeSet(MAttributeSet mas) {
        m_mas = mas;
        setM_AttributeSet_ID(mas.getM_AttributeSet_ID());
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public MAttributeSet getMAttributeSet() {
        if ((m_mas == null) && (getM_AttributeSet_ID() != 0)) {
            m_mas = new MAttributeSet(getCtx(), getM_AttributeSet_ID(), get_TrxName());
        }
        return m_mas;
    }

    /**
     * Descripción de Método
     *
     */
    public void setDescription() {
        getMAttributeSet();
        if (m_mas == null) {
            setDescription("");
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (m_mas.isSerNo() && (getSerNo() != null)) {
            sb.append("#").append(getSerNo());
        }
        if (m_mas.isLot() && (getLot() != null)) {
            sb.append("[").append(getLot()).append("]");
        }
        if (m_mas.isGuaranteeDate() && (getGuaranteeDate() != null)) {
            if (sb.length() > 0) {
                sb.append("_");
            }
            sb.append(m_dateFormat.format(getGuaranteeDate()));
        }
        MAttribute[] attributes = m_mas.getMAttributes(false);
        for (int i = 0; i < attributes.length; i++) {
            if ((sb.length() > 0) && (i == 0)) {
                sb.append("_");
            }
            if (i != 0) {
                sb.append("-");
            }
            MAttributeInstance mai = attributes[i].getMAttributeInstance(getM_AttributeSetInstance_ID());
            if ((mai != null) && (mai.getValue() != null)) {
                sb.append(mai.getValue());
            }
        }
        attributes = m_mas.getMAttributes(true);
        for (int i = 0; i < attributes.length; i++) {
            if ((sb.length() > 0) && (i == 0)) {
                sb.append("_");
            }
            if (i != 0) {
                sb.append("-");
            }
            MAttributeInstance mai = attributes[i].getMAttributeInstance(getM_AttributeSetInstance_ID());
            if ((mai != null) && (mai.getValue() != null)) {
                sb.append(mai.getValue());
            }
        }
        setDescription(sb.toString());
    }

    /**
     * Descripción de Método
     *
     *
     * @param getNew
     *
     * @return
     */
    public Timestamp getGuaranteeDate(boolean getNew) {
        if (getNew) {
            int days = getMAttributeSet().getGuaranteeDays();
            if (days > 0) {
                Timestamp ts = TimeUtil.addDays(new Timestamp(System.currentTimeMillis()), days);
                setGuaranteeDate(ts);
            }
        }
        return getGuaranteeDate();
    }

    public Timestamp getDueDate(boolean getNew) {
        if (getNew) {
            setGuaranteeDate(getMAttributeSet().getDueDate());
        }
        return getGuaranteeDate();
    }

    /**
     * Descripción de Método
     *
     *
     * @param getNew
     * @param M_Product_ID
     *
     * @return
     */
    public String getLot(boolean getNew, int M_Product_ID) {
        if (getNew) {
            createLot(M_Product_ID);
        }
        return getLot();
    }

    /**
     * Descripción de Método
     *
     *
     * @param M_Product_ID
     *
     * @return
     */
    public KeyNamePair createLot(int M_Product_ID) {
        KeyNamePair retValue = null;
        int M_LotCtl_ID = getMAttributeSet().getM_LotCtl_ID();
        if (M_LotCtl_ID != 0) {
            MLotCtl ctl = new MLotCtl(getCtx(), M_LotCtl_ID, null);
            MLot lot = ctl.createLot(M_Product_ID);
            setM_Lot_ID(lot.getM_Lot_ID());
            setLot(lot.getName());
            retValue = new KeyNamePair(lot.getM_Lot_ID(), lot.getName());
        }
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param getNew
     *
     * @return
     */
    public String getSerNo(boolean getNew) {
        if (getNew) {
            int M_SerNoCtl_ID = getMAttributeSet().getM_SerNoCtl_ID();
            if (M_SerNoCtl_ID != 0) {
                MSerNoCtl ctl = new MSerNoCtl(getCtx(), M_SerNoCtl_ID, get_TrxName());
                setSerNo(ctl.createSerNo());
            }
        }
        return getSerNo();
    }
}
