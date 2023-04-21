package wheel;

import wheel.components.StandaloneComponent;
import wheel.util.ActionRegistry;
import java.io.InputStream;
import java.util.Set;

public interface IResourceManager {

    public StandaloneComponent loadPage(String pageClass);

    public InputStream loadAsset(String path);

    public ActionRegistry getActionRegistry();

    public Set getResourcesFromPath(String path);

    public Class loadClass(String className) throws ClassNotFoundException;
}
