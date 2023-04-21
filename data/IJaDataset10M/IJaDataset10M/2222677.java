package oxygen.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstracts out a virtual file, which can be written to.
 * Usually, these have an actual file at the backend.
 * @author ugorjid
 */
public interface VirtualWritableFile extends VirtualFile {

    /**
   * Gets the actual file which is mapped to this VirtualFile.
   * This is an optional operation.
   */
    File getFile();

    /**
   * Make the directories denoted by the VirtualWritableFile
   * @throws IOException if the directories could not be created
   */
    void mkdirs() throws IOException;

    /**
   * @return an OutputStream for writing to the file
   */
    OutputStream getOutputStream() throws IOException;

    /**
   * Delete this file
   * @throws IOException if this could not be deleted for any reason
   */
    void delete() throws IOException;
}
