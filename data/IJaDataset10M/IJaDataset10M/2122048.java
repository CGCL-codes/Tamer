package org.xaware.schemanavigator.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Attribute;
import org.jdom.Element;
import org.xaware.schemanavigator.model.SchemaNavTreeNode;
import org.xaware.schemanavigator.panels.ICheckTreePanel;
import org.xaware.schemanavigator.util.IconFactory;
import org.xaware.schemanavigator.util.SearchUtil;

/**
 * SearchDlg is a dialog that provides the ability to search the schema navigator instance. Wildcard searches are also
 * allowed.
 * 
 * @author dwieland
 * 
 */
public class SearchDlg extends Dialog implements KeyListener {

    private ICheckTreePanel treePanel = null;

    private Text txtSearchElement = null;

    private Button chkbtnCaseSearch = null;

    private Button chkbtnWholeWord = null;

    private Button btnSearchNext = null;

    private Button btnClose = null;

    private Composite newCatComp;

    protected Shell shell;

    private Shell parent;

    protected int value;

    private String schemaInstName;

    private Label statusLabel;

    public SearchDlg(final Shell inParent, final ICheckTreePanel viewer, final String instanceName) {
        super(inParent);
        parent = inParent;
        treePanel = viewer;
        schemaInstName = instanceName;
        shell = new Shell(inParent, SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText("Search Schema Instance");
        shell.setLayout(new GridLayout());
        shell.setImage(IconFactory.XAWARE_IMAGE);
    }

    /**
     * open will open the dialog and dispose of it when complete.
     */
    @Override
    public int open() {
        value = 0;
        initialize();
        addListeners();
        txtSearchElement.forceFocus();
        shell.pack();
        shell.setDefaultButton(btnSearchNext);
        final Rectangle shellBounds = parent.getBounds();
        final Point dialogSize = shell.getSize();
        shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 4, shellBounds.y + (shellBounds.height - dialogSize.y) / 4);
        shell.open();
        final Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return value;
    }

    /**
     * Set the shell title for this dialog
     * 
     * @param titleTxt
     */
    public void setTitle(final String titleTxt) {
        shell.setText(titleTxt);
    }

    /**
     * Begin at the current node and traverse the tree searching for the text provided in txtSearchElement. This will
     * complete when the user closes from a not found message or end reached message or element found message
     * 
     */
    protected void searchTree() {
        String searchElemStr = txtSearchElement.getText();
        statusLabel.setText("");
        if (chkbtnWholeWord.getSelection() == false && searchElemStr.indexOf("*") == -1) {
            searchElemStr = "*" + searchElemStr + "*";
        }
        final TreeItem initSelNode = treePanel.getSelectedNode();
        TreeItem startNode = initSelNode;
        TreeItem srchNode = startNode;
        int startIndex = getStartIndex(startNode);
        int index = 0;
        final boolean done = false;
        boolean firstTime = true;
        while (done == false && srchNode != null) {
            final TreeItem[] children = srchNode.getItems();
            final int size = children.length;
            for (int i = index; i < size; i++) {
                final TreeItem foundNode = searchNode(children[i], searchElemStr, chkbtnCaseSearch.getSelection(), chkbtnWholeWord.getSelection());
                if (foundNode != null) {
                    treePanel.setSelectedNode(foundNode);
                    return;
                }
            }
            if (srchNode == startNode) {
                index = startIndex + 1;
                if (index >= size) {
                    if (firstTime == false) {
                        srchNode = srchNode.getParentItem();
                    }
                }
            } else {
                index = 0;
            }
            if (srchNode != null) {
                startIndex = getStartIndex(srchNode);
                startNode = srchNode;
                if (startIndex >= 0) {
                    index = startIndex + 1;
                }
                srchNode = srchNode.getParentItem();
            }
            if (srchNode == null) {
                final TreeItem rootNode = treePanel.getRootTreeNode();
                final TreeItem foundNode = searchNode(rootNode, searchElemStr, chkbtnCaseSearch.getSelection(), chkbtnWholeWord.getSelection());
                if (foundNode != null) {
                    treePanel.setSelectedNode(foundNode);
                    statusLabel.setText("Search restarted from top");
                    return;
                } else {
                    statusLabel.setText("Text not found");
                    treePanel.setSelectedNode(initSelNode);
                    return;
                }
            }
            firstTime = false;
        }
    }

