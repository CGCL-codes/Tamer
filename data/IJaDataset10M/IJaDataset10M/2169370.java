package gothwag.net;

import gothwag.core.*;
import java.util.*;

/**
 * <p>
 * 
 * </p>
 */
public class RemotePlayer extends Player {

    public RemotePlayer(Judge j) {
        super(j);
    }

    public String getName() {
        return "Remote";
    }

    public void sendMessage(String message) {
    }

    public void sendMove(Player player, Move move) {
    }
}
