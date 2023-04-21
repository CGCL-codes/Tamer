package org.wsmostudio.bpmo.ui.wizards;

import java.io.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.omwg.ontology.Ontology;
import org.sbpm.bpmo.factory.Factory;
import org.sbpm.bpmo.io.BpmoSerializer;
import org.wsmo.common.TopEntity;
import org.wsmo.wsml.Serializer;
import org.wsmostudio.bpmo.ImagePool;
import org.wsmostudio.bpmo.model.BpmoModel;
import org.wsmostudio.bpmo.model.io.BPMOExporter;
import org.wsmostudio.runtime.LogManager;
import org.wsmostudio.runtime.io.Utils;

public class BpmoExportWizard extends Wizard implements IExportWizard {

    protected WizardNewFileCreationPage filePage;

    private File sourceFile;

    public void addPages() {
        addPage(filePage);
        filePage.setImageDescriptor(ImagePool.getImage(ImagePool.BANNER_ICON));
        filePage.setTitle("BPMO Diagram Export");
        filePage.setMessage("BPMO Diagram Export");
        filePage.setDescription("Exports the BPMO diagram in WSML");
    }

    protected void initNewFilePage(String pageName, IStructuredSelection selection) {
        if (false == selection.isEmpty() && selection.getFirstElement() instanceof IFile) {
            this.sourceFile = ((IFile) selection.getFirstElement()).getLocation().toFile();
        }
        filePage = new WizardNewFileCreationPage(pageName, selection) {

            protected boolean validatePage() {
                if (sourceFile == null) {
                    setErrorMessage("No source BPMO file selected!");
                    return false;
                }
                if (false == super.validatePage()) {
                    return false;
                }
                String testName = getFileName();
                if (testName.length() == 0) {
                    return true;
                }
                if (false == testName.toLowerCase().endsWith(".wsml")) {
                    testName += ".wsml";
                }
                IPath path = super.getContainerFullPath().append(testName);
                if (ResourcesPlugin.getWorkspace().getRoot().getFile(path).exists()) {
                    setErrorMessage("The same name (" + testName + ") already exists!");
                    return false;
                }
                return true;
            }
        };
    }

    public boolean canFinish() {
        return filePage.isPageComplete();
    }

    public boolean isHelpAvailable() {
        return false;
    }

    public boolean needsPreviousAndNextButtons() {
        return false;
    }

    public boolean needsProgressMonitor() {
        return false;
    }

    public boolean performCancel() {
        return true;
    }

    public boolean performFinish() {
        BpmoModel model = null;
        ObjectInputStream in = null;
        try {
            FileInputStream fs = new FileInputStream(this.sourceFile);
            in = new ObjectInputStream(new BufferedInputStream(fs));
            model = (BpmoModel) in.readObject();
            in.close();
        } catch (Exception ex) {
            try {
                in.close();
            } catch (Exception e) {
            }
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error Reading Diagram", ex.getClass().getName() + " : " + ex.getMessage());
            LogManager.logError(ex.getClass().getName() + " : " + ex.getMessage());
            return false;
        }
        BPMOExporter exporter = new BPMOExporter();
        Object[] bpmoData = null;
        try {
            bpmoData = exporter.convertVisualModel(model);
        } catch (Exception ex) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPMO Export Error", "Error during exporting the diagram to BPMO (wsml) : " + ex.getMessage());
            LogManager.logError(ex);
            return false;
        }
        String newFileName = filePage.getFileName();
        if (newFileName == null) {
            return false;
        }
        if (false == newFileName.toLowerCase().endsWith(".wsml")) {
            newFileName += ".wsml";
        }
        filePage.setFileName(newFileName);
        IFile file = filePage.createNewFile();
        if (file == null) {
            return false;
        }
        BpmoSerializer serializer = Factory.createSerializer(null);
        Serializer wsmoSerializer = org.wsmo.factory.Factory.createSerializer(null);
        StringBuffer resultBuffer = new StringBuffer();
        try {
            Ontology instOntology = serializer.serialize(bpmoData);
            Utils.updateStudioNFP(instOntology);
            wsmoSerializer.serialize(new TopEntity[] { instOntology }, resultBuffer);
            file.setContents(new ByteArrayInputStream(resultBuffer.toString().getBytes()), true, false, null);
        } catch (Exception ioe) {
            LogManager.logError(ioe);
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error Reading Diagram", ioe.getClass().getName() + " : " + ioe.getMessage());
            return false;
        }
        BasicNewResourceWizard.selectAndReveal(file, Workbench.getInstance().getActiveWorkbenchWindow());
        return true;
    }

    public void dispose() {
        filePage.dispose();
        super.dispose();
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        initNewFilePage("New WSML file", selection);
        setDefaultPageImageDescriptor(ImagePool.getImage(ImagePool.LOGO_ICON));
    }
}
