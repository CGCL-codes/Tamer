package com.ivis.xprocess.ui.dialogs;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.ParameterGroup;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIConstants;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.view.providers.IElementProvider;
import com.ivis.xprocess.ui.widgets.IListenToSave;
import com.ivis.xprocess.ui.widgets.ParametersTableWidget;
import com.ivis.xprocess.ui.wizards.process.InternalParameter;

/**
 * A generic pattern instantiation dialog that displays all the parameter in
 * an editable format.
 *
 */
public class InstantiatePatternDialog extends Dialog implements IElementProvider, PaintListener, IListenToSave, IValidParameterListener {

    private Pattern pattern;

    private Xelement context;

    private List<InternalParameter> internalParameters = new LinkedList<InternalParameter>();

    private boolean multipleElementCreation = false;

    private String separator;

    private boolean openEditor = false;

    private ParametersTableWidget parametersTableWidget;

    private Control editorControl;

    private Button multipleCheckBox;

    private Combo separatorCombo;

    private Label noOfElementsToBeCreatedLabel;

    private Composite composite;

    private Button openEditorButton;

    private boolean userHasUsedMultipleCheckBox = false;

    /**
     * @param parentShell
     * @param pattern - the pattern to instantiated
     * @param context - the element it will be instantiated in
     */
    public InstantiatePatternDialog(Shell parentShell, Pattern pattern, Xelement context) {
        super(parentShell);
        setShellStyle(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.BORDER | getDefaultOrientation() | SWT.RESIZE);
        this.pattern = pattern;
        this.context = context;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(DialogMessages.pattern_instantiation_dialog_title + " " + pattern.getLabel());
    }

    @Override
    protected Point getInitialSize() {
        Point point = super.getInitialSize();
        if (point.y > 640) {
            point.y = 640;
        }
        if (point.y < 320) {
            point.y = 320;
        }
        composite.layout(true);
        return point;
    }

