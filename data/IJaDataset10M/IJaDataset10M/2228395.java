package org.directdemocracyportal.democracy.web.views;

import org.directdemocracyportal.democracy.model.application.OnlinePoliticalParty;
import org.directdemocracyportal.democracy.model.application.User;
import org.directdemocracyportal.democracy.model.core.GroupAgent;
import org.directdemocracyportal.democracy.model.world.Organisation;
import org.directdemocracyportal.democracy.model.world.Person;
import org.directdemocracyportal.democracy.web.ApplicationMessageHolder;
import org.directdemocracyportal.democracy.web.BeanLocator;
import org.directdemocracyportal.democracy.web.UserState;
import org.directdemocracyportal.democracy.web.ApplicationMessageHolder.MessageConstans;
import org.directdemocracyportal.democracy.web.action.JoinPoliticalPartyAction;
import org.directdemocracyportal.democracy.web.action.ShowOnlinePoliticalPartyAction;
import thinwire.ui.Component;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.Menu;
import thinwire.ui.Panel;
import thinwire.ui.TabFolder;
import thinwire.ui.TabSheet;
import thinwire.ui.layout.TableLayout;

/**
 * The Class OnlinePoliticalPartyOverviewPanel.
 */
public class OnlinePoliticalPartyOverviewPanel extends Panel {

    /** The party. */
    private final OnlinePoliticalParty party;

    /**
     * Instantiates a new online political party overview panel.
     *
     * @param party the party
     */
    public OnlinePoliticalPartyOverviewPanel(OnlinePoliticalParty party) {
        this.party = party;
        setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 1, 5));
        Menu menu = createMenu();
        menu.setLimit("0,0");
        getChildren().add(menu);
        TabFolder tabFolder = createTabFolder(party);
        tabFolder.setLimit("0,1,1,1");
        getChildren().add(tabFolder);
    }

    /**
     * Creates the tab folder.
     *
     * @param organisation the organisation
     * @return the tab folder
     */
    private TabFolder createTabFolder(Organisation organisation) {
        TabSheet orgStructure = createOrgStructureTabSheet(organisation);
        TabSheet members = createMembersTabSheet(organisation);
        TabFolder tabFolder = new TabFolder();
        tabFolder.getChildren().add(orgStructure);
        tabFolder.getChildren().add(members);
        tabFolder.setCurrentIndex(0);
        return tabFolder;
    }

    /**
     * Creates the members tab sheet.
     *
     * @param organisation the organisation
     * @return the tab sheet
     */
    private TabSheet createMembersTabSheet(Organisation organisation) {
        TabSheet members = new TabSheet();
        members.setText(organisation.getName() + ApplicationMessageHolder.getMessage(MessageConstans.MEMBERS));
        members.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 1, 5));
        Label nameLabel = new Label(ApplicationMessageHolder.getMessage(MessageConstans.MEMBERS));
        nameLabel.setLimit("0,0");
        members.getChildren().add(nameLabel);
        GridBox orgBox = getMemberGridBox(organisation);
        orgBox.setLimit("0,1");
        members.getChildren().add(orgBox);
        return members;
    }

    /**
     * Gets the member grid box.
     *
     * @param organisation the organisation
     * @return the member grid box
     */
    private GridBox getMemberGridBox(Organisation organisation) {
        GridBox gridBox = new GridBox();
        gridBox.setVisibleHeader(true);
        GridBox.Column nameHeader = new GridBox.Column();
        nameHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.HEADER_NAME));
        gridBox.getColumns().add(nameHeader);
        GridBox.Column rolesHeader = new GridBox.Column();
        rolesHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.ROLES));
        gridBox.getColumns().add(rolesHeader);
        GridBox.Column partyHeader = new GridBox.Column();
        partyHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.POLITICAL_PARTY));
        gridBox.getColumns().add(partyHeader);
        for (Person person : organisation.personsActive()) {
            gridBox.getRows().add(new GridBox.Row(person.getName(), person.getRoleInOrg(organisation).getName(), person.getPoliticalParty().getName()));
        }
        return gridBox;
    }

    /**
     * Creates the org structure tab sheet.
     *
     * @param organisation the organisation
     * @return the tab sheet
     */
    private TabSheet createOrgStructureTabSheet(Organisation organisation) {
        TabSheet orgStructure = new TabSheet();
        orgStructure.setText(organisation.getName() + ApplicationMessageHolder.getMessage(MessageConstans.ORGANISATIONS));
        orgStructure.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0 } }, 1, 5));
        Label nameLabel = new Label(ApplicationMessageHolder.getMessage(MessageConstans.ORGANISATIONS));
        nameLabel.setLimit("0,0");
        orgStructure.getChildren().add(nameLabel);
        GridBox orgBox = getOrgGridBox(organisation);
        orgBox.setLimit("0,1");
        orgStructure.getChildren().add(orgBox);
        return orgStructure;
    }

    /**
     * Gets the org grid box.
     *
     * @param organisation the organisation
     * @return the org grid box
     */
    private GridBox getOrgGridBox(Organisation organisation) {
        GridBox gridBox = new GridBox();
        gridBox.addActionListener(ACTION_CLICK, BeanLocator.getApplicationActionListener());
        gridBox.setVisibleHeader(true);
        GridBox.Column nameHeader = new GridBox.Column();
        nameHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.HEADER_NAME));
        gridBox.getColumns().add(nameHeader);
        GridBox.Column orgTypeHeader = new GridBox.Column();
        orgTypeHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.ORGANISATION_TYPE));
        gridBox.getColumns().add(orgTypeHeader);
        GridBox.Column numberPeopleHeader = new GridBox.Column();
        numberPeopleHeader.setName(ApplicationMessageHolder.getMessage(MessageConstans.NUMBER_OF_MEMBERS));
        gridBox.getColumns().add(numberPeopleHeader);
        for (GroupAgent groupAgent : organisation.getChildren()) {
            Organisation orgUnit = (Organisation) groupAgent;
            GridBox.Row row = new GridBox.Row(orgUnit.getName(), orgUnit.getOrganisationType(), orgUnit.getNumberOfMembers());
            row.setUserObject(new ShowOnlinePoliticalPartyAction(orgUnit.getId()));
            gridBox.getRows().add(row);
        }
        return gridBox;
    }

    /**
     * Creates the menu.
     *
     * @return the menu
     */
    public Menu createMenu() {
        Menu menu = new Menu();
        User user = UserState.user.get();
        if (user != null && (!party.playsRoleInOrganisation(user.getPerson()))) {
            Menu.Item joinPartyItem = new Menu.Item(ApplicationMessageHolder.getMessage(MessageConstans.BUTTON_JOIN_PARTY));
            joinPartyItem.setUserObject(new JoinPoliticalPartyAction(party.getId()));
            menu.getRootItem().getChildren().add(joinPartyItem);
        }
        menu.addActionListener(Component.ACTION_CLICK, BeanLocator.getApplicationActionListener());
        return menu;
    }
}
