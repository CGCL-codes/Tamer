package sourced;

import java.io.File;

/**
 *
 * @author Stuart
 */
public class TempCleaner {

    public TempCleaner() {
        String dir = System.getProperty("user.dir") + "/sourced_remote_temp/";
        deleteDirectory(new File(dir));
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void deleteDirectory(File dir) {
        if (!dir.exists()) {
            return;
        }
        System.out.println(">> " + dir.getName());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteDir(files[i]);
            } else {
                files[i].delete();
                return;
            }
        }
    }
}
