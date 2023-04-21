package at.riemers.zero.base.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

/**
 *
 * @author tobias
 */
public class Menu {

    private static final Logger log = Logger.getLogger(Menu.class);

    private MenuItem root = new MenuItem("", "");

    private MessageSource messages;

    private Locale locale;

    public Menu(MessageSource messages, Locale locale) {
        this.messages = messages;
        this.locale = locale;
    }

    /** Creates a new instance of Menu */
    public void add(Menu menu) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void add(String path, String action) {
        add(path, action, "");
    }

    public void add(String path, String action, String namespace) {
        add(path, action, namespace, false);
    }

    public void add(String path, String action, String namespace, boolean jsp) {
        add(path, action, namespace, jsp, 1);
    }

    public void add(String path, String action, String namespace, boolean jsp, int priority) {
        String[] tokens = path.split("\\.");
        MenuItem parent = root;
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i];
            try {
                t = messages.getMessage("menu" + (i + 1) + "." + tokens[i], null, locale);
                if (t.equals("menu" + (i + 1) + "." + tokens[i])) {
                    t = tokens[i];
                }
            } catch (org.springframework.context.NoSuchMessageException ex) {
            }
            log.debug(t + " " + action);
            MenuItem item = parent.getSubMenuItem(t);
            if (item == null) {
                item = createMenuItem(t, action, namespace, parent.getItems().size(), jsp, priority);
                parent.addSubMenuItem(item);
            }
            parent = item;
        }
    }

    private MenuItem createMenuItem(String name, String action, String namespace, int sort, boolean jsp, int priority) {
        return new MenuItem(name, action, namespace, sort, jsp, priority);
    }

    public Collection<MenuItem> getItems() {
        return root.getItems();
    }

    public MenuItem getRoot() {
        return root;
    }

    public void unselectAll() {
        root.unselectAll();
    }

    public void select(String action) {
        root.select(action);
    }

    public MenuItem getSelected() {
        return root.getSelected();
    }
}
