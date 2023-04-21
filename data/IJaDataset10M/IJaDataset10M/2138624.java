package dakside.flexui;

import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * Flex UI manager
 * @author takaji
 */
public class FlexUIHelper {

    public static final Logger logger = Logger.getLogger(FlexUIHelper.class.getName());

    public static Icon locateIcon(Object context, String path) {
        URL url = context.getClass().getResource(path);
        if (url == null) {
            return null;
        } else {
            return new ImageIcon(url);
        }
    }

    public static Icon locateIcon(URL url) {
        try {
            if (url == null) {
                return null;
            }
            return new ImageIcon(url);
        } catch (Exception ex) {
            logger.warning("Cannot locate icon: " + url);
            return null;
        }
    }

    public static Icon getIcon(Object context) {
        return getIcon(context, "icon.png");
    }

    /**
     * Get icon for module
     * @param context
     * @param iconName
     * @return
     */
    public static Icon getIcon(Object context, String iconName) {
        if (context == null || iconName == null || iconName.trim().isEmpty()) {
            return null;
        }
        String pkg = context.getClass().getPackage().getName();
        URL url = null;
        if (pkg.length() == 0) {
            url = context.getClass().getResource("/" + iconName);
        } else {
            url = context.getClass().getResource("/" + pkg.replace('.', '/') + "/" + iconName);
        }
        return (url != null) ? new ImageIcon(url) : null;
    }

    /**
     * Construct menu from path (Root\tParent\tOther)
     * @param path
     * @param pathText
     * @param bar
     * @return
     */
    public static JMenu constructMenu(String path, String pathText, JMenuBar bar) {
        StringTokenizer pathTokens = new StringTokenizer(path, "\t");
        StringTokenizer pathTextTokens = new StringTokenizer(pathText, "\t");
        if (pathTokens.countTokens() != pathTextTokens.countTokens() || pathTokens.countTokens() <= 0) {
            return null;
        }
        JMenu menu = null;
        while (pathTokens.hasMoreElements()) {
            RuntimeMenu menuInfo = new RuntimeMenu(pathTokens.nextToken(), pathTextTokens.nextToken());
            if (menu == null) {
                menu = FlexUIHelper.getMenu(menuInfo, bar, true);
            } else {
                menu = FlexUIHelper.getMenu(menuInfo, menu, true);
            }
        }
        return menu;
    }

    public static JMenu getMenu(RuntimeMenu menuInfo, JMenu parentMenu, boolean autoCreate) {
        if (parentMenu == null || menuInfo == null || menuInfo.getName() == null || menuInfo.getText() == null) {
            return null;
        }
        for (int i = 0; i < parentMenu.getMenuComponentCount(); i++) {
            if (menuInfo.getName().equals(parentMenu.getMenuComponent(i).getName())) {
                return (JMenu) parentMenu.getMenuComponent(i);
            }
        }
        if (autoCreate) {
            return (JMenu) menuInfo.bindTo(parentMenu);
        }
        return null;
    }

    public static JMenu getMenu(RuntimeMenu menuInfo, JMenuBar parentMenu, boolean autoCreate) {
        if (parentMenu == null || menuInfo == null || menuInfo.getName() == null || menuInfo.getText() == null) {
            return null;
        }
        for (int i = 0; i < parentMenu.getMenuCount(); i++) {
            if (menuInfo.getName().equals(parentMenu.getMenu(i).getName())) {
                return (JMenu) parentMenu.getMenu(i);
            }
        }
        if (autoCreate) {
            return (JMenu) menuInfo.bindTo(parentMenu);
        }
        return null;
    }
}
