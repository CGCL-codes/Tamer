package org.plazmaforge.studio.devassistant.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.plazmaforge.studio.devassistant.core.gui.GuiUtils;
import org.plazmaforge.studio.devassistant.core.gui.pages.CheckListPageListHosting;
import org.plazmaforge.studio.devassistant.core.gui.pages.JavaCheckListPage;
import org.plazmaforge.studio.devassistant.core.jdt.CodeUtils;
import org.plazmaforge.studio.devassistant.core.jdt.generators.CloneGenerator;

/**
 * CloneWizard.
 * $Id: CloneWizard.java,v 1.1 2008/07/01 06:15:11 ohapon Exp $
 */
public class CloneWizard extends Wizard {

    private IType type;

    private ArrayList allFields = new ArrayList();

    private ArrayList fields = new ArrayList();

    private JavaCheckListPage fieldsPage;

    public CloneWizard(IType type) {
        super();
        this.type = type;
    }

    /**
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish() {
        try {
            IField[] arr = (IField[]) fields.toArray(new IField[0]);
            ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
            progressDialog.open();
            IProgressMonitor progress = progressDialog.getProgressMonitor();
            try {
                String src = CloneGenerator.createCloneMethod(type.getElementName(), arr);
                src = "\n\n" + CodeUtils.format(src) + "\n";
                type.createMethod(src, null, false, progress);
            } finally {
                progressDialog.close();
            }
            return true;
        } catch (Exception ex) {
            GuiUtils.handleException(getShell(), ex);
            return false;
        }
    }

    /**
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        try {
            super.addPages();
            setWindowTitle("DevAssistant :: Implement Clone");
            IField[] fs = type.getFields();
            for (int i = 0; i < fs.length; i++) {
                int flags = fs[i].getFlags();
                if (!(Flags.isStatic(flags) || Flags.isFinal(flags))) {
                    allFields.add(fs[i]);
                }
            }
            fieldsPage = new JavaCheckListPage("fields");
            fieldsPage.setTitle("Fields");
            fieldsPage.setImageDescriptor(JavaPluginImages.DESC_WIZBAN_JAVA_WORKINGSET);
            fieldsPage.setDescription("Select fields that are to be cloned");
            fieldsPage.setAllowsEmptySelection(false);
            addPage(fieldsPage);
            new CheckListPageListHosting(fieldsPage, fields);
        } catch (Exception ex) {
            GuiUtils.handleException(getShell(), ex);
        }
    }

    /**
     * @see org.eclipse.jface.wizard.IWizard#createPageControls(Composite)
     */
    public void createPageControls(Composite pageContainer) {
        try {
            super.createPageControls(pageContainer);
            for (Iterator it = allFields.iterator(); it.hasNext(); ) {
                IField f = (IField) it.next();
                fieldsPage.addItem(f, true);
                fields.add(f);
            }
        } catch (Exception ex) {
            GuiUtils.handleException(getShell(), ex);
        }
    }
}
