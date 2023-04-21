package javaclient2.structures.log;

import javaclient2.structures.*;

/**
 * Request/reply: Rewind playback
 * To rewind log playback to beginning of logfile, send a
 * PLAYER_LOG_REQ_SET_READ_REWIND request.  Does not affect playback state
 * (i.e., whether it is started or stopped.  Null response. 
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v2.0 - Player 2.0 supported
 * </ul>
 */
public class PlayerLogSetReadRewind implements PlayerConstants {
}
