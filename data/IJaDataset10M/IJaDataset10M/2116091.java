package com.gestioni.adoc.apsadmin.titolario;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface ITitolarioUoAction {

    /**
	 * Accede all'interfaccia di gestione tuitolario-uo
	 * @return
	 * @throws ApsSystemException
	 */
    public String manageUoTitolario() throws ApsSystemException;

    /**
	 * Associa, senza salvare una uo al titolario
	 * @return
	 * @throws ApsSystemException
	 */
    public String selectNode() throws ApsSystemException;

    /**
	 * Aggiorna lo stato titolario-uo
	 * @return
	 * @throws ApsSystemException
	 */
    public String saveRelations() throws ApsSystemException;
}
