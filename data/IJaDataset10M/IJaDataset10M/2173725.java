package fi.foyt.cs.api.system;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import fi.foyt.cs.api.APIResource;

/**
 * Resource that tells client whatever server is alive
 */
public class PingResource extends APIResource {

    @Get
    @Post
    public Representation represent() {
        JSONObject result = new JSONObject();
        try {
            result.put("alive", true);
        } catch (JSONException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
        return new JsonRepresentation(result);
    }
}
