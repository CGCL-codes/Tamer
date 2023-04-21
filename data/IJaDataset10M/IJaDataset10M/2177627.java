package sfeir.gwt.ergosoom.server.service.rest;

import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import sfeir.gwt.ergosoom.client.model.Profile;
import sfeir.gwt.ergosoom.server.service.BackupAccess;
import sfeir.gwt.ergosoom.server.service.ProfileAccess;

public class BackupService extends ServerResource {

    private static final Logger logger = Logger.getLogger(BackupService.class.getName());

    @Post
    public Representation restore() {
        try {
            String email = (String) getRequest().getAttributes().get("alias");
            Profile p = new ProfileAccess().get(email);
            if (null == p) {
                setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Not Found");
                return new StringRepresentation("Not Found");
            }
            String passwd = (String) getRequest().getAttributes().get("passwd");
            if (!passwd.equals(p.getPassword())) {
                setStatus(Status.CLIENT_ERROR_FORBIDDEN, "Bad password");
                return new StringRepresentation("Bad password");
            }
            JSONArray contacts = BackupAccess.get(email);
            JsonRepresentation repr = new JsonRepresentation(contacts);
            logger.info(contacts.length() + " contacts restored by " + email);
            return repr;
        } catch (IllegalArgumentException e) {
            logger.severe(e.toString());
        } catch (Exception e) {
            logger.severe(e.toString());
        }
        logger.severe("I shouldn't be here !");
        return null;
    }

    @Put
    public String save(String repr) {
        try {
            String email = (String) getRequest().getAttributes().get("alias");
            Profile p = new ProfileAccess().get(email);
            String passwd = (String) getRequest().getAttributes().get("passwd");
            if (null == p || !passwd.equals(p.getPassword())) {
                setStatus(Status.CLIENT_ERROR_FORBIDDEN, "Bad credentials");
                return null;
            }
            JSONArray contacts = new JSONArray(repr);
            boolean result = BackupAccess.save(contacts, email);
            if (result) return "Contact backup succeeded."; else return "NOK";
        } catch (IllegalArgumentException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
            e.printStackTrace();
        }
        return "Contacts saved";
    }
}
