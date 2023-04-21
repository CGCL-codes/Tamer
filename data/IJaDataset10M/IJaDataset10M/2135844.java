package es.eucm.eadventure.engine.comm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JApplet;
import es.eucm.eadventure.common.data.assessment.AssessmentProperty;
import es.eucm.eadventure.engine.adaptation.AdaptationEngine;
import netscape.javascript.JSObject;

/**
 * Implementation of the AsynchronousCommunication API designed to function as
 * an applet that invokes Javascript operations from its parent window and
 * receives callbacks from that Javascript module.
 * 
 * 
 * This implementation is designed to communicate with an LD environment with
 * eAdventureWebSupport.
 * 
 * @author Pablo Moreno Ger
 * 
 */
public abstract class AsynchronousCommunicationAppletLD extends JApplet implements AsynchronousCommunicationApi {

    private boolean commWorking = false;

    private String userId = "";

    private String runId = "";

    protected boolean windowed = false;

    private String propertyNames = null;

    private AdaptationEngine adaptationEngine;

    public void readParameters() {
        userId = getParameter("USER_ID");
        if (userId == null) {
            userId = "";
            System.out.println("No UserId param available");
        }
        runId = getParameter("RUN_ID");
        if (runId == null) {
            runId = "";
            System.out.println("No RunId param available");
        }
        String temp = getParameter("WINDOWED");
        if (temp != null && temp.equalsIgnoreCase("yes")) windowed = true;
    }

    public String getRunId() {
        return runId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isConnected() {
        return commWorking;
    }

    public void communicationEstablished(String serverText) {
        System.out.println("OK Notification received: " + serverText);
        this.commWorking = true;
    }

    public void communicationFailed(String reason) {
        System.out.println("No communication link available: " + reason);
        this.commWorking = false;
    }

    public void startCommunication(String initMessage) {
        JSObject win = null;
        String command = "javascript:startCommunication(\"user_id:" + userId + ";run_id" + runId + "\");";
        try {
            System.out.println("Establishing communications...");
            System.out.println("Issuing command: " + command);
            win = JSObject.getWindow(this);
            win.eval(command);
        } catch (Exception e) {
        }
    }

    public void notifyRelevantState(List<AssessmentProperty> properties) {
        StringBuffer sb = new StringBuffer("");
        for (AssessmentProperty ap : properties) {
            sb.append(ap.getId());
            sb.append("=");
            sb.append(ap.getValue());
            sb.append(",");
        }
        String encodedProperties = sb.toString();
        if (encodedProperties.endsWith(",")) {
            encodedProperties = encodedProperties.substring(0, encodedProperties.length() - 1);
        }
        if (!encodedProperties.equals("")) {
            JSObject win = null;
            String command = "javascript:notifyRelevantState(\"" + userId + "," + runId + "#" + encodedProperties + "\");";
            try {
                System.out.println("Notifying new state...");
                System.out.println("Issuing command: " + command);
                win = JSObject.getWindow(this);
                win.eval(command);
            } catch (Exception e) {
            }
        }
    }

    public void getAdaptedState(Set<String> properties) {
        if (propertyNames == null) {
            StringBuffer sb = new StringBuffer("");
            for (String property : properties) {
                sb.append(property);
                sb.append(",");
            }
            propertyNames = sb.toString();
            if (propertyNames.endsWith(",")) {
                propertyNames = propertyNames.substring(0, propertyNames.length() - 1);
            }
        }
        if (!propertyNames.equals("")) {
            JSObject win = null;
            String command = "javascript:getState(\"" + userId + "," + runId + "#" + propertyNames + "\");";
            try {
                System.out.println("Retrieving properties...");
                System.out.println("Issuing command: " + command);
                win = JSObject.getWindow(this);
                win.eval(command);
            } catch (Exception e) {
            }
        }
    }

    public void newState(String newState) {
        System.out.println("The server answers with a new State: " + newState);
        Map<String, String> decodedState = new HashMap<String, String>();
        String[] properties = newState.split(",");
        for (String property : properties) {
            String[] aux = property.split("=");
            decodedState.put(aux[0], aux[1]);
        }
        adaptationEngine.processExternalState(decodedState);
    }

    public void setAdaptationEngine(AdaptationEngine engine) {
        this.adaptationEngine = engine;
    }
}
