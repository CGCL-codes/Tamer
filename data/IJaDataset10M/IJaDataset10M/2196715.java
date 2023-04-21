package org.openXpertya.process;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class ProcessInfoLog implements Serializable {

    /**
     * Constructor de la clase ...
     *
     *
     * @param P_ID
     * @param P_Date
     * @param P_Number
     * @param P_Msg
     */
    public ProcessInfoLog(int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg) {
        this(s_Log_ID++, P_ID, P_Date, P_Number, P_Msg);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param Log_ID
     * @param P_ID
     * @param P_Date
     * @param P_Number
     * @param P_Msg
     */
    public ProcessInfoLog(int Log_ID, int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg) {
        setLog_ID(Log_ID);
        setP_ID(P_ID);
        setP_Date(P_Date);
        setP_Number(P_Number);
        setP_Msg(P_Msg);
    }

    /** Descripción de Campos */
    private static int s_Log_ID = 0;

    /** Descripción de Campos */
    private int m_Log_ID;

    /** Descripción de Campos */
    private int m_P_ID;

    /** Descripción de Campos */
    private Timestamp m_P_Date;

    /** Descripción de Campos */
    private BigDecimal m_P_Number;

    /** Descripción de Campos */
    private String m_P_Msg;

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getLog_ID() {
        return m_Log_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @param Log_ID
     */
    public void setLog_ID(int Log_ID) {
        m_Log_ID = Log_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getP_ID() {
        return m_P_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @param P_ID
     */
    public void setP_ID(int P_ID) {
        m_P_ID = P_ID;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public Timestamp getP_Date() {
        return m_P_Date;
    }

    /**
     * Descripción de Método
     *
     *
     * @param P_Date
     */
    public void setP_Date(Timestamp P_Date) {
        m_P_Date = P_Date;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BigDecimal getP_Number() {
        return m_P_Number;
    }

    /**
     * Descripción de Método
     *
     *
     * @param P_Number
     */
    public void setP_Number(BigDecimal P_Number) {
        m_P_Number = P_Number;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getP_Msg() {
        return m_P_Msg;
    }

    /**
     * Descripción de Método
     *
     *
     * @param P_Msg
     */
    public void setP_Msg(String P_Msg) {
        m_P_Msg = P_Msg;
    }
}
