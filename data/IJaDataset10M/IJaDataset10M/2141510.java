package com.citep.web.admin.accounts.client;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.citep.web.gwt.module.Presentation;
import com.citep.web.gwt.module.BasePresentation;
import com.citep.web.gwt.validators.*;
import com.citep.web.gwt.dto.*;
import com.citep.web.gwt.widgets.CalendarEntry;
import com.citep.web.gwt.widgets.ShortcutTable;
import com.citep.web.gwt.widgets.StatusLabel;
import com.citep.web.gwt.widgets.Shortcut;

public class AccountEntryPresentation extends BasePresentation implements Presentation {

    protected Grid inputGrid;

    protected TextBox accountNameText;

    protected TextBox accountDescriptionText;

    protected TextBox initialAmountText;

    protected TextBox periodStartText;

    protected TextBox periodEndText;

    protected CheckBox accountEnabledCheck;

    protected ListBox accountTypeList;

    protected ListBox parentAccountList;

    protected Button submitButton;

    protected CalendarEntry periodStart;

    protected CalendarEntry periodEnd;

    protected StatusLabel statusLabel;

    protected VerticalPanel mainPanel;

    protected ShortcutTable shortcuts;

    protected HashMap errorLabels = new HashMap();

    protected ValidatorGroup validator;

    protected AccountEntryApplication application;

    protected ClickListener saveAction;

    protected ClickListener editAction;

    protected Button createAccountCategoryButton;

    protected Button createAccountButton;

    protected CategorySelection categorySelection = new CategorySelection();

    protected class CategorySelection extends Composite {

        protected ListBox categorySelect;

        protected TextBox categoryName;

        protected Button categorySubmitButton;

        protected Button enableAddButton;

        protected Grid categoryAddGrid;

        protected boolean visibleAdd;

        public void onCategoryAdd(String name) {
            categorySelect.addItem(name);
            hideAddOption();
            setListValue(categorySelect, name);
        }

        public CategorySelection() {
            categorySelect = new ListBox();
            categoryName = new TextBox();
            categoryName.setVisibleLength(10);
            categoryName.setStyleName("form_textentry");
            categoryName.addKeyboardListener(new KeyboardListener() {

                public void onKeyUp(Widget w, char code, int modifiers) {
                }

                public void onKeyDown(Widget w, char code, int modifiers) {
                }

                public void onKeyPress(Widget w, char code, int modifiers) {
                    if (code == KEY_ENTER && categoryName.getText().length() > 0) {
                        application.createAccountCategory(categoryName.getText(), categoryName.getText());
                    }
                }
            });
            categorySubmitButton = new Button("Add");
            categorySubmitButton.setStyleName("button_small");
            categorySubmitButton.addClickListener(new ClickListener() {

                public void onClick(Widget w) {
                    if (categoryName.getText().length() > 0) {
                        application.createAccountCategory(categoryName.getText(), categoryName.getText());
                    }
                }
            });
            enableAddButton = new Button("New");
            enableAddButton.setStyleName("button_small");
            enableAddButton.addClickListener(new ClickListener() {

                public void onClick(Widget w) {
                    if (visibleAdd) hideAddOption(); else showAddOption();
                }
            });
            categoryAddGrid = new Grid(1, 5);
            categoryAddGrid.setStyleName("table_border_collapse");
            categoryAddGrid.setCellSpacing(0);
            categoryAddGrid.setWidget(0, 0, categorySelect);
            categoryAddGrid.setWidget(0, 1, enableAddButton);
            categoryAddGrid.setWidget(0, 2, new Label("   "));
            categoryAddGrid.getCellFormatter().setWidth(0, 2, "15px");
            categoryAddGrid.setWidget(0, 3, categoryName);
            categoryAddGrid.setWidget(0, 4, categorySubmitButton);
            this.setWidget(categoryAddGrid);
            hideAddOption();
            visibleAdd = false;
        }

        public ListBox getSelectionBox() {
            return categorySelect;
        }

        public void showAddOption() {
            categoryName.setVisible(true);
            categorySubmitButton.setVisible(true);
            categoryAddGrid.getCellFormatter().setStyleName(0, 3, "form_highlight");
            categoryAddGrid.getCellFormatter().setStyleName(0, 4, "form_highlight");
            visibleAdd = true;
        }

