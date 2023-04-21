package se.inera.ifv.medcert.core.spi.citizen;

import se.inera.ifv.medcert.core.entity.CitizenInterface;

/**
* Service provider interface for retrieving citizen information. The service interface should be implemented
* for each service provider that should answer these citizen questions. 
*/
public interface GetCitizenInfoService {

    /**
     * Return information about a citizen.
     * @param citizenId
     * @return CitizenInterface
     */
    public CitizenInterface getCitizenFullInfo(String citizenId) throws Exception;
}
