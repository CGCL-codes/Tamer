package dsb.bar.tks.barkassa.persistence.dao.debiteur;

import java.util.List;
import dsb.bar.tks.barkassa.persistence.dao.HibernateEJBDAO;
import dsb.bar.tks.barkassa.persistence.model.debiteur.Debiteur;

public interface DebiteurDAO extends HibernateEJBDAO<Debiteur, Integer> {

    /**
	 * Haal alle debiteuren op waarvan de naam lijkt op de gegeven parameter.
	 * 
	 * @param naam
	 *            Deze string wordt gebruikt voor een 'like' (substring)
	 *            vergelijking met de naam uit de database.
	 * 
	 * @return Een List met corresponderende Debiteur objecten.
	 */
    public abstract List<Debiteur> getByNaam(String naam);

    /**
	 * Haal alle actieve of inactieve debiteuren op.
	 * 
	 * @param actief
	 *            Als <code>true</code>, dan worden alleen actieve debiteuren
	 *            opgehaald. Als <code>false</code>, dan worden alleen inactieve
	 *            debiteuren opgehaald.
	 * 
	 * @return Een List met corresponderende Debiteur objecten.
	 */
    public abstract List<Debiteur> getByActief(boolean actief);
}
