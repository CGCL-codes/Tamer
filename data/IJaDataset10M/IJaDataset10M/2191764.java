package org.apache.ibatis.abator.ui.actions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.apache.ibatis.abator.api.Abator;
import org.apache.ibatis.abator.config.AbatorConfiguration;
import org.apache.ibatis.abator.config.xml.AbatorConfigurationParser;
import org.apache.ibatis.abator.exception.InvalidConfigurationException;
import org.apache.ibatis.abator.exception.XMLParserException;
import org.apache.ibatis.abator.ui.plugin.AbatorUIPlugin;
import org.apache.ibatis.abator.ui.plugin.EclipseProgressCallback;
import org.apache.ibatis.abator.ui.plugin.EclipseShellCallback;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * @author Jeff Butler
 */
public class RunAbatorThread implements IWorkspaceRunnable {

    private File inputFile;

    private List warnings;

    /**
     *  
     */
    public RunAbatorThread(File inputFile, List warnings) {
        super();
        this.inputFile = inputFile;
        this.warnings = warnings;
    }

    public void run(IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Generating iBATIS Artifacts:", 1000);
        try {
            monitor.subTask("Parsing Configuration");
            AbatorConfigurationParser cp = new AbatorConfigurationParser(warnings);
            AbatorConfiguration config = cp.parseAbatorConfiguration(inputFile);
            monitor.worked(50);
            Abator abator = new Abator(config, new EclipseShellCallback(), warnings);
            monitor.subTask("Generating Files from Database Tables");
            SubProgressMonitor spm = new SubProgressMonitor(monitor, 475);
            abator.generate(new EclipseProgressCallback(spm));
        } catch (InterruptedException e) {
            throw new OperationCanceledException();
        } catch (SQLException e) {
            Status status = new Status(IStatus.ERROR, AbatorUIPlugin.getPluginId(), IStatus.ERROR, e.getMessage(), e);
            AbatorUIPlugin.getDefault().getLog().log(status);
            throw new CoreException(status);
        } catch (IOException e) {
            Status status = new Status(IStatus.ERROR, AbatorUIPlugin.getPluginId(), IStatus.ERROR, e.getMessage(), e);
            AbatorUIPlugin.getDefault().getLog().log(status);
            throw new CoreException(status);
        } catch (XMLParserException e) {
            List errors = e.getErrors();
            MultiStatus multiStatus = new MultiStatus(AbatorUIPlugin.getPluginId(), IStatus.ERROR, "XML Parser Errors\n  See Details for more Information", null);
            Iterator iter = errors.iterator();
            while (iter.hasNext()) {
                Status message = new Status(IStatus.ERROR, AbatorUIPlugin.getPluginId(), IStatus.ERROR, (String) iter.next(), null);
                multiStatus.add(message);
            }
            throw new CoreException(multiStatus);
        } catch (InvalidConfigurationException e) {
            List errors = e.getErrors();
            MultiStatus multiStatus = new MultiStatus(AbatorUIPlugin.getPluginId(), IStatus.ERROR, "Invalid Configuration\n  See Details for more Information", null);
            Iterator iter = errors.iterator();
            while (iter.hasNext()) {
                Status message = new Status(IStatus.ERROR, AbatorUIPlugin.getPluginId(), IStatus.ERROR, (String) iter.next(), null);
                multiStatus.add(message);
            }
            throw new CoreException(multiStatus);
        } finally {
            monitor.done();
        }
    }
}
