package deltree.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * from http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5056395;
 * (in order to test Channels in linux/JVM 1.4 & 1.5)
 * @author XEPDM
 */
public class FileCopyTest {

    public static void main(String[] args) {
        File a = new File("/tmp/a");
        File b = new File("/tmp/b");
        copyFile(a, b);
    }

    public static void copyFile(File source, File dest) {
        try {
            FileChannel in = new FileInputStream(source).getChannel();
            if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
            FileChannel out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
