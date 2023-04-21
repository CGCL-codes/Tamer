package gov.nist.mel.simsync.rest.client;

import gov.nist.mel.simsync.SimSync;
import gov.nist.mel.simsync.SimulationGroup;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.json.JsonRepresentation;

/**
 * 
 * @author guiradde
 */
public class SimSyncClient extends RestClientBase implements SimSync {

    public SimSyncClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void addGroup(SimulationGroup group) {
    }

    public SimulationGroup getGroup(String name) {
        Request request = new Request(Method.GET, baseUrl + "/simgroups/name");
        Response response = client.handle(request);
        SimulationGroup res = null;
        try {
            JSONObject JSONResponse = new JsonRepresentation(response.getEntity()).toJsonObject();
            res = parseSimulationGroup(JSONResponse);
        } catch (Exception ex) {
            Logger.getLogger(SimSyncClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public Collection<SimulationGroup> getGroups() {
        Collection<SimulationGroup> res = new ArrayList<SimulationGroup>();
        Request request = new Request(Method.GET, baseUrl + "/simgroups/");
        Response response = client.handle(request);
        try {
            JSONArray JSONResponse = new JsonRepresentation(response.getEntity()).toJsonArray();
            for (int i = 0; i < JSONResponse.length(); i++) {
                res.add(parseSimulationGroup(JSONResponse.getJSONObject(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(SimSyncClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public void removeGroup(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private SimulationGroup parseSimulationGroup(JSONObject jsonGroup) {
        String name = jsonGroup.optString("name");
        int capacity = jsonGroup.optInt("capacity");
        return new SimulationGroupClient(name, capacity, baseUrl);
    }

    public void addPropertyChangeListener(PropertyChangeListener x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
