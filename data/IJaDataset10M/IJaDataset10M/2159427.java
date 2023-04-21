package net.sourceforge.pmd.eclipse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import net.sourceforge.pmd.quickfix.Fix;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.PlatformUI;

/**
 * This class adapt a PMD quickfix to an Eclipse resolution. 
 * 
 * @author Philippe Herlin
 * @version $Revision: 2425 $
 * 
 * $Log$
 * Revision 1.1.2.2  2003/11/30 21:15:18  phherlin
 * Using Fix interface from PMD core
 *
 * Revision 1.1.2.1  2003/11/04 23:32:21  phherlin
 * Previewing Quick Fixes
 *
 */
public class PMDResolution implements IMarkerResolution, IRunnableWithProgress {

    private static Log log = LogFactory.getLog("net.sourceforge.pmd.eclipse.PMDResolution");

    private Fix fix;

    private IFile file;

    private int lineNumber;

    /**
     * PMDResolution adapts a Fix
     * @param fix
     */
    public PMDResolution(Fix fix) {
        this.fix = fix;
    }

    /**
     * @see org.eclipse.ui.IMarkerResolution#getLabel()
     */
    public String getLabel() {
        return fix.getLabel();
    }

    /**
     * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
     */
    public void run(IMarker marker) {
        log.debug("fixing...");
        IResource resource = marker.getResource();
        this.lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, 0);
        if (resource instanceof IFile) {
            this.file = (IFile) resource;
            try {
                ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
                dialog.run(false, false, this);
            } catch (InvocationTargetException e) {
                PMDPlugin.getDefault().showError(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_ERROR_INVOCATIONTARGET_EXCEPTION), e);
            } catch (InterruptedException e) {
                PMDPlugin.getDefault().showError(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_ERROR_INTERRUPTED_EXCEPTION), e);
            }
        }
    }

    /**
     * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            monitor.beginTask("", 2);
            monitor.subTask(this.file.getName());
            InputStream in = this.file.getContents();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            while (br.ready()) {
                String line = br.readLine();
                pw.println(line);
            }
            monitor.worked(1);
            String fixCode = this.fix.fix(sw.toString(), this.lineNumber);
            file.setContents(new ByteArrayInputStream(fixCode.getBytes()), false, true, monitor);
            monitor.worked(1);
        } catch (CoreException e) {
            PMDPlugin.getDefault().showError(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_ERROR_CORE_EXCEPTION), e);
        } catch (IOException e) {
            PMDPlugin.getDefault().showError(PMDPlugin.getDefault().getMessage(PMDConstants.MSGKEY_ERROR_IO_EXCEPTION), e);
        }
    }
}
