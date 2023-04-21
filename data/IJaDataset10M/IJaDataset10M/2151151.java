package uk.ac.reload.straker.editors.learningdesign.properties;

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
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LD_Property;
import uk.ac.reload.straker.editors.learningdesign.LearningDesignEditor;
import uk.ac.reload.straker.editors.learningdesign.shared.LD_TitledEditorPanel;
import uk.ac.reload.straker.editors.learningdesign.shared.MetadataDialog;

/**
 * Property Editor Panel
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyEditorPanel.java,v 1.8 2006/07/10 11:50:37 phillipus Exp $
 */
public abstract class PropertyEditorPanel extends LD_TitledEditorPanel {

    /**
     * Metadata button
     */
    protected Button _mdButton;

    /**
     * Field Composite
     */
    private Composite _fieldContainer;

    /**
     * Data Type
     */
    protected Combo _comboDataType;

    /**
     * Initial Value
     */
    protected Text _textInitialValue;

    /**
     * RestrictionType Editor Panel
     */
    private RestrictionTypeEditorPanel _restrictionTypeEditorPanel;

    /**
     * Flag to stop text modify events
     */
    protected boolean _settingFields;

    /**
     * Constructor
     */
    public PropertyEditorPanel(LearningDesignEditor ldEditor, Composite parent) {
        super(ldEditor, parent);
        addAdditionalViewParts();
    }

    /**
     * Add additional view parts
     */
    protected abstract void addAdditionalViewParts();

    /**
     * Create the fields common to all sub-classes
     */
    protected void setupCommonFields() {
        GridData gd;
        Label label = new Label(getFieldContainer(), SWT.NULL);
        label.setText("&Data Type:");
        _comboDataType = new Combo(getFieldContainer(), SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        _comboDataType.setLayoutData(gd);
        _comboDataType.setItems(LD_Property.DATA_TYPES);
        _mdButton = createMetadataButton(getFieldContainer());
        gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
        _mdButton.setLayoutData(gd);
        _mdButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleMetadataAction();
            }
        });
        label = new Label(getFieldContainer(), SWT.NULL);
        label.setText("&Initial Value:");
        _textInitialValue = new Text(getFieldContainer(), SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        _textInitialValue.setLayoutData(gd);
    }

    /**
     * @return The RestrictionTypeEditorPanel
     */
    protected RestrictionTypeEditorPanel getRestrictionTypeEditorPanel() {
        if (_restrictionTypeEditorPanel == null) {
            Label label = new Label(getFieldContainer(), SWT.NULL);
            label.setText("&Restrictions:");
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.verticalIndent = 5;
            gd.horizontalSpan = 3;
            label.setLayoutData(gd);
            _restrictionTypeEditorPanel = new RestrictionTypeEditorPanel(getFieldContainer(), SWT.NULL);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 3;
            gd.heightHint = 200;
            _restrictionTypeEditorPanel.setLayoutData(gd);
        }
        return _restrictionTypeEditorPanel;
    }

    /**
     * @return The field Composite Panel
     */
    protected Composite getFieldContainer() {
        if (_fieldContainer == null) {
            _fieldContainer = new Composite(getChildPanel(), SWT.NULL);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            _fieldContainer.setLayoutData(gd);
            GridLayout layout = new GridLayout(3, false);
            _fieldContainer.setLayout(layout);
        }
        return _fieldContainer;
    }

    /**
     * Set the DataComponent
     * @param component
     */
    public void setDataComponent(LD_DataComponent component) {
        super.setDataComponent(component);
        _settingFields = true;
        LD_Property property = (LD_Property) component;
        String s = property.getDataType();
        _comboDataType.setText(s == null ? "" : s);
        s = property.getInitialValue();
        _textInitialValue.setText(s == null ? "" : s);
        getRestrictionTypeEditorPanel().setLD_Property(property);
        _settingFields = false;
    }

    /**
     * Set up the Control Listeners
     */
    protected void setupControlListeners() {
        final LearningDesignDataModel dataModel = getLearningDesignEditor().getLearningDesignDataModel();
        _comboDataType.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ((LD_Property) getDataComponent()).setDataType(_comboDataType.getText());
                dataModel.setDirty(true);
            }
        });
        _textInitialValue.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (!_settingFields) {
                    ((LD_Property) getDataComponent()).setInitialValue(_textInitialValue.getText());
                    dataModel.setDirty(true);
                }
            }
        });
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
     * Dispose of stuff
     */
    public void dispose() {
        super.dispose();
        _comboDataType = null;
        _textInitialValue = null;
        _fieldContainer = null;
        _mdButton = null;
    }
}
