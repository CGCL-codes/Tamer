package net.community.chest.swing.component.menu;

import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.community.chest.awt.attributes.Backgrounded;
import net.community.chest.awt.attributes.Enabled;
import net.community.chest.awt.attributes.FontControl;
import net.community.chest.awt.attributes.Foregrounded;
import net.community.chest.awt.attributes.Tooltiped;
import net.community.chest.dom.DOMUtils;
import net.community.chest.dom.proxy.XmlProxyConvertible;
import net.community.chest.dom.transform.XmlConvertible;
import net.community.chest.reflect.ClassUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 4, 2009 1:57:59 PM
 */
public class BasePopupMenu extends JPopupMenu implements XmlConvertible<BasePopupMenu>, MenuItemExplorer, MenuExplorer, FontControl, Tooltiped, Enabled, Foregrounded, Backgrounded {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3807179691875112252L;

    public BasePopupMenu(String label) {
        super(label);
    }

    public BasePopupMenu() {
        this((String) null);
    }

    protected XmlProxyConvertible<?> getPopupMenuConverter(final Element elem) {
        return (null == elem) ? null : JPopupMenuReflectiveProxy.POPUPMENU;
    }

    @Override
    public BasePopupMenu fromXml(final Element elem) throws Exception {
        final XmlProxyConvertible<?> proxy = getPopupMenuConverter(elem);
        @SuppressWarnings("unchecked") final Object o = ((XmlProxyConvertible<Object>) proxy).fromXml(this, elem);
        if (o != this) throw new IllegalStateException("fromXml(" + DOMUtils.toString(elem) + " mismatched initialization instances");
        return this;
    }

    public BasePopupMenu(final Element elem) throws Exception {
        final JPopupMenu menu = fromXml(elem);
        if (menu != this) throw new IllegalStateException("<init>(" + DOMUtils.toString(elem) + ") mismatched restored " + JMenu.class.getName() + ") instances");
    }

    @Override
    public Element toXml(Document doc) throws Exception {
        throw new UnsupportedOperationException(ClassUtil.getExceptionLocation(getClass(), "toXml") + " N/A");
    }

    private Map<String, JMenuItem> _itemsMap;

    @Override
    public synchronized Map<String, ? extends JMenuItem> getItemsMap() {
        if (null == _itemsMap) _itemsMap = MenuUtil.updateItemsMap(null, this, true);
        return _itemsMap;
    }

    @Override
    public synchronized void resetItemsMap() {
        if (_itemsMap != null) _itemsMap = null;
    }

    @Override
    public JMenuItem findMenuItemByCommand(final String cmd) {
        if ((null == cmd) || (cmd.length() <= 0)) return null;
        final Map<String, ? extends JMenuItem> itemsMap = getItemsMap();
        return ((null == itemsMap) || (itemsMap.size() <= 0)) ? null : itemsMap.get(cmd);
    }

    @Override
    public JMenuItem addItemActionListenerByCommand(final String cmd, final ActionListener listener) {
        final JMenuItem item = (null == listener) ? null : findMenuItemByCommand(cmd);
        if (item != null) item.addActionListener(listener);
        return item;
    }

    private Map<String, JMenu> _menusMap;

    @Override
    public synchronized Map<String, ? extends JMenu> getMenusMap() {
        if (null == _menusMap) _menusMap = MenuUtil.updateMenusMap(null, this, true);
        return _menusMap;
    }

    @Override
    public synchronized void resetMenusMap() {
        if (_menusMap != null) _menusMap = null;
    }

    @Override
    public JMenu findMenuByCommand(final String cmd) {
        if ((null == cmd) || (cmd.length() <= 0)) return null;
        final Map<String, ? extends JMenu> menusMap = getMenusMap();
        return ((null == menusMap) || (menusMap.size() <= 0)) ? null : menusMap.get(cmd);
    }

    @Override
    public JMenu addMenuActionListenerByCommand(String cmd, ActionListener listener, boolean recursive) {
        return MenuUtil.addMenuActionHandler(this, cmd, listener, recursive);
    }
}
