package org.nomicron.suber.model.object;

import org.nomicron.suber.enums.SortType;
import org.nomicron.suber.model.factory.BallotItemFactory;
import org.nomicron.suber.model.factory.MetaFactory;
import com.dreamlizard.miles.interfaces.Named;
import com.dreamlizard.miles.time.Moment;
import java.util.Collections;
import java.util.List;

/**
 * Business object representing a Nomicron turn.
 */
public class Turn extends ElementListObject implements Named {

    private Integer ordinal;

    private Moment startDate;

    private Moment endDate;

    private Boolean arePagesUpdated;

    private Boolean areVotesTallied;

    private String designation = null;

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Moment getStartDate() {
        return startDate;
    }

    public void setStartDate(Moment startDate) {
        this.startDate = startDate;
    }

    public Moment getEndDate() {
        return endDate;
    }

    public void setEndDate(Moment endDate) {
        this.endDate = endDate;
    }

    public Boolean getArePagesUpdated() {
        return arePagesUpdated;
    }

    public void setArePagesUpdated(Boolean arePagesUpdated) {
        this.arePagesUpdated = arePagesUpdated;
    }

    public Boolean getAreVotesTallied() {
        return areVotesTallied;
    }

    public void setAreVotesTallied(Boolean areVotesTallied) {
        this.areVotesTallied = areVotesTallied;
    }

    public List<BallotItem> getProposalList() {
        BallotItemFactory ballotItemFactory = MetaFactory.getInstance().getBallotItemFactory();
        List<BallotItem> proposalList = ballotItemFactory.getBallotItemListByTurnVoted(this);
        Collections.sort(proposalList, new BallotItem.ProposalNumberComparator(SortType.ASCENDING));
        return proposalList;
    }

    /**
     * Get the turn with the specified offset relationship with the current turn. For example, -1 will return the previous
     * turn, 1 will return the following turn.
     *
     * @param offset offset value
     * @return Turn
     */
    public Turn getOffsetTurn(int offset) {
        return MetaFactory.getInstance().getTurnFactory().getOffsetTurn(this, offset);
    }

    public String getDesignation() {
        if (this.designation == null) {
            StringBuilder designationB = new StringBuilder();
            designationB.append(MetaFactory.getInstance().getGameStateFactory().getGameState().getRound());
            designationB.append(" - ");
            designationB.append(getOrdinal());
            this.designation = designationB.toString();
        }
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Get the name of the object.
     *
     * @return name
     */
    public String getName() {
        return getDesignation();
    }
}
