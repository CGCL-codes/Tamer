package org.openXpertya.model;

import java.sql.Timestamp;
import java.util.Properties;

/**
 * Descripción de Interface
 *
 *
 * @version    2.2, 12.10.07
 * @author         Equipo de Desarrollo de openXpertya    
 */
public interface ProcesadorOXP {

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getAD_Client_ID();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getName();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getDescription();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public Properties getCtx();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getFrequencyType();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int getFrequency();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getServerID();

    /**
     * Descripción de Método
     *
     *
     * @param requery
     *
     * @return
     */
    public Timestamp getDateNextRun(boolean requery);

    /**
     * Descripción de Método
     *
     *
     * @param dateNextWork
     */
    public void setDateNextRun(Timestamp dateNextWork);

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public Timestamp getDateLastRun();

    /**
     * Descripción de Método
     *
     *
     * @param dateLastRun
     */
    public void setDateLastRun(Timestamp dateLastRun);

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean save();

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public ProcesadorLogOXP[] getLogs();
}
