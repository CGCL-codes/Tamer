package org.moyoman.comm.client;

import org.moyoman.util.*;
import java.util.*;

/** Any class which extends this abstract class can be used as one of
  * the players in a game of Go.  These classes validate their
  * own moves, and so do not need any external error checking.
  * An example of this class is the Moyoman back-end sever.
  */
public abstract class ValidatedPlayer extends Player {

    /** Create the ValidatedPlayer object.
	  * @param c The color of the player.
	  * @param h The handicap of the game.
	  * @throws InternalErrorException Thrown if the operation fails for any reason.
	  */
    public ValidatedPlayer(Color c, Handicap h) throws InternalErrorException {
        super(c, h);
    }

    /** All derived classes are validated, so return true.
	  * @return true, since all derived classes are validated.
	  */
    public boolean isValidatedUser() {
        return true;
    }
}
