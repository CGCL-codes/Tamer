package it.webscience.kpeople.dal.event;

import it.webscience.kpeople.be.Event;
import it.webscience.kpeople.be.EventFilter;
import it.webscience.kpeople.be.ObjectType;
import it.webscience.kpeople.be.User;
import it.webscience.kpeople.dal.exception.KPeopleDAOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per la classe DAO relativa agli eventi.
 */
public interface IEventDAO {

    /**
     * @param eventFilter oggetto contenente i parametri di ricerca.
     * @param user utente che ha effttuato la chiamata al servizio.
     * @return lista degli eventi associati ad un processo.
     * @throws SQLException eccezione generata durante l'elaborazione.
     */
    List<Event> getEvents(final EventFilter eventFilter, final User user) throws KPeopleDAOException;

    /**
     * salva un oggetto di tipo Event.
     * @param event
     *            oggetto da salvare su db
     * @param conn
     *            connection per la gestione della transazione
     * @param user
     *            first action performer
     * @param now
     *            first action date
     * @return id autogenerato
     * @throws SQLException
     *             if a database access error occurs
     */
    int saveEvent(final Event event, final User user, final java.util.Date now, final Connection conn) throws SQLException;

    /**
	 * 
	 * @param pConn connessione verso il db hpm
	 * @param pEvent evento da aggiornare
	 * @return pattern aggiornato
	 * @throws SQLException eccezione
	 */
    void updateEvent(final Connection pConn, final Event pEvent) throws SQLException;

    /**
	 * Memorizza una associazione tra un pattern e un evento
	 * @param pConn connessione verso il db HPM
	 * @param pIdAttachment identificativo dell'attachment da associare
	 * @param pIdEvent identificativo dell'evento da associare
	 * @throws SQLException
	 */
    void saveEventAttachmentAssociation(final Connection pConn, final int pIdAttachment, final int pIdEvent) throws SQLException;

    /**
	 * Verifica se esiste una associazione tra evento e attachment
	 * @param pIdAttachment identificativo dell'attachment da associare
	 * @param pIdEvent identificativo dell'evento da associare
	 * @throws SQLException
	 */
    boolean existsEventAttachmentAssociation(final int pIdAttachment, final int pIdEvent) throws SQLException;
}
