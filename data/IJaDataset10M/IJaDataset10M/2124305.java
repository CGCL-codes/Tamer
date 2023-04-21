package org.nomicron.suber.form;

import org.nomicron.suber.enums.StrawPollOption;
import org.nomicron.suber.model.object.BallotItem;

/**
 * Form bean for the Straw poll form.
 */
public class StrawPollForm extends BaseForm {

    private StrawPollOption vote;

    private BallotItem ballotItem;

    public StrawPollOption getVote() {
        return vote;
    }

    public void setVote(StrawPollOption vote) {
        this.vote = vote;
    }

    public BallotItem getBallotItem() {
        return ballotItem;
    }

    public void setBallotItem(BallotItem ballotItem) {
        this.ballotItem = ballotItem;
    }
}
