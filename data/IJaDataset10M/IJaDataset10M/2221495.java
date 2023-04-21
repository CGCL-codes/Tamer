package com.prolix.editor.main.workspace.reload.activities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.prolix.editor.main.workspace.reload.ReloadWorkspace;
import com.prolix.editor.main.workspace.reload.expressions.ChangePropertyValueEditorDialog;
import com.prolix.editor.main.workspace.reload.expressions.ExpressionsEditorDialog;
import com.prolix.editor.main.workspace.reload.expressions.NotificationsEditorDialog;
import com.prolix.editor.main.workspace.reload.expressions.WhenPropertyValueSetEditorDialog;
import com.prolix.editor.main.workspace.reload.utils.ItemModelTypeEditor;
import com.prolix.editor.main.workspace.reload.utils.MetadataDialog;
import com.prolix.editor.main.workspace.reload.utils.PropertyRefComboViewer;
import com.prolix.editor.main.workspace.reload.utils.TitledReloadToProlixComposite;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.components.activities.Activity;
import uk.ac.reload.straker.datamodel.learningdesign.components.activities.CompleteActivityType;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalProperty;
import uk.ac.reload.straker.datamodel.learningdesign.types.ItemModelType;
import uk.ac.reload.straker.datamodel.learningdesign.types.OnCompletionType;
import uk.ac.reload.straker.datamodel.learningdesign.types.TimeLimitType;
import uk.ac.reload.straker.ui.widgets.BannerCLabel;
import uk.ac.reload.straker.ui.widgets.DurationEditField;

/**
 * Activity Abstract Editor Panel
 * 
 * @author Phillip Beauvoir
 * @version $Id: ActivityEditorPanel.java,v 1.15 2006/07/10 11:50:36 phillipus
 *          Exp $
 */
public abstract class ActivityEditorPanel extends TitledReloadToProlixComposite {

    protected Text _tfParameters;

    /**
	 * Is Visible Button
	 */
    protected Button _bnIsVisible;

    protected Button _bnNone, _bnUserChoice, _bnTimeLimit, _bnPropValue, _bnSetPropValue, _bnChangePropertyValue, _bnNotifications;

    protected BannerCLabel _bannerCompleteActivity, _bannerOnCompletion;

    /**
	 * Feedback Description Editor
	 */
    private ItemModelTypeEditor _feedbackDescriptionEditor;

    /**
	 * Duration EditField
	 */
    private DurationEditField _durationEditField;

    /**
	 * Duration Property Ref Label
	 */
    private Label _durationPropertyRefLabel;

    /**
	 * Duration Property Ref Combo
	 */
    private Combo _durationPropertyRefCombo;

    /**
	 * Combo Viewer for Duration Property Ref
	 */
    private PropertyRefComboViewer _durationPropertyRefComboViewer;

    /**
	 * Field Composite
	 */
    private Composite _fieldContainer;

    /**
	 * Metadata button
	 */
    private Button _mdButton;

    /**
	 * Flag to stop text modify events
	 */
    protected boolean _settingFields;

    /**
	 * Constructor
	 */
    protected ActivityEditorPanel(ReloadWorkspace ldEditor, Composite parent) {
        super(ldEditor, parent);
    }

