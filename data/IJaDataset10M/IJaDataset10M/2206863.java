package linker.plugin;

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import linker.message.MessageItem;

/**
 * The base class of plugin classes.It defined the methods that can be used in
 * plugin.
 * 
 * @version 2008-05-16
 * @author Jianfeng tujf.cn@gmail.com
 * @author Tan tan1986@gmail.com
 */
public class PluginItem extends MessageItem {

    /**
	 * The holded plugin class.
	 */
    private Class pluginClass;

    /**
	 * The holded plugin.
	 */
    private Plugin plugin;

    /**
	 * Saved path.
	 */
    private String path;

    /**
	 * 
	 * @param file
	 *            The plugin dir.
	 * 
	 */
    public PluginItem(final File file) {
        super(null, file.getName());
        String pluginClassPath = file.getAbsolutePath() + File.separator + file.getName() + ".class";
        pluginClass = PluginClassLoader.getClassLoader().findClass(new File(pluginClassPath));
        ImageIcon icon = new ImageIcon(file.getAbsoluteFile() + File.separator + "icon.png");
        this.setIcon(icon);
        path = file.getAbsolutePath();
    }

    /**
	 * Double click event.
	 */
    public final void doubleClick() {
        if (plugin != null) {
            plugin.doubleClick();
        } else {
            try {
                plugin = (Plugin) pluginClass.newInstance();
                plugin.setPath(path);
                plugin.setPluginItem(this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Double click event.
	 * 
	 * @return The PluginItem's popupMenu.
	 */
    public final JPopupMenu getPopupMenu() {
        return null;
    }
}
