package tms.client.admin.termbases;

import java.util.ArrayList;
import java.util.Date;
import tms.client.accesscontrol.AccessController;
import tms.client.accesscontrol.User;
import tms.client.admin.LocalCache;
import tms.client.controls.ExtendedListBox;
import tms.client.controls.dialogs.AlertBox;
import tms.client.controls.dialogs.ErrorBox;
import tms.client.controls.dialogs.SuccessBox;
import tms.client.entities.TerminologyDatabase;
import tms.client.entities.Topic;
import tms.client.i18n.TMSConstants;
import tms.client.services.DatabaseRetrievalService;
import tms.client.services.DatabaseRetrievalServiceAsync;
import tms.client.services.DatabaseUpdateService;
import tms.client.services.DatabaseUpdateServiceAsync;
import tms.client.services.result.DataAdditionResult;
import tms.client.services.result.DataCheckResult;
import tms.client.services.result.DataUpdateResult;
import tms.client.util.ErrorHandler;
import tms.client.util.ListBoxUtility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A panel that displays the properties of a selected termbase.
 * @author Werner Liebenberg
 * @author Wildrich Fourie
 */
public class DatabaseDetailsPanel extends VerticalPanel {

    private static final TMSConstants constants = GWT.create(TMSConstants.class);

    private DatabaseRetrievalServiceAsync databaseRetriever = GWT.create(DatabaseRetrievalService.class);

    private DatabaseUpdateServiceAsync databaseUpdater = GWT.create(DatabaseUpdateService.class);

    private ExtendedListBox<TerminologyDatabase> termDbsListBox;

    protected final TerminologyDatabase termbase = new TerminologyDatabase();

    private TextBox databaseNameBox;

    private Button saveDatabaseButton;

    private TextBox adminEmailBox;

    private FlexTable flexTable;

    private Button proj_create;

    public DatabaseDetailsPanel(ExtendedListBox<TerminologyDatabase> termDbsListBox, Button proj_create) {
        this.termDbsListBox = termDbsListBox;
        createDatabaseNameBox();
        createEmailBox();
        createSaveDatabaseButton();
        this.proj_create = proj_create;
        this.proj_create.addStyleName("adminButton");
        layOut();
    }

    public void reset(boolean visible) {
        User user = AccessController.getSignedOnUser();
        this.termbase.reset();
        if (user != null) this.termbase.setOwnername(user.getFullName());
        populate(null);
        super.setVisible(visible);
        databaseNameBox.setText("");
        adminEmailBox.setText("");
        saveDatabaseButton.setEnabled(false);
    }

    private void createEmailBox() {
        adminEmailBox = new TextBox();
        adminEmailBox.setWidth("200px");
        adminEmailBox.addChangeHandler(new TextBoxChangeHandler());
        adminEmailBox.addKeyPressHandler(new TextBoxKeyHandler());
    }

    private void createDatabaseNameBox() {
        databaseNameBox = new TextBox();
        databaseNameBox.setWidth("200px");
        databaseNameBox.addChangeHandler(new TextBoxChangeHandler());
        databaseNameBox.addKeyPressHandler(new TextBoxKeyHandler());
    }

