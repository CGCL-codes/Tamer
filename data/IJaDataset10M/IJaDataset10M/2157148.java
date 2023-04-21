package net.sf.cclearly.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    /**
	 * 
	 * @param srcFile
	 * @param destFolder
	 * @param copyListener For larger files you can specify a listener to monitor the copy. Can be null.
	 */
    public static File copyFile(File srcFile, File destFolder, FileCopyListener copyListener) {
        File dest = new File(destFolder, srcFile.getName());
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(dest);
            byte[] readBuf = new byte[1024 * 512];
            int readLength;
            long totalCopiedSize = 0;
            long totalSize = srcFile.length();
            boolean canceled = false;
            if (copyListener == null) {
                while ((readLength = in.read(readBuf)) != -1) {
                    out.write(readBuf, 0, readLength);
                }
            } else {
                while ((readLength = in.read(readBuf)) != -1) {
                    out.write(readBuf, 0, readLength);
                    if (!copyListener.updateCheck(readLength, totalSize)) {
                        canceled = true;
                        break;
                    }
                }
            }
            in.close();
            out.close();
            if (canceled) {
                dest.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public static File moveFile(File srcFile, File destFolder) {
        File dest = new File(destFolder, srcFile.getName());
        if (dest.exists()) {
            dest.delete();
        }
        srcFile.renameTo(dest);
        return dest;
    }
}
