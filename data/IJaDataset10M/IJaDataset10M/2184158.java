package de.hackerdan.projectcreator.internal.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import de.hackerdan.projectcreator.internal.Activator;

/**
 * Dateizugriffe.
 *
 * @author Daniel Hirscher
 */
public final class FileUtils {

    private static final int BUFFER_SIZE = 0xffff;

    private FileUtils() {
    }

    /**
    * L�dt den Inhalt eines IFiles als String.
    * 
    * @param file IFile
    * @return String
    * @throws CoreException
    */
    public static String loadFileAsString(final IFile file) throws CoreException {
        final StringBuilder sb = new StringBuilder();
        InputStream in = null;
        try {
            in = file.getContents();
            final byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while (true) {
                len = in.read(buffer);
                if (len == -1) {
                    break;
                }
                sb.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            Activator.log("Could not read contents: " + file.getName(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    /**
    * Speichert den String in das IFile.
    * 
    * @param file IFile
    * @param string String
    * @param monitor Progress Monitor
    * @throws CoreException
    */
    public static void saveFileAsString(final IFile file, final String string, final IProgressMonitor monitor) throws CoreException {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(string.getBytes());
            if (file.exists()) {
                file.setContents(in, IResource.KEEP_HISTORY, monitor);
            } else {
                file.create(in, true, monitor);
            }
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
