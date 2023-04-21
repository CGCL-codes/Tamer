package cn.sduo.app.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Description: 
 * Util class for creating zip file 
 * 
 * @author Anselm Hou
 * @since  May 20, 2009
 * @version 1.0
 */
public class ZipUtil {

    /**
    * Zip array of files
    * @param destFile Zip file that has been produced
    * @param fileA Array of files that to be zip
    * @throws Exception
    */
    public static void zipFile(File destFile, File[] fileA) throws Exception {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(destFile));
            ZipEntry entry;
            FileInputStream fis = null;
            for (File file : fileA) {
                entry = new ZipEntry(file.getName());
                zos.putNextEntry(entry);
                try {
                    fis = new FileInputStream(file);
                    int read = 0;
                    byte[] bytes;
                    while (read != -1) {
                        bytes = new byte[1024];
                        read = fis.read(bytes);
                        if (read != -1) {
                            zos.write(bytes);
                        }
                    }
                    zos.flush();
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            zos.flush();
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }

    /**
    * Construct for ZipUtil
    */
    private ZipUtil() {
        super();
    }
}
