package no.ugland.utransprod.dao;

import java.util.Collection;
import java.util.List;
import no.ugland.utransprod.model.DeviationStatus;

/**
 * Interface for DAO mot tabell DEVIATION_STATUS
 * 
 * @author atle.brekka
 * 
 */
public interface DeviationStatusDAO extends DAO<DeviationStatus> {

    /**
	 * Oppdaterer objekt
	 * 
	 * @param deviationStatus
	 */
    void refreshObject(DeviationStatus deviationStatus);

    /**
	 * Finner alle status som ikke er for leder eller administrator
	 * 
	 * @return status
	 */
    List<DeviationStatus> findAllNotForManager();

    Integer countUsedByDeviation(final DeviationStatus deviationStatus);

    List<DeviationStatus> findAllForDeviation();

    Collection<DeviationStatus> findAllNotForManagerForAccident();

    Collection<DeviationStatus> findAllNotForManagerForDeviation();

    Collection<DeviationStatus> findAllForAccident();
}
