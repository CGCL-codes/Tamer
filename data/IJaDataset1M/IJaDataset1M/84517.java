package org.openXpertya.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MForecastLine extends X_M_ForecastLine {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param M_ForecastLine_ID
     * @param trxName
     */
    public MForecastLine(Properties ctx, int M_ForecastLine_ID, String trxName) {
        super(ctx, M_ForecastLine_ID, trxName);
        if (M_ForecastLine_ID == 0) {
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
    public MForecastLine(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Descripción de Método
     *
     *
     * @param newRecord
     *
     * @return
     */
    protected boolean beforeSave(boolean newRecord) {
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param newRecord
     * @param success
     *
     * @return
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!success) {
            return success;
        }
        MMPCMRP.M_ForecastLine(this, get_TrxName(), false);
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param success
     *
     * @return
     */
    protected boolean afterDelete(boolean success) {
        if (!success) {
            return success;
        }
        MMPCMRP.M_ForecastLine(this, get_TrxName(), true);
        return true;
    }
}
