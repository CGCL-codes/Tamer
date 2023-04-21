package gruntspud.ui.view;

import gruntspud.CVSFileNode;
import gruntspud.GruntspudContext;
import gruntspud.ui.FolderBar;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public class SplitCompoundView extends AbstractCVSView implements ViewListener, CompoundView {

    private AbstractCVSView view1;

    private AbstractCVSView view2;

    private AbstractCVSView viewToGetCWDFrom;

    private boolean adjusting;

    private String dividerPropertyName;

    private ViewManager viewManager;

    private JSplitPane split;

    private Icon icon;

    private JPanel mainRightPanel;

    private FolderBar folderBar;

    private GruntspudContext context;

    /**
     * Creates a new SplitCompoundView object.
     *
     * @param name DOCUMENT ME!
     * @param toolTipText DOCUMENT ME!
     * @param dividerPropertyName DOCUMENT ME!
     * @param view1 DOCUMENT ME!
     * @param view2 DOCUMENT ME!
     * @param viewToGetCWDFrom DOCUMENT ME!
     * @param icon DOCUMENT ME!
     * @param context DOCUMENT ME!
     */
    public SplitCompoundView(String name, String toolTipText, String dividerPropertyName, AbstractCVSView view1, AbstractCVSView view2, AbstractCVSView viewToGetCWDFrom, Icon icon, GruntspudContext context) {
        super(name, icon, toolTipText);
        this.dividerPropertyName = dividerPropertyName;
        this.view1 = view1;
        this.view2 = view2;
        this.icon = icon;
        this.context = context;
        this.viewToGetCWDFrom = viewToGetCWDFrom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getViewCount() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View getView(int v) {
        return (v == 0) ? view1 : ((v == 1) ? view2 : null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsView(View view) {
        return (view == view1) || (view == view2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isViewVisible(View view) {
        return containsView(view);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsView(String name) {
        return getView(name) != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     */
    public void setSelectedView(View view) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View getView(String view) {
        if (view.equals(view1.getViewName())) {
            return view1;
        }
        if (view.equals(view2.getViewName())) {
            return view2;
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canClose() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cwdNode DOCUMENT ME!
     */
    public void setCWDNode(CVSFileNode cwdNode) {
        adjusting = true;
        view1.setCWDNode(cwdNode);
        view2.setCWDNode(cwdNode);
        setFolderBar(cwdNode);
        adjusting = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileNode getCWDNode() {
        return viewToGetCWDFrom.getCWDNode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param manager DOCUMENT ME!
     */
    public void start(ViewManager manager) {
        super.start(manager);
        view1.start(getManager());
        view2.start(getManager());
        folderBar = new FolderBar(view2.getViewName(), view2.getViewIcon());
        JPanel m = new JPanel(new BorderLayout());
        m.add(folderBar, BorderLayout.NORTH);
        mainRightPanel = new JPanel(new GridLayout(1, 1));
        mainRightPanel.add(view2.getViewComponent());
        m.add(mainRightPanel, BorderLayout.CENTER);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, view1.getViewComponent(), m);
        split.setOneTouchExpandable(true);
        split.setDividerSize(9);
        int i = getManager().getContext().getHost().getIntegerProperty(dividerPropertyName, -1);
        if (i != -1) {
            split.setDividerLocation(i);
        } else {
            split.setDividerLocation(200);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        view1.stop();
        view2.stop();
        getManager().getContext().getHost().setIntegerProperty(dividerPropertyName, split.getDividerLocation());
    }

    /**
     * DOCUMENT ME!
     */
    public void viewSelected() {
        view1.viewSelected();
        view2.viewSelected();
    }

    /**
     * DOCUMENT ME!
     */
    public void viewDeselected() {
        view1.viewDeselected();
        view2.viewDeselected();
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void reload(CVSFileNode node) {
        adjusting = true;
        view1.reload(node);
        view2.reload(node);
        adjusting = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void viewEvent(ViewEvent evt) {
        View view = (View) evt.getSource();
        if (view instanceof AbstractCVSView && (evt.getType() == ViewEvent.SELECTION_CHANGED) && !evt.isAdjusting()) {
            CVSFileNode[] sel = ((AbstractCVSView) view).getSelectedNodes();
            if (view == view1) {
                view2.setSelectedNodes(sel);
            } else {
                view1.setSelectedNodes(sel);
            }
        } else if (view instanceof AbstractCVSView && (evt.getType() == ViewEvent.CWD_CHANGED) && !evt.isAdjusting()) {
            CVSFileNode sel = ((AbstractCVSView) view).getCWDNode();
            if (view == view1) {
                view2.setCWDNode(sel);
            } else {
                view1.setCWDNode(sel);
            }
            setFolderBar(sel);
        } else if (view instanceof AbstractCVSView && (evt.getType() == ViewEvent.VIEW_CHANGED)) {
            int i = split.getDividerLocation();
            if (view == view1) {
                split.setLeftComponent(view.getViewComponent());
            } else if ((view == view2) && (mainRightPanel.getComponent(0) != view.getViewComponent())) {
                mainRightPanel.invalidate();
                mainRightPanel.removeAll();
                mainRightPanel.add(view.getViewComponent());
                mainRightPanel.validate();
                mainRightPanel.repaint();
            }
            setFolderBar(((AbstractCVSView) view).getCWDNode());
            split.setDividerLocation(i);
        }
        evt.setSource(this);
        fireViewEvent(evt);
    }

    private void setFolderBar(CVSFileNode cwdNode) {
        folderBar.setIcon(view2.getViewIcon());
        folderBar.setText(view2.getViewToolTipText());
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void addViewListener(ViewListener l) {
        super.addViewListener(l);
        view1.addViewListener(this);
        view2.addViewListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void removeViewListener(ViewListener l) {
        super.removeViewListener(l);
        view1.removeViewListener(this);
        view2.removeViewListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JComponent getViewComponent() {
        return split;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rootNode DOCUMENT ME!
     */
    public void setRootNode(CVSFileNode rootNode) {
        adjusting = true;
        view1.setRootNode(rootNode);
        view2.setRootNode(rootNode);
        setFolderBar(rootNode);
        adjusting = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sel DOCUMENT ME!
     */
    public void setSelectedNodes(CVSFileNode[] sel) {
        adjusting = true;
        view1.setSelectedNodes(sel);
        view2.setSelectedNodes(sel);
        adjusting = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void updateNode(CVSFileNode node) {
        view1.updateNode(node);
        view2.updateNode(node);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param node DOCUMENT ME!
	 */
    public void refilterAndResort() {
        view1.refilterAndResort();
        view2.refilterAndResort();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileNode[] getSelectedNodes() {
        return view1.getSelectedNodes();
    }

    public void viewEventNotify(ViewEvent evt) {
        view1.viewEventNotify(evt);
        view2.viewEventNotify(evt);
    }
}
