package dbmi_server.resources;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import dbmi_server.database.Airport;
import dbmi_server.database.PMF;

public class AllAirportsResource extends ServerResource {

    @Get
    public Representation getAllAirports() throws Exception {
        JSONObject airportsObject = new JSONObject();
        JSONArray airportsArray = new JSONArray();
        try {
            addAirports(airportsArray);
            airportsObject.put("airports", airportsArray);
            return getRepresentation(airportsObject);
        } catch (Exception e) {
            airportsObject.put("error", e.getMessage());
            return getRepresentation(airportsObject);
        }
    }

    @SuppressWarnings("unchecked")
    private void addAirports(JSONArray jsonArray) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Airport.class);
        try {
            List<Airport> airports = (List<Airport>) query.execute();
            if (airports.iterator().hasNext()) {
                for (Airport airport : airports) {
                    jsonArray.put(airport.getIcaoCode());
                }
            } else {
                jsonArray.put("");
            }
        } finally {
            query.closeAll();
        }
    }

    private Representation getRepresentation(JSONObject jsonObject) {
        JsonRepresentation jr = new JsonRepresentation(jsonObject);
        jr.setCharacterSet(CharacterSet.UTF_8);
        return jr;
    }
}
