package logic.comparators;

import data.Users;
import java.util.Comparator;

/**
 * Comparator used to sort users based on their category match rating (used for collaborative filtering) from highest yo lowest
 * @author Branislav Vaclav
 */
public class UsersOrderingComparator implements Comparator<Users> {

    public int compare(Users user1, Users user2) {
        return user1.getCategoryMatchRating().compareTo(user2.getCategoryMatchRating()) * -1;
    }
}
