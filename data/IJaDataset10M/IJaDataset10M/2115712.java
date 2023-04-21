package net.narusas.cafelibrary.apps;

import java.util.HashMap;
import javax.swing.Icon;
import net.narusas.cafelibrary.Book;
import net.narusas.cafelibrary.ui.LazyIcon;

public class IconHolder {

    private static IconHolder instance;

    private HashMap<Long, Icon> icons;

    public static final Icon[] favorite = new Icon[6];

    static {
        for (int i = 0; i <= 5; i++) {
            String file = "images/favorite_star_" + i + ".png";
            Icon icon = new LazyIcon(file);
            favorite[i] = icon;
        }
    }

    public static IconHolder getInstance() {
        if (instance == null) {
            instance = new IconHolder();
        }
        return instance;
    }

    public IconHolder() {
        icons = new HashMap<Long, Icon>();
    }

    public Icon getIconFor(Book book) {
        long id = book.getId();
        return getIcon(id);
    }

    private Icon getIcon(long id) {
        if (icons.containsKey(id)) {
            return icons.get(id);
        }
        Icon icon = new LazyIcon("data/" + id + "T.jpg");
        icons.put(id, icon);
        return icon;
    }
}
