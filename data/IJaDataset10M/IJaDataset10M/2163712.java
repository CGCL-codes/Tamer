package astcentric.editor.eclipse.plugin;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.themes.ITheme;
import org.osgi.framework.BundleContext;
import astcentric.editor.common.view.tree.TreeNodeFrameFactoryStore;
import astcentric.editor.common.view.tree.frame.TreeConfiguration;
import astcentric.editor.common.view.tree.frame.fanzy.FancyTreeNodeFrameFactory;
import astcentric.editor.common.view.tree.frame.simple.SimpleTreeNodeFrameFactory;
import astcentric.editor.eclipse.AsynchExecutor;
import astcentric.editor.eclipse.EclipseTreeConfiguration;
import astcentric.structure.basic.tool.Executor;

/**
 */
public class ASTEditorPlugin extends AbstractUIPlugin {

    private static ASTEditorPlugin plugin;

    /**
   * Returns the shared instance.
   */
    public static ASTEditorPlugin getPlugin() {
        return plugin;
    }

    /**
   * Returns an image descriptor for the image file at the given
   * plug-in relative path.
   *
   * @param path the path
   * @return the image descriptor
   */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("net.sourceforge.astcentric", path);
    }

    private final AsynchExecutor _executor;

    private final ASTStore _store;

    private final TreeNodeFrameFactoryStore _frameFactory;

    private ASTLabelDecorator _decorator;

    /**
   * The constructor.
   */
    public ASTEditorPlugin() {
        _store = new ASTStore();
        _frameFactory = new TreeNodeFrameFactoryStore();
        ITheme theme = getWorkbench().getThemeManager().getCurrentTheme();
        ColorRegistry colorRegistry = theme.getColorRegistry();
        TreeConfiguration treeConfiguration = new EclipseTreeConfiguration(colorRegistry);
        _frameFactory.add(new SimpleTreeNodeFrameFactory(treeConfiguration));
        _frameFactory.add(new FancyTreeNodeFrameFactory(treeConfiguration));
        plugin = this;
        _executor = new AsynchExecutor(getWorkbench().getDisplay());
    }

    /**
   * This method is called upon plug-in activation
   */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
   * This method is called when the plug-in is stopped
   */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    Executor getExecutor() {
        return _executor;
    }

    ASTStore getASTStore() {
        return _store;
    }

    void setDecorator(ASTLabelDecorator decorator) {
        _decorator = decorator;
    }

    ASTLabelDecorator getDecorator() {
        return _decorator;
    }

    TreeNodeFrameFactoryStore getTreeNodeFrameFactoryStore() {
        return _frameFactory;
    }
}
