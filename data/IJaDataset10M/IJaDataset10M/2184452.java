package net.sf.doolin.app.sc.game.managed;

import java.util.Map;
import net.sf.doolin.app.sc.engine.ClientID;
import net.sf.doolin.app.sc.engine.support.AbstractManagedClient;
import net.sf.doolin.app.sc.game.SCClientResponse;
import net.sf.doolin.app.sc.game.SCClientState;
import net.sf.doolin.app.sc.game.support.SCClientResponseImpl;

public class SCAIClient extends AbstractManagedClient<SCClientState, SCClientResponse> {

    public SCAIClient(ClientID clientID, Map<String, String> properties) {
        super(clientID, properties);
    }

    @Override
    public SCClientResponse play(int turnNumber, SCClientState state) {
        return new SCClientResponseImpl(state.getClientID());
    }
}
