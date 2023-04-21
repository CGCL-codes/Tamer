package org.gvt.inspector;

import java.util.Set;
import org.biopax.paxtools.model.level2.physicalEntity;
import org.biopax.paxtools.model.level2.xref;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.gvt.ChisioMain;
import org.gvt.util.AbstractOptionsPack;

/**
 * This class is abstract class for Local Query Dialogs
 *
 * @author Ozgun Babur
 * @author Merve Cakir
 * @author Shatlyk Ashyralyev
 * 
 * Copyright: I-Vis Research Group, Bilkent University, 2007
 */
public abstract class AbstractQueryParamDialog extends Dialog {

    /**
	 * buttons for adding and removing entities
	 */
    protected Button addButton;

    protected Button removeButton;

    protected List entityList;

    /**
	 * show result
	 */
    protected Group resultViewGroup;

    protected Button currentViewButton;

    protected Button newViewButton;

    /**
	 * buttons to quit the dialog
	 */
    protected Button executeButton;

    protected Button cancelButton;

    /**
	 * Button to restore default valus of dialog
	 */
    protected Button defaultButton;

    /**
	 * length limit (depth)
	 */
    protected Label lengthLimitLabel;

    protected Text lengthLimit;

    /**
	 * Shell used for query dialogs
	 */
    protected Shell shell;

    /**
	 * Main ChisioMain
	 */
    protected ChisioMain main;

    /**
	 * Key adapter to filter nonDigit characters from Length Limit Text
	 */
    protected KeyAdapter keyAdapter = new KeyAdapter() {

        public void keyPressed(KeyEvent arg0) {
            arg0.doit = isDigit(arg0.keyCode);
        }

        public boolean isDigit(int keyCode) {
            if (Character.isDigit(keyCode) || keyCode == SWT.DEL || keyCode == 8 || keyCode == SWT.ARROW_LEFT || keyCode == SWT.ARROW_RIGHT) {
                return true;
            }
            return false;
        }
    };

    /**
	 * Create the dialog
	 */
    public AbstractQueryParamDialog(ChisioMain main) {
        super(main.getShell(), SWT.NONE);
        this.main = main;
    }

    /**
	 * Create shell for query dialogs
	 * @param opt
	 */
    protected void createContents(AbstractOptionsPack opt) {
        shell = new Shell(getParent(), SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    }

    /**
	 * Method for creating a list
	 */
    protected void createList(int horizontalSpan, int verticalSpan, int widthHint, int numberOfItems) {
        entityList = new List(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.HORIZONTAL);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.verticalSpan = verticalSpan;
        gridData.horizontalSpan = horizontalSpan;
        gridData.heightHint = entityList.getItemHeight() * numberOfItems;
        gridData.widthHint = widthHint;
        entityList.setLayoutData(gridData);
    }

    /**
	 * Method for creating Result View Group
	 */
    protected void createResultViewGroup(int horizontalSpan, int verticalSpan) {
        resultViewGroup = new Group(shell, SWT.NONE);
        resultViewGroup.setText("Show the result in:");
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        gridData.horizontalSpan = horizontalSpan;
        gridData.verticalSpan = verticalSpan;
        resultViewGroup.setLayoutData(gridData);
        resultViewGroup.setLayout(new GridLayout());
        currentViewButton = new Button(resultViewGroup, SWT.RADIO);
        currentViewButton.setText("Current View");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        currentViewButton.setLayoutData(gridData);
        newViewButton = new Button(resultViewGroup, SWT.RADIO);
        newViewButton.setText("New View");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        newViewButton.setLayoutData(gridData);
    }

    /**
	 * Method for creating Length Limit Label and Text
	 */
    protected void createLengthLimit(int horizontalSpanLabel, int verticalSpanLabel, int horizontalSpanText, int verticalSpanText, int minTextWidth) {
        lengthLimitLabel = new Label(shell, SWT.NONE);
        lengthLimitLabel.setText("Length Limit:");
        GridData gridData = new GridData(GridData.END, GridData.CENTER, true, false);
        gridData.horizontalSpan = horizontalSpanLabel;
        gridData.verticalSpan = verticalSpanLabel;
        lengthLimitLabel.setLayoutData(gridData);
        lengthLimit = new Text(shell, SWT.BORDER);
        lengthLimit.addKeyListener(keyAdapter);
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gridData.horizontalSpan = horizontalSpanText;
        gridData.verticalSpan = verticalSpanText;
        gridData.minimumWidth = minTextWidth;
        lengthLimit.setLayoutData(gridData);
    }

    /**
	 * Method to create cancel button
	 */
    protected void createCancelButton(GridData gridData) {
        cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setText("Cancel");
        cancelButton.setLayoutData(gridData);
        cancelButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                shell.close();
            }
        });
    }

    /**
	 * Method to set default values of abstract dialog
	 */
    public void setDefaultQueryDialogOptions() {
        currentViewButton.setSelection(CURRENT_VIEW);
        newViewButton.setSelection(!CURRENT_VIEW);
        lengthLimit.setText(String.valueOf(DEFAULT_LENGTH_LIMIT));
    }

    /**
	 * This method gets NAME of entity if NAME is not null.
	 * If it is null, then it gets ShortName of entity.
	 * If ShortName is also null, then it checks for synonyms.
	 * Lastly, it checks referenceID's
	 */
    public static String getKeyNAME(physicalEntity entity) {
        String entityName = entity.getNAME();
        if (entityName != null) {
            return entityName;
        }
        String entityShortName = entity.getSHORT_NAME();
        if (entityShortName != null) {
            return entityShortName;
        }
        Set<String> entitySynonyms = entity.getSYNONYMS();
        for (String synonym : entitySynonyms) {
            if (synonym != null) {
                return synonym;
            }
        }
        Set<xref> entityRef = entity.getXREF();
        for (xref reference : entityRef) {
            String refID = reference.getID();
            if (refID != null) {
                return refID;
            }
        }
        return null;
    }

    /**
	 * Values for default options
	 */
    public static final boolean DOWNSTREAM = true;

    public static final boolean UPSTREAM = true;

    public static final boolean CURRENT_VIEW = true;

    public static final int DEFAULT_LENGTH_LIMIT = 1;

    public static final int DEFAULT_SHORTEST_PLUS_K = 0;

    public static final boolean LIMIT_TYPE = true;

    public static final boolean STRICT = false;
}
