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
public class MCounterCount extends X_W_CounterCount {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param W_CounterCount_ID
     * @param trxName
     */
    public MCounterCount(Properties ctx, int W_CounterCount_ID, String trxName) {
        super(ctx, W_CounterCount_ID, trxName);
        if (W_CounterCount_ID == 0) {
            setCounter(0);
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
    public MCounterCount(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
