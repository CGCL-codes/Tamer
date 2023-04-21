package net.pleso.framework.client.ui.custom;

import net.pleso.framework.client.bl.actions.IAddRowExecuter;
import net.pleso.framework.client.bl.actions.IEmptyRowCreator;
import net.pleso.framework.client.bl.actions.IRowLoader;
import net.pleso.framework.client.bl.actions.IUpdateRowExecutor;
import net.pleso.framework.client.bl.auth.AuthorizationProvider;
import net.pleso.framework.client.bl.exceptions.AsyncCallbackFailureException;
import net.pleso.framework.client.bl.forms.IAddForm;
import net.pleso.framework.client.bl.forms.IEditForm;
import net.pleso.framework.client.bl.forms.IForm;
import net.pleso.framework.client.bl.forms.IParametersForm;
import net.pleso.framework.client.bl.forms.IViewForm;
import net.pleso.framework.client.bl.forms.items.IFormItemsGroup;
import net.pleso.framework.client.bl.providers.IAddFormProvider;
import net.pleso.framework.client.dal.IDataRow;
import net.pleso.framework.client.localization.FrameworkLocale;
import net.pleso.framework.client.ui.custom.controls.ActionButtonPanel;
import net.pleso.framework.client.ui.custom.controls.FormItemsGroupWidget;
import net.pleso.framework.client.ui.custom.controls.FormItemsGroupWidgetCollection;
import net.pleso.framework.client.ui.custom.controls.data.ActionSliderControl;
import net.pleso.framework.client.ui.interfaces.IBindableDataControl;
import net.pleso.framework.client.ui.interfaces.IFocusControl;
import net.pleso.framework.client.ui.interfaces.IUpdateControlListener;
import net.pleso.framework.client.ui.windows.Slider;
import net.pleso.framework.client.ui.windows.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Customizable form for any type of forms (see {@link CustomFormType}). Builds
 * action controls (text, numbers, enums, etc.), action sliders (search, edit,
 * insert, etc.) and buttons by business-logic interfaces.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul>
 * <li>.pf-custom-form { custom form itself }</li>
 * <li>.pf-custom-form-header { header text of custom form )</li>
 * <li>.pf-custom-form-status { status text of custom form }</li>
 * <li>.pf-custom-form-datagroups-grid {grid with form items groups}</li>
 * <li>.pf-custom-form-datagroups-grid-cell {each cell in grid with form items
 * groups}</li>
 * </ul>
 * 
 * 
 * @author Scater
 * 
 */
public class CustomFormWindow extends Window implements ClickListener, IUpdateControlListener, KeyboardListener {

    /**
	 * Main widget of this composite widget
	 */
    private VerticalPanel panel = new VerticalPanel();

    /**
	 * Grid with form items groups
	 */
    private Grid formItemsGroupsGrid = new Grid();

    /**
	 * Panel with buttons
	 */
    private ActionButtonPanel buttonsPanel = new ActionButtonPanel(HorizontalPanel.ALIGN_RIGHT);

    /**
	 * Default cancel button (closes form without changes)
	 */
    private Button btnCancel = new Button(FrameworkLocale.constants().cancel_button_caption(), this);

    /**
	 * Default ok button (comfirm user input)
	 */
    private Button btnOk = new Button(FrameworkLocale.constants().ok_button_caption(), this);

    /**
	 * Default reset button (resets form to initial state)
	 */
    private Button btnReset = new Button(FrameworkLocale.constants().reset_button_caption(), this);

    /**
	 * Default "save and add" button. Used in insert forms. Workes like Ok
	 * button, but after that opens new insert form. Convenient when user
	 * inserts many values into single data source.
	 */
    private Button btnSaveAndAdd = new Button(FrameworkLocale.constants().save_and_add_button_caption(), this);

    /**
	 * Current data row in form
	 */
    private IDataRow dataRow;

    private FormItemsGroupWidgetCollection groupWidgets = null;

    private IForm form = null;

    private IAddForm addForm = null;

    private IEditForm editForm = null;

    private IParametersForm searchForm = null;

    private IViewForm viewForm = null;

    private Boolean canceled = null;

    /**
	 * Indicates that data in form was changed
	 */
    private boolean dataChanged = false;

    /**
	 * Status text of custom form
	 */
    private HTML status = new HTML();

    /**
	 * Header of custom form
	 */
    private HTML header = new HTML();

