package org.compiere.model;

import java.math.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Inventory Movement Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutMovement.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 * 
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			<li>BF [ 1879568 ] CalloutMouvement QtyAvailable issues
 */
public class CalloutMovement extends CalloutEngine {

    /**
	 *  Product modified
	 * 		Set Attribute Set Instance
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param GridTab     Model Tab
	 *  @param GridField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
    public String product(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
        Integer M_Product_ID = (Integer) value;
        if (M_Product_ID == null || M_Product_ID.intValue() == 0) return "";
        if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue() && Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0) mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID"))); else mTab.setValue("M_AttributeSetInstance_ID", null);
        checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, null);
        return "";
    }

    /**
	 *  Movement Line - MovementQty modified
	 *              called from MovementQty
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param GridTab     Model Tab
	 *  @param GridField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
    public String qty(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
        if (isCalloutActive() || value == null) return "";
        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, (BigDecimal) value);
        return "";
    }

    /**
	 * Movement Line - Locator modified
	 * 
	 * @param ctx      Context
	 * @param WindowNo current Window No
	 * @param GridTab     Model Tab
	 * @param GridField   Model Field
	 * @param value    The new value
	 * @return Error message or ""
	 */
    public String locator(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
        if (value == null) return "";
        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        checkQtyAvailable(ctx, mTab, WindowNo, M_Product_ID, null);
        return "";
    }

    /**
	 * Check available qty
	 * 
	 * @param ctx context
	 * @param mTab Model Tab
	 * @param WindowNo current Window No
	 * @param M_Product_ID product ID
	 * @param MovementQty movement qty (if null will be get from context "MovementQty")
	 */
    private void checkQtyAvailable(Properties ctx, GridTab mTab, int WindowNo, int M_Product_ID, BigDecimal MovementQty) {
        if (M_Product_ID != 0) {
            MProduct product = MProduct.get(ctx, M_Product_ID);
            if (product.isStocked()) {
                if (MovementQty == null) MovementQty = (BigDecimal) mTab.getValue("MovementQty");
                int M_Locator_ID = Env.getContextAsInt(ctx, WindowNo, "M_Locator_ID");
                if (M_Locator_ID <= 0) return;
                int M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, "M_AttributeSetInstance_ID");
                BigDecimal available = MStorage.getQtyAvailable(0, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID, null);
                if (available == null) available = Env.ZERO;
                if (available.signum() == 0) mTab.fireDataStatusEEvent("NoQtyAvailable", "0", false); else if (available.compareTo(MovementQty) < 0) mTab.fireDataStatusEEvent("InsufficientQtyAvailable", available.toString(), false);
            }
        }
    }
}
