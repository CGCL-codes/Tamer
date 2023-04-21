package it.unina.seclab.jafimon.activators;

import it.unina.seclab.jafimon.Configuration;
import it.unina.seclab.jafimon.MonitoredDataManager;
import it.unina.seclab.jafimon.exceptions.CannotPrepareSystemException;

/**
 * Definisce il comportamento del generico <code>PhysicalActivator</code>.
 * Il <code>PhysicalActivator</code> ha la conoscenza dello specifico sistema
 * da testare ed e' quindi in grado di configurarlo, avviarlo ed arrestarlo
 * correttamente.
 * 
 * @author 	Mauro Iorio
 * @version	1.0
 * @see		it.unina.seclab.jafimon.Activator
 *
 */
public interface IPhysicalActivator {

    /**
	 * Prepara il sistema da testare. La preparazione pu� consistere nella modifica
	 * di alcuni file di configurazione del sistema (es. catalina.sh), oppure nella
	 * creazione di script di avvio customizzati, ecc.
	 * 
	 * @throws CannotPrepareSystemException 
	 *
	 */
    public void prepareSystem(Configuration cfg, MonitoredDataManager dataMg) throws CannotPrepareSystemException;

    /**
	 * Avvia il sistema da testare agganciandolo al componente di Monitoraggio
	 * secondo la configurazione specificata
	 * 
	 * @param cfg la <code>Configuration</code> da utilizzare
	 * @param dataMgr il <code>MonitoredDataManager</code> con cui comunicare
	 * @throws Exception 
	 * 
	 */
    public void startSystem(Configuration cfg, MonitoredDataManager dataMgr) throws Exception;

    /**
	 * Arresta la fase di monitoraggio del sistema in test. Il metodo arresta
	 * anche il sistema stesso
	 * @throws Exception 
	 * 
	 */
    public void stopSystem() throws Exception;

    /**
	 * Ripristina il sistema da testare al suo stato precedente. Il ripristino pu� 
	 * consistere nella modifica di alcuni file di configurazione del sistema 
	 * precedentemente alterati (es. catalina.sh), oppure nella
	 * creazione di script di avvio customizzati, ecc.
	 * 
	 * @throws CannotPrepareSystemException
	 */
    public void restoreSystem(Configuration cfg, MonitoredDataManager dataMg) throws CannotPrepareSystemException;

    /**
	 * Consente di recuperare il nome del sistema. Usata internamente
	 * dal framework
	 * 
	 * @return il nome del sistema
	 */
    public String getSystemName();
}
