package org.xaware.ide.xadev.gui.dialog;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.IllegalNameException;
import org.jdom.Namespace;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.gui.XADialog;
import org.xaware.ide.xadev.gui.XADialogOperation;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.PictureClauseUtility;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Class to add an element to the root element.
 * 
 * @author GSVSN Murthy
 * @version 1.0
 */
public class AddMultiFieldDlg implements XADialogOperation {

    /** Holds instance of XA_Designer_Plugin translator */
    public static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Logger for XAware. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(AddMultiFieldDlg.class.getName());

    /** Reference to the namespace. */
    protected static final Namespace ns = XAwareConstants.xaNamespace;

    /** Holds instance of string constant. */
    public static final String FILE_READ = XAwareConstants.XAWARE_FILE_READ;

    /** Holds instance of string constant. */
    public static final String FILE_WRITE = XAwareConstants.XAWARE_FILE_WRITE;

    /** Reference to String */
    public static final String DELIMITED = "Delimited";

    /** Reference to String */
    public static final String FIXED_LENGTH = "Fixed length";

    /** Reference to String */
    public static final String TEXT2XML = "Text to XML";

    /** Reference to String */
    public static final String XML2TEXT = "XML to Text";

    /** Reference to Text Filed */
    protected Text startTF;

    /** Reference to Text Filed */
    protected Text fieldNameTF;

    /** Reference to Text Filed */
    protected Text fieldLengthTF;

    /** Reference to Button */
    protected Button fieldTrimCB;

    /** Reference to Element */
    protected Element elem;

    /** Reference to Element */
    protected Element rootElem;

    /** Reference to Shell */
    protected Shell owner;

    /** Reference to String */
    protected String initName = "";

    /** Reference to boolean Variable */
    protected boolean trimWhiteSpace = false;

    /** Reference to boolean Variable */
    protected boolean isMatchField = false;

    /** Reference to String */
    private String title;

    /** Reference to XADialog */
    private XADialog dialog;

    /** Reference to composite */
    private Composite rootComp;

    /** Reference to composite */
    private Composite mainComp;

    /** Reference to Text Filed */
    private Text fieldMatchTF;

    /** Reference to Text Filed */
    private Text fieldPicTF;