    /**
	 * Save data callback handler
	 * 
	 * @author Scater
	 */
    private class SaveDataAsyncCallback implements AsyncCallback {

        /**
		 * Form will be shown next after this form
		 */
        private IForm nextForm;

        /**
		 * Constructor
		 * 
		 * @param nextForm
		 *            form will be shown next after this form
		 */
        public SaveDataAsyncCallback(IForm nextForm) {
            this.nextForm = nextForm;
        }

        public void onFailure(Throwable caught) {
            if (!isHidden()) {
                status.setText(FrameworkLocale.messages().error());
                setButtonsEnable(true);
                throw new AsyncCallbackFailureException(FrameworkLocale.messages().asyncerror_save(), caught);
            }
        }

        public void onSuccess(Object result) {
            if (!isHidden()) {
                dataChanged = true;
                if (this.nextForm != null) {
                    status.setText("");
                    buildForm(this.nextForm);
                    setButtonsEnable(true);
                } else hideWindow();
            }
        }
    }

    /**
	 * Constructor for {@link IAddForm}
	 * 
	 * @param parentSlider
	 *            slider to show form in it
	 * @param form
	 *            {@link IAddForm} instance
	 */
    public CustomFormWindow(Slider parentSlider, IAddForm form) {
        this(parentSlider, (IForm) form);
    }

    /**
	 * Constructor for {@link IEditForm}
	 * 
	 * @param parentSlider
	 *            slider to show form in it
	 * @param form
	 *            {@link IEditForm} instance
	 */
    public CustomFormWindow(Slider parentSlider, IEditForm form) {
        this(parentSlider, (IForm) form);
    }

    /**
	 * Constructor for {@link IParametersForm}
	 * 
	 * @param parentSlider
	 *            slider to show form in it
	 * @param form
	 *            {@link IParametersForm} instance
	 */
    public CustomFormWindow(Slider parentSlider, IParametersForm form) {
        this(parentSlider, (IForm) form);
    }

    /**
	 * Constructor for {@link IViewForm}
	 * 
	 * @param parentSlider
	 *            slider to show form in it
	 * @param form
	 *            {@link IViewForm} instance
	 */
    public CustomFormWindow(Slider parentSlider, IViewForm form) {
        this(parentSlider, (IForm) form);
    }

    /**
	 * Universal constructor
	 * 
	 * @param parentSlider
	 *            slider to show form in it
	 * @param form
	 *            {@link IForm} instance
	 */
    private CustomFormWindow(Slider parentSlider, IForm form) {
        super(parentSlider);
        if (form == null) throw new IllegalArgumentException("Form can`t be null.");
        this.form = form;
        this.setCaption(this.form.getCaption());
        this.panel.add(header);
        this.header.setStyleName("pf-custom-form-header");
        this.panel.add(status);
        this.status.setStyleName("pf-custom-form-status");
        this.panel.add(formItemsGroupsGrid);
        this.formItemsGroupsGrid.setStyleName("pf-custom-form-datagroups-grid");
        initWidget(this.panel);
        this.setStyleName("pf-custom-form");
    }

    /**
	 * Adds button panel into main widget if it was not added
	 */
    private void addButtonsPanel() {
        if (this.panel.getWidgetIndex(buttonsPanel) == -1) {
            this.panel.add(buttonsPanel);
        }
    }

    /**
	 * Sets enabled for action buttons
	 * 
	 * @param enabled
	 *            is enabled
	 */
    private void setButtonsEnable(boolean enabled) {
        this.btnOk.setEnabled(enabled);
        this.btnSaveAndAdd.setEnabled(enabled);
        this.btnReset.setEnabled(enabled);
    }

    /**
	 * Build panel with buttons
	 */
    private void buildButtonsPanel() {
        this.buttonsPanel.clear();
        if (form instanceof IAddFormProvider) {
            if (AuthorizationProvider.isObjectAuthorized(((IAddFormProvider) form).getAddForm(null))) buttonsPanel.add(this.btnSaveAndAdd);
        }
        if (this.searchForm != null || this.addForm != null || this.editForm != null) this.buttonsPanel.add(this.btnOk);
        this.buttonsPanel.add(this.btnReset);
        this.buttonsPanel.add(this.btnCancel);
    }

