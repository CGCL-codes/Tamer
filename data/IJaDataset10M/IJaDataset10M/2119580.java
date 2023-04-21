package jadex.contractnet.seller;

import alice.c4jadex.bridge.*;
import alice.cartago.*;
import jadex.contractnet.common.*;
import jadex.runtime.*;

/**
 *    ObserveCounterPlan
 * @author Michele Piunti
 */
public class ObserveArtifactPlan extends CartagoPlan {

    String aName, sensorName, filter;

    /** Creates a new instance of ObserveCounterPlan */
    public ObserveArtifactPlan() {
    }

    public void body() {
        log("Hello! going to focus:" + aName + " with sensor: " + sensorName);
        try {
            aName = (String) getParameter("aName").getValue();
        } catch (Exception e) {
            log("Problem occurred in retrieving ObserveArtifact paramenters");
            return;
        }
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
    }

    public void aborted() {
        if (isAbortedOnSuccess()) log(" GOAL ACHIEVED"); else log("GOAL FAILED");
        IGoal unfocus = createGoal("stop_focus");
        unfocus.getParameter("artifact_name").setValue(aName);
        dispatchSubgoalAndWait(unfocus);
    }
}