    /**
     * Creates a new AddMultiFieldDlg object.
     * 
     * @param owner
     *            Reference to Shell
     * @param title
     *            title of Dialog
     * @param modal
     *            boolean variable
     * @param enableMatchField
     *            boolean variable
     */
    public AddMultiFieldDlg(final Shell owner, final String title, final boolean modal, final boolean enableMatchField) {
        rootElem = null;
        this.owner = owner;
        this.title = title;
        isMatchField = enableMatchField;
        GridData gridData;
        mainComp = new Composite(owner, SWT.NONE);
        mainComp.setLayout(new GridLayout(1, false));
        mainComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.GRAB_VERTICAL));
        rootComp = new Composite(mainComp, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.verticalSpacing = 10;
        rootComp.setLayout(gridLayout);
        rootComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL));
        final Label fieldNameLabel = new Label(rootComp, SWT.NONE);
        fieldNameLabel.setText(translator.getString("Field name:"));
        fieldNameTF = ControlFactory.createText(rootComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gridData.widthHint = 200;
        gridData.heightHint = 16;
        fieldNameTF.setLayoutData(gridData);
        final Label startPosLabel = new Label(rootComp, SWT.NONE);
        startPosLabel.setText(translator.getString("Start position:"));
        startTF = ControlFactory.createText(rootComp, SWT.BORDER);
        startTF.setLayoutData(gridData);
        final Label fieldLengthLabel = new Label(rootComp, SWT.NONE);
        fieldLengthLabel.setText(translator.getString("Field length:"));
        fieldLengthTF = ControlFactory.createText(rootComp, SWT.BORDER);
        fieldLengthTF.setLayoutData(gridData);
        final Label fieldMatchLabel = new Label(rootComp, SWT.NONE);
        fieldMatchLabel.setText(translator.getString("Field match:"));
        fieldMatchTF = ControlFactory.createText(rootComp, SWT.BORDER);
        fieldMatchTF.setEnabled(isMatchField);
        fieldMatchTF.setLayoutData(gridData);
        final Label fieldPictLabel = new Label(rootComp, SWT.NONE);
        fieldPictLabel.setText(translator.getString("Field picture:"));
        fieldPicTF = ControlFactory.createText(rootComp, SWT.BORDER);
        fieldPicTF.setLayoutData(gridData);
        final Label dummyLbl = new Label(rootComp, SWT.NONE);
        fieldTrimCB = new Button(rootComp, SWT.CHECK);
        fieldTrimCB.setText(translator.getString("Trim white space"));
        fieldTrimCB.setSelection(false);
        trimWhiteSpace = false;
    }

    /**
     * responsible for resetting the fields
     * 
     * @param isMatchFieldEnabled
     *            boolean variable
     */
    public void resetFields(final boolean isMatchFieldEnabled) {
        startTF.setText("0");
        fieldNameTF.setText("");
        fieldLengthTF.setText("");
        fieldMatchTF.setText("");
        fieldMatchTF.setEnabled(isMatchFieldEnabled);
        fieldPicTF.setText("");
        fieldTrimCB.setSelection(false);
        trimWhiteSpace = false;
    }

    /**
     * Set the name and remember initial name for unique validation in isUnique()
     * 
     * @param name
     */
    public void setInitialName(final String name) {
        initName = name;
        setName(name);
    }

    /**
     * Sets the Pic field value.
     * 
     * @param name
     *            reference to String
     */
    public void setPic(final String name) {
        if (fieldPicTF != null) {
            fieldPicTF.setText(name);
        }
    }

    /**
     * Sets the Match field value.
     * 
     * @param name
     *            reference to String
     */
    public void setMatch(final String name) {
        if (fieldMatchTF != null) {
            fieldMatchTF.setText(name);
        }
    }

    /**
     * Sets the Name field value.
     * 
     * @param name
     *            reference to String
     */
    public void setName(final String name) {
        if (fieldNameTF != null) {
            fieldNameTF.setText(name);
        }
    }

    /**
     * Sets the FieldLength value.
     * 
     * @param length
     *            reference to String
     */
    public void setFieldLength(final String length) {
        if (fieldLengthTF != null) {
            fieldLengthTF.setText(length);
        }
    }

    /**
     * Sets the TrimWhiteSpace value.
     * 
     * @param trim
     *            boolean variable
     */
    public void setTrimWhiteSpace(final boolean trim) {
        fieldTrimCB.setSelection(trim);
        trimWhiteSpace = trim;
    }

    /**
     * sets Start Position filed value
     * 
     * @param length
     *            reference to String
     */
    public void setStartTF(final String length) {
        if (startTF != null) {
            startTF.setText(length);
        }
    }

    /**
     * Displays Dialog
     * 
     * @param requestType
     *            reference to String
     */
    public void showDialog(final String requestType) {
        try {
            fieldNameTF.setFocus();
            dialog = new XADialog(owner, this, mainComp, title, true, true);
            dialog.open();
        } catch (final Exception e) {
            ControlFactory.showMessageDialog("ERROR: " + e.getMessage(), "ERROR", MessageDialog.ERROR);
            logger.printStackTrace(e);
        }
    }

    /**
     * if rootElem has been set then make sure the new name is unique among the rootElem children.
     * 
     * @param name
     * 
     * @return boolean variable
     */
    protected boolean isUnique(final String name) {
        boolean isUnique = true;
        if (rootElem != null) {
            final List children = rootElem.getChildren();
            final Iterator itr = children.iterator();
            while (itr.hasNext()) {
                final Element elem = (Element) itr.next();
                final String elemName = elem.getName();
                if ((initName.equals(name) == false) && elemName.equals(name)) {
                    isUnique = false;
                    break;
                }
            }
        }
        return isUnique;
    }

    /**
     * Checks for validity of values are entered for fileds in MultiFieldDlg
     * 
     * @return boolean variable
     */
    private boolean isValiedMultiField() {
        if (fieldNameTF.isVisible() && (fieldNameTF.getText().trim().length() == 0)) {
            ControlFactory.showMessageDialog(translator.getString("Please enter a field name."), translator.getString("Information"));
            fieldNameTF.setFocus();
            return false;
        }
        if (startTF.isVisible() && (startTF.getText().trim().length() == 0)) {
            ControlFactory.showMessageDialog(translator.getString("Please enter field start position."), translator.getString("Information"));
            setFocus(startTF);
            return false;
        }
        if (startTF.isVisible()) {
            try {
                final int pos = new Integer(startTF.getText().trim()).intValue();
                if (pos < 0) {
                    ControlFactory.showMessageDialog(translator.getString("Please enter value greater than or equal to 0 for field start position."), translator.getString("Information"));
                    setFocus(startTF);
                    return false;
                }
                if (pos > 999999999) {
                    ControlFactory.showMessageDialog(translator.getString("Please enter value less than 999,999,999 for field start position."), translator.getString("Information"));
                    setFocus(startTF);
                    return false;
                }
            } catch (final Throwable ex) {
                ControlFactory.showMessageDialog(translator.getString("Please enter numeric value for field start position."), translator.getString("Information"));
                setFocus(startTF);
                return false;
            }
        }
        if (fieldLengthTF.getText().trim().length() == 0) {
            ControlFactory.showMessageDialog(translator.getString("Please enter field length."), translator.getString("Information"));
            setFocus(fieldLengthTF);
            return false;
        }
        try {
            final int pos = new Integer(fieldLengthTF.getText().trim()).intValue();
            if (pos <= 0) {
                ControlFactory.showMessageDialog(translator.getString("Please enter value greater than 0 for field length."), translator.getString("Information"));
                setFocus(fieldLengthTF);
                return false;
            }
            if (pos > 999999999) {
                ControlFactory.showMessageDialog(translator.getString("Please enter value less than 999,999,999 for field length."), translator.getString("Information"));
                setFocus(fieldLengthTF);
                return false;
            }
        } catch (final Throwable ex) {
            ControlFactory.showMessageDialog(translator.getString("Please enter numeric value for field length."), translator.getString("Information"));
            setFocus(fieldLengthTF);
            return false;
        }
        try {
            if (isUnique(fieldNameTF.getText()) == false) {
                ControlFactory.showMessageDialog(translator.getString("Element name \"" + fieldNameTF.getText() + "\" is not unique."), "Information");
                return false;
            }
            if ((fieldPicTF.getText().length() > 0) && (fieldMatchTF.getText().length() > 0)) {
                try {
                    Integer.parseInt(fieldMatchTF.getText());
                } catch (final NumberFormatException e1) {
                    ControlFactory.showMessageDialog(translator.getString("Match field string \"" + fieldMatchTF.getText() + "\" must be an integer if picture value is set."), "Information");
                    return false;
                }
            }
            if (isPicValid() == false) {
                return false;
            }
            elem = new Element(fieldNameTF.getText());
        } catch (final IllegalNameException ine) {
            ControlFactory.showMessageDialog(translator.getString("Illegal element name \"" + fieldNameTF.getText() + "\"."), "Information");
            return false;
        }
        if (startTF.isVisible()) {
            elem.setAttribute("start", startTF.getText().trim(), ns);
        }
        elem.setAttribute(XAwareConstants.BIZCOMPONENT_ATTR_FIELDLENGTH, fieldLengthTF.getText().trim(), ns);
        if (fieldPicTF.getText().length() > 0) {
            elem.setAttribute(XAwareConstants.XAWARE_ATTR_PIC_VALUE, fieldPicTF.getText().trim(), ns);
        }
        if (fieldMatchTF.getText().length() > 0) {
            elem.setAttribute(XAwareConstants.BIZCOMPONENT_ATTR_FIELD_MATCH, fieldMatchTF.getText().trim(), ns);
        }
        String trim = XAwareConstants.XAWARE_YES;
        if (fieldTrimCB.getSelection()) {
            trimWhiteSpace = true;
            trim = XAwareConstants.XAWARE_YES;
        } else {
            trimWhiteSpace = false;
            trim = XAwareConstants.XAWARE_NO;
        }
        elem.setAttribute(XAwareConstants.XAWARE_TRIM, trim, ns);
        return true;
    }

    /**
     * validate the Pic field as valid picture clause and that the length is the same as field length. This will also
     * display error messages if fields are not valid.
     * 
     * @return
     */
    protected boolean isPicValid() {
        if (fieldPicTF.getText().length() <= 0) {
            return true;
        }
        final PictureClauseUtility picTool = new PictureClauseUtility();
        try {
            final int picLen = picTool.getFieldLength(fieldPicTF.getText(), "");
            final int fieldLen = Integer.parseInt(fieldLengthTF.getText());
            if (picLen != fieldLen) {
                ControlFactory.showMessageDialog(translator.getString("Length of Pic and field length should be equal. " + "Pic length = " + picLen + "  Field length = " + fieldLengthTF.getText()), "Information");
                return false;
            }
        } catch (final Exception e) {
            ControlFactory.showMessageDialog(translator.getString("Picture Clause value is invalid: " + fieldPicTF.getText()), "Information");
            return false;
        }
        return true;
    }

    /**
     * Sets Root Element.
     * 
     * @param elem
     *            reference to Element.
     */
    public void setRootElem(final Element elem) {
        rootElem = elem;
    }

    /**
     * returns Element.
     * 
     * @return instance of Element.
     */
    public Element getElement() {
        return elem;
    }

    /**
     * Implements okPressed() of XADialog.
     * 
     * @return boolean variable
     */
    public boolean okPressed() {
        return isValiedMultiField();
    }

    /**
     * Implements cancelPressed()of XADialog.
     * 
     * @return boolean variable
     */
    public boolean cancelPressed() {
        elem = null;
        fieldNameTF.setFocus();
        return true;
    }

    /**
     * sets focus to the previously control.
     * 
     * @param control
     *            control
     */
    private void setFocus(final Control control) {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                if (control != null) {
                    control.setFocus();
                }
            }
        });
    }
}
