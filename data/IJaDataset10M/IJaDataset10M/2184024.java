package core.view.builder;

import core.view.Menu;
import core.view.component.ComponentFactories;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * MenuBuilder create a JMenu.
 * 
 * @since 23/10/2008
 * @version 1.0
 * @author gael
 */
public class MenuBuilder extends AbstractBuilder {

    private static MenuBuilder singleton = null;

    private Menu menu;

    private ComponentFactories factory = ComponentFactories.getInstance();

    private Item item = new Item();

    private MenuBuilder() {
    }

    public static MenuBuilder getInstance() {
        if (singleton == null) singleton = new MenuBuilder();
        return singleton;
    }

    public void build(HashMap<String, String> args, Menu m) {
        menu = m;
        m.setText(args.get("Label"));
        if (args.containsKey("Icon")) m.setIcon(new ImageIcon(ClassLoader.getSystemResource(args.get("Icon"))));
        extractItems(args);
    }

    @Override
    protected void newItem(String name, String type, String icon, String label, String values) {
        if (name.equals("Separator")) menu.addSeparator(); else {
            Item item = this.item;
            factory.create(item, name, type, icon, label, values);
            menu.add((JMenuItem) item.getComponent());
        }
    }
}
