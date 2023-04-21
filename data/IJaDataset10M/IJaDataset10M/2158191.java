package synthlab.api;

public class ModulePoolFactory {

    /**
   * This will return a default implementation of the module pool.
   * @return
   */
    public static ModulePool createDefault() {
        return new synthlab.internal.BasicModulePool();
    }
}