    /**
     * Use element as a starting point
     * 
     * @param treeNode
     * @param searchElemStr
     * @param isCaseSensitive
     * @param isWholeWord
     *            TODO
     */
    public TreeItem searchNode(final TreeItem treeNode, final String searchElemStr, final boolean isCaseSensitive, final boolean isWholeWord) {
        final SchemaNavTreeNode node = (SchemaNavTreeNode) treeNode.getData();
        if (node == null) {
            return null;
        }
        final String name = node.getName();
        if (stringMatches(name, searchElemStr, isCaseSensitive, isWholeWord)) {
            return treeNode;
        }
        final Object obj = node.getData();
        if (obj instanceof Element) {
            final Element nodeElem = ((Element) obj);
            final String text = nodeElem.getText();
            if (stringMatches(text, searchElemStr, isCaseSensitive, isWholeWord)) {
                return treeNode;
            }
            if (node.hasXasnLabel()) {
                final String value = nodeElem.getAttributeValue(SchemaNavTreeNode.LABEL, SchemaNavTreeNode.snNamespace);
                if (stringMatches(value, searchElemStr, isCaseSensitive, isWholeWord)) {
                    return treeNode;
                }
            }
        } else if (obj instanceof Attribute) {
            final String value = ((Attribute) obj).getValue();
            if (stringMatches(value, searchElemStr, isCaseSensitive, isWholeWord)) {
                return treeNode;
            }
        }
        final TreeItem[] children = treeNode.getItems();
        final int size = children.length;
        for (int i = 0; i < size; i++) {
            final TreeItem foundItem = searchNode(children[i], searchElemStr, isCaseSensitive, isWholeWord);
            if (foundItem != null) {
                return foundItem;
            }
        }
        return null;
    }

    /**
     * calls either whole word match or normal match
     * 
     * @param baseStr
     * @param searchStr
     * @param isCaseSensitive
     * @param isWholeWord
     * @return
     */
    protected boolean stringMatches(final String baseStr, final String searchStr, final boolean isCaseSensitive, final boolean isWholeWord) {
        boolean matches = false;
        if (isWholeWord) {
            if (SearchUtil.stringRexEx(baseStr, "\\b" + searchStr + "\\b", isCaseSensitive)) {
                matches = true;
            }
        } else {
            if (SearchUtil.stringMatches(baseStr, searchStr, isCaseSensitive)) {
                matches = true;
            }
        }
        return matches;
    }

