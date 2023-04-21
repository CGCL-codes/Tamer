package br.com.mcampos.ejb.cloudsystem.locality.state.session;

import br.com.mcampos.ejb.cloudsystem.locality.state.entity.State;
import br.com.mcampos.ejb.cloudsystem.locality.state.entity.StatePK;
import br.com.mcampos.ejb.cloudsystem.locality.country.entity.Country;
import br.com.mcampos.ejb.cloudsystem.locality.region.entity.Region;
import br.com.mcampos.exception.ApplicationException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;

@Local
public interface StateSessionLocal extends Serializable {

    void delete(StatePK key) throws ApplicationException;

    State get(StatePK key) throws ApplicationException;

    List<State> getAll(Region user) throws ApplicationException;

    List<State> getAll(Country user) throws ApplicationException;

    State add(State entity) throws ApplicationException;

    State update(State entity) throws ApplicationException;
}
