package tms.client.services;

import java.util.ArrayList;
import tms.client.accesscontrol.UserCategory;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserCategoriesServiceAsync {

    void getAllUserCategories(String authToken, AsyncCallback<ArrayList<UserCategory>> callback);

    void updateUserCategory(String authToken, UserCategory userCategory, AsyncCallback<Void> callback);

    void createUserCategory(String authToken, UserCategory userCategory, AsyncCallback<UserCategory> callback);

    void getAllUserCategoriesWithUsers(String authToken, AsyncCallback<ArrayList<UserCategory>> callback);
}
