package net.sf.brightside.instantevents.service.usecases;

import net.sf.brightside.instantevents.metamodel.City;
import net.sf.brightside.instantevents.metamodel.beans.exceptions.CityAlreadyExistsException;

public interface CityRegistration {

    public City register(City city) throws CityAlreadyExistsException;
}
