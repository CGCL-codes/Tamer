package com.hack23.cia.web.views.gridboxes;

import java.util.List;
import thinwire.ui.GridBox;
import com.hack23.cia.model.application.ActionType;
import com.hack23.cia.model.sweden.ParliamentMember;
import com.hack23.cia.web.action.ParliamentMemberAction;
import com.hack23.cia.web.common.ApplicationMessageHolder;
import com.hack23.cia.web.common.ApplicationMessageHolder.MessageConstans;

/**
 * The Class TopPresenceGridBox.
 */
public class TopPresenceGridBox extends AbstractParliamentMemberGridBox {

    /**
     * Instantiates a new top presence grid box.
     * 
     * @param currentList the current list
     */
    public TopPresenceGridBox(final List<ParliamentMember> currentList) {
        super(currentList);
    }

    @Override
    final void addGridBoxColumns() {
        this.getColumns().add(GridBoxColumnFactory.getRankHeader());
        GridBox.Column absentHeader = new GridBox.Column();
        absentHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.PRESENCE_PERCENTAGE));
        this.getColumns().add(absentHeader);
        GridBox.Column tillfallenFranvaroHeader = new GridBox.Column();
        tillfallenFranvaroHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.PRESENCE_OCCASSIONS));
        this.getColumns().add(tillfallenFranvaroHeader);
    }

    @Override
    final Row createRow(final int rank, final ParliamentMember parliamentMember) {
        GridBox.Row row = new GridBox.Row(rank, parliamentMember.getPercentagePresent(), parliamentMember.getPresent(), parliamentMember.getTotalVotes(), parliamentMember.getName(), parliamentMember.getParty(), parliamentMember.getElectoralRegion(), parliamentMember.getFirstVoteDate(), parliamentMember.getLastVoteDate());
        row.setUserObject(new ParliamentMemberAction(ActionType.ShowParliamentMemberSummary, parliamentMember));
        return row;
    }
}
