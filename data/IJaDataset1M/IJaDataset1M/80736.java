package playground.christoph.withinday2;

import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringActivityIdentifier;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringActivityIdentifierFactory;

public class OldPeopleIdentifierFactory extends DuringActivityIdentifierFactory {

    private Netsim mobsim;

    public OldPeopleIdentifierFactory(Netsim mobsim) {
        this.mobsim = mobsim;
    }

    @Override
    public DuringActivityIdentifier createIdentifier() {
        DuringActivityIdentifier identifier = new OldPeopleIdentifier(mobsim);
        identifier.setIdentifierFactory(this);
        return identifier;
    }
}