    /**
	 * Create the fields common to all sub-classes
	 */
    protected void setupCommonFields() {
        Label label = new Label(getFieldContainer(), SWT.NULL);
        label.setText("&Parameters:");
        _tfParameters = new Text(getFieldContainer(), SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        _tfParameters.setLayoutData(gd);
        _bnIsVisible = new Button(getFieldContainer(), SWT.CHECK);
        _bnIsVisible.setText("&Visible");
        gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        _bnIsVisible.setLayoutData(gd);
        _mdButton = createMetadataButton(getFieldContainer());
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        _mdButton.setLayoutData(gd);
        _mdButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleMetadataAction();
            }
        });
    }

    /**
	 * @return The field Composite Panel
	 */
    protected Composite getFieldContainer() {
        if (_fieldContainer == null) {
            _fieldContainer = new Composite(getChildPanel(), SWT.NULL);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            _fieldContainer.setLayoutData(gd);
            GridLayout layout = new GridLayout(2, false);
            _fieldContainer.setLayout(layout);
        }
        return _fieldContainer;
    }

    /**
	 * Set up the CompleteActivity Choices
	 */
    protected void setupCompleteActivityChoices() {
        Composite composite = new Composite(getChildPanel(), SWT.NULL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gd);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        Label label = new Label(composite, SWT.NULL);
        gd = new GridData(GridData.BEGINNING);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);
        _bannerCompleteActivity = new BannerCLabel(composite, "Complete Activity", true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        _bannerCompleteActivity.setLayoutData(gd);
        _bnNone = new Button(composite, SWT.RADIO);
        _bnNone.setText("None");
        gd = new GridData(GridData.BEGINNING);
        gd.horizontalSpan = 2;
        _bnNone.setLayoutData(gd);
        _bnUserChoice = new Button(composite, SWT.RADIO);
        _bnUserChoice.setText("User Choice");
        gd = new GridData(GridData.BEGINNING);
        gd.horizontalSpan = 2;
        _bnUserChoice.setLayoutData(gd);
        _bnTimeLimit = new Button(composite, SWT.RADIO);
        _bnTimeLimit.setText("Time Limit");
        _durationEditField = new DurationEditField(composite);
        _durationPropertyRefLabel = new Label(composite, SWT.NULL);
        _durationPropertyRefLabel.setText("Property Ref");
        _durationPropertyRefLabel.setEnabled(false);
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        _durationPropertyRefLabel.setLayoutData(gd);
        _durationPropertyRefCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        _durationPropertyRefCombo.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        _durationPropertyRefCombo.setLayoutData(gd);
        _durationPropertyRefComboViewer = new PropertyRefComboViewer(getLearningDesignEditor(), _durationPropertyRefCombo);
        _durationPropertyRefComboViewer.setTypeFilter(new String[] { LocalProperty.XML_ELEMENT_NAME });
        _bnPropValue = new Button(composite, SWT.RADIO);
        _bnPropValue.setText("When a Property is Set");
        gd = new GridData(GridData.BEGINNING);
        _bnPropValue.setLayoutData(gd);
        _bnSetPropValue = new Button(composite, SWT.FLAT);
        _bnSetPropValue.setText("Define");
        _bnSetPropValue.setEnabled(false);
        _bnSetPropValue.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleSetPropertyValueAction();
            }
        });
    }

    /**
	 * Set up the On Completion Panel
	 */
    protected void setupOnCompletionPanel() {
        _feedbackDescriptionEditor = new ItemModelTypeEditor(getLearningDesignEditor(), getChildPanel(), "On Completion Feedback Description");
        Composite composite = new Composite(getChildPanel(), SWT.NULL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gd);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        _bannerOnCompletion = new BannerCLabel(composite, "On Completion", true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        _bannerOnCompletion.setLayoutData(gd);
        Label label = new Label(composite, SWT.NULL);
        label.setText("Change Property Value");
        _bnChangePropertyValue = new Button(composite, SWT.FLAT);
        _bnChangePropertyValue.setText("Define");
        _bnChangePropertyValue.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleChangePropertyValueAction();
            }
        });
        label = new Label(composite, SWT.NULL);
        label.setText("Notifications");
        _bnNotifications = new Button(composite, SWT.FLAT);
        _bnNotifications.setText("Define");
        _bnNotifications.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleNotificationsAction();
            }
        });
    }

    /**
	 * Set the DataComponent
	 * 
	 * @param component
	 */
    public void setDataComponent(LD_DataComponent component) {
        super.setDataComponent(component);
        _settingFields = true;
        Activity activity = (Activity) component;
        boolean tf = activity.getIsVisible();
        _bnIsVisible.setSelection(tf);
        String s = activity.getParameters();
        _tfParameters.setText(s == null ? "" : s);
        _bnNone.setSelection(false);
        _bnUserChoice.setSelection(false);
        _bnTimeLimit.setSelection(false);
        _bnPropValue.setSelection(false);
        _bnSetPropValue.setEnabled(false);
        setDurationFieldEnabled(false);
        _durationPropertyRefComboViewer.setSelectedComponent(null);
        CompleteActivityType cType = activity.getCompleteActivityType();
        String choice = cType.getChoice();
        if (choice == null) {
            _bnNone.setSelection(true);
        } else if (choice.equals(CompleteActivityType.USER_CHOICE)) {
            _bnUserChoice.setSelection(true);
        } else if (choice.equals(CompleteActivityType.TIME_LIMIT)) {
            _bnTimeLimit.setSelection(true);
            setDurationFieldEnabled(true);
            TimeLimitType timeLimitType = cType.getTimeLimitType();
            _durationEditField.setDurationType(timeLimitType.getDurationType());
            DataComponent property = timeLimitType.getPropertyRefComponent();
            _durationPropertyRefComboViewer.setSelectedComponent(property);
        } else if (choice.equals(CompleteActivityType.WHEN_PROPERTY_VALUE_IS_SET)) {
            _bnPropValue.setSelection(true);
            _bnSetPropValue.setEnabled(true);
        }
        OnCompletionType ocType = activity.getOnCompletionType();
        ItemModelType descriptionType = ocType.getFeedbackDescriptionType();
        _feedbackDescriptionEditor.setItemModelType(descriptionType);
        _settingFields = false;
        initEnabled(true, true);
    }

    private void initEnabled(boolean enabled, boolean syncEndabled) {
        _bnNone.setEnabled(enabled);
        _bnUserChoice.setEnabled(enabled);
        _bnTimeLimit.setEnabled(enabled);
        _bnPropValue.setEnabled(enabled);
        _bnTimeLimit.setEnabled(enabled);
        if (!enabled) {
            _durationEditField.setEnabled(enabled);
            _durationEditField.setFieldsEnabled(enabled);
            _durationPropertyRefLabel.setEnabled(enabled);
            _durationPropertyRefCombo.setEnabled(enabled);
            _bnSetPropValue.setEnabled(enabled);
        }
        _bnIsVisible.setEnabled(syncEndabled);
        _tfParameters.setEnabled(syncEndabled);
        _feedbackDescriptionEditor.setEnable(syncEndabled);
        _mdButton.setEnabled(syncEndabled);
        _bnChangePropertyValue.setEnabled(syncEndabled);
        _bnNotifications.setEnabled(syncEndabled);
    }

    /**
	 * Set up the Control Listeners
	 */
    protected void setupControlListeners() {
        final LearningDesignDataModel dataModel = getLearningDesignEditor().getLearningDesignDataModel();
        _bnIsVisible.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ((Activity) getDataComponent()).setIsVisible(_bnIsVisible.getSelection());
                dataModel.setDirty(true);
            }
        });
        _tfParameters.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!_settingFields) {
                    ((Activity) getDataComponent()).setParameters(_tfParameters.getText());
                    dataModel.setDirty(true);
                }
            }
        });
        _bnNone.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
                if (cType.getChoice() != null && _bnNone.getSelection()) {
                    cType.setChoice(null);
                    dataModel.setDirty(true);
                }
            }
        });
        _bnUserChoice.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
                if (cType.getChoice() != CompleteActivityType.USER_CHOICE && _bnUserChoice.getSelection()) {
                    cType.setChoice(CompleteActivityType.USER_CHOICE);
                    dataModel.setDirty(true);
                }
            }
        });
        _bnTimeLimit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean selected = _bnTimeLimit.getSelection();
                setDurationFieldEnabled(selected);
                if (selected) {
                    CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
                    if (cType.getChoice() != CompleteActivityType.TIME_LIMIT) {
                        cType.setChoice(CompleteActivityType.TIME_LIMIT);
                        _durationEditField.setDurationType(cType.getTimeLimitType().getDurationType());
                        dataModel.setDirty(true);
                    }
                }
            }
        });
        _durationPropertyRefCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                DataComponent dc = _durationPropertyRefComboViewer.getSelectedComponent();
                if (dc != null) {
                    String idref = dc.getIdentifier();
                    CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
                    cType.getTimeLimitType().setPropertyRef(idref);
                    dataModel.setDirty(true);
                }
            }
        });
        _bnPropValue.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean selected = _bnPropValue.getSelection();
                _bnSetPropValue.setEnabled(selected);
                if (selected) {
                    CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
                    if (cType.getChoice() != CompleteActivityType.WHEN_PROPERTY_VALUE_IS_SET) {
                        cType.setChoice(CompleteActivityType.WHEN_PROPERTY_VALUE_IS_SET);
                        dataModel.setDirty(true);
                    }
                }
            }
        });
    }

    /**
	 * Set the Duration Edit Field enabled or disabled
	 * 
	 * @param enabled
	 */
    private void setDurationFieldEnabled(boolean enabled) {
        if (enabled) {
            _durationEditField.setEnabled(true);
        } else {
            _durationEditField.clear();
        }
        _durationPropertyRefLabel.setEnabled(enabled);
        _durationPropertyRefCombo.setEnabled(enabled);
    }

    /**
	 * Handle the Metadata button pressed Action
	 */
    protected void handleMetadataAction() {
        if (getDataComponent() != null) {
            MetadataDialog dialog = new MetadataDialog(getShell(), getDataComponent());
            dialog.open();
        }
    }

    /**
	 * Handle the "Define" Set Property value Action
	 */
    protected void handleSetPropertyValueAction() {
        if (getDataComponent() != null) {
            CompleteActivityType cType = ((Activity) getDataComponent()).getCompleteActivityType();
            ExpressionsEditorDialog dialog = new WhenPropertyValueSetEditorDialog(getShell(), cType);
            dialog.open();
        }
    }

    /**
	 * Handle the "Define" Change Property value Action
	 */
    protected void handleChangePropertyValueAction() {
        if (getDataComponent() != null) {
            OnCompletionType ocType = ((Activity) getDataComponent()).getOnCompletionType();
            ExpressionsEditorDialog dialog = new ChangePropertyValueEditorDialog(getShell(), ocType);
            dialog.open();
        }
    }

    /**
	 * Handle the "Notifications" Action
	 */
    protected void handleNotificationsAction() {
        if (getDataComponent() != null) {
            OnCompletionType ocType = ((Activity) getDataComponent()).getOnCompletionType();
            NotificationsEditorDialog dialog = new NotificationsEditorDialog(getShell(), ocType);
            dialog.open();
        }
    }

    /**
	 * Dispose of stuff
	 */
    public void dispose() {
        super.dispose();
        _tfParameters = null;
        _bnIsVisible = null;
        _fieldContainer = null;
        _bannerCompleteActivity.dispose();
        _bannerCompleteActivity = null;
        _bannerOnCompletion.dispose();
        _bannerOnCompletion = null;
        _bnNone = null;
        _bnUserChoice = null;
        _bnTimeLimit = null;
        _bnPropValue = null;
        _bnSetPropValue = null;
        _feedbackDescriptionEditor.dispose();
        _feedbackDescriptionEditor = null;
        _durationEditField.dispose();
        _durationEditField = null;
        _durationPropertyRefLabel = null;
        _durationPropertyRefCombo = null;
        _durationPropertyRefComboViewer.dispose();
        _durationPropertyRefComboViewer = null;
        _mdButton = null;
    }
}
