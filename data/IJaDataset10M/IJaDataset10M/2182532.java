package no.ugland.utransprod.dao;

import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.model.TransportSumV;

/**
 * Interface for dao mot view TRANSPORT_SUM_V
 * 
 * @author atle.brekka
 * 
 */
public interface TransportSumVDAO extends DAO<TransportSumV> {

    /**
	 * Finner sum for �r og uke
	 * 
	 * @param currentYear
	 * @param currentWeek
	 * @return sum
	 */
    TransportSumV findYearAndWeek(Integer currentYear, Integer currentWeek);

    /**
	 * Finner sum for �r,uke og produktomr�degruppe
	 * 
	 * @param currentYear
	 * @param currentWeek
	 * @param productAreaGroup
	 * @return sum
	 */
    TransportSumV findYearAndWeekByProductAreaGroup(Integer currentYear, Integer currentWeek, ProductAreaGroup productAreaGroup);
}
