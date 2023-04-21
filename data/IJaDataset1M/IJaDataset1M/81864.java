package openXpertya.model;

import java.sql.*;
import java.util.*;
import org.openXpertya.model.*;
import org.openXpertya.util.*;

/**
 *      Project Line Model
 *
 *      @author Jorg Janke
 *      @version $Id: MProjectLine.java,v 1.5 2003/11/20 02:31:24 jjanke Exp $
 */
public class MMPCProfileBOMProduct extends X_MPC_ProfileBOM_Product {

    /**
     *      Standard Constructor
     *      @param ctx context
     *      @param C_ProjectLine_ID id
     * @param MPC_ProfileBOM_Product_ID
     * @param trxName
     */
    public MMPCProfileBOMProduct(Properties ctx, int MPC_ProfileBOM_Product_ID, String trxName) {
        super(ctx, MPC_ProfileBOM_Product_ID, trxName);
        if (MPC_ProfileBOM_Product_ID == 0) {
        }
    }

    /**
     *      Load Constructor
     *      @param ctx context
     *      @param rs result set
     * @param trxName
     */
    public MMPCProfileBOMProduct(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     *      Parent Constructor
     *      @param project parent
     *
     * @param profile
     */
    public MMPCProfileBOMProduct(MMPCProfileBOM profile) {
        this(profile.getCtx(), 0, null);
        setClientOrg(profile.getAD_Client_ID(), profile.getAD_Org_ID());
        setMPC_ProfileBOM_ID(profile.getMPC_ProfileBOM_ID());
        setLine(getNextLine());
    }

    /**
     *      Set AD_Org_ID
     *      @param AD_Org_ID Org ID
     */
    public void setAD_Org_ID(int AD_Org_ID) {
        super.setAD_Org_ID(AD_Org_ID);
    }

    /**
     *      Get the next Line No
     *      @return next line no
     */
    private int getNextLine() {
        return DB.getSQLValue("MPC_ProfileBOM_Product", "SELECT COALESCE(MAX(Line),0)+10 FROM MPC_ProfileBOM_Product WHERE MPC_ProfileBOM_ID=?", getMPC_ProfileBOM_ID());
    }
}
