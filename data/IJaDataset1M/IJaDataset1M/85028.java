package plugin.voting;

import eu.popeye.networkabstraction.communication.message.PopeyeMessage;

/**
 *
 * @author root
 */
public class VoteMessage implements PopeyeMessage {

    private int option;

    private boolean valid;

    private String voteId;

    /** Creates a new instance of VoteMessage */
    public VoteMessage(String voteId, int option, boolean valid) {
        this.option = option;
        this.voteId = voteId;
        this.valid = valid;
    }

    public int getOption() {
        return option;
    }

    public boolean isValid() {
        return valid;
    }

    public String getVoteId() {
        return voteId;
    }

    public String toString() {
        return "ANSWER:\nId:" + voteId + "\noption:" + option + "\nvalid:" + valid;
    }
}
