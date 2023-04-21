package edu.mit.lcs.haystack.server.extensions.infoextraction.domnav;

import java.util.LinkedHashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import edu.mit.lcs.haystack.server.extensions.infoextraction.BrowserFrame;
import edu.mit.lcs.haystack.server.extensions.infoextraction.Const;
import edu.mit.lcs.haystack.server.extensions.infoextraction.DefaultComposite;
import edu.mit.lcs.haystack.server.extensions.infoextraction.IDocumentFocus;
import edu.mit.lcs.haystack.server.extensions.infoextraction.IHighlighter;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.IDOMBrowser;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.INode;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.NodeID;

/**
 * @author yks
 */
public class DOMNavigatorView extends DefaultComposite implements IDOMNavHandler, IDocumentFocus {

    protected BrowserFrame browserFrame;

    protected Button nextSiblingButton = null;

    protected Button prevSiblingButton = null;

    protected Button downTreeButton = null;

    protected Button upTreeButton = null;

    protected DOMNavigator domNavigator;

    protected List domList = null;

    protected LinkedHashMap domListMap = null;

    public void setBrowserFrame(BrowserFrame browserFrame) {
        this.browserFrame = browserFrame;
    }

    public IDOMBrowser getBrowser() {
        return this.browserFrame.getBrowserWidget();
    }

    public IHighlighter getHighlighter() {
        return this.browserFrame;
    }

    public INode getDOM(IDOMBrowser browser) {
        if (browser != null && browser.getDocument() != null && browser.getDocument().getDocumentElement() != null) {
            return (INode) browser.getDocument().getDocumentElement();
        } else {
            return null;
        }
    }

    public INode getDOM() {
        return getDOM(this.getBrowser());
    }

    public void runAction(INode node) {
        this.setFocus(node);
        NodeID curNodeID = node.getNodeID();
        getHighlighter().highlightNodeByNodeID(curNodeID, Const.DEFAULT_BGCOLOR, Const.DEFAULT_FGCOLOR);
    }

    private LinkedHashMap buildPathTrace(NodeID nodeID, INode root) {
        LinkedHashMap lhm = new LinkedHashMap();
        buildPathTraceRecur(lhm, nodeID, root, nodeID.getLength());
        return lhm;
    }

    private void buildPathTraceRecur(LinkedHashMap lhm, NodeID nodeID, INode root, int depth) {
        System.err.println("DOMNAV: " + nodeID.toString());
        INode nodes[] = nodeID.getNodes(root);
        if (depth <= 0) {
            return;
        }
        if (nodes != null && nodes.length > 0) {
            INode cur = nodes[0];
            INode parent = (INode) cur.getParentNode();
            lhm.put(cur.getNodeID().toString(), cur);
            if (parent != null && parent != cur && parent != root) {
                NodeID parentID = parent.getNodeID();
                if (parentID != null) {
                    buildPathTraceRecur(lhm, parentID, root, depth - 1);
                }
            }
        }
    }

    public void setFocus(INode node) {
        NodeID nodeID = node.getNodeID();
        domListMap = buildPathTrace(nodeID, (INode) this.getDOM());
        domNavigator.setCurNodeID(nodeID);
    }

    /**
     * Dom navigation tab.
     * 
     * @param parent
     * @return
     */
    public DOMNavigatorView(Display display, Composite parent, int style, BrowserFrame browserFrame) {
        super(display, parent, style, browserFrame);
        setLayout(new GridLayout());
        domNavigator = new DOMNavigator(this);
        Group controlsGroup = new Group(this, SWT.NONE);
        GridLayout gl = new GridLayout();
        controlsGroup.setText("Controls");
        gl.numColumns = 4;
        controlsGroup.setLayout(gl);
        controlsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        prevSiblingButton = new Button(controlsGroup, SWT.PUSH | SWT.ARROW_LEFT);
        prevSiblingButton.setText("Prev Sib");
        prevSiblingButton.setLayoutData(new GridData());
        prevSiblingButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                domNavigator.nextSibling();
            }
        });
        nextSiblingButton = new Button(controlsGroup, SWT.PUSH | SWT.ARROW_RIGHT);
        nextSiblingButton.setText("Next Sib");
        nextSiblingButton.setLayoutData(new GridData());
        nextSiblingButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                domNavigator.prevSibling();
            }
        });
        upTreeButton = new Button(controlsGroup, SWT.PUSH | SWT.ARROW_UP);
        upTreeButton.setText("Up Level");
        upTreeButton.setLayoutData(new GridData());
        upTreeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                domNavigator.moveUpTree();
            }
        });
        downTreeButton = new Button(controlsGroup, SWT.PUSH | SWT.ARROW_DOWN);
        downTreeButton.setText("Down Level");
        downTreeButton.setLayoutData(new GridData());
        downTreeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                domNavigator.moveDownTree();
            }
        });
        Group domListGroup = new Group(this, SWT.NONE);
        domListGroup.setLayout(new GridLayout());
        domListGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        domListGroup.setText("DOM hierarchy");
        domList = new List(domListGroup, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        domList.setToolTipText("click on an item to see its info");
        domList.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
        domList.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String selection[] = domList.getSelection();
                if (selection != null && selection.length > 0) {
                    INode node = (INode) domListMap.get(selection[0]);
                    getHighlighter().clearHighlighted();
                    getHighlighter().highlightNodeByNodeID(node.getNodeID(), Const.DEFAULT_BGCOLOR, Const.DEFAULT_FGCOLOR);
                }
            }
        });
    }
}
