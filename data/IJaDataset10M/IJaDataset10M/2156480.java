package common.devbot.util.file;

import java.io.File;
import java.security.InvalidParameterException;

public class DirTransformer {

    public static void run(File aDir, FileTransformer tran, boolean recursive, boolean tranformHidden) {
        if (aDir == null || !aDir.exists() || !aDir.isDirectory()) {
            throw new InvalidParameterException();
        }
        if (aDir.isHidden()) {
        } else {
            for (File file : aDir.listFiles()) {
                if (recursive && file.isDirectory()) {
                    run(file, tran, recursive, tranformHidden);
                } else if (file.isFile()) {
                    tran.perfomOperation(file);
                }
            }
        }
    }
}
