package ag.ion.noa4e.ui.operations;

import ag.ion.bion.officelayer.application.IOfficeApplication;
import ag.ion.bion.officelayer.desktop.IFrame;
import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.document.IDocumentDescriptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Operation in order to load a OpenOffice.org document. 
 * 
 * @author Andreas Br�ker
 * @version $Revision: 11672 $
 */
public class LoadDocumentOperation implements IRunnableWithProgress {

    private IOfficeApplication officeApplication = null;

    private IFrame frame = null;

    private IDocumentDescriptor documentDescriptor = null;

    private String documentType = null;

    private URL url = null;

    private InputStream inputStream = null;

    private boolean isSubTask = false;

    private boolean updateProgressMonitor = true;

    private boolean useStream = false;

    private InternalThread internalThread = null;

    /**
   * Internal thread class in order load a a OpenOffice.org text document.
   * 
   * @author Andreas Br�ker
   */
    private class InternalThread extends Thread {

        private Exception exception = null;

        private IDocument document = null;

        private boolean done = false;

        /**
     * Executes thread logic.
     * 
     * @author Andreas Br�ker
     */
        public void run() {
            try {
                if (useStream || inputStream != null) {
                    InputStream inputStream = null;
                    if (LoadDocumentOperation.this.inputStream != null) inputStream = LoadDocumentOperation.this.inputStream; else inputStream = url.openStream();
                    if (frame != null) document = officeApplication.getDocumentService().loadDocument(frame, inputStream, documentDescriptor); else document = officeApplication.getDocumentService().loadDocument(inputStream, documentDescriptor);
                    try {
                        inputStream.close();
                    } catch (Throwable throwable) {
                    }
                } else {
                    if (frame != null) document = officeApplication.getDocumentService().loadDocument(frame, url.toString(), documentDescriptor); else document = officeApplication.getDocumentService().loadDocument(url.toString(), documentDescriptor);
                }
                done = true;
            } catch (Exception exception) {
                this.exception = exception;
            } catch (ThreadDeath threadDeath) {
            }
        }

        /**
     * Returns exception. Returns null if no exception was thrown.
     * 
     * @return exception - returns null if no exception was thrown
     * 
     * @author Andreas Br�ker
     */
        public Exception getException() {
            return exception;
        }

        /**
     * Returns loaded document.
     * 
     * @return loaded document
     * 
     * @author Andreas Br�ker
     */
        public IDocument getDocument() {
            return document;
        }

        /**
     * Returns information whether the thread has finished his
     * work.
     * 
     * @return information whether the thread has finished his
     * work
     * 
     * @author Andreas Br�ker
     */
        public boolean done() {
            if (exception != null) return true;
            return done;
        }
    }

    /**
   * Constructs new LoadDocumentOperation.
   * 
   * @param officeApplication office application to be use
   * @param frame frame to be used
   * @param url URL of the document
   * @param documentDescriptor document descriptor to be used (can be null)
   * 
   * @throws IllegalArgumentException if the submitted office application, frame or URL is not valid
   * 
   * @author Andreas Br�ker
   */
    public LoadDocumentOperation(IOfficeApplication officeApplication, IFrame frame, URL url, IDocumentDescriptor documentDescriptor) throws IllegalArgumentException {
        this(null, officeApplication, frame, url, documentDescriptor);
    }

    /**
   * Constructs new LoadDocumentOperation.
   * 
   * @param documentType document type to be loaded (can be null)
   * @param officeApplication office application to be use
   * @param frame frame to be used
   * @param url URL of the document
   * @param documentDescriptor document descriptor to be used (can be null)
   * 
   * @author Andreas Br�ker
   */
    public LoadDocumentOperation(String documentType, IOfficeApplication officeApplication, IFrame frame, URL url, IDocumentDescriptor documentDescriptor) {
        assert officeApplication != null;
        this.officeApplication = officeApplication;
        this.frame = frame;
        this.url = url;
        this.documentDescriptor = documentDescriptor;
        this.documentType = documentType;
    }

    /**
   * Constructs new LoadDocumentOperation.
   * 
   * @param documentType document type to be loaded (can be null)
   * @param officeApplication office application to be use
   * @param url URL of the document
   * @param documentDescriptor document descriptor to be used (can be null)
   * 
   * @author Andreas Br�ker
   */
    public LoadDocumentOperation(String documentType, IOfficeApplication officeApplication, URL url, IDocumentDescriptor documentDescriptor) {
        assert officeApplication != null;
        this.officeApplication = officeApplication;
        this.url = url;
        this.documentDescriptor = documentDescriptor;
        this.documentType = documentType;
    }

