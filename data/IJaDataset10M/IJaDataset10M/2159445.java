package ressources;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.ImageIcon;
import exceptions.RessourceNotAvailableException;

public class IconCache {

    private static final HashMap<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();

    private static final String ressourceFile = "iconFiles.properties";

    private static Properties p = null;

    /**
	 * Returns an Icon for a specific key, caches icons
	 * 
	 * @param key
	 *            the key
	 * @return the {@link ImageIcon}
	 * @throws RessourceNotAvailableException
	 */
    public static ImageIcon getIcon(final String key) throws RessourceNotAvailableException {
        if (IconCache.iconCache.containsKey(key)) return IconCache.iconCache.get(key);
        if (IconCache.p == null) {
            try {
                IconCache.p = RessourceCache.getProperties(IconCache.ressourceFile);
            } catch (final IOException e) {
                throw new RessourceNotAvailableException();
            }
        }
        final URL res = IconCache.class.getResource(p.getProperty(key));
        if (res != null) IconCache.iconCache.put(key, new ImageIcon(res));
        return IconCache.iconCache.get(key);
    }
}