    /**
     * Add the listeners for the buttons and text fields in the dialog.
     * 
     */
    private void addListeners() {
        btnSearchNext.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                shell.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
                try {
                    value = 1;
                    searchTree();
                } catch (final RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    shell.setCursor(null);
                }
            }
        });
        btnClose.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                value = 0;
                shell.dispose();
            }
        });
        txtSearchElement.addKeyListener(this);
        shell.addListener(SWT.Traverse, new Listener() {

            public void handleEvent(final Event event) {
                if (event.detail == SWT.TRAVERSE_ESCAPE) {
                    event.doit = false;
                }
            }
        });
    }

    /**
     * loop thru the children of the parent of startNode to determine the index of startNode. If parent is null then
     * return -1
     * 
     * @param startNode
     * @return
     */
    private int getStartIndex(final TreeItem startNode) {
        int index = -1;
        final TreeItem parentItem = startNode.getParentItem();
        if (parentItem != null) {
            final TreeItem[] children = parentItem.getItems();
            final int size = children.length;
            for (int i = 0; i < size; i++) {
                if (children[i].equals(startNode)) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * create and initialize the main composite for inputing the description and location of the new category. If
     * editing the text fields will need to be set separately.
     * 
     */
    private void initialize() {
        newCatComp = new Composite(shell, SWT.NONE | SWT.RESIZE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        newCatComp.setLayout(gridLayout);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL);
        gridData.grabExcessHorizontalSpace = true;
        newCatComp.setLayoutData(gridData);
        newCatComp.setSize(new Point(408, 118));
        buildSearchCriteriaGroup();
        buildSearchCancelButtons();
    }

    /**
     * create group container for the search criteria
     * 
     */
    private void buildSearchCriteriaGroup() {
        final Group border = new Group(newCatComp, SWT.SHADOW_OUT);
        border.setText("Find in " + schemaInstName);
        final GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5;
        border.setLayout(gridLayout1);
        final GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL);
        border.setLayoutData(gridData1);
        final GridData dLabelGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        dLabelGridData.horizontalSpan = 5;
        dLabelGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        dLabelGridData.widthHint = 300;
        final CLabel lblSearchElement = new CLabel(border, SWT.NONE);
        lblSearchElement.setText("Matching text:");
        lblSearchElement.setLayoutData(dLabelGridData);
        final GridData tDescGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        tDescGridData.horizontalSpan = 5;
        tDescGridData.horizontalAlignment = GridData.FILL;
        tDescGridData.grabExcessHorizontalSpace = true;
        tDescGridData.widthHint = 200;
        txtSearchElement = new Text(border, SWT.BORDER);
        txtSearchElement.setLayoutData(tDescGridData);
        final GridData labelGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        labelGridData.horizontalSpan = 5;
        labelGridData.horizontalAlignment = GridData.FILL;
        labelGridData.grabExcessHorizontalSpace = true;
        labelGridData.widthHint = 100;
        final CLabel lblInstructions = new CLabel(border, SWT.NONE);
        lblInstructions.setText("( * = any string ) ");
        lblInstructions.setLayoutData(labelGridData);
        chkbtnCaseSearch = new Button(border, SWT.CHECK);
        chkbtnCaseSearch.setText("Case sensitive");
        chkbtnCaseSearch.setSelection(true);
        chkbtnWholeWord = new Button(border, SWT.CHECK);
        chkbtnWholeWord.setText("Whole word");
        chkbtnWholeWord.setSelection(false);
    }

    /**
     * Add Search/Cancel buttons to main component "newCatComp"
     * 
     */
    private void buildSearchCancelButtons() {
        final Composite btnComp = new Composite(newCatComp, SWT.NONE | SWT.RESIZE);
        final GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 4;
        btnComp.setLayout(gridLayout2);
        final GridData btnCompGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        btnCompGridData.grabExcessHorizontalSpace = true;
        btnCompGridData.horizontalIndent = 5;
        btnCompGridData.widthHint = 515;
        btnCompGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
        btnComp.setLayoutData(btnCompGridData);
        final GridData filler2GridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        filler2GridData.horizontalSpan = 2;
        filler2GridData.widthHint = 300;
        filler2GridData.grabExcessHorizontalSpace = true;
        filler2GridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        statusLabel = new Label(btnComp, SWT.NONE | SWT.RESIZE);
        statusLabel.setLayoutData(filler2GridData);
        final GridData btnGridData = new GridData();
        btnGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
        btnGridData.grabExcessHorizontalSpace = false;
        btnGridData.widthHint = 100;
        btnSearchNext = new Button(btnComp, SWT.NONE);
        btnSearchNext.setText("&Find Next");
        btnSearchNext.setLayoutData(btnGridData);
        btnSearchNext.setEnabled(false);
        btnClose = new Button(btnComp, SWT.NONE);
        btnClose.setText("&Close");
        btnClose.setLayoutData(btnGridData);
    }

    public void keyPressed(final KeyEvent arg0) {
    }

    /**
     * detect condition that text exists in text search and enable find button
     */
    public void keyReleased(final KeyEvent arg0) {
        if (txtSearchElement.getText().trim().length() > 0) {
            btnSearchNext.setEnabled(true);
            if (txtSearchElement.getText().indexOf("*") >= 0) {
                chkbtnWholeWord.setSelection(false);
                chkbtnWholeWord.setEnabled(false);
            } else {
                chkbtnWholeWord.setEnabled(true);
            }
        } else {
            btnSearchNext.setEnabled(false);
            chkbtnWholeWord.setEnabled(true);
        }
    }
}
