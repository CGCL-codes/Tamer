package de.rowbuddy.client.logbook;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.rowbuddy.client.HeaderButtonView;

public class StartTripView extends HeaderButtonView implements StartTripPresenter.Display {

    private FlexTable content;

    private TextBox routeName;

    private TextBox memberName;

    private TextBox boatName;

    private Button addButton;

    private Button cancelButton;

    private Button deleteTripMemberButton;

    private Button setCoxButton;

    private SuggestBox route;

    private SuggestBox member;

    private SuggestBox boat;

    private ListBox multiBox;

    public StartTripView(String pageTitle) {
        super(pageTitle);
        content = new FlexTable();
        routeName = new TextBox();
        boatName = new TextBox();
        memberName = new TextBox();
        routeName = new TextBox();
        addButton = new Button("Hinzuf&uuml;gen");
        addButton.setStylePrimaryName("buttonApply buttonPositive");
        cancelButton = new Button("Abbrechen");
        cancelButton.setStylePrimaryName("buttonCancel buttonNegative");
        addButton(addButton);
        addButton(cancelButton);
        content.setText(0, 0, "Routenname:");
        content.setWidget(0, 1, routeName);
        content.setText(1, 0, "Boot:");
        content.setWidget(1, 1, boatName);
        content.setText(2, 0, "Mitglied(er):");
        content.setWidget(2, 1, memberName);
        multiBox = new ListBox(true);
        multiBox.ensureDebugId("cwListBox-multiBox");
        multiBox.setWidth("100%");
        multiBox.setVisibleItemCount(10);
        VerticalPanel multiBoxPanel = new VerticalPanel();
        multiBoxPanel.setSpacing(4);
        multiBoxPanel.add(multiBox);
        multiBoxPanel.setWidth("100%");
        content.setWidget(3, 1, multiBoxPanel);
        FlexTable ft2 = new FlexTable();
        deleteTripMemberButton = new Button("TripMember löschen");
        deleteTripMemberButton.setStylePrimaryName("buttonCancel buttonNegative");
        ft2.setWidget(0, 0, deleteTripMemberButton);
        setCoxButton = new Button("Als Steuermann setzen");
        setCoxButton.setStylePrimaryName("buttonApply buttonRegular");
        ft2.setWidget(1, 0, setCoxButton);
        content.setWidget(3, 2, ft2);
        content.getFlexCellFormatter().setVerticalAlignment(3, 2, HasVerticalAlignment.ALIGN_TOP);
        setContent(content);
    }

    public void setBoatInformation(String name, boolean coxed, int rowers) {
        if (coxed) {
            content.setText(1, 2, "Gesteuert: ja");
        } else {
            content.setText(1, 2, "Gesteuert: nein");
        }
        content.setText(2, 2, "Bootsplätze: " + rowers);
    }

    public void setRouteInformation(double length) {
        content.setText(0, 2, "Streckenlänge: " + length + " km");
    }

    @Override
    public void showTripMembers(String[] tripMembers) {
        multiBox.clear();
        for (String tm : tripMembers) {
            multiBox.addItem(tm);
        }
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addButton;
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    @Override
    public HasValue<String> getRouteName() {
        return route;
    }

    @Override
    public HasValue<String> getBoatName() {
        return boat;
    }

    @Override
    public HasValue<String> getMemberName() {
        return member;
    }

    @Override
    public SuggestBox getMember() {
        return member;
    }

    @Override
    public ListBox getListBox() {
        return multiBox;
    }

    @Override
    public void setRouteOracle(SuggestOracle oracle) {
        route = new SuggestBox(oracle, routeName);
        content.setWidget(0, 1, route);
    }

    @Override
    public void setBoatOracle(SuggestOracle oracle) {
        boat = new SuggestBox(oracle, boatName);
        content.setWidget(1, 1, boat);
    }

    @Override
    public void setMemberOracle(SuggestOracle oracle) {
        member = new SuggestBox(oracle, memberName);
        content.setWidget(2, 1, member);
    }

    @Override
    public HasClickHandlers getDeleteTripMemberButton() {
        return deleteTripMemberButton;
    }

    @Override
    public HasClickHandlers getSetCoxButton() {
        return setCoxButton;
    }
}
