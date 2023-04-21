package es.devel.opentrats.dao;

import es.devel.opentrats.dao.exception.ProviderDaoException;
import es.devel.opentrats.model.Provider;
import java.util.Collection;

/**
 *
 * @author pau
 */
public interface IProviderDao {

    Collection<Provider> getAllProviders() throws ProviderDaoException;

    Provider load(Integer idProviderArg) throws ProviderDaoException;

    void save(Provider productArg) throws ProviderDaoException;

    void delete(Provider productArg) throws ProviderDaoException;
}
