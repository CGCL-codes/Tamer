package no.ugland.utransprod.dao;

import java.util.List;
import no.ugland.utransprod.model.FrontProductionV;
import no.ugland.utransprod.model.Produceable;
import no.ugland.utransprod.model.ProductAreaGroup;

/**
 * Interface for DAO mot view FRONT_PRODUCTION_V
 * @author atle.brekka
 *
 */
public interface FrontProductionVDAO extends DAO<FrontProductionV> {

    /**
	 * Finner alle fronter til produksjon
	 * @return fronter
	 */
    List<Produceable> findAll();

    /**
	 * Finner front som skal produseres basert p� ordrenummer
	 * 
	 * @param orderNr
	 * @return front
	 */
    List<Produceable> findByOrderNr(String orderNr);

    /**
	 * Oppdaterer
	 * @param frontProductionV
	 */
    void refresh(Produceable frontProductionV);

    /**
	 * Finner basert p� kunde
	 * @param customerNr
	 * @return frontproduksjon
	 */
    List<Produceable> findByCustomerNr(Integer customerNr);

    List<Produceable> findByCustomerNrProductAreaGroup(Integer customerNr, ProductAreaGroup productAreaGroup);

    List<Produceable> findByOrderNrAndProductAreaGroup(String orderNr, ProductAreaGroup productAreaGroup);
}
