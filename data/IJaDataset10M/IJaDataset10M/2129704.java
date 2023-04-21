package jadex.contractnet.buyer;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import alice.cartago.examples.contractnet.AgentIdentifier;
import alice.cartago.examples.contractnet.Proposal;
import jadex.contractnet.common.*;
import jadex.runtime.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *    InitiatePlan
 * @author Michele Piunti
 */
public class PurchaseBookPlan extends CartagoPlan {

    String sensorName, agentName;

    Op op;

    /** Creates a new instance of InitiatePlan */
    public PurchaseBookPlan() {
        sensorName = (String) getBeliefbase().getBelief("my_sensor").getFact();
        agentName = getAgentName();
    }

    public void body() {
        Order order = (Order) getParameter("order").getValue();
        String aName = getParameter("aName").getValue().toString();
        String[] sellers = (String[]) getBeliefbase().getBeliefSet("sellers").getFacts();
        int acceptable_price = 10;
        IGoal lookup = createGoal("lookup");
        lookup.getParameter("artifact_name").setValue(aName);
        dispatchSubgoalAndWait(lookup);
        ArtifactId aid = (ArtifactId) lookup.getParameter("aid").getValue();
        if (aid == null) {
            System.out.println("Artifact not found, creating it...");
            String aTemplate = "alice.cartago.examples.basic.Count";
            ArtifactConfig aConfig = new ArtifactConfig();
            IGoal make = createGoal("make");
            make.getParameter("artifact_name").setValue(aName);
            make.getParameter("artifact_template").setValue(aTemplate);
            make.getParameter("artifact_config").setValue(aConfig);
            dispatchSubgoalAndWait(make);
        } else log("Artifact: " + aName + " found with id: " + aid);
        IGoal focus = createGoal("focus");
        focus.getParameter("artifact_name").setValue(aName);
        dispatchSubgoalAndWait(focus);
        IGoal cnp = createGoal("cnp_initiate");
        cnp.getParameter("aName").setValue(aName);
        cnp.getParameter("cfp").setValue(order.getTitle());
        cnp.getParameter("cfp_info").setValue(new Integer(acceptable_price));
        cnp.getParameterSet("receivers").addValues(sellers);
        dispatchSubgoalAndWait(cnp);
    }
}
