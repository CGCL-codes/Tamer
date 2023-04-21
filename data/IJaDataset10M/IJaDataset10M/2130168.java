package org.snipsnap.snip.attachment.storage;

import org.snipsnap.snip.attachment.Attachment;
import snipsnap.api.config.Configuration;
import snipsnap.api.app.Application;
import org.radeox.util.logging.Logger;
import java.io.*;

/**
 * AttachmentStorage which stores attachment in the file system
 *
 * @author Stephan J. Schmidt
 * @version $Id: FileAttachmentStorage.java 1819 2005-04-06 17:56:22Z stephan $
 */
public class FileAttachmentStorage implements AttachmentStorage {

    private File getFile(Attachment attachment) {
        Configuration config = Application.get().getConfiguration();
        File filePath = config.getFilePath();
        return new File(filePath, attachment.getLocation());
    }

    public boolean exists(Attachment attachment) {
        return getFile(attachment).exists();
    }

    public OutputStream getOutputStream(Attachment attachment) throws IOException {
        File file = getFile(attachment);
        if (!file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        return new FileOutputStream(file);
    }

    public InputStream getInputStream(Attachment attachment) throws IOException {
        return new FileInputStream(getFile(attachment));
    }

    public void delete(Attachment attachment) {
        getFile(attachment).delete();
    }

    public boolean verify(Attachment attachment) throws IOException {
        if (exists(attachment)) {
            boolean modified = false;
            File file = getFile(attachment);
            if (file.length() != attachment.getSize()) {
                attachment.setSize(file.length());
                modified = true;
            }
            return !modified;
        }
        throw new FileNotFoundException(getFile(attachment).getCanonicalPath());
    }

    /**
   * Copy one attachment to another
   *
   * @param from the source attachment
   * @param to   the destination attachment
   * @throws java.io.IOException
   */
    public void copy(Attachment from, Attachment to) throws IOException {
        to.setDate(from.getDate());
        if (exists(from)) {
            BufferedOutputStream out = new BufferedOutputStream(getOutputStream(to));
            BufferedInputStream in = new BufferedInputStream(getInputStream(from));
            byte buf[] = new byte[4096];
            int length = -1;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
            out.close();
            in.close();
            to.setLocation(getFile(to).getPath());
        } else {
            throw new IOException("Cannot find source file for copy");
        }
    }
}
