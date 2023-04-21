package playground.jhackney.deprecated;

import java.util.ArrayList;
import org.matsim.plans.Plans;
import playground.jhackney.socialnet.SocialNetwork;

public class JGNSocialNetwork extends SocialNetwork {

    private String linkRemovalCondition_;

    private String linkStrengthAlgorithm_;

    public JGNSocialNetwork(Plans plans) {
        super(plans);
        setupIter = 10;
        System.out.println(this.getClass() + " is not written yet.");
    }

    public void generateLinks(int iteration) {
        System.out.println("*** " + this.getClass() + ".addLinks is not written yet.");
    }

    @Override
    public ArrayList getLinks() {
        return null;
    }

    public void removeLinks() {
    }
}
