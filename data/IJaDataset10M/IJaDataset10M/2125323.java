package br.com.mcampos.ejb.cloudsystem.user;

import br.com.mcampos.dto.address.CityDTO;
import br.com.mcampos.dto.address.CountryDTO;
import br.com.mcampos.dto.address.StateDTO;
import br.com.mcampos.dto.user.UserDTO;
import br.com.mcampos.ejb.cloudsystem.locality.city.CityUtil;
import br.com.mcampos.ejb.cloudsystem.locality.city.entity.City;
import br.com.mcampos.ejb.cloudsystem.locality.city.session.CitySessionLocal;
import br.com.mcampos.ejb.cloudsystem.locality.country.CountryUtil;
import br.com.mcampos.ejb.cloudsystem.locality.country.entity.Country;
import br.com.mcampos.ejb.cloudsystem.locality.country.session.CountrySessionLocal;
import br.com.mcampos.ejb.cloudsystem.locality.state.StateUtil;
import br.com.mcampos.ejb.cloudsystem.locality.state.entity.State;
import br.com.mcampos.ejb.cloudsystem.locality.state.entity.StatePK;
import br.com.mcampos.ejb.cloudsystem.locality.state.session.StateSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.address.AddressUtil;
import br.com.mcampos.ejb.cloudsystem.user.address.session.AddressSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.contact.UserContactUtil;
import br.com.mcampos.ejb.cloudsystem.user.contact.session.UserContactSessionLocal;
import br.com.mcampos.ejb.cloudsystem.user.document.UserDocumentUtil;
import br.com.mcampos.ejb.cloudsystem.user.document.session.UserDocumentSessionLocal;
import br.com.mcampos.ejb.core.AbstractSecurity;
import br.com.mcampos.exception.ApplicationException;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;

public abstract class UserFacadeUtil extends AbstractSecurity {

    @EJB
    protected AddressSessionLocal addressSession;

    @EJB
    protected UserDocumentSessionLocal documentSession;

    @EJB
    protected UserContactSessionLocal contactSession;

    @EJB
    protected CountrySessionLocal countrySession;

    @EJB
    protected StateSessionLocal stateSession;

    @EJB
    private CitySessionLocal citySession;

    protected void refreshUserAttributes(Users user, UserDTO dto) throws ApplicationException {
        documentSession.refresh(user, UserDocumentUtil.toEntityList(user, dto.getDocumentList()));
        addressSession.refresh(user, AddressUtil.toEntityList(user, dto.getAddressList()));
        contactSession.refresh(user, UserContactUtil.toEntityList(user, dto.getContactList()));
    }

    public List<StateDTO> getStates(CountryDTO dto) throws ApplicationException {
        Country country = countrySession.get(dto.getId());
        return StateUtil.toDTOList(stateSession.getAll(country));
    }

    public List<CountryDTO> getCountries() throws ApplicationException {
        List<Country> countries = countrySession.getAllWithCities();
        return CountryUtil.toDTOList(countries);
    }

    public List<CityDTO> getCities(StateDTO dto) throws ApplicationException {
        if (dto != null) {
            State state = stateSession.get(new StatePK(dto));
            List<City> list = citySession.getAll(state);
            return CityUtil.toDTOList(list);
        }
        return Collections.emptyList();
    }
}
