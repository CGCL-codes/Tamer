package uk.org.ogsadai.common.files;

import java.io.File;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Exception thrown when a file to be created already exists.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FileAlreadyExistsException extends FileException {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002-2010.";

    /** Serialization value. */
    private static final long serialVersionUID = -8660019208405623752L;

    /**
     * Constructor.
     * 
     * @param file 
     *     File that already exists.
     */
    public FileAlreadyExistsException(File file) {
        super(ErrorID.FILE_ALREADY_EXISTS, new Object[] { file.getAbsolutePath() });
    }
}