        public void hideAddOption() {
            categoryName.setVisible(false);
            categorySubmitButton.setVisible(false);
            categoryAddGrid.getCellFormatter().removeStyleName(0, 3, "form_highlight");
            categoryAddGrid.getCellFormatter().removeStyleName(0, 4, "form_highlight");
            visibleAdd = false;
        }

        public void clear() {
            categorySelect.clear();
            categorySelect.addItem(" ");
            hideAddOption();
        }
    }

    public void onCategoryAdd(String name) {
        categorySelection.onCategoryAdd(name);
    }

    public void clearCategories() {
        categorySelection.clear();
    }

    public void addWidgetToInputGrid(Grid g, String description, Widget w) {
        int rows = g.getRowCount();
        int cols = g.getColumnCount();
        g.resize(rows + 1, cols);
        g.setText(rows, 0, description);
        g.setWidget(rows, 1, w);
        g.getCellFormatter().setStyleName(rows, 0, "input_cell");
        g.getCellFormatter().setStyleName(rows, 1, "input_cell");
    }

    protected String formatDate(Date d) {
        if (d == null) return "";
        return "" + (d.getMonth() + 1) + "/" + d.getDate() + "/" + (d.getYear() + 1900);
    }

    public void setApplication(AccountEntryApplication application) {
        this.application = application;
    }

    public void displayStatus(String text, int seconds) {
        getStatusLabel().setText(text);
        getStatusLabel().show(seconds);
    }

    public void addShortcut(Shortcut shortcut, boolean highlight) {
        shortcuts.addShortcut(shortcut, highlight);
    }

    public void updateShortcut(Shortcut shortcut, boolean highlight) {
        shortcuts.updateShortcut(shortcut, highlight);
    }

    protected void clearFields() {
        accountNameText.setText("");
        accountDescriptionText.setText("");
        accountTypeList.setSelectedIndex(0);
        initialAmountText.setText("");
        accountEnabledCheck.setChecked(false);
        periodStart.getTextBox().setText("");
        periodEnd.getTextBox().setText("");
    }

    public void setSaveState() {
        titleLabel.setText("Create new Account");
        clearFields();
        submitButton.setText("Save");
        submitButton.removeClickListener(getEditAction());
        submitButton.removeClickListener(getSaveAction());
        submitButton.addClickListener(getSaveAction());
        submitButton.setEnabled(true);
    }

    public void setSaveReadyState() {
        submitButton.setText("Save");
        submitButton.setEnabled(true);
    }

    public void setEditReadyState() {
        submitButton.setText("Update");
        submitButton.setEnabled(true);
    }

    public void setEditState() {
        titleLabel.setText("Edit Account");
        submitButton.setText("Update");
        submitButton.removeClickListener(getEditAction());
        submitButton.removeClickListener(getSaveAction());
        submitButton.addClickListener(getEditAction());
        submitButton.setEnabled(true);
    }

    public void setWaitState() {
        submitButton.setText("Wait...");
        submitButton.setEnabled(false);
    }

    public void setAccountData(AccountDTO account) {
        if (account != null) {
            accountNameText.setText(account.getName());
            accountDescriptionText.setText(account.getDescription());
            setListValue(accountTypeList, account.getType());
            setListValue(categorySelection.getSelectionBox(), account.getAccountCategory().getName());
            initialAmountText.setText(account.getStartingBalance());
            accountEnabledCheck.setChecked(account.isEnabled());
            periodStart.getTextBox().setText(formatDate(account.getStartPeriod()));
            periodEnd.getTextBox().setText(formatDate(account.getEndPeriod()));
        }
    }

    public void getAccountData(AccountDTO account) {
        if (account != null) {
            account.setName(accountNameText.getText());
            account.setDescription(accountDescriptionText.getText());
            account.setStartingBalance(initialAmountText.getText());
            account.setEnabled(accountEnabledCheck.isChecked());
            account.setStartPeriod(new Date(periodStart.getTextBox().getText()));
            account.setEndPeriod(new Date(periodEnd.getTextBox().getText()));
        }
    }

