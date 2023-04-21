package architecture.common.lifecycle;

import java.io.File;

public interface ConfigRoot {

    public static int NODE = 0;

    public abstract String getConfigRootPath();

    public abstract String getURI(String name);

    public abstract File getFile(String name);

    public String getRootURI();
}
