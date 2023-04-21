package org.openXpertya.model;

import java.math.BigDecimal;
import java.util.Properties;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CalloutProject extends CalloutEngine {

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
    public String planned(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if (isCalloutActive() || (value == null)) {
            return "";
        }
        setCalloutActive(true);
        BigDecimal PlannedQty, PlannedPrice;
        int StdPrecision = Env.getContextAsInt(ctx, WindowNo, "StdPrecision");
        PlannedQty = (BigDecimal) mTab.getValue("PlannedQty");
        if (PlannedQty == null) {
            PlannedQty = Env.ONE;
        }
        PlannedPrice = ((BigDecimal) mTab.getValue("PlannedPrice"));
        if (PlannedPrice == null) {
            PlannedPrice = Env.ZERO;
        }
        BigDecimal PlannedAmt = PlannedQty.multiply(PlannedPrice);
        if (PlannedAmt.scale() > StdPrecision) {
            PlannedAmt = PlannedAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
        }
        log.fine("PlannedQty=" + PlannedQty + " * PlannedPrice=" + PlannedPrice + " -> PlannedAmt=" + PlannedAmt + " (Precision=" + StdPrecision + ")");
        mTab.setValue("PlannedAmt", PlannedAmt);
        setCalloutActive(false);
        return "";
    }
}
