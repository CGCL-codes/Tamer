package org.openXpertya.impexp;

import java.sql.ResultSet;
import java.util.Properties;
import org.openXpertya.model.X_AD_ImpFormat_Row;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MImpFormatRow extends X_AD_ImpFormat_Row {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param AD_ImpFormat_Row_ID
     * @param trxName
     */
    public MImpFormatRow(Properties ctx, int AD_ImpFormat_Row_ID, String trxName) {
        super(ctx, AD_ImpFormat_Row_ID, trxName);
        if (AD_ImpFormat_Row_ID == 0) {
            setDecimalPoint(".");
            setDivideBy100(false);
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
    public MImpFormatRow(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param format
     */
    public MImpFormatRow(MImpFormat format) {
        this(format.getCtx(), 0, format.get_TrxName());
        setAD_ImpFormat_ID(format.getAD_ImpFormat_ID());
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param parent
     * @param original
     */
    public MImpFormatRow(MImpFormat parent, MImpFormatRow original) {
        this(parent.getCtx(), 0, parent.get_TrxName());
        copyValues(original, this);
        setClientOrg(parent);
        setAD_ImpFormat_ID(parent.getAD_ImpFormat_ID());
    }
}
