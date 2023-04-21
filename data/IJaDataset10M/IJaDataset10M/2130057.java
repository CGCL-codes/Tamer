package org.xaware.ide.xadev.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.gui.XADialog;
import org.xaware.ide.xadev.gui.XADialogOperation;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;

/**
 * The class ColumnDefaultDlg is a dialog, appears when Apply Default value menu item is click on right click of Popup
 * menu.
 * 
 * @author Bharath
 * @version 1.0
 */
public class ColumnDefaultDlg implements XADialogOperation, SelectionListener {

    /** Holds instance of XA_Designer_Plugin translator */
    private static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Holds parent shell. */
    private Shell parent;

    /** Holds controls. */
    private Composite dlgPanel;

    /** Holds dialog title. */
    private String title;

    /** Holds name of the selected column. */
    private Text nameTxt;

    /** Holds default value for the selected column. */
    private Text valueTxt;

    /** Check box used to apply/remove default to all columns. */
    private Button applyAllChk;

    /** Holds label for applyAllChk check box. */
    private String applyAllCBStr;

    /** Button used to select the path as a default value. */
    private Button pathBrowseBtn;

    /** Holds the inputXML as tree used in selecting path. */
    private Element inputXML;

    /** Carries selected default text field value. */
    private String selectedValue;

    /** Holds selection status of check box. */
    private boolean applyAllStatus;

    /** Holds status of OK and Cancel buttons. */
    private boolean okClicked;

    private Element selElem;

    /**
     * Creates a new ColumnDefaultDlg object.
     * 
     * @param parent
     *            holds active parent shell.
     * @param title
     *            holds dialog title.
     * @param msg
     *            holds label for check box.
     * @param p_selectedElement
     *            Element the path will be applied on. It the path will be applied on an Attribute, then pass in the
     *            parent element for that Attribute.
     */
    public ColumnDefaultDlg(final Shell parent, final String title, final String msg, final Element p_selectedElement) {
        this.parent = parent;
        this.title = title;
        applyAllCBStr = msg;
        selElem = p_selectedElement;
        init();
    }

    /**
     * Used to construct Column Default Dialog used by Apply/Remove Default Value.
     */
    public void init() {
        dlgPanel = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout(2, false);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER | GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_CENTER);
        dlgPanel.setLayout(gridLayout);
        dlgPanel.setLayoutData(gridData);
        final Label colLbl = new Label(dlgPanel, SWT.NONE);
        colLbl.setText(translator.getString("Column:"));
        gridData = new GridData();
        gridData.widthHint = 40;
        colLbl.setLayoutData(gridData);
        nameTxt = new Text(dlgPanel, SWT.BORDER);
        nameTxt.setEditable(false);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL);
        gridData.widthHint = 320;
        gridData.heightHint = 16;
        gridData.horizontalIndent = 4;
        nameTxt.setLayoutData(gridData);
        final Label defLbl = new Label(dlgPanel, SWT.NONE);
        defLbl.setText(translator.getString("Default:"));
        gridData = new GridData();
        gridData.widthHint = 40;
        defLbl.setLayoutData(gridData);
        final Composite pathPanel = new Composite(dlgPanel, SWT.NONE);
        gridLayout = new GridLayout(2, false);
        pathPanel.setLayout(gridLayout);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL);
        pathPanel.setLayoutData(gridData);
        valueTxt = new Text(pathPanel, SWT.BORDER);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL);
        gridData.widthHint = 100;
        gridData.heightHint = 16;
        valueTxt.setLayoutData(gridData);
        pathBrowseBtn = ControlFactory.createButton(pathPanel, translator.getString("&Path..."));
        pathBrowseBtn.addSelectionListener(this);
        final Label empLbl = new Label(dlgPanel, SWT.NONE);
        empLbl.setVisible(false);
        applyAllChk = new Button(dlgPanel, SWT.CHECK);
        applyAllChk.setText(applyAllCBStr);
        gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        gridData.horizontalIndent = 4;
        applyAllChk.setLayoutData(gridData);
    }

    /**
     * The method show() opens the dialog.
     * 
     * @return int returns dialog open status.
     */
    public int show() {
        final XADialog dialog = new XADialog(parent, this, dlgPanel, title, true, true);
        return dialog.open();
    }

    /**
     * The method widgetDefaultSelected() is implemented as this class implements SelectionListener interface.
     * 
     * @param e
     *            holds SelectionEvent instance.
     */
    public void widgetDefaultSelected(final SelectionEvent e) {
    }

    /**
     * The method widgetSelected() holds action for Path button.
     * 
     * @param e
     *            holds SelectionEvent instance.
     */
    public void widgetSelected(final SelectionEvent e) {
        if (e.getSource() == pathBrowseBtn) {
            final XMLPathChooserDlg dlg = new XMLPathChooserDlg(Display.getCurrent().getActiveShell(), translator.getString(XAwareConstants.PATH_SELECTOR_TITLE), inputXML, inputXML, selElem);
            if (dlg.showDialog()) {
                valueTxt.setText(translator.getString("%") + dlg.getSelectedPath() + translator.getString("%"));
            }
        }
    }

    /**
     * Notifies that the OK button of XADialog has been pressed.
     * 
     * @return true to close the dialog.
     */
    public boolean okPressed() {
        okClicked = true;
        selectedValue = valueTxt.getText().trim();
        applyAllStatus = applyAllChk.getSelection();
        return true;
    }

    /**
     * Notifies that the cancel button of XADialog has been pressed.
     * 
     * @return true
     */
    public boolean cancelPressed() {
        return true;
    }

    /**
     * Checks the status of OK and Cancel buttons.
     * 
     * @return boolean holds true or false.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Used to return column name from Name Text field.
     * 
     * @return string returns Name text field's column name.
     */
    public String getName() {
        return nameTxt.getText().trim();
    }

    /**
     * Used to get default value from Value Text field.
     * 
     * @return string returns Value text field's default value.
     */
    public String getValue() {
        return selectedValue;
    }

    /**
     * Checks whether to apply/remove default value to all columns or not.
     * 
     * @return boolean returns true if checked or false if unchecked.
     */
    public boolean isApplyAll() {
        return applyAllStatus;
    }

    /**
     * Used to set column name for Name Text field.
     * 
     * @param name
     *            holds column name.
     */
    public void setName(final String name) {
        nameTxt.setText(name);
    }

    /**
     * Used to set default value for Value Text field.
     * 
     * @param defaultStr
     *            holds default value.
     */
    public void setValue(final String defaultStr) {
        valueTxt.setText(defaultStr);
    }

    /**
     * Used to enable edit feature for Value Text field.
     * 
     * @param state
     *            holds true or false.
     */
    public void setValueEnabled(final boolean state) {
        valueTxt.setEditable(state);
    }

    /**
     * Used to set tree for selecting path.
     * 
     * @param elem
     *            holds tree input XML.
     */
    public void setInputXML(final Element elem) {
        inputXML = elem;
    }
}
