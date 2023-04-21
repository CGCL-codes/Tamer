package it.infodea.tapestrydea.services.jcr;

import javax.jcr.Repository;

/**
 * Servizio per il caricamento del repository.
 * Si tratta del principale servizio di accesso 
 * al repository.
 * TODO [marco]: per generalizzarlo si dovrebbe
 * creare una pipeline di <code>RepositoryAccessService</code>
 * con la corrispondente contribution strategy al fine di poter
 * estendere la pipeline e far eseguire eventuali strategie custom 
 * di accesso al repository.
 * @author bobpuley
 *
 */
public interface RepositoryAccessService {

    /**
	 * Reperisce l'istanza del repository 
	 * e la restituisce al client
	 * @return il repository richiesto o null.
	 */
    public Repository getRepository();
}
