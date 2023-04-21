package alice.cartago.security;

import alice.cartago.ArtifactId;
import alice.cartago.UserId;

public class AlwaysForbidObservePolicy implements IObservePolicy {

    public boolean allow(UserId aid, ArtifactId id, String propName) {
        return false;
    }
}
