package com.hack23.cia.web.impl.ui.viewfactory.api.user;

import java.util.List;
import com.hack23.cia.model.api.dto.application.UserSessionDto;
import com.hack23.cia.model.api.dto.sweden.ParliamentMemberDto;
import com.hack23.cia.model.api.dto.sweden.ParliamentMemberVoteCompareResultDto;
import com.hack23.cia.model.api.dto.sweden.RegisterInformationDto;
import com.hack23.cia.model.api.dto.sweden.VoteDto;
import com.hack23.cia.web.api.common.ControllerAction;

/**
 * The Class ParliamentMemberSummaryModelAndView.
 */
public class ParliamentMemberSummaryModelAndView extends AbstractParliamentMemberModelAndView {

    /**
     * The Enum ParliamentMemberSummaryViewSpecification.
     */
    public enum ParliamentMemberSummaryViewSpecification {

        /** The Parliament member summary view. */
        ParliamentMemberSummaryView
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The enemy list. */
    private final List<ParliamentMemberVoteCompareResultDto> enemyList;

    /** The enemy party list. */
    private final List<ParliamentMemberVoteCompareResultDto> enemyPartyList;

    /** The friend list. */
    private final List<ParliamentMemberVoteCompareResultDto> friendList;

    /** The friend party list. */
    private final List<ParliamentMemberVoteCompareResultDto> friendPartyList;

    /** The parliament member summary view specification. */
    private final ParliamentMemberSummaryViewSpecification parliamentMemberSummaryViewSpecification;

    /** The register information. */
    private final RegisterInformationDto registerInformation;

    /** The votes. */
    private final List<VoteDto> votes;

    /**
     * Instantiates a new parliament member summary model and view.
     * 
     * @param userSessionDTO the user session dto
     * @param controllerAction the controller action
     * @param parliamentMemberSummaryViewSpecification the parliament member summary view specification
     * @param parliamentMemberDto the parliament member dto
     * @param list the list
     * @param list2 the list2
     * @param list3 the list3
     * @param list4 the list4
     * @param list5 the list5
     * @param registerInformationDto the register information dto
     */
    public ParliamentMemberSummaryModelAndView(final UserSessionDto userSessionDTO, final ControllerAction controllerAction, final ParliamentMemberSummaryViewSpecification parliamentMemberSummaryViewSpecification, final ParliamentMemberDto parliamentMemberDto, final List<ParliamentMemberVoteCompareResultDto> list, final List<ParliamentMemberVoteCompareResultDto> list2, final List<ParliamentMemberVoteCompareResultDto> list3, final List<ParliamentMemberVoteCompareResultDto> list4, final List<VoteDto> list5, final RegisterInformationDto registerInformationDto) {
        super(userSessionDTO, controllerAction, parliamentMemberDto);
        this.parliamentMemberSummaryViewSpecification = parliamentMemberSummaryViewSpecification;
        this.friendList = list;
        this.enemyList = list2;
        this.friendPartyList = list3;
        this.enemyPartyList = list4;
        this.votes = list5;
        this.registerInformation = registerInformationDto;
    }

    /**
     * Gets the enemy list.
     * 
     * @return the enemy list
     */
    public final List<ParliamentMemberVoteCompareResultDto> getEnemyList() {
        return enemyList;
    }

    /**
     * Gets the enemy party list.
     * 
     * @return the enemy party list
     */
    public final List<ParliamentMemberVoteCompareResultDto> getEnemyPartyList() {
        return enemyPartyList;
    }

    /**
     * Gets the friend list.
     * 
     * @return the friend list
     */
    public final List<ParliamentMemberVoteCompareResultDto> getFriendList() {
        return friendList;
    }

    /**
     * Gets the friend party list.
     * 
     * @return the friend party list
     */
    public final List<ParliamentMemberVoteCompareResultDto> getFriendPartyList() {
        return friendPartyList;
    }

    /**
     * Gets the parliament member summary view specification.
     * 
     * @return the parliament member summary view specification
     */
    public final ParliamentMemberSummaryViewSpecification getParliamentMemberSummaryViewSpecification() {
        return parliamentMemberSummaryViewSpecification;
    }

    /**
     * Gets the register information.
     * 
     * @return the register information
     */
    public final RegisterInformationDto getRegisterInformation() {
        return registerInformation;
    }

    @Override
    public final String getViewSpecificationDescription() {
        return parliamentMemberSummaryViewSpecification.toString();
    }

    /**
     * Gets the votes.
     * 
     * @return the votes
     */
    public final List<VoteDto> getVotes() {
        return votes;
    }
}
