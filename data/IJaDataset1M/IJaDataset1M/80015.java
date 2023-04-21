package guijavacommander.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import guijavacommander.JavaCommander;
import guijavacommander.FilePanel;

/**
 * User: Deady
 * Date: 17.07.2009
 * Time: 10:23:51
 */
public class CopyFileTask extends AbstractFilesTask {

    private File[] srcFiles;

    private File destFolder;

    private Log logger = LogFactory.getLog(CopyFileTask.class);

    private long totalSize = 0;

    private int copied = 0;

    private int currentFileSize = 0;

    private static final int BUFFER_SIZE = 1024;

    private long div = 1;

    public CopyFileTask(File destFolder, File[] srcFiles) {
        this.destFolder = destFolder;
        this.srcFiles = srcFiles;
        for (File file : srcFiles) {
            totalSize += getFileSize(file);
        }
        if (totalSize > Integer.MAX_VALUE) {
            div = totalSize / Integer.MAX_VALUE + 1;
            totalSize /= div;
            logger.debug("Div: " + div);
        }
    }

    private long getFileSize(File file) {
        long res = file.length();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                res += getFileSize(child);
            }
        }
        return res;
    }

    public void execute() {
        logger.debug("Copying " + srcFiles.length + " files to " + destFolder);
        for (File file : srcFiles) {
            if (!isStopRequested()) {
                try {
                    copyFile(file, destFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(JavaCommander.instance, "Error while copying " + file, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        JavaCommander.instance.refreshPanels();
    }

    public int getMin() {
        return 0;
    }

    public int getMax() {
        return (int) totalSize;
    }

    public String getDescription() {
        return "Copy";
    }

    protected void copyFile(File src, File dstFolder) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        int copiedFile = 0;
        final File dst = new File(dstFolder, src.getName());
        logger.debug("Copying " + src + " to " + dst);
        try {
            currentFileSize = (int) src.length();
            fireDescriptionChanged(src.getName());
            fireProgressChanged(copied);
            if (src.isFile()) {
                fis = new FileInputStream(src);
                fos = new FileOutputStream(dst);
                byte[] buf = new byte[BUFFER_SIZE];
                int i = 0;
                while ((i = fis.read(buf)) != -1 && !isStopRequested()) {
                    fos.write(buf, 0, i);
                    copied += i;
                    copiedFile += i;
                    fireProgressChanged((int) (copied / div));
                    fireSubProgressChanged(copiedFile);
                }
            } else {
                File newParent = new File(dstFolder, src.getName());
                newParent.mkdir();
                logger.debug("Created dir: " + newParent);
                copied += src.length();
                fireProgressChanged((int) (copied / div));
                for (File child : src.listFiles()) {
                    copyFile(child, newParent);
                }
            }
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
            if (isStopRequested()) {
                if (JOptionPane.showConfirmDialog(JavaCommander.instance, "Delete unfinished file " + src + "?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    logger.debug("Deleting unfinished " + src);
                    dst.delete();
                }
            }
        }
    }

    public int getSubMin() {
        return 0;
    }

    public int getSubMax() {
        return currentFileSize;
    }
}
