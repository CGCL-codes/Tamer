package br.ufmg.lcc.eid.model.conciliator;

import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.eid.commons.EidException;

/**
 * Interface to be implemented to create a map
 * of objects to be conciliated
 * @author Edr�
 *
 */
public interface IConciliator {

    /**
	 * Starts the match of all pending objects.
	 * Creates all Match objects and consolidates objects not conciliable.
	 * @param dao DAO that controls the transaction
	 */
    public void match(IPersistenceObject dao) throws EidException;

    /**
	 * Verifies if there are objects pending to be conciliated
	 * @param dao DAO that controls transaction
	 * @return true, if there are objetcs to be conciliated; false, otherwise
	 * @throws BasicException 
	 */
    public boolean hasConciliatoinPending(IPersistenceObject dao) throws BasicException;
}