    public ListBox getAccountTypeList() {
        if (accountTypeList == null) {
            accountTypeList = new ListBox();
            accountTypeList.addItem("");
            accountTypeList.addItem("Income");
            accountTypeList.addItem("Liability");
            accountTypeList.addItem("Assets");
            accountTypeList.addItem("Expenses");
            accountTypeList.addItem("Equity");
            accountTypeList.setStyleName("form_textentry");
        }
        return accountTypeList;
    }

    private ListBox getParentAccountList() {
        if (parentAccountList == null) {
            parentAccountList = new ListBox();
        }
        return parentAccountList;
    }

    public void clearParentAccountList() {
        getParentAccountList().clear();
    }

    public void addAccountToParentAccountList(AccountDTO account, int level) {
        String spacing = "";
        while (level-- > 0) {
            spacing += "__";
        }
        getParentAccountList().addItem(spacing + account.getName(), "" + account.getId());
    }

    public int getSelectedParentId() {
        return Integer.parseInt(getParentAccountList().getValue(getParentAccountList().getSelectedIndex()));
    }

    public void setSelectedParent(int parentId) {
        String value = "" + parentId;
        ListBox select = getParentAccountList();
        for (int i = 0; i < select.getItemCount(); i++) {
            if (select.getValue(i).equals(value)) {
                select.setSelectedIndex(i);
            }
        }
    }

    protected CalendarEntry getPeriodStart() {
        if (periodStart == null) {
            periodStart = new CalendarEntry("img/icon-calendar.png");
            periodStart.setStyleName("calendar_entry");
        }
        return periodStart;
    }

    protected CalendarEntry getPeriodEnd() {
        if (periodEnd == null) {
            periodEnd = new CalendarEntry("img/icon-calendar.png");
            periodEnd.setStyleName("calendar_entry");
        }
        return periodEnd;
    }

