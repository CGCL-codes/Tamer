package org.remus.infomngmnt.pdf.ui;

import java.lang.reflect.InvocationTargetException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.remus.Category;
import org.eclipse.remus.InformationUnitListItem;
import org.eclipse.remus.core.commands.CommandFactory;
import org.eclipse.remus.core.commands.CreateBinaryReferenceCommand;
import org.eclipse.remus.core.model.InformationStructureEdit;
import org.eclipse.remus.core.services.IEditingHandler;
import org.eclipse.remus.ui.newwizards.NewInfoObjectWizard;
import org.eclipse.remus.ui.operation.LoadFileToTmpFromPathRunnable;
import org.eclipse.ui.IWorkbench;
import org.remus.infomngmnt.pdf.Activator;
import org.remus.infomngmnt.pdf.messages.Messages;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class NewPdfWizard extends NewInfoObjectWizard {

    /**
	 * 
	 */
    public NewPdfWizard() {
        setNeedsProgressMonitor(true);
        setWindowTitle(Messages.NewPdfWizard_NewPDFDocument);
    }

    @Override
    protected Command getAdditionalCommands() {
        IFile tmpFile = getTmpFile();
        if (tmpFile != null) {
            try {
                PDDocument pdfDocument = PDDocument.load(tmpFile.getLocationURI().toURL());
                PDDocumentInformation info = pdfDocument.getDocumentInformation();
                InformationStructureEdit edit = InformationStructureEdit.newSession(Activator.TYPE_ID);
                edit.setValue(newElement, Activator.AUTHOR, info.getAuthor());
                edit.setValue(newElement, Activator.TITLE, info.getTitle());
                edit.setValue(newElement, Activator.CREATOR, info.getCreator());
                edit.setValue(newElement, Activator.PRODUCER, info.getProducer());
                try {
                    edit.setValue(newElement, Activator.CREATION_DATE, info.getCreationDate().getTime());
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            } catch (Throwable e1) {
            }
            final LoadFileToTmpFromPathRunnable loadImageRunnable = new LoadFileToTmpFromPathRunnable();
            loadImageRunnable.setFilePath(tmpFile.getLocation().toOSString());
            getShell().getDisplay().syncExec(new Runnable() {

                public void run() {
                    try {
                        new ProgressMonitorDialog(getShell()).run(true, false, loadImageRunnable);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            IEditingHandler service = Activator.getDefault().getServiceTracker().getService(IEditingHandler.class);
            CreateBinaryReferenceCommand addFileToInfoUnit = CommandFactory.addFileToInfoUnit(tmpFile, newElement, service.getNavigationEditingDomain());
            Activator.getDefault().getServiceTracker().ungetService(service);
            return addFileToInfoUnit;
        }
        return super.getAdditionalCommands();
    }

    protected IFile getTmpFile() {
        return ((GeneralPdfPage) page1).getTmpFile();
    }

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        Object firstElement = selection.getFirstElement();
        if (firstElement instanceof Category) {
            page1 = new GeneralPdfPage((Category) firstElement);
        } else if (firstElement instanceof InformationUnitListItem) {
            page1 = new GeneralPdfPage((InformationUnitListItem) firstElement);
        } else {
            page1 = new GeneralPdfPage((Category) null);
        }
        if (files != null) {
            page1.setFiles(files);
        }
        setCategoryToPage();
        setFilesToPage();
    }

    @Override
    protected String getInfoTypeId() {
        return Activator.TYPE_ID;
    }
}
