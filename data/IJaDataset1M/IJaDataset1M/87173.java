package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CalloutInventory extends CalloutEngine {

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
    public String product(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer M_Product_ID = (Integer) value;
        if ((M_Product_ID == null) || (M_Product_ID.intValue() == 0)) {
            return "";
        }
        if ((Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()) && (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)) {
            mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
        } else {
            mTab.setValue("M_AttributeSetInstance_ID", null);
        }
        Integer inventoryID = (Integer) mTab.getValue("M_Inventory_ID");
        boolean calculateQtyBook = true;
        if (inventoryID != null && inventoryID > 0) {
            MInventory inventory = new MInventory(ctx, inventoryID, null);
            calculateQtyBook = inventory.getInventoryKind().equals(MInventory.INVENTORYKIND_PhysicalInventory);
        }
        Integer ID = (Integer) mTab.getValue("M_InventoryLine_ID");
        if ((ID != null) && (ID.intValue() == 0) && calculateQtyBook) {
            int M_Locator_ID = Env.getContextAsInt(ctx, WindowNo, "M_Locator_ID");
            String sql = "SELECT QtyOnHand FROM M_Storage " + "WHERE M_Product_ID=?" + " AND M_Locator_ID=?";
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql);
                pstmt.setInt(1, M_Product_ID.intValue());
                pstmt.setInt(2, M_Locator_ID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    BigDecimal bd = rs.getBigDecimal(1);
                    if (!rs.wasNull()) {
                        mTab.setValue("QtyBook", bd);
                    }
                }
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                log.log(Level.SEVERE, "product", e);
                return e.getLocalizedMessage();
            }
        }
        return "";
    }

    public String inventoryKind(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        String inventoryKind = (String) value;
        if (inventoryKind != null) {
            MDocType docType = null;
            if (inventoryKind.equals(MInventory.INVENTORYKIND_SimpleInOut)) {
                docType = MDocType.getDocType(ctx, MDocType.DOCTYPE_SimpleMaterialInOut, null);
            } else if (inventoryKind.equals(MInventory.INVENTORYKIND_PhysicalInventory)) {
                docType = MDocType.getDocType(ctx, MDocType.DOCTYPE_MaterialPhysicalInventory, null);
            }
            if (docType != null) {
                mTab.setValue("C_DocType_ID", docType.getC_DocType_ID());
            }
        }
        return "";
    }

    public String paperForm(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        String strValue = (String) value;
        setCalloutActive(true);
        setCalloutActive(false);
        return "";
    }
}
