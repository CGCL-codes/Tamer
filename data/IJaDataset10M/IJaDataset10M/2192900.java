package playground.wrashid.parkingSearch.withinday;

import org.matsim.ptproject.qsim.interfaces.Mobsim;
import playground.christoph.withinday.replanning.identifiers.interfaces.DuringLegIdentifier;
import playground.christoph.withinday.replanning.identifiers.interfaces.DuringLegIdentifierFactory;

public class YoungPeopleIdentifierFactory implements DuringLegIdentifierFactory {

    private Mobsim mobsim;

    public YoungPeopleIdentifierFactory(Mobsim mobsim) {
        this.mobsim = mobsim;
    }

    @Override
    public DuringLegIdentifier createIdentifier() {
        DuringLegIdentifier identifier = new YoungPeopleIdentifier(mobsim);
        identifier.setIdentifierFactory(this);
        return identifier;
    }
}
