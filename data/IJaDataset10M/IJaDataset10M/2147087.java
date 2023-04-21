package tms.client.admin;

import java.util.ArrayList;
import tms.client.accesscontrol.User;
import tms.client.accesscontrol.UserCategory;
import tms.client.entities.Field;
import tms.client.entities.InputModel;
import tms.client.entities.PresetAttribute;
import tms.client.entities.TerminologyDatabase;
import tms.client.entities.Topic;

/**
 * Stores local client data.
 * 
 * @author Wildrich Fourie
 * @author Werner Liebenberg.
 */
public class LocalCache {

    private static TerminologyDatabase currentDatabase;

    private static Topic currentTopic;

    private static Field currentField;

    private static PresetAttribute currentPresetAttribute;

    private static InputModel currentInputModel;

    private static User currentUser;

    private static UserCategory currentCategory;

    private static ArrayList<User> users;

    private static ArrayList<UserCategory> userCategories;

    public LocalCache() {
    }

    public static TerminologyDatabase getCurrentDatabase() {
        return currentDatabase;
    }

    public static void setCurrentDatabase(TerminologyDatabase currentDatabase) {
        LocalCache.currentDatabase = currentDatabase;
    }

    public static Topic getCurrentTopic() {
        return currentTopic;
    }

    public static void setCurrentTopic(Topic currentTopic) {
        LocalCache.currentTopic = currentTopic;
    }

    public static Field getCurrentField() {
        return currentField;
    }

    public static void setCurrentField(Field currentField) {
        LocalCache.currentField = currentField;
    }

    public static PresetAttribute getCurrentPresetAttribute() {
        return currentPresetAttribute;
    }

    public static void setCurrentPresetAttribute(PresetAttribute currentPresetAttribute) {
        LocalCache.currentPresetAttribute = currentPresetAttribute;
    }

    public static InputModel getCurrentInputModel() {
        return currentInputModel;
    }

    public static void setCurrentInputModel(InputModel currentInputModel) {
        LocalCache.currentInputModel = currentInputModel;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        LocalCache.currentUser = currentUser;
    }

    public static void setCurrentCategory(UserCategory currentCategory) {
        LocalCache.currentCategory = currentCategory;
    }

    public static UserCategory getCurrentCategory() {
        return currentCategory;
    }

    public static void setUsers(ArrayList<User> users) {
        LocalCache.users = users;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUserCategories(ArrayList<UserCategory> userCategories) {
        LocalCache.userCategories = userCategories;
    }

    public static ArrayList<UserCategory> getUserCategories() {
        return userCategories;
    }
}
