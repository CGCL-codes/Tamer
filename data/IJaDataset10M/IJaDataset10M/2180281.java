package wjhk.jupload2.exception;

import java.io.File;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * 
 * This exception is a trick, to stop adding files to the file list, when too
 * many files don't match the allowed extension (see
 * {@link wjhk.jupload2.gui.FilePanelDataModel2#addFile(File, File)}, or any other control of the
 * current upload policy (see {@link UploadPolicy#createFileData(File, File)}.
 * 
 * @author etienne_sf
 * 
 */
public class JUploadStopAddingFiles extends JUploadException {

    /**
     * @param message
     */
    public JUploadStopAddingFiles(String message) {
        super(message);
    }
}
