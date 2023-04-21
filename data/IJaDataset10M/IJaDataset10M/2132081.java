package de.beas.explicanto.client.rcp.wizards;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import de.bea.services.vidya.client.datasource.VidUtil;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.CStatusNode;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.I18N;

/**
 * 
 * GenericEditWizard
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class EditCustomerWizard extends Wizard implements IRunnableWithProgress {

    protected CCustomer rootCustomer;

    private CustomerEditPage selectPage;

    private CustomerEditPage nodePage;

    private boolean doFinish;

    /**
     * @param customer
     */
    public EditCustomerWizard(CCustomer customer) {
        rootCustomer = customer;
        setNeedsProgressMonitor(true);
        setWindowTitle(I18N.translate("wiz.edit.title"));
        setForcePreviousAndNextButtons(true);
    }

    public void addPages() {
        selectPage = new CustomerEditPage(rootCustomer);
        addPage(selectPage);
        nodePage = new CustomerEditPage(rootCustomer, true);
        addPage(nodePage);
    }

    public boolean performFinish() {
        try {
            doFinish = true;
            getContainer().run(false, false, this);
        } catch (InvocationTargetException e) {
            ExplicantoClientPlugin.handleException(e, nodePage.getEditedNode());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        if (doFinish) finish(monitor); else cancel(monitor);
    }

    private void finish(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        TreeNode tempNode = nodePage.getEditedNode();
        TreeNode selectedNode = rootCustomer;
        monitor.beginTask(I18N.translate("wiz.edit.msg.save"), 2);
        try {
            VidyaDataTree.getDefault().storeUnlockObject((TreeNode) tempNode);
            VidUtil.transferAttributes(tempNode, selectedNode);
            if (selectedNode instanceof CStatusNode) {
                selectedNode.propagateStatusDown(((CStatusNode) selectedNode).getStatus());
            }
            monitor.done();
        } catch (Exception e) {
            if (e instanceof InterruptedException) throw (InterruptedException) e;
            throw new InvocationTargetException(e);
        }
    }

    public boolean performCancel() {
        if (getContainer().getCurrentPage() == selectPage) return true;
        try {
            doFinish = false;
            getContainer().run(false, false, this);
        } catch (InvocationTargetException e) {
            ExplicantoClientPlugin.handleException(e, nodePage.getEditedNode());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void cancel(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask(I18N.translate("wiz.nodePage.task.abort"), 2);
        monitor.worked(1);
        try {
            VidyaDataTree.getDefault().unlockObject(rootCustomer);
            monitor.done();
        } catch (Exception e) {
            if (e instanceof InterruptedException) throw (InterruptedException) e;
            throw new InvocationTargetException(e);
        }
    }
}
