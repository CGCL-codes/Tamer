package consumerest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import it.polimi.miaria.perla2gis.db.perla.Temperature;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/** Jersey REST client generated for REST resource:SensoriFacadeREST [it.polimi.miaria.perla2gis.db.perla.sensori]<br>
 *  USAGE:<pre>
 *        sensorilist client = new sensorilist();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 *  </pre>
 */
public class TemperatureServiceREST {

    private WebResource webResource;

    private Client client;

    private static final String BASE_URI = "http://localhost:8080/SocialEmis-WSNService/resources";

    public TemperatureServiceREST() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("it.polimi.miaria.perla2gis.db.perla.temperature");
    }

    public void remove(String id) throws UniformInterfaceException {
        webResource.path(java.text.MessageFormat.format("{0}", new Object[] { id })).delete();
    }

    public String countREST() throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path("count");
        return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T findAll_XML(Class<T> responseType) throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws UniformInterfaceException {
        WebResource resource = webResource;
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void edit_XML(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_XML).put(requestEntity);
    }

    public void edit_JSON(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(requestEntity);
    }

    public void create_XML(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_XML).post(requestEntity);
    }

    public void create_JSON(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(requestEntity);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] { from, to }));
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] { from, to }));
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] { id }));
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] { id }));
        return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.destroy();
    }

    public static ArrayList getAllTemperaturesByIdSensore(String idSensore) {
        TemperatureServiceREST cl = new TemperatureServiceREST();
        JSONArray jsonobjectarray = cl.findAll_JSON(JSONArray.class);
        ArrayList<Temperature> temperatures = new ArrayList<Temperature>();
        for (int i = 0; i < jsonobjectarray.length(); i++) {
            try {
                JSONObject object = jsonobjectarray.getJSONObject(i);
                String json = "{'temperature':" + object.toString() + "}";
                XStream xstream = new XStream(new JettisonMappedXmlDriver());
                xstream.alias("temperature", Temperature.class);
                Temperature t = (Temperature) xstream.fromXML(json);
                if (t.getTemperaturePK().getIdSensore().equals(idSensore)) {
                    temperatures.add(t);
                }
            } catch (JSONException ex) {
                Logger.getLogger(SensoreServiceREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cl.close();
        return temperatures;
    }
}
