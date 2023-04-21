package whiteboard.poll;

import java.util.HashMap;
import java.util.Vector;

/**
 * A poll that users can vote in for each of four options. A poll has a title,
 * an admin who created it (a professor), the course for which it is created,
 * and a unique pollid used as an identifier in the database.
 * 
 * Instructors can create polls, and students can vote in them.
 * 
 * @author Gooble
 *
 */
public class Poll {

    static final int MAX_OPTIONS = 4;

    private String admin;

    private String courseid;

    private String title;

    private int pollid;

    private HashMap<String, Integer> pollOptions;

    public Poll(String admin, String courseid, String title, int pollid) {
        this.admin = admin;
        this.courseid = courseid;
        this.title = title;
        this.pollid = pollid;
        this.pollOptions = new HashMap<String, Integer>();
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setCourseId(String courseid) {
        this.courseid = courseid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPollId(int pollid) {
        this.pollid = pollid;
    }

    public String getAdmin() {
        return admin;
    }

    public String getCourseId() {
        return courseid;
    }

    public String getTitle() {
        return title;
    }

    public int getPollId() {
        return pollid;
    }

    public HashMap<String, Integer> getPollOptions() {
        return pollOptions;
    }

    public Vector<String> getKeys() {
        return new Vector<String>(pollOptions.keySet());
    }

    /**
	 * Add an option into the HashMap with the given option key and votes value.
	 * 
	 * @param option the option title
	 * @param votes the number of votes for the option
	 */
    public void addPollOption(String option, int votes) {
        pollOptions.put(option, votes);
    }

    /**
	 * Vote for the given option by increasing its value in the hashmap by 1.
	 * 
	 * @param option the option to vote for
	 */
    public void voteForOption(String option) {
        int votes = pollOptions.get(option);
        pollOptions.put(option, votes + 1);
    }

    public int getVotes(String choice) {
        return pollOptions.get(choice);
    }
}
