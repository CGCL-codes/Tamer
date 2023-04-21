package backend;

import java.lang.*;
import connectivity.*;

/**
 * This is the superclass for DCLocalGameEnv and DCNetworkGameEnv. It only
 * implements the correct interfaces and some attributes.
 * @Author		Davy Herben
 */
public abstract class DCGameEnv implements DCMessageable {

    protected DCMessageable msgCarrier;

    /**
	 * Sends a DCMessage to the class
	 */
    public abstract void sendMessage(DCMessage message);
}