    @Override
    protected void handleShellCloseEvent() {
        if ((editorControl != null) && !editorControl.isDisposed()) {
            editorControl.dispose();
        }
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            if ((editorControl != null) && !editorControl.isDisposed()) {
                parametersTableWidget.saveText(true);
            }
            multipleElementCreation = multipleCheckBox.getSelection();
            separator = separatorCombo.getText();
            setOpenEditor(openEditorButton.getSelection());
            okPressed();
        }
        if (buttonId == IDialogConstants.CANCEL_ID) {
            cancelPressed();
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL);
        composite.setLayoutData(data);
        setupData();
        parametersTableWidget = new ParametersTableWidget(composite, SWT.NONE, this);
        parametersTableWidget.addSaveListener(this);
        parametersTableWidget.setValidParameterListener(this);
        parametersTableWidget.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (!userHasUsedMultipleCheckBox) {
                    if (parametersTableWidget.hasSeparatorInParameters(separatorCombo.getText())) {
                        multipleCheckBox.setSelection(true);
                        updateElementCountLabel();
                    }
                }
            }
        });
        noOfElementsToBeCreatedLabel = new Label(composite, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        noOfElementsToBeCreatedLabel.setLayoutData(data);
        createSeparatorCombo(composite, DialogMessages.pattern_instantiation_multiple_label);
        updateElementCountLabel();
        createOpenEditorCheckBox(composite);
        composite.addPaintListener(this);
        setupTestHarness();
        return composite;
    }

    public void paintControl(PaintEvent e) {
        TableColumn[] columns = parametersTableWidget.getColumns();
        int optimalWidth = composite.getClientArea().width - 10;
        int firstColumnWidth = optimalWidth / columns.length;
        int secondColmnWidth = optimalWidth - firstColumnWidth;
        if (columns[0].getWidth() != firstColumnWidth) {
            columns[0].setWidth(firstColumnWidth);
        }
        if (columns[1].getWidth() != secondColmnWidth) {
            columns[1].setWidth(secondColmnWidth);
        }
        composite.removePaintListener(this);
    }

    private void createSeparatorCombo(Composite container, String checkBoxText) {
        Composite separatorComposite = new Composite(container, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.numColumns = 4;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        separatorComposite.setLayout(gridLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        data.horizontalSpan = 2;
        separatorComposite.setLayoutData(data);
        multipleCheckBox = new Button(separatorComposite, SWT.CHECK);
        multipleCheckBox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                userHasUsedMultipleCheckBox = true;
                updateElementCountLabel();
            }
        });
        multipleCheckBox.setText(checkBoxText);
        separatorCombo = new Combo(separatorComposite, SWT.NONE);
        separatorCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateElementCountLabel();
            }
        });
        separatorCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateElementCountLabel();
            }
        });
        separatorCombo.add(",");
        separatorCombo.add(";");
        separatorCombo.select(0);
        data = new GridData();
        data.widthHint = UIConstants.separator_combo_width;
        separatorCombo.setLayoutData(data);
    }

    private void createOpenEditorCheckBox(Composite container) {
        Composite openEditorComposite = new Composite(container, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.numColumns = 4;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        openEditorComposite.setLayout(gridLayout);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.BEGINNING;
        data.horizontalSpan = 2;
        openEditorComposite.setLayoutData(data);
        openEditorButton = new Button(openEditorComposite, SWT.CHECK);
        openEditorButton.setText(DialogMessages.open_editor);
        openEditorButton.setSelection(System.getProperty("abbot") != null);
    }

    @SuppressWarnings("unchecked")
    private void setupData() {
        for (ParameterGroup parameterGroup : pattern.getParameterGroups()) {
            for (Parameter parameter : parameterGroup.getParameters()) {
                InternalParameter internalParameter = new InternalParameter(parameterGroup, parameter, context);
                internalParameters.add(internalParameter);
            }
        }
        Collections.sort((List) internalParameters, new Comparator<InternalParameter>() {

            public int compare(InternalParameter a1, InternalParameter a2) {
                if (a1.getParameterGroup().getRank() == a2.getParameterGroup().getRank()) {
                    if (a1.getRank() >= a2.getRank()) {
                        return 1;
                    }
                    return 0;
                } else if (a1.getParameterGroup().getRank() > a2.getParameterGroup().getRank()) {
                    return 1;
                } else if (a1.getParameterGroup().getRank() < a2.getParameterGroup().getRank()) {
                    return 0;
                }
                return 0;
            }
        });
    }

    protected void workOutSelection(MouseEvent e, Table table) {
        Object item = table.getItem(new Point(e.x, e.y));
        if (item == null) {
            table.deselectAll();
        }
    }

    public Object[] getContentsByType() {
        return null;
    }

    public String getType() {
        return null;
    }

    public void setDirty(boolean dirty) {
    }

    public Object[] getElements() {
        return internalParameters.toArray();
    }

    /**
     * @return all the parameters
     */
    public InternalParameter[] getParameters() {
        return internalParameters.toArray(new InternalParameter[] {});
    }

    /**
     * @return true if more than 1 element should be instantiated
     */
    public boolean isMultiple() {
        return multipleElementCreation;
    }

    /**
     * @return the separator String
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @return how many elements to create
     */
    public int getNumberOfElementsToCreate() {
        int max = 1;
        if (!multipleCheckBox.isDisposed() && !multipleCheckBox.getSelection()) {
            return max;
        }
        for (InternalParameter internalParameter : internalParameters) {
            StringTokenizer stringTokenizer = new StringTokenizer(internalParameter.getValueAsString(), separator);
            int numberOfTokens = stringTokenizer.countTokens();
            if (numberOfTokens > max) {
                max = numberOfTokens;
            }
        }
        return max;
    }

    private void updateElementCountLabel() {
        separator = separatorCombo.getText();
        noOfElementsToBeCreatedLabel.setText(DialogMessages.pattern_instantiation_no_of_elements_label + " " + getNumberOfElementsToCreate());
    }

    protected void setupTestHarness() {
        TestHarness.name(multipleCheckBox, TestHarness.MULTIPLE_ELEMENT_BUTTON);
        TestHarness.name(openEditorButton, TestHarness.OPEN_EDITOR_BUTTON);
    }

    /**
     * @return true if the editor for the created element should be opened
     */
    public boolean openEditor() {
        return openEditor;
    }

    private void setOpenEditor(boolean openEditor) {
        this.openEditor = openEditor;
    }

    public void saved() {
        if (multipleCheckBox.getSelection()) {
            updateElementCountLabel();
        }
    }

    public void validParameters(String message) {
        getButton(IDialogConstants.OK_ID).setEnabled(message == null);
    }

    public void userChangedSeparator() {
        if (parametersTableWidget.hasSeparatorInParameters(separatorCombo.getText())) {
            multipleCheckBox.setSelection(true);
        } else {
            multipleCheckBox.setSelection(false);
        }
    }
}