    /**
	 * Builds form by {@link IForm}
	 * 
	 * @param form
	 *            {@link IForm} instance
	 */
    private void buildForm(IForm form) {
        if (form == null) throw new IllegalArgumentException("Form can`t be null.");
        this.form = form;
        if (form instanceof IAddForm) this.addForm = (IAddForm) form; else this.addForm = null;
        if (form instanceof IEditForm) this.editForm = (IEditForm) form; else this.editForm = null;
        if (form instanceof IParametersForm) this.searchForm = (IParametersForm) form; else this.searchForm = null;
        if (form instanceof IViewForm) this.viewForm = (IViewForm) form; else this.viewForm = null;
        this.header.setText(this.form.getCaption());
        this.setCaption(this.form.getCaption());
        buildButtonsPanel();
        if (this.addForm != null) {
            createFormForEmptyRow(this.addForm);
        } else if (this.editForm != null) {
            createFormForExistingRow(this.editForm);
        } else if (this.searchForm != null) {
            createFormForEmptyRow(this.searchForm);
        } else if (this.viewForm != null) {
            createFormForExistingRow(this.viewForm);
        }
    }

    /**
	 * Builds form for empty row.
	 * 
	 * @param rowCreator
	 *            {@link IEmptyRowCreator} instance for creating empty row
	 */
    private void createFormForEmptyRow(IEmptyRowCreator rowCreator) {
        this.dataRow = null;
        if (rowCreator != null) this.dataRow = rowCreator.createEmptyRow();
        if (this.dataRow == null) throw new NullPointerException(FrameworkLocale.messages().error_emptyrowisnull());
        buildFormItemsGroups();
        addButtonsPanel();
        scrollToTop();
    }

    /**
	 * Builds form for existing row (for edit forms). Loads row from
	 * asynchronous {@link IRowLoader} instance.
	 * 
	 * @param rowLoader
	 *            {@link IRowLoader} instance
	 */
    private void createFormForExistingRow(IRowLoader rowLoader) {
        status.setText(FrameworkLocale.messages().loading());
        setButtonsEnable(false);
        rowLoader.GetData(new AsyncCallback() {

            public void onFailure(Throwable caught) {
                if (!isHidden()) {
                    status.setText(FrameworkLocale.messages().error());
                    throw new AsyncCallbackFailureException(FrameworkLocale.messages().asyncerror_loadrow(), caught);
                }
            }

            public void onSuccess(Object result) {
                if (!isHidden()) {
                    dataRow = (IDataRow) result;
                    status.setText("");
                    buildFormItemsGroups();
                    setButtonsEnable(true);
                    addButtonsPanel();
                    scrollToTop();
                    focusOnFirstControl();
                }
            }
        });
    }

    /**
	 * Builds form item groups by {@link IFormItemsGroup} array in form
	 */
    private void buildFormItemsGroups() {
        for (int i = 0; i < this.panel.getWidgetCount(); ) if (this.panel.getWidget(i) instanceof ActionSliderControl) this.panel.remove(i); else i++;
        this.formItemsGroupsGrid.clear();
        IFormItemsGroup[] groups = form.getGroups();
        this.groupWidgets = new FormItemsGroupWidgetCollection();
        if (this.dataRow == null) throw new NullPointerException(FrameworkLocale.messages().error_rowisnull());
        for (int i = 0; i < groups.length; i++) {
            if (groups[i] == null) throw new IllegalArgumentException("Group cann`t be null.");
            FormItemsGroupWidget groupWidget = new FormItemsGroupWidget(this, this, this, this.panel);
            groupWidget.initFormItemsGroup(groups[i], this.dataRow);
            this.groupWidgets.addFormItemsGroupWidget(groupWidget);
            if (i == 0) {
                this.formItemsGroupsGrid.resize(1, 2);
                this.formItemsGroupsGrid.getCellFormatter().setStyleName(0, 0, "pf-custom-form-datagroups-grid-cell");
                this.formItemsGroupsGrid.setWidget(0, 0, groupWidget);
            } else {
                if ((i % 2) > 0) {
                    this.formItemsGroupsGrid.setWidget(this.formItemsGroupsGrid.getRowCount() - 1, 1, groupWidget);
                    this.formItemsGroupsGrid.getCellFormatter().setStyleName(this.formItemsGroupsGrid.getRowCount() - 1, 1, "pf-custom-form-datagroups-grid-cell");
                } else {
                    this.formItemsGroupsGrid.resizeRows(this.formItemsGroupsGrid.getRowCount() + 1);
                    this.formItemsGroupsGrid.setWidget(this.formItemsGroupsGrid.getRowCount() - 1, 0, groupWidget);
                    this.formItemsGroupsGrid.getCellFormatter().setStyleName(this.formItemsGroupsGrid.getRowCount() - 1, 0, "pf-custom-form-datagroups-grid-cell");
                }
            }
        }
    }

