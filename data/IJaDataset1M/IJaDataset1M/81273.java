package fbpwn.plugins.core;

import fbpwn.core.AuthenticatedAccount;
import fbpwn.core.FacebookAccount;
import fbpwn.core.FacebookException;
import fbpwn.ui.FacebookGUI;
import fbpwn.core.FacebookTask;
import fbpwn.core.FacebookTaskState;
import fbpwn.core.RequestState;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the check friend request task 
 */
public class CheckFriendRequestTask extends FacebookTask {

    private RequestState friendRequestState;

    /**
     * Constructs the task
     * @param FacebookGUI The GUI used for updating the task's progress
     * @param facebookProfile The victim's profile
     * @param authenticatedProfile The attacker's profile
     * @param workingDirectory The directory used to save all the dumped data
     */
    public CheckFriendRequestTask(FacebookGUI FacebookGUI, FacebookAccount facebookProfile, AuthenticatedAccount authenticatedProfile, File workingDirectory) {
        super(FacebookGUI, facebookProfile, authenticatedProfile, workingDirectory);
        friendRequestState = RequestState.Waiting;
    }

    /**
     * Initialize this task, called once the task is added to the queue 
     */
    @Override
    public void init() {
    }

    /**
     * Runs the check friend request task
     * @return true if completed, false if error occurred so that it will be rerun after a small delay.
     */
    @Override
    public boolean run() {
        setTaskState(FacebookTaskState.Running);
        getFacebookGUI().updateTaskProgress(this);
        try {
            try {
                getAuthenticatedProfile().sendFriendRequest(getFacebookTargetProfile().getProfilePageURL());
            } catch (FacebookException ex) {
                Logger.getLogger(CheckFriendRequestTask.class.getName()).log(Level.SEVERE, "Exception in thread: " + Thread.currentThread().getName(), ex);
                return false;
            }
            while (friendRequestState != RequestState.RequestAccepted) {
                if (checkForCancel()) {
                    return true;
                }
                friendRequestState = getAuthenticatedProfile().getFriendRequestState(getFacebookTargetProfile().getProfilePageURL());
                setMessage(friendRequestState.toString());
                setPercentage(0.0);
                getFacebookGUI().updateTaskProgress(this);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CheckFriendRequestTask.class.getName()).log(Level.SEVERE, "Exception in thread: " + Thread.currentThread().getName(), ex);
                }
            }
            setPercentage(100.0);
            getFacebookGUI().updateTaskProgress(this);
        } catch (IOException ex) {
            Logger.getLogger(CheckFriendRequestTask.class.getName()).log(Level.SEVERE, "Exception in thread: " + Thread.currentThread().getName(), ex);
            return false;
        }
        setPercentage(100.0);
        setTaskState(FacebookTaskState.Finished);
        getFacebookGUI().updateTaskProgress(this);
        return true;
    }

    @Override
    public String toString() {
        return "Check friend request";
    }
}
