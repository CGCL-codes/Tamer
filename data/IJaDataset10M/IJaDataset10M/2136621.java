package br.com.mcampos.ejb.cloudsystem.locality.region.session;

import br.com.mcampos.ejb.cloudsystem.locality.country.entity.Country;
import br.com.mcampos.ejb.cloudsystem.locality.region.entity.Region;
import br.com.mcampos.ejb.cloudsystem.locality.region.entity.RegionPK;
import br.com.mcampos.ejb.session.core.Crud;
import br.com.mcampos.exception.ApplicationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name = "RegionSession", mappedName = "CloudSystems-EjbPrj-RegionSession")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RegionSessionBean extends Crud<RegionPK, Region> implements RegionSessionLocal {

    public void delete(RegionPK key) throws ApplicationException {
        delete(Region.class, key);
    }

    public Region get(RegionPK key) throws ApplicationException {
        return get(Region.class, key);
    }

    public List<Region> getAll(Country country) throws ApplicationException {
        return (List<Region>) getResultList(Region.getAll, country);
    }

    public Integer nextIntegerId(Country country) throws ApplicationException {
        return (Integer) getSingleResult(Region.nextId, country);
    }
}
