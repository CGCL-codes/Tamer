package dbmi_server.resources;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import dbmi_server.database.Country;
import dbmi_server.database.PMF;

public class CountriesResource extends ServerResource {

    @Get
    public Representation getCountries() throws Exception {
        JSONObject countriesObject = new JSONObject();
        try {
            addCountries(countriesObject);
            return getRepresentation(countriesObject);
        } catch (Exception e) {
            countriesObject = new JSONObject();
            countriesObject.put("error", e.getMessage());
            return getRepresentation(countriesObject);
        }
    }

    @SuppressWarnings("unchecked")
    private void addCountries(JSONObject jsonObject) throws JSONException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Country.class);
        query.setOrdering("name desc");
        try {
            List<Country> countries = (List<Country>) query.execute();
            if (countries.iterator().hasNext()) {
                for (Country country : countries) {
                    jsonObject.put(country.getCode(), country.getName());
                }
            } else {
                jsonObject.put("", "");
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
