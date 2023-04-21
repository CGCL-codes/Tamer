package v201203.userservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.Statement;
import com.google.api.ads.dfp.v201203.User;
import com.google.api.ads.dfp.v201203.UserPage;
import com.google.api.ads.dfp.v201203.UserServiceInterface;

/**
 * This example updates all users by adding "Sr." to the end of each
 * name (after a very large baby boom and lack of creativity). To
 * determine which users exist, run GetAllUsersExample.java.
 *
 * Tags: UserService.getUsersByStatement, UserService.updateUsers
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateUsersExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            UserServiceInterface userService = user.getService(DfpService.V201203.USER_SERVICE);
            Statement filterStatement = new Statement("LIMIT 500", null);
            UserPage page = userService.getUsersByStatement(filterStatement);
            if (page.getResults() != null) {
                User[] users = page.getResults();
                for (User usr : users) {
                    usr.setName(usr.getName() + " Sr.");
                }
                users = userService.updateUsers(users);
                if (users != null) {
                    for (User usr : users) {
                        System.out.println("A user with ID \"" + usr.getId() + "\", name \"" + usr.getName() + "\", and role \"" + usr.getRoleName() + "\" was updated.");
                    }
                } else {
                    System.out.println("No users updated.");
                }
            } else {
                System.out.println("No users found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
