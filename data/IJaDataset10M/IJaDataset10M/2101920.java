package net.raymanoz.io;

import java.io.IOException;
import net.raymanoz.util.DirType;

public interface File {

    java.io.File getJavaFile();

    boolean exists(boolean createDirIfnotExists);

    File[] listFiles();

    String getName();

    String getAbsolutePath();

    String getCanonicalPath();

    boolean createNewFile() throws IOException;

    Long getDBVersion();

    DirType getDirType();
}
