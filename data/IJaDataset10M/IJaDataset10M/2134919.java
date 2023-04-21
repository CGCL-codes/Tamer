package playground.dgrether.designdrafts;

import java.util.Set;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;

/**
 * @author dgrether
 *
 */
public class DgTurnInfo {

    private final Id fromLinkId;

    private Set<String> modes;

    public DgTurnInfo(final Id fromLinkId) {
        this.fromLinkId = fromLinkId;
    }

    public Id getFromLinkId() {
        return this.fromLinkId;
    }

    public Set<TransportMode> getAllowedModes() {
        return null;
    }

    public Set<Id> getToLinkIdsAllModes() {
        return null;
    }

    public Set<Id> getToLinkIds(TransportMode mode) {
        return null;
    }

    public void addToLink(TransportMode mode) {
    }
}
