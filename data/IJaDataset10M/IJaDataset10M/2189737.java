package org.xaware.ide.xadev.gui;

import java.io.UnsupportedEncodingException;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jdom.Element;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.server.utils.FunctoidHelper;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareException;

/**
 * This calss is to open the functoid panel wich contains the
 * PredefinedFunctoid and AlFunctions tabs.
 *
 * @author GSVSN Murthy
 * @version 1.0
 */
public class FunctoidPanel {

    /** XA_Designer's Translator */
    public static final Translator t = XA_Designer_Plugin.getTranslator();

    /**Holds the String constant to be used as note in the functoid panels.*/
    public static final String ESCAPE_CHARACTERS_NOTE = "Note: To escape a character, type \\u and the unicode characters to replace " + "it. \nThe following characters should be escaped: $  , ( )";

    /** Holds previously selected tab index */
    private static int functoidPanelSelection = 0;

    /** AllFunctoidsPanel */
    private AllFunctoidsPanel allFunctoidsPanel;

    /** PredefinedFunctoidsPanel */
    private PredefinedFunctoidsPanel predefinedFunctoidsPanel;

    /** TabFolder */
    private TabFolder functoidTabFld;

    /** TabItems */
    private TabItem predefinedFunctionTabItm;

    /** TabItems */
    private TabItem allFunctionsTabItm;

    /** Shell */
    private Shell shell;

    /** Composite */
    private Composite functoidComp;

    /** boolean */
    protected boolean canceled;

    /** Selected tabtem */
    private int selectedTab;

    /** Stores the functoidString which will be applied to the selected item */
    private String functoidString = "";

    private Element selectedElem;

    /** Edit functoid flag */
    private boolean isEditFunctoId;

    /**
	 * Creates a new FunctoidPanel object.
	 *
	 * @param inParent Shell
	 * @param param String
	 * @param inputParams Vector
	 * @param sourceRoot Element
     * @param p_selectedElement the element the path will be applied on.  If
     * the path is being applied on an attribute then the parent element
     * for that attribute should be passed in.
	 * @throws XAwareException if functoid string is invalid.
	 * @throws UnsupportedEncodingException if error occurs while decoding params.
	 */
    public FunctoidPanel(final Shell inParent, final String param, final Vector inputParams, final Element sourceRoot, final Element p_selectedElement) throws XAwareException, UnsupportedEncodingException {
        this(inParent, param, inputParams, sourceRoot, p_selectedElement, false);
    }

    /**
	 * Creates a new FunctoidPanel object.
	 *
	 * @param inParent Shell
	 * @param param String
	 * @param inputParams Vector
	 * @param sourceRoot Element
     * @param p_selectedElement the element the path will be applied on.  If
     * the path is being applied on an attribute then the parent element
     * for that attribute should be passed in.
     * @param editFunctoId edit functoid flag.
	 * @throws XAwareException if functoid string is invalid.
	 * @throws UnsupportedEncodingException if error occurs while decoding params.
	 */
    public FunctoidPanel(final Shell inParent, final String param, final Vector inputParams, final Element sourceRoot, final Element p_selectedElement, boolean editFunctoId) throws XAwareException, UnsupportedEncodingException {
        shell = inParent;
        selectedElem = p_selectedElement;
        functoidComp = new Composite(shell, SWT.NONE);
        isEditFunctoId = editFunctoId;
        final GridLayout functoidGrid = new GridLayout();
        functoidGrid.numColumns = 1;
        functoidComp.setLayout(functoidGrid);
        functoidTabFld = new TabFolder(functoidComp, SWT.NONE);
        predefinedFunctionTabItm = new TabItem(functoidTabFld, SWT.NONE);
        predefinedFunctionTabItm.setText(t.getString("Predefined Functions"));
        allFunctionsTabItm = new TabItem(functoidTabFld, SWT.NONE);
        allFunctionsTabItm.setText(t.getString("All Functions"));
        final GridData tabFolderGrid = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        tabFolderGrid.heightHint = 420;
        functoidTabFld.setLayoutData(tabFolderGrid);
        predefinedFunctoidsPanel = new PredefinedFunctoidsPanel(functoidTabFld, param, inputParams, (Element) sourceRoot.clone(), selectedElem, isEditFunctoId);
        predefinedFunctionTabItm.setControl(predefinedFunctoidsPanel);
        allFunctoidsPanel = new AllFunctoidsPanel(functoidTabFld, param, inputParams, (Element) sourceRoot.clone(), selectedElem, isEditFunctoId);
        allFunctionsTabItm.setControl(allFunctoidsPanel);
        if (isEditFunctoId) {
            if (FunctoidHelper.isXAwareFunctoid(param)) {
                functoidTabFld.setSelection(0);
            } else if (FunctoidHelper.isJavaFunctoid(param)) {
                functoidTabFld.setSelection(1);
            }
        } else {
            functoidTabFld.setSelection(functoidPanelSelection);
        }
    }

    /**
	 * Constructs the functoid and assingns to the functoidString.
	 *
	 * @return boolean.
	 */
    protected boolean constructFunctoid() {
        String ret = "";
        selectedTab = functoidTabFld.getSelectionIndex();
        if (selectedTab == 0) {
            if (!predefinedFunctoidsPanel.isPanelValid()) {
                return false;
            }
            ret = predefinedFunctoidsPanel.getFunctoidString();
        } else if (selectedTab == 1) {
            if (!allFunctoidsPanel.isPanelValid()) {
                return false;
            }
            ret = allFunctoidsPanel.getFunctoidString();
        }
        functoidString = ret;
        return true;
    }

    /**
	 * Returns the functoid string.
	 *
	 * @return functoidString of type String.
	 */
    public String getFunctoidString() {
        return functoidString;
    }

    /**
	 * Dispalys the Apply functoid Dialog.
	 *
	 * @return true of ok pressed, false if Cancel pressed.
	 */
    public boolean showDialog() {
        String title = "";
        if (isEditFunctoId) title = t.getString("Edit Functoid"); else title = t.getString("Apply Functoid");
        canceled = true;
        final XADialog dialog = new XADialog(shell, new XADialogOperation() {

            public boolean okPressed() {
                if (!constructFunctoid()) {
                    return false;
                }
                canceled = false;
                saveFunctoidPanelStatus();
                return true;
            }

            public boolean cancelPressed() {
                canceled = true;
                return true;
            }
        }, functoidComp, title, true, true);
        dialog.open();
        if (canceled) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * Sets the table columns.
	 *
	 * @param vec Vector.
	 */
    public void setTableColumns(final Vector vec) {
        predefinedFunctoidsPanel.setTableColumns(vec);
        allFunctoidsPanel.setTableColumns(vec);
    }

    /**
	 * Saves the current status of the functoid panel so that it can be displayed
	 * when functoid is again selected.
	 *
	 */
    protected void saveFunctoidPanelStatus() {
        functoidPanelSelection = functoidTabFld.getSelectionIndex();
        predefinedFunctoidsPanel.saveStatus();
        allFunctoidsPanel.saveStatus();
    }
}
