package booksandfilms.client.view;

import java.util.List;
import booksandfilms.client.Resources.GlobalResources;
import booksandfilms.client.presenter.UserListPresenter;
import booksandfilms.client.entities.UserAccount;
import booksandfilms.client.helper.ClickPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UserListView extends Composite implements UserListPresenter.Display {

    @UiField
    FlexTable usersTable;

    @UiField
    Hyperlink addNew;

    @UiField
    Label loadingLabel;

    @UiField
    TextBox searchBox;

    private static UserListUiBinder uiBinder = GWT.create(UserListUiBinder.class);

    interface UserListUiBinder extends UiBinder<Widget, UserListView> {
    }

    private static boolean adminUser = true;

    private static final int HeaderRowIndex = 0;

    private static final int imageColumnNumber = 4;

    public UserListView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void displayUsers(List<UserAccount> users) {
        usersTable.clear();
        usersTable.removeAllRows();
        int row = 1;
        usersTable.insertRow(HeaderRowIndex);
        if (users == null || users.size() == 0) {
            loadingLabel.setText("No users.");
            return;
        }
        loadingLabel.setVisible(false);
        addColumn("Name");
        addColumn("Email");
        addColumn("Last Login");
        addColumn("Admin");
        for (UserAccount user : users) {
            String name = truncateLongName(user.getName());
            usersTable.setText(row, 0, name);
            usersTable.getCellFormatter().addStyleName(row, 0, "flexTableCell");
            usersTable.getCellFormatter().addStyleName(row, 0, "flexTableTitle");
            usersTable.setText(row, 1, user.getEmailAddress());
            usersTable.getCellFormatter().addStyleName(row, 1, "flexTableCell");
            DateTimeFormat formatter = DateTimeFormat.getFormat("dd-MMM-yyyy");
            String strReadDate = formatter.format(user.getLastLoginOn());
            usersTable.setText(row, 2, strReadDate);
            usersTable.getCellFormatter().addStyleName(row, 2, "flexTableCell");
            usersTable.setText(row, 3, Boolean.toString(user.getAdminUser()));
            usersTable.getCellFormatter().addStyleName(row, 3, "flexTableCell");
            if (adminUser) {
                final Image propertyImg = new Image(GlobalResources.RESOURCE.propertyButton());
                propertyImg.setStyleName("pointer");
                usersTable.setWidget(row, imageColumnNumber, propertyImg);
            }
            row++;
        }
    }

    private String truncateLongName(String displayName) {
        final int MAX = 26;
        final String SUFFIX = "...";
        if (displayName.length() < MAX) return displayName;
        String shortened = displayName.substring(0, MAX - SUFFIX.length()) + SUFFIX;
        return shortened;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addNew;
    }

    @Override
    public int getClickedRow(ClickEvent event) {
        int selectedRow = -1;
        HTMLTable.Cell cell = usersTable.getCellForEvent(event);
        if (cell != null) {
            if (cell.getCellIndex() == imageColumnNumber) {
                selectedRow = cell.getRowIndex() - 1;
            }
        }
        return selectedRow;
    }

    @Override
    public ClickPoint getClickedPoint(ClickEvent event) {
        final Image img;
        int selectedRow = -1;
        ClickPoint point = null;
        HTMLTable.Cell cell = usersTable.getCellForEvent(event);
        if (cell != null) {
            if (cell.getCellIndex() == imageColumnNumber) {
                selectedRow = cell.getRowIndex();
                img = (Image) usersTable.getWidget(selectedRow, imageColumnNumber);
                int left = img.getAbsoluteLeft();
                int top = img.getAbsoluteTop();
                point = new ClickPoint(top, left);
            }
        }
        return point;
    }

    private void addColumn(Object columnHeading) {
        Widget widget = createCellWidget(columnHeading);
        int cell = usersTable.getCellCount(HeaderRowIndex);
        widget.setWidth("100%");
        widget.addStyleName("flexTableColumnLabel");
        usersTable.setWidget(HeaderRowIndex, cell, widget);
        usersTable.getCellFormatter().addStyleName(HeaderRowIndex, cell, "flexTableColumnLabelCell");
    }

    private Widget createCellWidget(Object cellObject) {
        Widget widget = null;
        if (cellObject instanceof Widget) widget = (Widget) cellObject; else widget = new Label(cellObject.toString());
        return widget;
    }

    @Override
    public HasClickHandlers getList() {
        return usersTable;
    }

    @Override
    public void setData(List<UserAccount> data) {
        displayUsers(data);
    }

    @Override
    public HasKeyPressHandlers getSearchBox() {
        return searchBox;
    }

    @Override
    public String getSearchValue() {
        return searchBox.getText();
    }
}
