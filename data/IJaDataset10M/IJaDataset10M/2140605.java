package q_impress.pmi.plugin.editors.pmiform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import q_impress.pmi.lib.project.ModelingProject;
import q_impress.pmi.lib.services.ServiceException;
import q_impress.pmi.lib.services.loadsave.SavingService;

/**
 * This document provider provides a textual representation of a Modeling Project.
 * @author Mauro Luigi Drago
 *
 */
public class ModelingProjectDocumentProvider extends AbstractDocumentProvider {

    public ModelingProjectDocumentProvider() {
    }

    @Override
    protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
        return new AnnotationModel();
    }

    @Override
    protected IDocument createDocument(Object element) throws CoreException {
        if (element instanceof FileEditorInput) {
            FileDocumentProvider fileProvider = new FileDocumentProvider();
            IDocument res = fileProvider.getDocument(element);
            fileProvider.connect(element);
            res = fileProvider.getDocument(element);
            return res;
        }
        if (element instanceof ModelingProjectEditorInput) {
            ModelingProject project = ((ModelingProjectEditorInput) element).getProject();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try {
                SavingService service = new SavingService();
                service.setOutStream(outStream);
                service.setTarget(project);
                service.initialize();
                service.invoke();
                Document doc = new Document();
                doc.set(outStream.toString());
                return doc;
            } catch (ServiceException e) {
                throw new CoreException(new Status(IStatus.ERROR, "q_impress", "Invalid input for document", e));
            } finally {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
        throw new CoreException(new Status(IStatus.ERROR, "q_impress", "Invalid input for document provider."));
    }

    @Override
    protected void doSaveDocument(IProgressMonitor arg0, Object arg1, IDocument arg2, boolean arg3) throws CoreException {
    }

    @Override
    protected IRunnableContext getOperationRunner(IProgressMonitor arg0) {
        return null;
    }
}
