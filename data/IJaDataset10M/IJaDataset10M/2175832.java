package moteaccess;

/** Basic mote accessor for system with no reservation. */
public class MoteAccess extends AbstractMoteAccess {

    /** Check if mote has a reservation.
	 *
	 * @param mote_id	ID of the mote to check for reservation.
	 * @param session_id	ID of the client session.
	 * @return Always false, since reservation is not supported.
	 */
    protected boolean hasReservation(long mote_id, String session_id) {
        return false;
    }
}
