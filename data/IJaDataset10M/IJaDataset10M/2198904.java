package v201008;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201008.cm.UserList;
import com.google.api.adwords.v201008.cm.UserListPage;
import com.google.api.adwords.v201008.cm.UserListSelector;
import com.google.api.adwords.v201008.cm.UserListServiceInterface;

/**
 * This example gets all users lists. To add a user list, run AddUserList.java.
 *
 * Tags: UserListService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllUserLists {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            UserListServiceInterface userListService = user.getService(AdWordsService.V201008.USER_LIST_SERVICE);
            UserListSelector selector = new UserListSelector();
            UserListPage page = userListService.get(selector);
            if (page.getEntries() != null) {
                for (UserList userList : page.getEntries()) {
                    System.out.printf("User list with name '%s', id '%d', status '%s', and number of " + "users '%d' was found.\n", userList.getName(), userList.getId(), userList.getStatus(), userList.getSize());
                }
            } else {
                System.out.println("No user lists were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
