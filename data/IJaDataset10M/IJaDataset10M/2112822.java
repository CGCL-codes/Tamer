package playground.mrieser.core.mobsim.network.api;

import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.Steppable;

public interface MobsimNetwork extends Steppable {

    public void beforeMobSim();

    public void afterMobSim();

    Map<Id, ? extends MobsimLink> getLinks();

    Map<Id, ? extends MobsimNode> getNodes();
}
