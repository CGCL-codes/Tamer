package de.beas.explicanto.client.rcp.wizards;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.datastructures.CCourseName;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.CProject;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.rcp.viewerproviders.CoursesTreeCP;
import de.beas.explicanto.client.rcp.viewerproviders.CoursesTreeLP;
import de.beas.explicanto.client.rcp.viewerproviders.OutlineTreeInput;

/**
 * 
 * CopyNodesWizardPage
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class CopyNodesWizardPage extends GenericWizardPage implements ISelectionChangedListener {

    private static final String NAME = "wizardPages.copyNodes";

    public static final int COPY = 0;

    public static final int PASTE = 1;

    private int type;

    private TreeViewer treeViewer;

    private TreeNode sourceNode;

    private TreeNode destNode;

    private CopyNodesWizardPage sourcePage;

    public CopyNodesWizardPage(CCustomer root, int type, CopyNodesWizardPage sourcePage) {
        super(root, NAME);
        this.type = type;
        this.sourcePage = sourcePage;
    }

    public CopyNodesWizardPage(CCustomer root, int type) {
        this(root, type, null);
        this.type = type;
    }

    protected String getPageTitle() {
        return translate("wiz.copyNodes.page.title");
    }

    protected String getTranslatedMessage() {
        if (type == COPY) return translate("wiz.copyNodes.page.msgCopy"); else if (type == PASTE) return translate("wiz.copyNodes.page.msgPaste");
        return "no msg";
    }

    public void createControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(rootCustomer.getCustomerName());
        group.setLayout(new FillLayout());
        treeViewer = new TreeViewer(group, SWT.BORDER);
        if (type == COPY) treeViewer.setContentProvider(new CoursesTreeCP(CCourse.class)); else if (type == PASTE) treeViewer.setContentProvider(new CoursesTreeCP(CCourseName.class));
        treeViewer.setLabelProvider(new CoursesTreeLP());
        if (type == COPY) treeViewer.setInput(rootCustomer); else if (type == PASTE) treeViewer.setInput(new OutlineTreeInput(rootCustomer));
        treeViewer.addSelectionChangedListener(this);
        setControl(group);
        setPageComplete(false);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection sel = (IStructuredSelection) event.getSelection();
        if (type == COPY) sourceNode = (TreeNode) sel.getFirstElement(); else if (type == PASTE) destNode = (TreeNode) sel.getFirstElement();
        setPageComplete(validatePage());
    }

    private boolean validatePage() {
        setErrorMessage(null);
        if (type == COPY) {
            if (sourceNode == null) {
                setErrorMessage(translate("wiz.copyNodes.errors.noSel"));
                return false;
            } else {
                setMessage(translate("wiz.copyNodes.msg.selectSource"));
                return true;
            }
        } else if (type == PASTE) {
            if (destNode == null) {
                setErrorMessage(translate("wiz.copyNodes.errors.noSel"));
                return false;
            }
            TreeNode source = sourcePage.getSourceNode();
            if (source instanceof CProject) {
                if (!(destNode instanceof CCustomer)) {
                    setErrorMessage(translate("wiz.copyNodes.errors.wrongDest"));
                    return false;
                }
            } else if (source instanceof CCourseName) {
                if (!(destNode instanceof CProject)) {
                    setErrorMessage(translate("wiz.copyNodes.errors.wrongDest"));
                    return false;
                }
            } else if (source instanceof CCourse) {
                if (!(destNode instanceof CCourseName)) {
                    setErrorMessage(translate("wiz.copyNodes.errors.wrongDest"));
                    return false;
                }
            }
            setMessage(translate("wiz.copyNodes.msg.selectDest"));
            return true;
        }
        getContainer().updateMessage();
        return false;
    }

    public void setVisible(boolean visible) {
        if (visible && (type == CopyNodesWizardPage.PASTE)) {
            setPageComplete(validatePage());
            TreeNode source = sourcePage.getSourceNode();
            if (source instanceof CCourse) {
                long templId = ((CCourse) source).getTemplateID();
                treeViewer.addFilter(new TemplateFilter(templId));
            }
        }
        super.setVisible(visible);
    }

    public TreeNode getSourceNode() {
        return sourceNode;
    }

    public TreeNode getDestNode() {
        return destNode;
    }
}

class TemplateFilter extends ViewerFilter {

    private long templId;

    public TemplateFilter(long tId) {
        templId = tId;
    }

    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof CCourseName) return ((CCourseName) element).getMasterCourse().getTemplateID() == templId;
        return true;
    }
}
