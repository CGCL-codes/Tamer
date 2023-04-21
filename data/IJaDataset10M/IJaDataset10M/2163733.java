package test;

import java.io.*;
import org.apache.log4j.Logger;

public class CopyDirectory {

    private static Logger logger = Logger.getLogger(CopyDirectory.class.getName());

    public void copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
            }
            String files[] = srcPath.list();
            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
            }
        } else {
            if (!srcPath.exists()) {
                logger.debug("Source File or directory does not exist.");
                System.exit(0);
            } else {
                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
        logger.info("[copyDirectory]: Directory copied.");
    }

    public static void main(String[] args) throws IOException {
        CopyDirectory cd = new CopyDirectory();
        File src = new File("C:/Documents%20and%20Settings/Dickson/My%20Documents/NetBeansProjects/CalendarCVS/build/classes/Database");
        File dest = new File("D:/nageIT");
        cd.copyDirectory(src, dest);
    }
}
