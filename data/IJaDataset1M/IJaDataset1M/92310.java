package com.gestioni.adoc.aps.system.services.uo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface IUnitaOrganizzativaManager {

    /**
	 * Restituisce la root
	 * @throws ApsSystemException
	 */
    public UnitaOrganizzativa getRoot() throws ApsSystemException;

    /**
	 * Estrae e restituisce una struttura delle uo ripulita in base alla data di scadenza e alla uo padre
	 * @param profilo
	 * @return
	 * @throws ApsSystemException
	 */
    public UnitaOrganizzativa getAllowedRoot(UnitaOrganizzativa uo) throws ApsSystemException;

    /**
	 * Aggiunge una UO
	 * @param uo
	 * @throws ApsSystemException
	 */
    public void addUo(UnitaOrganizzativa uo) throws ApsSystemException;

    /**
	 * Aggiorna una UO
	 * @param uo
	 * @throws ApsSystemException
	 */
    public void updateUo(UnitaOrganizzativa uo) throws ApsSystemException;

    /**
	 * Elimina una UO
	 * @param id
	 * @throws ApsSystemException
	 */
    public void deleteUo(int id) throws ApsSystemException;

    /**
	 * Restituisce una lista di UO
	 * @return
	 * @throws ApsSystemException
	 */
    public List<UnitaOrganizzativa> getList() throws ApsSystemException;

    /**
	 * Restituisce una UO un base all'identificativo
	 * @param id int
	 * @return
	 * @throws ApsSystemException
	 */
    public UnitaOrganizzativa getUoById(int id) throws ApsSystemException;

    /**
	 * Restituisce una mappa dei nopdi configurati a sistema
	 * @return
	 */
    public Map<Integer, NodoUO> getNodi();

    /**
	 * Restituisce un nodo in base all'identificativo
	 * @param id
	 * @return
	 */
    public NodoUO getNodo(int id);

    /**
	 * Restituisce una mappa dei tipi di relazione 1=Gerarchica 2=Funzionale
	 * @return
	 */
    public Map<Integer, String> getTipiRelazionePadre();

    /**
	 * Sposta una uo
	 * @param id
	 * @throws ApsSystemException
	 */
    public void moveUo(UnitaOrganizzativa uo) throws ApsSystemException;

    public String getPath(UnitaOrganizzativa uo, String separator);

    /**
	 * Restituisce una lista contente gli id della uo passata e delle uo sottostanti.
	 * @param unitaOrganizzativa
	 * @return
	 */
    public Set<Integer> getUoList(UnitaOrganizzativa unitaOrganizzativa) throws ApsSystemException;

    public void addUoFunzionale(UoFunzionale uoFunzionale) throws ApsSystemException;

    public void updateUoFunzionale(UoFunzionale uoFunzionale) throws ApsSystemException;

    public UoFunzionale getUoFunzionale(int id);
}
