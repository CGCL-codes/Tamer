package org.file4j.identifier;

import org.file4j.FileType;
import java.io.IOException;
import java.io.InputStream;

/**
 * An object that can check if a byte chunk matches a specific pattern. Each FileTypeIdentifier is capable of
 * identifying a single FileType, i.e. there would be a FileTypeIdentifier for GIF images, and a separate for JPEG
 * images.
 *
 * @author $Author: genlbm $
 * @version $Revision: 1.1 $
 */
public interface FileTypeIdentifier {

    /**
     * Checks if the data in the input stream is something this identifier is capable of identifying.
     *
     * @param is              The input stream.
     * @param extractMetaData if set to <code>true</code>, the identifier will extract extra meta data about the file
     *                        (if possible).
     * @return The matched file type, or <code>null</code> if no match could be made.
     * @throws IOException If an IO exception occurs while reading from the input stream.
     */
    FileType identify(InputStream is, boolean extractMetaData) throws IOException;

    /**
     * Returns the File Type that this Identifier is capable of identifying.
     *
     * @return The file type that can be matched with this identifier.
     */
    FileType getFileType();

    /**
     * Set the file type this identifier is capable of identifying.
     *
     * @param fileType The file type.
     */
    void setFileType(FileType fileType);
}
