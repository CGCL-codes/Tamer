package unbbayes.util.mebn.extension.manager;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.Extension.Parameter;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import unbbayes.draw.extension.IPluginUShape;
import unbbayes.draw.extension.IPluginUShapeBuilder;
import unbbayes.draw.extension.impl.ClassInstantiationPluginUShapeBuilder;
import unbbayes.gui.table.extension.IProbabilityFunctionPanelBuilder;
import unbbayes.prs.builder.INodeBuilder;
import unbbayes.prs.builder.extension.PluginNodeBuilder;
import unbbayes.prs.builder.extension.impl.ClassInstantiationPluginNodeBuilder;
import unbbayes.prs.extension.IPluginNode;
import unbbayes.util.Debug;
import unbbayes.util.extension.dto.INodeClassDataTransferObject;
import unbbayes.util.extension.dto.impl.NodeDto;
import unbbayes.util.extension.manager.CorePluginNodeManager;
import unbbayes.util.extension.manager.UnBBayesPluginContextHolder;

/**
 * This class manages the plugin nodes
 * (nodes loaded by plugins)
 * for the UnBBayes' core module.
 * @author Shou Matsumoto
 *
 */
public class MEBNPluginNodeManager extends CorePluginNodeManager {

    /** ID of UnBBayes-MEBN */
    public static final String MEBN_PLUGIN_ID = "unbbayes.prs.mebn";

    /** ID of the plugin where we can find new node declarations */
    public static final String MEBN_PLUGIN_NODE_EXTENSION_POINT_ID = "MEBNPluginNode";

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
     * or the first access to SingletonHolder.INSTANCE, not before.
     * This is used for creating singleton instances of compiler
     */
    private static class SingletonHolder {

        private static final MEBNPluginNodeManager INSTANCE = new MEBNPluginNodeManager();
    }

    /**
	 * the default constructor is made protected in order to make
	 * it easy to extend
	 */
    protected MEBNPluginNodeManager() {
        super();
        this.setMainPluginID(MEBN_PLUGIN_ID);
        this.setMainExtensionPointID(MEBN_PLUGIN_NODE_EXTENSION_POINT_ID);
        this.getUnbbayesPluginContextHolder().addListener(new UnBBayesPluginContextHolder.OnReloadActionListener() {

            public void onReload(EventObject eventObject) {
                try {
                    reloadPlugin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Obtains a singleton instance of CorePluginNodeManager
	 * @return a instance of CorePluginNodeManager
	 */
    public static MEBNPluginNodeManager newInstance() {
        return SingletonHolder.INSTANCE;
    }
}
