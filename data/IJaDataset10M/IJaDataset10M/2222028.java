package openXpertya.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.openXpertya.model.*;
import org.openXpertya.util.*;

/**
 *      Cost Element
 *
 *  @author Jorg Janke
 *  @version $Id: MProductCosting.java,v 1.4 2004/05/13 06:05:22 jjanke Exp $
 */
public class MMPCCostElement extends X_MPC_Cost_Element {

    private static CLogger log = CLogger.getCLogger(MMPCCostElement.class);

    /** Descripción de Campo */
    MMPCCostElement[] m_lines = null;

    /**
     *      Default Constructor
     *      @param ctx context
     *      @param M_Product_Costing_ID id
     * @param MPC_Cost_Element_ID
     * @param trxName
     */
    public MMPCCostElement(Properties ctx, int MPC_Cost_Element_ID, String trxName) {
        super(ctx, MPC_Cost_Element_ID, trxName);
        if (MPC_Cost_Element_ID == 0) {
        }
    }

    /**
     *      Load Constructor
     *      @param ctx context
     *      @param rs result set
     * @param trxName
     */
    public MMPCCostElement(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     *      Get Element Cost
     *
     * @param AD_Client_ID
     *      @return lines
     */
    public static MMPCCostElement[] getElements(int AD_Client_ID) {
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM MPC_Cost_Element WHERE AD_Client_ID =" + AD_Client_ID;
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMPCCostElement(Env.getCtx(), rs, null));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "getLines", ex);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException ex1) {
        }
        pstmt = null;
        MMPCCostElement[] retValue = new MMPCCostElement[list.size()];
        list.toArray(retValue);
        return retValue;
    }
}
