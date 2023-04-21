package simis.reputation.rt.ebay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implement the Ebay Reputation and Trust model.
 * From
 *  http://pages.ebay.com/help/feedback/questions/feedback.html
 *  
 *  @author IIIA, UBT
 */
public class EBay {

    /** The map with users and reputations. */
    protected HashMap<String, User> users;

    /**
   * Default empty constructor.
   */
    public EBay() {
        this.users = new HashMap<String, User>(0);
    }

    /**
   * Report a new feedback to the model.
   *
   * @param feedback reported.
   */
    public void newFeedback(FeedbackRating feedback) {
        User user = this.getUser(feedback.getUser());
        User ratingUser = this.getUser(feedback.getRatingUser());
        user.addReceivedFeedback(feedback);
        ratingUser.addLeftFeedback(feedback);
    }

    /**
   * Retrun the user from the user's map.
   *
   * @param user id to retrieve.
   * @return the user from the user's map.
   */
    public User getUser(String user) {
        if (!this.users.containsKey(user)) {
            this.users.put(user, new User(user));
        }
        return this.users.get(user);
    }

    /**
   * Return the user's star from the user's map.
   *
   * @param user id to retrieve.
   * @return the user's star from the user's map.
   */
    public String getUserStar(String user) {
        if (!this.users.containsKey(user)) {
            this.users.put(user, new User(user));
        }
        return this.users.get(user).getStar();
    }

    /**
   * Return the received feedbacks for a given user.
   *
   * @param userId to retrieve it's received feedbacks.
   * @return the received feedbacks for a given user.
   */
    public ArrayList getReceivedFeedbacks(String userId) {
        User user = this.getUser(userId);
        return user.getReceivedFeedbacks();
    }

    /**
   * Return the received feedbacks for a given user and sign.
   *
   * @param userId to retrieve it's received feedbacks.
   * @param sign of the received feedbacks.
   * @return the received feedbacks for a given user and sign.
   */
    public ArrayList getReceivedFeedbacks(String userId, int sign) {
        User user = this.getUser(userId);
        return user.getReceivedFeedbacks(sign);
    }

    /**
   * Return the left feedbacks for a given user.
   *
   * @param userId to retrieve it's left feedbacks.
   * @return the left feedbacks for a given user.
   */
    public ArrayList getLeftFeedbacks(String userId) {
        User user = this.getUser(userId);
        return user.getLeftFeedbacks();
    }

    /**
   * Return the left feedbacks for a given user and sign.
   *
   * @param userId to retrieve it's left feedbacks.
   * @param sign of the left feedbacks.
   * @return the left feedbacks for a given user and sign.
   */
    public ArrayList getLeftFeedbacks(String userId, int sign) {
        User user = this.getUser(userId);
        return user.getLeftFeedbacks(sign);
    }
}
