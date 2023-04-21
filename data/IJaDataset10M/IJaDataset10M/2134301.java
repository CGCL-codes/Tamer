package web.json.managers;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import db.beans.User;
import db.managers.UserManager;

public class GenerateUsersJsonObject {

    private JSONObject root = new JSONObject();

    private JSONArray object = new JSONArray();

    private JSONObject element = new JSONObject();

    /**
	 * 
	 * @param _key
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public String generateJsonObjectById(long _id) {
        User user = UserManager.getById(_id);
        element = generateJsonObject(user);
        object.add(element);
        root.put("users", object);
        return root.toString();
    }

    /**
	 * 
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public String generateJsonObjectAll() {
        List<User> users = UserManager.getAll();
        for (User user : users) {
            element = generateJsonObject(user);
            object.add(element);
        }
        root.put("users", object);
        return root.toString();
    }

    /**
	 * 
	 * @param _object
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private static JSONObject generateJsonObject(Object _object) {
        JSONObject element = new JSONObject();
        User user = (User) _object;
        element.put("id", new Long(user.getId()));
        element.put("name", new String(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName()));
        element.put("email", new String(user.getEmail()));
        element.put("username", new String(user.getUsername()));
        element.put("rights", new String(user.getRights()));
        return element;
    }
}
