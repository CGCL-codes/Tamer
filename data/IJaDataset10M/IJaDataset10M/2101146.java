package com.osgisamples.congress.business;

import java.util.Set;
import com.osgisamples.congress.domain.Congress;
import com.osgisamples.congress.domain.Registrant;
import com.osgisamples.congress.domain.Session;

/**
 * @author Jettro.Coenradie
 * Manager object that handles all calls to functionality related to the congress registration process.
 * It contains methods for storing new/updated congresses, registering participants, stands, sessions, etc.
 */
public interface CongressManager {

    /**
	 * Store a new or updated congress. 
	 * @param congress Congress item to store
	 */
    public void storeCongress(Congress congress);

    /**
	 * Creates a new CongressRegistration object for the provided (new) Registrant and congress. You do not need
	 * to provided stored instances of the registrant and congress. If the provided data is not sufficient to
	 * create a new registrant or find an existing registrant or congress, and exception is thrown.
	 * @param registrant Registrant object used to create a new Registrant or find an existing one
	 * @param congress Congress object used to find the stored congress
	 * @return String Registration number for the (new) Registrant
	 * @throws CongressNotFoundException thrown if the provided data is not enough to determine exactly one Congress
	 * @throws RegistrantValidationException thrown if the provided registrant is not correct
	 */
    public String registerNewRegistrantForCongress(Registrant registrant, Congress congress) throws CongressNotFoundException, RegistrantValidationException;

    /**
	 * Returns a set with all registrants for the provided congress, if the data provided is not sufficient to
	 * identify a unique Congress a CongressNotFoundException is thrown.
	 * @param congress Congress item that contains items we can search on (mainly id or name)
	 * @return Set with Registrants for the congress
	 * @throws CongressNotFoundException Thrown if the provided congress information is not sifficient to uniquely
	 * identify a congress.
	 */
    public Set<Registrant> listAllRegistrantsForCongress(Congress congress) throws CongressNotFoundException;

    /**
	 * Creates a new session if the name of the session does not exist. If the session allready exists a 
	 * SessionValidationexception is thrown.
	 * @param session Session object to be created
	 * @param congress Congress to attach the new session to
	 * @return String with the id of the new session
	 * @throws CongressNotFoundException Thrown if the provided congress information is not sifficient to uniquely
	 * identify a congress.
	 * @throws SessionValidationException Thrown if the session allready exists (identified by name)
	 */
    public String registerNewSessionForCongress(Session session, Congress congress) throws CongressNotFoundException, SessionValidationException;

    /**
	 * Returns a set with all sessions for the provided congress, if the data provided is not sufficient to
	 * identify a unique Congress a CongressNotFoundException is thrown.
	 * @param congress Congress item that contains items we can search on (mainly id or name)
	 * @return Set with sessions for the congress
	 * @throws CongressNotFoundException Thrown if the provided congress information is not sifficient to uniquely
	 * identify a congress.
	 */
    public Set<Session> listAllSessionsForCongress(Congress congress) throws CongressNotFoundException;

    /**
	 * Updates the sessions a Registrant for a congress is registered for as a participant. A CongressNotFoundException
	 * is thrown if the congress is not uniquely determined. If the registrant is not registered for the found cognress
	 * a RegistrantNotFoundForCongress is thrown. Registrants can only be registered for sessions that are registered
	 * for the found congress, in any other case a SessionsNotAvailableForCongress exception is thrown
	 * @param congress Congress item that contains items we can search on (mainly id or name)
	 * @param registrant Registrant that should allready be registered for the provided Congress
	 * @param sessions Set with sessions that should all be attached to the Congress allready
	 * @throws CongressNotFoundException Thrown if provided data cannot be used to identify a unique congress
	 * @throws SessionNotAvailableForCongressException Thrown if the provided session is not registered for the congress
	 * @throws RegistrantNotFoundForCongressException Thrown if the provided Registrants is not registered for the congress
	 */
    public void updateSessionsForParticipantOfCongress(Congress congress, Registrant registrant, Set<Session> sessions) throws CongressNotFoundException, SessionNotAvailableForCongressException, RegistrantNotFoundForCongressException;
}