    protected HorizontalPanel getPeriod() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(new Label("From:"));
        panel.add(getPeriodStart());
        panel.add(new Label("To:"));
        panel.add(getPeriodEnd());
        panel.setStyleName("period_table");
        return panel;
    }

    public void setValidation() {
        if (validator == null) validator = new ValidatorGroup();
        ValidationErrorListener listener = new ValidationErrorListener();
        validator.addValidator("Account.name", accountNameText, new RequiredValidator(listener, "Please enter a valid account name"));
        validator.addValidator("Account.description", accountDescriptionText, new RequiredValidator(listener, "Please enter a valid description"));
        validator.addValidator("Account.type", accountTypeList, new RequiredValidator(listener, "Please select an account type"));
        validator.addValidator("Account.accountCategory", categorySelection.getSelectionBox(), new RequiredValidator(listener, "Please select an account category"));
        validator.addValidator("Account.balance", initialAmountText, new RequiredValidator(listener, "Please enter an initial amount"));
        validator.addValidator("Account.balance", initialAmountText, new NumberValidator(listener, "Please enter a valid number for the initial amount"));
        validator.addValidator("Account.startPeriod", periodStart.getTextBox(), new RequiredValidator(listener, "Please enter a staring period"));
        validator.addValidator("Account.endPeriod", periodEnd.getTextBox(), new RequiredValidator(listener, "Please enter an ending period"));
        validator.addValidator("Account.startPeriod", periodEnd.getTextBox(), new DateValidator(listener, "Please enter a valid date format for the start period"));
        validator.addValidator("Account.endPeriod", periodEnd.getTextBox(), new DateValidator(listener, "Please enter a valida date format for the end period"));
    }

    public Grid getInputGrid() {
        if (inputGrid == null) {
            inputGrid = new Grid(0, 2);
            inputGrid.setCellPadding(0);
            inputGrid.setCellSpacing(0);
            inputGrid.setStyleName("input_grid");
            addWidgetToInputGrid(inputGrid, "Parent Account:", getParentAccountList());
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.name"));
            accountNameText = createTextBox("form_textentry", 30);
            addWidgetToInputGrid(inputGrid, "Account Name:", accountNameText);
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.description"));
            accountDescriptionText = createTextBox("form_textentry", 50);
            addWidgetToInputGrid(inputGrid, "Account Description:", accountDescriptionText);
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.type"));
            addWidgetToInputGrid(inputGrid, "Account Type:", getAccountTypeList());
            addValidationLabelToGrid(inputGrid, createErrorLabel(new String[] { "Account.accountCategory", "AccountCategory.name" }));
            addWidgetToInputGrid(inputGrid, "Account Category:", categorySelection);
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.balance"));
            initialAmountText = createTextBox("form_textentry", 10);
            addWidgetToInputGrid(inputGrid, "Initial Amount:", initialAmountText);
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.startPeriod"));
            addValidationLabelToGrid(inputGrid, createErrorLabel("Account.endPeriod"));
            addWidgetToInputGrid(inputGrid, "Period:", getPeriod());
            accountEnabledCheck = new CheckBox();
            addWidgetToInputGrid(inputGrid, "Enable:", accountEnabledCheck);
            int rows = inputGrid.getRowCount();
            int cols = inputGrid.getColumnCount();
            inputGrid.resize(rows + 1, cols);
        }
        setValidation();
        return inputGrid;
    }

    public StatusLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new StatusLabel();
            statusLabel.setStyleName("status_label");
            statusLabel.setVisible(false);
        }
        return statusLabel;
    }

    public void clearShortcuts() {
        System.out.println("CLEAR SHORTCUTS");
        shortcuts.clearEntries();
    }

    protected ClickListener getSaveAction() {
        if (saveAction == null) {
            saveAction = new ClickListener() {

                public void onClick(Widget w) {
                    clearErrors();
                    if (validator.validate()) {
                        String accountType = accountTypeList.getItemText(accountTypeList.getSelectedIndex());
                        String accountCategory = categorySelection.getSelectionBox().getItemText(categorySelection.getSelectionBox().getSelectedIndex());
                        int parentAccountId = Integer.parseInt(parentAccountList.getValue(parentAccountList.getSelectedIndex()));
                        application.createAccount(parentAccountId, accountNameText.getText(), accountDescriptionText.getText(), accountType, accountCategory, initialAmountText.getText(), new Date(periodStart.getTextBox().getText()), new Date(periodEnd.getTextBox().getText()), accountEnabledCheck.isChecked());
                    }
                }

                ;
            };
        }
        return saveAction;
    }

    protected ClickListener getEditAction() {
        if (editAction == null) {
            editAction = new ClickListener() {

                public void onClick(Widget w) {
                    clearErrors();
                    if (validator.validate()) {
                        application.saveCurrentAccount();
                    }
                }

                ;
            };
        }
        return editAction;
    }

    protected Button getSubmitButton() {
        if (submitButton == null) {
            submitButton = new Button("Save");
            submitButton.setStyleName("form_big_button");
            submitButton.addClickListener(getSaveAction());
        }
        return submitButton;
    }

    protected Button getCreateAccountButton() {
        if (createAccountButton == null) {
            createAccountButton = new Button("Create Account");
            createAccountButton.setStyleName("form_button");
            createAccountButton.addClickListener(new ClickListener() {

                public void onClick(Widget w) {
                    setSaveState();
                }
            });
        }
        return createAccountButton;
    }

    protected Button getCreateAccountCategoryButton() {
        if (createAccountCategoryButton == null) {
            createAccountCategoryButton = new Button("Create Account Category");
            createAccountCategoryButton.setStyleName("form_button");
        }
        return createAccountCategoryButton;
    }

    public AccountEntryPresentation() {
        shortcuts = new ShortcutTable("Accounts", 10);
        shortcuts.setMoreLink(new Hyperlink("[more]", "__account_list__"));
        mainPanel = new VerticalPanel();
        mainPanel.setWidth("100%");
        mainPanel.add(getTitlePanel());
        mainPanel.add(getInputGrid());
        mainPanel.add(getSubmitButton());
        mainPanel.setCellHorizontalAlignment(getSubmitButton(), HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(getStatusLabel());
        mainPanel.setCellHorizontalAlignment(getStatusLabel(), HasHorizontalAlignment.ALIGN_CENTER);
        setSaveState();
    }

    public Widget getMainPanel() {
        return mainPanel;
    }

    public Widget getContext() {
        VerticalPanel p = new VerticalPanel();
        p.setStyleName("context_table");
        p.setSpacing(0);
        p.add(getCreateAccountButton());
        p.add(shortcuts);
        return p;
    }

    public void init(Object arg) {
        if (arg == null) {
        } else {
        }
    }

    public void onShow() {
    }

    public void onHide() {
    }
}
