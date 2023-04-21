package fi.foyt.cs.api.json;

import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import fi.foyt.cs.controller.UserController;
import fi.foyt.cs.persistence.domainmodel.user.User;
import fi.foyt.cs.persistence.domainmodel.user.UserLocation;

public class UserRenderer implements EntityRenderer<User> {

    @Override
    public JSONObject getAsJson(Locale locale, User user) {
        JSONObject jsonObject = new JSONObject();
        UserController userController = new UserController();
        UserLocation userLocation = userController.findUserLocationByUser(user);
        try {
            jsonObject.put("id", user.getId().getId());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("firstName", user.getFirstName());
            if (userLocation != null) {
                jsonObject.put("location", EntityRendererVault.getInstance().renderEntity(locale, userLocation));
            }
        } catch (JSONException e1) {
            throw new RenderingException(e1);
        }
        return jsonObject;
    }
}