    private void createSaveDatabaseButton() {
        saveDatabaseButton = new Button(constants.controls_save());
        saveDatabaseButton.addStyleName("adminButton");
        saveDatabaseButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                TerminologyDatabase database = LocalCache.getCurrentDatabase();
                if (database != null) updateDatabase(databaseNameBox.getText(), adminEmailBox.getText(), AccessController.getSignedOnUser().getUserId()); else saveDatabaseName(databaseNameBox.getText(), adminEmailBox.getText(), AccessController.getSignedOnUser().getUserId());
                proj_create.setEnabled(true);
            }
        });
    }

    public void layOut() {
        this.clear();
        super.setStyleName("borderedBlock");
        super.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        super.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        flexTable = new FlexTable();
        flexTable.setCellPadding(7);
        Label label = new Label(constants.admin_termbase_termbaseName(), false);
        label.addStyleName("labelTextBold");
        label.addStyleName("plainLabelText");
        flexTable.setWidget(0, 0, label);
        flexTable.setWidget(0, 1, databaseNameBox);
        int rowIndex = 1;
        Label emailLabel = new Label(constants.admin_termbase_email(), false);
        emailLabel.addStyleName("labelTextBold");
        emailLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, emailLabel);
        flexTable.setWidget(rowIndex, 1, adminEmailBox);
        rowIndex++;
        Label dateCreatedLabel = new Label(constants.admin_termbase_dateTimeCreated(), false);
        dateCreatedLabel.addStyleName("labelTextBold");
        dateCreatedLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, dateCreatedLabel);
        Label dateCreatedValue = new Label(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(this.termbase.getDatetimecreated()), false);
        dateCreatedValue.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 1, dateCreatedValue);
        rowIndex++;
        if (this.termbase.getDatetimelastupdated() != null) {
            Label dateUpdatedLabel = new Label(constants.admin_termbase_lastUpdated(), false);
            dateUpdatedLabel.addStyleName("labelTextBold");
            dateUpdatedLabel.addStyleName("plainLabelText");
            flexTable.setWidget(rowIndex, 0, dateUpdatedLabel);
            Label dateUpdatedValue = new Label(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(this.termbase.getDatetimelastupdated()), false);
            dateUpdatedValue.addStyleName("plainLabelText");
            flexTable.setWidget(rowIndex, 1, dateUpdatedValue);
            rowIndex++;
        }
        Label ownerLabel = new Label(constants.admin_termbase_owner(), false);
        ownerLabel.addStyleName("labelTextBold");
        ownerLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, ownerLabel);
        Label ownerValue = new Label(this.termbase.getOwnername(), false);
        ownerValue.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 1, ownerValue);
        rowIndex++;
        flexTable.setWidget(rowIndex, 1, saveDatabaseButton);
        this.add(flexTable);
        super.setVisible(true);
    }

    public void populate(final TerminologyDatabase theDatabase) {
        LocalCache.setCurrentDatabase(theDatabase);
        this.clear();
        super.setStyleName("borderedBlock");
        super.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        super.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        flexTable = new FlexTable();
        flexTable.setCellPadding(7);
        Label label = new Label(constants.admin_termbase_termbaseName(), false);
        label.addStyleName("labelTextBold");
        label.addStyleName("plainLabelText");
        flexTable.setWidget(0, 0, label);
        flexTable.setWidget(0, 1, databaseNameBox);
        int rowIndex = 1;
        Label emailLabel = new Label(constants.admin_termbase_email(), false);
        emailLabel.addStyleName("labelTextBold");
        emailLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, emailLabel);
        flexTable.setWidget(rowIndex, 1, adminEmailBox);
        rowIndex++;
        if (theDatabase != null) {
            this.setDatabaseName(theDatabase.getTermdbname());
            this.setEmail(theDatabase.getEmail());
            this.setDatabaseOwner(theDatabase.getOwneruserid());
            this.setDateTimeCreated(theDatabase.getDatetimecreated());
            this.setDateTimeLastUpdated(theDatabase.getDatetimelastupdated());
            this.setDatabaseOwnerName(theDatabase.getOwnername());
            this.setTopics(theDatabase.getTopics());
        }
        Label dateCreatedLabel = new Label(constants.admin_termbase_dateTimeCreated(), false);
        dateCreatedLabel.addStyleName("labelTextBold");
        dateCreatedLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, dateCreatedLabel);
        Label dateCreatedValue = new Label(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(this.termbase.getDatetimecreated()), false);
        dateCreatedValue.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 1, dateCreatedValue);
        rowIndex++;
        if (this.termbase.getDatetimelastupdated() != null) {
            Label dateUpdatedLabel = new Label(constants.admin_termbase_lastUpdated(), false);
            dateUpdatedLabel.addStyleName("labelTextBold");
            dateUpdatedLabel.addStyleName("plainLabelText");
            flexTable.setWidget(rowIndex, 0, dateUpdatedLabel);
            Label dateUpdatedValue = new Label(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(this.termbase.getDatetimelastupdated()), false);
            dateUpdatedValue.addStyleName("plainLabelText");
            flexTable.setWidget(rowIndex, 1, dateUpdatedValue);
            rowIndex++;
        }
        Label ownerLabel = new Label(constants.admin_termbase_owner(), false);
        ownerLabel.addStyleName("labelTextBold");
        ownerLabel.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 0, ownerLabel);
        Label ownerValue = new Label(this.termbase.getOwnername(), false);
        ownerValue.addStyleName("plainLabelText");
        flexTable.setWidget(rowIndex, 1, ownerValue);
        rowIndex++;
        flexTable.setWidget(rowIndex, 1, saveDatabaseButton);
        this.add(flexTable);
        super.setVisible(true);
    }

    private void saveDatabaseName(final String name, final String email, final long ownerUserId) {
        databaseRetriever.checkForDatabaseByName(AccessController.getAuthToken(), name, new AsyncCallback<DataCheckResult<Boolean>>() {

            @Override
            public void onSuccess(DataCheckResult<Boolean> result) {
                if (result != null) {
                    try {
                        if (result.getResult()) AlertBox.show(constants.fault_minor(), result.getMessage(), false, true); else {
                            databaseUpdater.createDatabase(AccessController.getAuthToken(), name, ownerUserId, email, new AsyncCallback<DataAdditionResult<TerminologyDatabase>>() {

                                @Override
                                public void onSuccess(DataAdditionResult<TerminologyDatabase> result) {
                                    if (result != null) {
                                        if (result.isSuccessful()) {
                                            SuccessBox.show(result.getMessage(), false, true);
                                            TerminologyDatabase database = result.getResult();
                                            termDbsListBox.update(database.getTermdbname(), termDbsListBox.getSelectedIndex(), database);
                                            populate(database);
                                            saveDatabaseButton.setEnabled(false);
                                        } else {
                                            ErrorBox.show(result.getMessage(), false, true);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    ErrorBox.show(constants.admin_field_errorSave(), false, true);
                                }
                            });
                        }
                    } catch (Exception e) {
                        ErrorBox.show(result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.handle(caught.getMessage());
            }
        });
    }

    protected void updateDatabase(String newDatabaseName, String email, long ownerUserId) {
        final TerminologyDatabase database = LocalCache.getCurrentDatabase();
        if (database != null) {
            database.setTermdbname(newDatabaseName);
            databaseUpdater.updateDatabase(AccessController.getAuthToken(), database, new AsyncCallback<DataUpdateResult<TerminologyDatabase>>() {

                @Override
                public void onSuccess(DataUpdateResult<TerminologyDatabase> result) {
                    if (result != null) if (result.isSuccessful()) {
                        LocalCache.setCurrentDatabase(database);
                        SuccessBox.show(result.getMessage(), false, true);
                        ArrayList<TerminologyDatabase> items = termDbsListBox.getItems();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i) != null) if (items.get(i).getTermdbid() == result.getResult().getTermdbid()) {
                                items.set(i, result.getResult());
                                termDbsListBox.setItemText(i, result.getResult().getTermdbname());
                            }
                        }
                        termDbsListBox.setItems(items);
                        ListBoxUtility.setListBox(termDbsListBox, result.getResult().getTermdbid());
                        saveDatabaseButton.setEnabled(false);
                    } else {
                        ErrorBox.show(result.getMessage(), false, true);
                    } else ErrorHandler.handle(constants.admin_field_errorComplete());
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.handle(caught.getMessage());
                }
            });
        }
    }

    public void resetDatabaseDetailsPanel(boolean visible) {
        reset(visible);
        termDbsListBox.setSelectedIndex(0);
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.termbase.setTopics(topics);
    }

    public ArrayList<Topic> getTopics() {
        return this.termbase.getTopics();
    }

    public String getDatabaseName() {
        return this.termbase.getTermdbname();
    }

    public void setDatabaseName(String databaseName) {
        this.termbase.setTermdbname(databaseName);
        databaseNameBox.setText(termbase.getTermdbname());
    }

    public void setEmail(String email) {
        this.termbase.setEmail(email);
        adminEmailBox.setText(termbase.getEmail());
    }

    public String getDatabaseOwner() {
        return this.termbase.getOwnername();
    }

    public String getDatabaseOwnerName() {
        return this.termbase.getOwnername();
    }

    public void setDatabaseOwnerName(String ownername) {
        this.termbase.setOwnername(ownername);
    }

    public void setDatabaseOwner(long ownerUserId) {
        this.termbase.setOwneruserid(ownerUserId);
    }

    public Date getDateTimeCreated() {
        return this.termbase.getDatetimecreated();
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.termbase.setDatetimecreated(dateTimeCreated);
    }

    public Date getDateTimeLastUpdated() {
        return this.termbase.getDatetimelastupdated();
    }

    public void setDateTimeLastUpdated(Date dateTimeLastUpdated) {
        this.termbase.setDatetimelastupdated(dateTimeLastUpdated);
    }

    public FlexTable getFlexTable() {
        return flexTable;
    }

    public void setFlexTable(FlexTable flexTable) {
        this.flexTable = flexTable;
    }

    private class TextBoxChangeHandler implements ChangeHandler {

        @Override
        public void onChange(ChangeEvent event) {
            if (databaseNameBox.getText() != null && !databaseNameBox.getText().trim().equals("") && adminEmailBox.getText() != null && !adminEmailBox.getText().trim().equals("")) {
                saveDatabaseButton.setEnabled(true);
                termbase.setTermdbname(databaseNameBox.getText());
                termbase.setEmail(adminEmailBox.getText());
            } else {
                termbase.setTermdbname(null);
                termbase.setEmail(null);
                saveDatabaseButton.setEnabled(false);
            }
        }
    }

    private class TextBoxKeyHandler implements KeyPressHandler {

        @Override
        public void onKeyPress(KeyPressEvent event) {
            if (databaseNameBox.getText() != null && !databaseNameBox.getText().trim().equals("") && adminEmailBox.getText() != null && !adminEmailBox.getText().trim().equals("")) {
                saveDatabaseButton.setEnabled(true);
                termbase.setTermdbname(databaseNameBox.getText());
                termbase.setEmail(adminEmailBox.getText());
            } else {
                saveDatabaseButton.setEnabled(false);
                termbase.setTermdbname(null);
                termbase.setEmail(null);
            }
            if (event.getCharCode() == KeyCodes.KEY_ENTER && databaseNameBox.getText() != null && !databaseNameBox.getText().trim().equals("") && adminEmailBox.getText() != null && !adminEmailBox.getText().trim().equals("")) {
                TerminologyDatabase database = LocalCache.getCurrentDatabase();
                if (database != null) updateDatabase(databaseNameBox.getText(), adminEmailBox.getText(), AccessController.getSignedOnUser().getUserId()); else saveDatabaseName(databaseNameBox.getText(), adminEmailBox.getText(), AccessController.getSignedOnUser().getUserId());
                proj_create.setEnabled(true);
            }
        }
    }
}