    /**
   * Constructs new LoadDocumentOperation.
   * 
   * @param documentType document type to be loaded (can be null)
   * @param officeApplication office application to be use
   * @param frame frame to be used
   * @param inputStream input stream to be used
   * @param documentDescriptor document descriptor to be used (can be null)
   * 
   * @author Andreas Br�ker
   * @date 06.07.2006
   */
    public LoadDocumentOperation(String documentType, IOfficeApplication officeApplication, IFrame frame, InputStream inputStream, IDocumentDescriptor documentDescriptor) {
        assert officeApplication != null;
        assert inputStream != null;
        this.officeApplication = officeApplication;
        this.frame = frame;
        this.inputStream = inputStream;
        this.documentDescriptor = documentDescriptor;
        this.documentType = documentType;
    }

    /**
   * Constructs new LoadDocumentOperation.
   * 
   * @param officeApplication office application to be use
   * @param inputStream input stream to be used
   * @param documentDescriptor document descriptor to be used (can be null)
   * 
   * @author Andreas Br�ker
   * @date 10.07.2006
   */
    public LoadDocumentOperation(IOfficeApplication officeApplication, InputStream inputStream, IDocumentDescriptor documentDescriptor) {
        this(null, officeApplication, null, inputStream, documentDescriptor);
    }

    /**
   * Sets information whether this operation is a sub task.
   * 
   * @param isSubTask information whether this operation is a sub task
   * 
   * @author Andreas Br�ker
   */
    public void setIsSubTask(boolean isSubTask) {
        this.isSubTask = isSubTask;
    }

    /**
   * Sets information whether the progress monitor should be updated.
   * 
   * @param updateProgressMonitor information whether the progress monitor should be updated
   * 
   * @author Andreas Br�ker
   * @date 10.07.2006
   */
    public void setUpdateProgressMonitor(boolean updateProgressMonitor) {
        this.updateProgressMonitor = updateProgressMonitor;
    }

    /**
   * Sets information whether the document should be loaded by
   * an input stream.
   * 
   * @param useStream information whether the document should be loaded by
   * an input stream
   * 
   * @author Andreas Br�ker
   * @date 13.06.2006
   */
    public void setUseStream(boolean useStream) {
        this.useStream = useStream;
    }

    /**
   * Returns exception. Returns null if no exception was thrown.
   * 
   * @return exception - returns null if no exception was thrown
   * 
   * @author Andreas Br�ker
   */
    public Exception getException() {
        return internalThread.getException();
    }

    /**
   * Returns loaded document. Returns null if no document is
   * available. If a document type was submitted - only a document
   * will be returned if the type of the loaded document matches the
   * submitted type.
   * 
   * @return loaded document or null if no document is
   * available
   * 
   * @author Andreas Br�ker
   */
    public IDocument getDocument() {
        if (documentType == null) return internalThread.getDocument(); else {
            IDocument document = internalThread.getDocument();
            if (document != null) {
                if (document.getDocumentType().equals(documentType)) return document;
            }
            return null;
        }
    }

    /**
   * Runs this operation.  Progress should be reported to the given progress monitor.
   * This method is usually invoked by an <code>IRunnableContext</code>'s <code>run</code> method,
   * which supplies the progress monitor.
   * A request to cancel the operation should be honored and acknowledged 
   * by throwing <code>InterruptedException</code>.
   *
   * @param progressMonitor the progress monitor to use to display progress and receive
   *   requests for cancelation
   * 
   * @exception InvocationTargetException if the run method must propagate a checked exception,
   *  it should wrap it inside an <code>InvocationTargetException</code>; runtime exceptions are automatically
   *  wrapped in an <code>InvocationTargetException</code> by the calling context
   * @exception InterruptedException if the operation detects a request to cancel, 
   *  using <code>IProgressMonitor.isCanceled()</code>, it should exit by throwing 
   *  <code>InterruptedException</code>
   * 
   * @author Andreas Br�ker
   */
    public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
        internalThread = new InternalThread();
        if (isSubTask) {
            if (updateProgressMonitor) progressMonitor.subTask(Messages.LoadDocumentOperation_monitor_loading_document);
        } else {
            if (updateProgressMonitor) progressMonitor.beginTask(Messages.LoadDocumentOperation_monitor_loading_document, 50);
        }
        internalThread.start();
        while (!internalThread.done()) {
            Thread.sleep(500);
            if (!isSubTask) progressMonitor.worked(1);
            if (progressMonitor.isCanceled()) {
                try {
                    internalThread.stop();
                } catch (Throwable throwable) {
                }
                progressMonitor.done();
                throw new InterruptedException(Messages.LoadDocumentOperation_exception_message_operation_interrupted);
            }
        }
        Thread.sleep(3000);
        if (!isSubTask) progressMonitor.done();
    }
}
