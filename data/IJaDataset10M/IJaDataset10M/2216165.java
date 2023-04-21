package ontool.app.modelview;

import java.beans.Beans;
import java.util.Properties;
import javax.swing.JPopupMenu;
import ontool.model.Model;
import ontool.model.NameableModel;
import org.apache.log4j.Category;

/**
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Id: ModelActions.java,v 1.1 2003/10/22 03:06:43 asrgomes Exp $
 */
public class ModelActions {

    private static Properties props;

    private static Category _cat;

    static {
        props = new Properties();
        _cat = Category.getInstance(ModelActions.class);
        try {
            props.load(ModelActions.class.getResourceAsStream("/menu_providers.properties"));
        } catch (Exception ie) {
            _cat.error("Could not load properties", ie);
        }
    }

    /**
	 * Creates a suitable menu provider.
	 * 
	 * @param m model
	 * @return found menu provider
	 * @return provider
	 */
    public static MenuBuilder getMenuProvider(Model m) {
        String pn = props.getProperty(m.getClass().getName());
        if (pn == null) return null;
        try {
            DefaultMenuBuilder mp = (DefaultMenuBuilder) Beans.instantiate(MenuBuilder.class.getClassLoader(), pn);
            mp.setModel((NameableModel) m);
            _cat.debug("created menu provider for " + m);
            return mp;
        } catch (Exception e) {
            _cat.error("Could not create menu provider", e);
            return null;
        }
    }

    /**
	 * Returns a popup menu suitable for a given model.
	 * 
	 * @param m model
	 * @return menu
	 * @return menu
	 */
    public static JPopupMenu getMenu(Model m) {
        JPopupMenu menu = new JPopupMenu();
        MenuBuilder mp = getMenuProvider(m);
        mp.updateMenu(menu);
        return menu;
    }
}
