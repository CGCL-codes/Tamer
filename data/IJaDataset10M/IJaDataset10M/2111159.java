package eu.popeye.middleware.pluginmanagement.runtime.data;

import java.io.Serializable;
import java.util.LinkedList;
import eu.popeye.middleware.pluginmanagement.plugin.PlugManager;
import eu.popeye.middleware.pluginmanagement.plugin.Plugin;

/**
 * @author michele
 * @version 2.1
 * Charmy plug-in
 * 
 * Classe per lo scambio dei dati con il plug-in, contiene tutti i dati
 * che si possono scambiare con il plug-in
 *  **/
public class PlugDataManager implements Serializable {

    /**
	 * variabile indicante una transazione, ovvero un'insieme di operazioni
	 * sui dati che riguardano la stessa sessione
	 */
    private boolean inTransaction = false;

    /**
	 * identificativo di sessione della transazione, ogni movimento
	 * di transazione avr� il medesimo id
	 */
    private long idSessione = 0;

    private LinkedList plugins;

    private PlugManager pm;

    /**
	 * costruttore, per default costruisce tutte le liste vuote
	 */
    public LinkedList getPluginsList() {
        return plugins;
    }

    public PlugDataManager() {
        plugins = new LinkedList();
    }

    public void setPlugManager(PlugManager pm) {
        this.pm = pm;
    }

    public IPlugData getPlugData(String plugID) {
        try {
            Plugin editor = pm.getPlugEdit(plugID);
            if (editor != null) return editor.getPlugData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPlugin(IPlugData plug) {
        for (int i = 0; i < plugins.size(); i++) {
            if (plugins.get(i).equals(plug)) return;
        }
        plugins.add(plug);
    }

    /**
	 * pulisce tutti i dati contenuti nelle liste
	 * la cancellazzione dei dati viene vista come 
	 * un'unica sessione
	 *
	 */
    public synchronized void clearAll() {
        boolean bo = this.startSessione();
        for (int i = 0; i < plugins.size(); i++) {
            ((IPlugData) plugins.get(i)).clearAll();
        }
        this.stopSessione(bo);
    }

    public synchronized void clearAll(IPlugData[] plugsData) {
        boolean bo = this.startSessione();
        for (int i = 0; i < plugsData.length; i++) {
            plugsData[i].clearAll();
        }
        this.stopSessione(bo);
    }

    /**
	 * ritorna l'idSessione corrente
	 * @return
	 */
    public synchronized long getIdSessione() {
        return idSessione;
    }

    /**
	 * ritorna la modalità di transazione
	 * @return true, sono in uno stato di medesima transizione, false nessuna transizione pendente
	 */
    public synchronized boolean isInTransaction() {
        return inTransaction;
    }

    /**
	 * attiva una transazione e genera un nuovo id Di sessione
	 * @return nuovo id di sessione
	 */
    private synchronized long startTransaction() {
        if (!inTransaction) {
            idSessione++;
            inTransaction = true;
        }
        return idSessione;
    }

    /**
	 * controlla se la sessione ha una transazione attiva, se la transizione non era attiva 
	 * la attiva, e restituisce false, altrimenti true, il numero di sessione va recuperato mediante 
	 * <code>plugData.getIdSessione()</code>
	 * @param plugData
	 * @return true se la transazione era attiva, false attiva una transazione e ritorna
	 */
    public synchronized boolean startSessione() {
        boolean bo = isInTransaction();
        startTransaction();
        return bo;
    }

    /**
	 * conclude una transazione 
	 */
    public synchronized void stopSessione(boolean trans) {
        inTransaction = trans;
    }
}
