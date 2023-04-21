package de.beas.explicanto.client.rcp.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.datastructures.CLesson;
import de.bea.services.vidya.client.datastructures.CPage;
import de.bea.services.vidya.client.datastructures.CStatusNode;
import de.bea.services.vidya.client.datastructures.CUnit;
import de.bea.services.vidya.client.datastructures.CUnitItem;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.model.Node;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.rcp.editor.parts.DiagramEditPart;
import de.beas.explicanto.client.rcp.pageedit2.PageEditor;
import de.beas.explicanto.client.sec.model.Model;

/**
 * The node properties view 
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class PropertiesView extends GenericView {

    public static final String ID = "de.beas.explicanto.client.rcp.views.PropertiesView";

    public static final int LOCK = 1;

    public static final int STORE = 2;

    public static final int UNLOCK = 3;

    private LessonPanel lessonPanel;

    private CoursePanel coursePanel;

    private UnitPanel unitPanel;

    private Composite composite;

    private StatusPanel statusPanel;

    private PageEditor pageEditor;

    private GenericNodePanel currentPanel;

    private boolean pageShowing;

    private UnitItemPanel unitItemPanel;

    public void createPartControl(Composite parent) {
        setPartName(translate("propsView.title"));
        composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = layout.marginWidth = 0;
        composite.setLayout(layout);
        statusPanel = new StatusPanel(composite);
        coursePanel = new CoursePanel(composite, statusPanel, getViewSite().getWorkbenchWindow());
        lessonPanel = new LessonPanel(composite, statusPanel, getViewSite().getWorkbenchWindow());
        unitPanel = new UnitPanel(composite, statusPanel, getViewSite().getWorkbenchWindow(), this);
        unitItemPanel = new UnitItemPanel(composite, statusPanel, getViewSite().getWorkbenchWindow());
        pageEditor = new PageEditor(composite);
        statusPanel.moveBelow(unitPanel);
        getSite().getPage().addSelectionListener(this);
        getSite().setSelectionProvider(pageEditor);
        getSite().getPage().addPartListener(this);
        super.createPartControl(parent);
    }

    public void setFocus() {
        if (currentPanel != null) currentPanel.setFocus();
        if (pageShowing) {
            if (pageEditor.getSelectedComp() != null) pageEditor.getSelectedComp().setFocus(); else if (pageEditor.getSelRegion() != null) pageEditor.getSelRegion().setFocus();
        }
    }

    protected void populateFor(TreeNode treeNode, Node csdeNode) {
        pageShowing = false;
        if (treeNode instanceof CCourse) {
            showPanel(coursePanel);
            coursePanel.setNode(csdeNode);
            coursePanel.setEditableNode(csdeNode.getTemporaryTreeNode());
            coursePanel.populate((CCourse) treeNode);
            statusPanel.setPanelVisible(true);
            getSite().getSelectionProvider().setSelection(new StructuredSelection(csdeNode));
        } else if (treeNode instanceof CLesson) {
            showPanel(lessonPanel);
            lessonPanel.setNode(csdeNode);
            lessonPanel.setEditableNode(csdeNode.getTemporaryTreeNode());
            lessonPanel.populate((CLesson) treeNode);
            statusPanel.setPanelVisible(true);
            getSite().getSelectionProvider().setSelection(new StructuredSelection(csdeNode));
        } else if (treeNode instanceof CUnit) {
            showPanel(unitPanel);
            unitPanel.setNode(csdeNode);
            unitPanel.setEditableNode(csdeNode.getTemporaryTreeNode());
            unitPanel.populate((CUnit) treeNode);
            statusPanel.setPanelVisible(true);
            getSite().getSelectionProvider().setSelection(new StructuredSelection(csdeNode));
        } else if (treeNode instanceof CUnitItem) {
            showPanel(unitItemPanel);
            unitItemPanel.populate((CUnitItem) treeNode);
            statusPanel.setPanelVisible(false);
            getSite().getSelectionProvider().setSelection(new StructuredSelection(csdeNode));
        } else if (treeNode instanceof CPage) {
            final CPage page = (CPage) treeNode;
            boolean hadToLoadPage = false;
            try {
                if (!page.isLoaded()) {
                    VidyaDataTree.getDefault().loadPageTree(page);
                    hadToLoadPage = true;
                }
            } catch (Exception e) {
                ExplicantoClientPlugin.handleException(e, page);
            }
            getViewSite().setSelectionProvider(pageEditor);
            showPanel(null);
            pageEditor.setEditorVisible(true);
            pageEditor.setInput(page);
            pageEditor.setCsdeNode(csdeNode);
            pageShowing = true;
            if (hadToLoadPage) csdeNode.setProperty(Node.LABEL_PROP, csdeNode.getProperty(Node.LABEL_PROP));
            getSite().getSelectionProvider().setSelection(new StructuredSelection(csdeNode));
        } else {
            showPanel(null);
            composite.layout();
        }
        if (treeNode instanceof CStatusNode) statusPanel.assign((CStatusNode) treeNode);
        composite.layout();
    }

    public void savePage() {
        if (!pageShowing) return;
        CPage page = (CPage) getSelectedCSDENode().getTreeNode();
        CPage tmpPage = pageEditor.getPage();
        try {
            page.takeAttributes(tmpPage);
            VidyaDataTree.getDefault().storePage(page);
            page.setLocallyModified(true);
        } catch (Exception e) {
            ExplicantoClientPlugin.handleException(e, page);
        }
    }

    protected void diagramSelected(DiagramEditPart part) {
        showPanel(null);
    }

    private void showPanel(GenericNodePanel panel) {
        lessonPanel.setPanelVisible(false);
        coursePanel.setPanelVisible(false);
        unitPanel.setPanelVisible(false);
        unitItemPanel.setPanelVisible(false);
        pageEditor.setEditorVisible(false);
        currentPanel = panel;
        if (panel == null) {
            statusPanel.setPanelVisible(false);
            return;
        }
        panel.setPanelVisible(true);
    }

    public ISelection getCurrentSelection() {
        return pageEditor.getSelection();
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        if (arg0.getPropertyName().equals(Node.LABEL_PROP)) {
            if (currentPanel != null) if (arg0.getNewValue() != null) currentPanel.setProperty(GenericNodePanel.NAME, arg0.getNewValue().toString());
        }
    }

    protected void performNoEditorAction() {
        showPanel(null);
    }

    public PageEditor getPageEditor() {
        return pageEditor;
    }

    public List getScpToDelete() {
        return pageEditor.getScpToDelete();
    }
}
