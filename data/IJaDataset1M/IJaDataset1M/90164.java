package org.remus.infomngmnt.common.ui.swt;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.remus.infomngmnt.common.ui.image.CommonImageRegistry;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class SearchText extends Composite {

    private Text searchText;

    private ToolBarManager filterToolBar;

    public SearchText(Composite parent, int style) {
        super(parent, style);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);
        init();
    }

    private void init() {
        this.searchText = doCreateFilterText(this);
        doAddListenerToTextField();
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.widthHint = SWT.DEFAULT;
        gridData.heightHint = SWT.DEFAULT;
        this.searchText.setLayoutData(gridData);
        this.filterToolBar = doCreateButtons();
    }

    /**
	 * Creates the text control for entering the filter text. Subclasses may
	 * override.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the text widget
	 * 
	 * @since 3.3
	 */
    protected Text doCreateFilterText(Composite parent) {
        return new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.CANCEL);
    }

    protected void handleSearchButtonPresses() {
        System.out.println("Search");
    }

    protected void doAddListenerToTextField() {
        this.searchText.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event event) {
                handleSearchButtonPresses();
            }
        });
    }

    protected ToolBarManager doCreateButtons() {
        ToolBarManager returnValue = new ToolBarManager(SWT.FLAT | SWT.HORIZONTAL | SWT.NO_BACKGROUND);
        ToolBar createControl = returnValue.createControl(this);
        GridData gridData = new GridData(SWT.END, SWT.FILL, false, false);
        gridData.widthHint = SWT.DEFAULT;
        gridData.heightHint = SWT.DEFAULT;
        createControl.setLayoutData(gridData);
        if ((this.searchText.getStyle() & SWT.CANCEL) == 0) {
            IAction clearTextAction = new Action("", IAction.AS_PUSH_BUTTON) {

                @Override
                public void run() {
                    clearText();
                }
            };
            clearTextAction.setToolTipText(WorkbenchMessages.FilteredTree_ClearToolTip);
            clearTextAction.setImageDescriptor(CommonImageRegistry.getInstance().getDescriptor(CommonImageRegistry.CLEAR_FILTER));
            returnValue.add(clearTextAction);
        }
        IAction searchAction = new Action("", IAction.AS_PUSH_BUTTON) {

            @Override
            public void run() {
                handleSearchButtonPresses();
            }
        };
        searchAction.setToolTipText("Search");
        searchAction.setImageDescriptor(CommonImageRegistry.getInstance().getDescriptor(CommonImageRegistry.START_TASK));
        returnValue.add(searchAction);
        returnValue.update(true);
        return returnValue;
    }

    /**
	 * Set the text in the search control.
	 * 
	 * @param string
	 */
    protected void setFilterText(String string) {
        if (this.searchText != null) {
            this.searchText.setText(string);
            updateToolbar(true);
        }
    }

    protected void clearText() {
        setFilterText("");
    }

    protected void updateToolbar(boolean visible) {
        if (this.filterToolBar != null) {
            this.filterToolBar.getControl().setVisible(visible);
        }
    }
}
