package de.beas.explicanto.client.rcp.wizards;

import java.util.List;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.rcp.viewerproviders.CoursesTreeCP;
import de.beas.explicanto.client.rcp.viewerproviders.CoursesTreeLP;

/**
 * Delete Wizard Page
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class DeleteWizardPage extends GenericWizardPage implements ICheckStateListener {

    public static final String NAME = "wizardPages.delete";

    private CheckboxTreeViewer treeViewer;

    public DeleteWizardPage(CCustomer root) {
        super(root, NAME);
    }

    protected String getPageTitle() {
        return translate("wiz.delete.page.title");
    }

    protected String getTranslatedMessage() {
        return translate("wiz.delete.page.message");
    }

    public void createControl(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(rootCustomer.getCustomerName());
        group.setLayout(new FillLayout());
        treeViewer = new CheckboxTreeViewer(group, SWT.BORDER);
        treeViewer.setContentProvider(new CoursesTreeCP(CCourse.class));
        treeViewer.setLabelProvider(new CoursesTreeLP());
        treeViewer.setInput(rootCustomer);
        treeViewer.addCheckStateListener(this);
        setControl(group);
        setPageComplete(false);
    }

    public void checkStateChanged(CheckStateChangedEvent event) {
        TreeNode node = (TreeNode) event.getElement();
        if (!(node.getParent() instanceof CCustomer)) if (treeViewer.getChecked(node.getParent())) treeViewer.setChecked(node, true);
        if (event.getChecked()) checkNodes(node, true);
        setPageComplete(treeViewer.getCheckedElements().length > 0);
    }

    private void checkNodes(TreeNode node, boolean check) {
        treeViewer.setChecked(node, check);
        List children = node.getChildren();
        if (children.size() == 0) return;
        for (int i = 0; i < children.size(); i++) checkNodes((TreeNode) children.get(i), check);
    }

    public Object[] getCheckedElements() {
        return treeViewer.getCheckedElements();
    }
}
