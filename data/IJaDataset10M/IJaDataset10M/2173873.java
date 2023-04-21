package org.specrunner.plugins.impl.factories;

import nu.xom.Element;
import nu.xom.Node;
import org.specrunner.context.IContext;
import org.specrunner.plugins.IPlugin;
import org.specrunner.plugins.PluginException;
import org.specrunner.plugins.impl.PluginNop;
import org.specrunner.plugins.impl.UtilPlugin;

/**
 * Returns a plugin based on a 'custom' attribute.
 * <p>
 * i.e. custom="test.PluginSysout",
 * <p>
 * or based on a file ' <code>plugin_custom.properties</code> , i.e. add
 * <code>new=test.PluginCustomized</code>, and use <code>custom="new"</code>
 * anywhere in the specification.
 * 
 * @author Thiago Santos
 * 
 */
public class PluginFactoryCustom extends PluginFactoryImpl {

    private static final String ATTRIBUTE = "custom";

    public PluginFactoryCustom() {
        super("plugin_custom.properties");
    }

    @Override
    @SuppressWarnings("unchecked")
    public IPlugin newPlugin(Node node, IContext context) throws PluginException {
        initialize();
        if (node instanceof Element) {
            Element ele = (Element) node;
            Node att = ele.getAttribute(ATTRIBUTE);
            if (att != null) {
                String clazz = att.getValue();
                Class<? extends IPlugin> c = types.get(clazz);
                try {
                    if (c == null) {
                        c = (Class<? extends IPlugin>) Class.forName(clazz);
                    }
                    return UtilPlugin.create(context, c, ele);
                } catch (ClassNotFoundException e) {
                    throw new PluginException("Plugin class " + clazz + " not found.", e);
                } catch (ClassCastException e) {
                    throw new PluginException("Plugin class " + clazz + " is not an instance of IPlugin.", e);
                }
            }
        }
        return PluginNop.emptyPlugin();
    }
}
