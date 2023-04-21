package no.ugland.utransprod.gui.handlers;

import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.PostShipment;

/**
 * Interface for objekter som skal v�re med i fax
 * 
 * @author atle.brekka
 * 
 */
public interface TransportLetterObject {

    /**
	 * Henter ordre
	 * 
	 * @return ordre
	 */
    Order getLetterOrder();

    /**
	 * Henter navn p� objekt
	 * 
	 * @return navn
	 */
    String getName();

    /**
	 * Henter detaljer
	 * 
	 * @return detaljer
	 */
    String getDetails();

    /**
	 * Henter typenavn
	 * 
	 * @return typenavn
	 */
    String getTypeName();

    /**
	 * Sjekker om objekt er etterlevering
	 * 
	 * @return true derosm etterlevering
	 */
    Boolean isNotPostShipment();

    /**
	 * Henter etterlevering
	 * 
	 * @return etterlevering
	 */
    PostShipment getPostShipment();

    Integer getNumberOf();
}
