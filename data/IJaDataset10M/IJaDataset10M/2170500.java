package uk.ac.ed.rapid.button;

import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.data.filesystem.AbstractFileSystem;
import uk.ac.ed.rapid.exception.RapidException;
import uk.ac.ed.rapid.jobdata.JobData;
import uk.ac.ed.rapid.jobdata.JobID;
import uk.ac.ed.rapid.jobdata.VariableResolver;

public class DeleteFileAction extends FileAction {

    public void performAction(RapidData rapidData, JobID jobID, int subJobIndex) throws RapidException {
        this.performAction(rapidData, rapidData.getJobDataTable().getJobData(jobID), subJobIndex);
    }

    public void performAction(RapidData rapidData) throws RapidException {
        this.performAction(rapidData, rapidData.getJobData(), 0);
    }

    private void performAction(RapidData rapidData, JobData jobData, int subJobIndex) throws RapidException {
        try {
            String resolvedFileSystem = VariableResolver.resolve(this.getFileSystem(), jobData, rapidData.getStaticTable(), subJobIndex);
            String resolvedPath = VariableResolver.resolve(this.getPath(), jobData, rapidData.getStaticTable(), subJobIndex);
            AbstractFileSystem fileSystem = rapidData.getFileSystemTable().getFileSystem(resolvedFileSystem);
            FileObject file = rapidData.getFileSystemTable().getFileSystemConnector().connect(fileSystem, jobData, subJobIndex);
            file = file.resolveFile(resolvedPath);
            if (file.exists()) file.delete(new AllFileSelector());
            file.close();
        } catch (Exception ex) {
            throw new RapidException("Error deleting files: " + ex.getMessage());
        }
    }
}