    /**
	 * Updates {@link #dataRow} by values from all form controls.
	 */
    private void updateAllData() {
        this.groupWidgets.updateAllData();
    }

    /**
	 * Forces bindable controls to read binded data.
	 */
    private void readReadableControls() {
        this.groupWidgets.reReadBindableControls();
    }

    public void onClick(Widget sender) {
        if (sender == this.btnCancel) {
            this.canceled = Boolean.TRUE;
            hideWindow();
        } else if (sender == this.btnOk || sender == this.btnSaveAndAdd) {
            setButtonsEnable(false);
            this.canceled = Boolean.FALSE;
            if (internalValidate()) {
                updateAllData();
                IForm nextForm = null;
                if (sender == this.btnSaveAndAdd && this.form instanceof IAddFormProvider) nextForm = ((IAddFormProvider) this.form).getAddForm(this.dataRow);
                if (this.addForm != null) {
                    insertRow(nextForm);
                } else if (this.editForm != null) {
                    editRow(nextForm);
                } else if (this.searchForm != null) {
                    hideWindow();
                }
            } else {
                setButtonsEnable(true);
            }
        } else if (sender == this.btnReset) {
            buildForm(this.form);
        }
    }

    /**
	 * Inserts row to specified by form data set.
	 * 
	 * @param nextForm
	 *            next form to build after operation success. Can be null if no
	 *            next form expected.
	 */
    private void insertRow(IForm nextForm) {
        if ((form instanceof IAddRowExecuter) == false) throw new UnsupportedOperationException("Can't insert row. Form is not implement IAddRowExecuter.");
        status.setText(FrameworkLocale.messages().loading());
        ((IAddRowExecuter) form).addRow(dataRow, new SaveDataAsyncCallback(nextForm));
    }

    /**
	 * Updates row in specified by form data set.
	 * 
	 * @param nextForm
	 *            next form to build after operation success. Can be null if no
	 *            next form expected.
	 */
    private void editRow(IForm nextForm) {
        if ((form instanceof IUpdateRowExecutor) == false) throw new UnsupportedOperationException("Can't update row. Form is not implement IUpdateRowExecutor.");
        status.setText(FrameworkLocale.messages().loading());
        ((IUpdateRowExecutor) form).updateRow(dataRow, new SaveDataAsyncCallback(nextForm));
    }

    /**
	 * Validates form input controls and returns validation success result.
	 * 
	 * @return <code>true</code> if all form controls' values are valid
	 */
    private boolean internalValidate() {
        return this.groupWidgets.validate();
    }

    public void controlUpdated(IBindableDataControl sender) {
        readReadableControls();
    }

    /**
	 * @return a {@link IDataRow} being edited in form
	 */
    public IDataRow getDataRow() {
        return dataRow;
    }

    /**
	 * @return a {@link CustomFormType} indicating form type
	 */
    public CustomFormType getCustomFormType() {
        if (this.addForm != null) return CustomFormType.Insert; else if (this.editForm != null) return CustomFormType.Edit; else if (this.searchForm != null) return CustomFormType.Search; else if (this.viewForm != null) return CustomFormType.Show;
        return null;
    }

    /**
	 * @return <code>true</code> if form changed data set
	 */
    public boolean dataChanged() {
        return this.dataChanged;
    }

    /**
	 * @return <code>true</code> if form was closed because of cancel
	 *         operation; <code>false</code> if form was closed on user "ok"
	 *         button click.
	 */
    public boolean isCanceled() {
        if (canceled != null) return canceled.booleanValue(); else return true;
    }

    public void hideWindow() {
        this.groupWidgets.hideWindows();
        if (canceled == null) canceled = Boolean.TRUE;
        super.hideWindow();
    }

    /**
	 * Makes first {@link IFocusControl} controls in all controlls list to be
	 * focused.
	 */
    private void focusOnFirstControl() {
        if (this.groupWidgets != null) this.groupWidgets.focusOnFirstControl();
    }

    protected void showEvent() {
        buildForm(this.form);
        focusOnFirstControl();
    }

    public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        switch(keyCode) {
            case 13:
            case 10:
                onClick(this.btnOk);
                break;
            case 27:
                onClick(this.btnCancel);
                break;
        }
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    }
}
