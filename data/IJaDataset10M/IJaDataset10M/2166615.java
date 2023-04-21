package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.model.attribute.RecommendedAtributeInstance;
import org.openXpertya.process.ProcessInfo;
import org.openXpertya.process.Quarter_Process;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CalloutProduction extends CalloutEngine {

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
        setCalloutActive(true);
        int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
        if ((Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()) && (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)) {
            mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
        } else {
            mTab.setValue("M_AttributeSetInstance_ID", null);
        }
        MProduct product = MProduct.get(ctx, M_Product_ID.intValue());
        mTab.setValue("C_UOM_ID", new Integer(product.getC_UOM_ID()));
        if (product.getM_Locator_ID() != 0) {
            MLocator loc = MLocator.get(ctx, product.getM_Locator_ID());
            if (M_Warehouse_ID == loc.getM_Warehouse_ID()) {
                mTab.setValue("M_Locator_ID", new Integer(product.getM_Locator_ID()));
            } else {
                log.fine("No Locator for M_Product_ID=" + M_Product_ID + " and M_Warehouse_ID=" + M_Warehouse_ID);
            }
        } else {
            log.fine("No Locator for M_Product_ID=" + M_Product_ID);
        }
        setCalloutActive(false);
        return "";
    }
}
