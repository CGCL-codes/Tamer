package com.hack23.cia.web.controller.viewFactory;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import thinwire.ui.Frame;
import thinwire.ui.Menu;
import thinwire.ui.Panel;
import thinwire.ui.layout.TableLayout;
import com.hack23.cia.model.application.ActionType;
import com.hack23.cia.model.application.User;
import com.hack23.cia.model.sweden.ParliamentMember;
import com.hack23.cia.model.sweden.ParliamentMemberVoteCompareResult;
import com.hack23.cia.service.ActivityService;
import com.hack23.cia.service.GraphService;
import com.hack23.cia.service.InformationService;
import com.hack23.cia.web.ApplicationMessageHolder;
import com.hack23.cia.web.ImageConstants;
import com.hack23.cia.web.ApplicationMessageHolder.MessageConstans;
import com.hack23.cia.web.action.SimpleAction;
import com.hack23.cia.web.views.ActionBarPanel;
import com.hack23.cia.web.views.ActiveUserView;
import com.hack23.cia.web.views.ImagePanel;
import com.hack23.cia.web.views.ParliamentMemberSummaryPanel;
import com.hack23.cia.web.views.TopLoosersPanel;
import com.hack23.cia.web.views.TopLoyalPanel;
import com.hack23.cia.web.views.TopPresentPanel;
import com.hack23.cia.web.views.TopRebelsPanel;
import com.hack23.cia.web.views.TopSlackersPanel;
import com.hack23.cia.web.views.TopWinnersPanel;
import com.hack23.cia.web.views.TreeViewModelPanel;
import com.hack23.cia.web.views.WelcomePanel;

/**
 * The Class ViewFactoryServiceImpl.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class ViewFactoryServiceImpl implements ViewFactoryService {

    /**
     * The Constant TOP_NUMBER.
     */
    private static final int TOP_NUMBER = 30;

    /**
     * The information service.
     */
    private final InformationService informationService;

    /**
     * The user service.
     */
    private final ActivityService activityService;

    /**
     * The menufactory.
     */
    private final MenuFactory menufactory;

    /**
     * The graph service.
     */
    private final GraphService graphService;

    /**
     * Instantiates a new view factory service impl.
     * 
     * @param informationService the information service
     * @param userService the user service
     * @param menufactory the menufactory
     * @param graphService the graph service
     */
    public ViewFactoryServiceImpl(final InformationService informationService, final ActivityService userService, final MenuFactory menufactory, final GraphService graphService) {
        super();
        this.menufactory = menufactory;
        this.informationService = informationService;
        this.activityService = userService;
        this.graphService = graphService;
    }

    public final void createFrame(final Frame frame) {
        informationService.getCurrentList();
        frame.setTitle(ApplicationMessageHolder.getMessage(MessageConstans.APPLICATION_NAME) + " -::- " + ApplicationMessageHolder.getMessage(MessageConstans.TRACKING_POLITICIANS_LIKE_BUGS) + " " + ApplicationMessageHolder.getMessage(MessageConstans.APPLICATION_VERSION));
        frame.setLayout(new TableLayout(new double[][] { { 0.15, 0.85 }, { 0 } }, 1, 0));
        Menu menu = this.menufactory.createApplicationMenu(null, TOP_NUMBER);
        frame.setMenu(menu);
        Panel actionBarPanel = new ActionBarPanel(ImageConstants.HOME_ICON, ApplicationMessageHolder.getMessage(MessageConstans.HOME_PAGE), new SimpleAction(ActionType.ShowStartPage));
        actionBarPanel.setLimit("0,0,1,1");
        frame.getChildren().add(actionBarPanel);
        Panel contentView = new WelcomePanel(this.informationService.getLastDecidedCommiteeReports());
        contentView.setLimit("1,0,1,1");
        frame.getChildren().add(contentView);
        ActiveUserView.getActiveContentView().set(contentView);
    }

    @Override
    public final void createMenu(final User user) {
        ActiveUserView.changeActiveMenu(this.menufactory.createApplicationMenu(user, TOP_NUMBER));
    }

    @Override
    public final void createParliamentMemberSummaryPanel(final ParliamentMember member) {
        ParliamentMember parliamentMember = informationService.getParliamentMember(member);
        List<ParliamentMemberVoteCompareResult> friendList = informationService.getParliamentMemberFriends(parliamentMember);
        List<ParliamentMemberVoteCompareResult> enemyList = informationService.getParliamentMemberEnemies(parliamentMember);
        List<ParliamentMemberVoteCompareResult> friendPartyList = informationService.getParliamentMemberPartyFriends(parliamentMember);
        List<ParliamentMemberVoteCompareResult> enemyPartyList = informationService.getParliamentMemberPartyEnemies(parliamentMember);
        ActiveUserView.changeContentView(new ParliamentMemberSummaryPanel(parliamentMember, friendList, enemyList, friendPartyList, enemyPartyList));
    }

    @Override
    public final void createShowTopLoosersInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListLoosersInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopLoosersPanel(currentList));
    }

    @Override
    public final void createShowTopLoosersPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListLoosers(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopLoosersPanel(currentList));
    }

    @Override
    public final void createShowTopLoyalInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListLoyalInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopLoyalPanel(currentList));
    }

    @Override
    public final void createShowTopLoyalPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListLoyal(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopLoyalPanel(currentList));
    }

    @Override
    public final void createShowTopPresentInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListPresentInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopPresentPanel(currentList));
    }

    @Override
    public final void createShowTopPresentPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListPresent(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopPresentPanel(currentList));
    }

    @Override
    public final void createShowTopRebelsInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListRebelsInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopRebelsPanel(currentList));
    }

    @Override
    public final void createShowTopRebelsPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListRebels(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopRebelsPanel(currentList));
    }

    public final void createShowTopSlackersInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListAbsentInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopSlackersPanel(currentList));
    }

    @Override
    public final void createShowTopSlackersPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListAbsent(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopSlackersPanel(currentList));
    }

    @Override
    public final void createShowTopWinnersInPartyPanel(final String party) {
        List<ParliamentMember> currentList = informationService.getCurrentTopListWinnersInParty(TOP_NUMBER, party);
        ActiveUserView.changeContentView(new TopWinnersPanel(currentList));
    }

    @Override
    public final void createShowTopWinnersPanel() {
        List<ParliamentMember> currentList = informationService.getCurrentTopListWinners(TOP_NUMBER);
        ActiveUserView.changeContentView(new TopWinnersPanel(currentList));
    }

    @Override
    public final void createTreeViewModelPanel() {
        ActiveUserView.changeContentView(new TreeViewModelPanel(informationService.getAllCommiteeReports()));
    }

    @Override
    public final void createShowResponseTimeView() {
        ActiveUserView.changeContentView(new ImagePanel(graphService.createResponseTimeGraph(this.activityService.getResponseTimeHistory()).getPath()));
    }

    @Override
    public final void createShowRecentActionsView() {
        ActiveUserView.changeContentView(new ImagePanel(graphService.createRecentActionsGraph(this.activityService.getActionEventHistory()).getPath()));
    }

    /**
     * Creates the show winning party over time view.
     */
    public final void createShowWinningPartyOverTimeView() {
        ActiveUserView.changeContentView(new ImagePanel(graphService.createShowWinningPartyOverTimeGraph().getPath()));
    }

    @Override
    public final void createWelcomePageView() {
        ActiveUserView.changeContentView(new WelcomePanel(this.informationService.getLastDecidedCommiteeReports()));
    }
}
