package consciouscode.seedling.boot;

import java.io.File;
import java.io.IOException;
import consciouscode.seedling.ConfigTree;
import consciouscode.seedling.ConfigTreeFactory;
import consciouscode.seedling.Root;
import consciouscode.seedling.Seedling;
import consciouscode.seedling.ValidatingConfigTree;
import consciouscode.seedling.config.ConfigResourceFactory;
import consciouscode.util.PrintLogger;

/**
   Entry point for creating Seedling instances.
*/
public class Booter {

    public static void launch(File seedlingHome, String[] modules, boolean debug) throws IOException, Exception {
        ConfigResourceFactory resourceFactory = new ConfigResourceFactory();
        File modulesDir = new File(seedlingHome, "modules");
        ConfigTreeFactory treeFactory = new ConfigTreeFactory(modulesDir, resourceFactory);
        ConfigTree configTree = treeFactory.makeConfigTree(modules);
        if (debug) {
            configTree = new ValidatingConfigTree(configTree);
        }
        Root root = new Root(configTree, debug);
        if (root.getLog() == null) {
            String message = "No logger configured for root, installing one.";
            System.out.println(message);
            root.setLog(new PrintLogger());
        }
        Seedling seedling = new Seedling(root, configTree);
        seedling.setHandlingSeedlingUrls(true);
        seedling.run();
    }
}
