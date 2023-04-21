package net.community.chest.swing.component.menu;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.community.chest.awt.menu.MenuReflectiveProxy;
import net.community.chest.dom.DOMUtils;
import net.community.chest.dom.transform.XmlValueInstantiator;
import net.community.chest.reflect.ClassUtil;
import net.community.chest.swing.ActionReflectiveProxy;
import net.community.chest.swing.component.JComponentReflectiveProxy;
import net.community.chest.swing.component.JSeparatorReflectiveProxy;
import net.community.chest.swing.component.button.BaseCheckBox;
import net.community.chest.swing.component.button.BaseRadioButton;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <M> The reflected {@link JPopupMenu}
 * @author Lyor G.
 * @since Sep 24, 2008 12:07:08 PM
 */
public class JPopupMenuReflectiveProxy<M extends JPopupMenu> extends JComponentReflectiveProxy<M> {

    public JPopupMenuReflectiveProxy(Class<M> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    protected JPopupMenuReflectiveProxy(Class<M> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }

    public XmlValueInstantiator<? extends JMenu> getMenuConverter(final Element elem) throws Exception {
        return (null == elem) ? null : JMenuReflectiveProxy.MENU;
    }

    /**
	 * Called by {@link #fromXmlChild(JPopupMenu, Element)} when a sub-menu
	 * XML {@link Element} is encountered in order to re-construct the
	 * sub-menu
	 * @param src The {@link JMenu} instance to which to add the sub-menu
	 * @param elem XML element to use for data reconstruction
	 * @return Created {@link JMenu} instance default calls
	 * {@link #getMenuConverter(Element)} and then invokes its
	 * {@link XmlValueInstantiator#fromXml(Element)} method
	 * @throws Exception if cannot re-construct the sub-menu
	 */
    public JMenu createSubMenu(final M src, final Element elem) throws Exception {
        final XmlValueInstantiator<? extends JMenu> proxy = getMenuConverter(elem);
        final JMenu subMenu = proxy.fromXml(elem);
        if (subMenu != null) src.add(subMenu);
        return subMenu;
    }

    public boolean isRadioButtonMenuItemElement(final Element elem, final String itemClass) {
        return isMatchingAttribute(elem, itemClass, BaseRadioButton.RADIO_ELEMNAME);
    }

    public boolean isCheckboxMenuItemElement(final Element elem, final String itemClass) {
        return isMatchingAttribute(elem, itemClass, BaseCheckBox.CHECKBOX_ELEM_NAME);
    }

    public XmlValueInstantiator<? extends JMenuItem> getSubItemConverter(final Element elem) throws Exception {
        final String itemClass = elem.getAttribute(CLASS_ATTR);
        if (isRadioButtonMenuItemElement(elem, itemClass)) return JRadioButtonMenuItemReflectiveProxy.RADIOMENUITEM; else if (isCheckboxMenuItemElement(elem, itemClass)) return JCheckBoxMenuItemReflectiveProxy.CBMENUITEM; else return JMenuItemReflectiveProxy.MENUITEM;
    }

    /**
	 * Called by default implementation of {@link #createSubItem(JPopupMenu, Element)}
	 * in order to create the {@link JMenuItem} instance to be added.
	 * @param elem The XML element to be used for the item initialization
	 * @return <P>The initialized item - by default, checks the {@link net.community.chest.dom.proxy.ReflectiveAttributesProxy#CLASS_ATTR}
	 * attribute in order to determine what type of item to create. Allowed
	 * values are:</P></BR>
	 * <UL>
	 * 		<LI><I>radio</I> - create a {@link javax.swing.JRadioButtonMenuItem} instance</LI>
	 * 		<LI><I>checkbox</I> - create a {@link javax.swing.JCheckBoxMenuItem} instance</LI>
	 * 		<LI>default = {@link JMenuItem} instance</LI>
	 * </UL>
	 * @throws Exception
	 */
    public JMenuItem createSubItemInstance(final Element elem) throws Exception {
        final XmlValueInstantiator<? extends JMenuItem> proxy = getSubItemConverter(elem);
        final JMenuItem item = proxy.fromXml(elem);
        return item;
    }

    /**
	 * Called by {@link #fromXmlChild(JPopupMenu, Element)} when a menu-item
	 * XML {@link Element} is encountered in order to re-construct the
	 * sub-menu
	 * @param src The {@link JMenu} instance to which to add the sub item
	 * @param elem XML element to use for reconstructing the data
	 * @return Created {@link JMenuItem} instance - default calls
	 * {@link #createSubItemInstance(Element)}
	 * @throws Exception if cannot re-construct the sub-item
	 */
    public JMenuItem createSubItem(final M src, final Element elem) throws Exception {
        final JMenuItem item = createSubItemInstance(elem);
        if (item != null) src.add(item);
        return item;
    }

    /**
	 * Called by {@link #fromXmlChild(JPopupMenu, Element)} when a separator
	 * XML {@link Element} is encountered in order to re-construct the
	 * sub-menu
	 * @param src The {@link JMenu} instance to which to add the sub item
	 * @param elem XML element to use for reconstructing the data
	 * @return Updated {@link JMenu} instance (usually same as input) -
	 * default calls {@link JMenu#addSeparator()} method
	 * @throws Exception if cannot add the separator
	 */
    public M createMenuSeparator(final M src, final Element elem) throws Exception {
        if (null == elem) throw new IllegalArgumentException(ClassUtil.getExceptionLocation(getClass(), "createMenuSeparator") + " no " + Element.class.getSimpleName() + " instance");
        src.addSeparator();
        return src;
    }

    public boolean isSubMenuElement(final Element elem, final String tagName) {
        return isMatchingElement(elem, tagName, MenuReflectiveProxy.MENU_ELEMNAME);
    }

    public boolean isSubItemElement(final Element elem, final String tagName) {
        return isMatchingElement(elem, tagName, BaseMenuItem.ITEM_ELEMNAME);
    }

    public boolean isSeparatorElement(final Element elem, final String tagName) {
        return isMatchingElement(elem, tagName, JSeparatorReflectiveProxy.SEPARATOR_ELEMNAME);
    }

    public boolean isActionElement(final Element elem, final String tagName) {
        return isMatchingElement(elem, tagName, ActionReflectiveProxy.ACTION_ELEMNAME);
    }

    public XmlValueInstantiator<? extends Action> getActionConverter(final Element elem) throws Exception {
        if (null == elem) return null;
        throw new UnsupportedOperationException("getActionConverter(" + DOMUtils.toString(elem) + ") N/A");
    }

    public Action addAction(final M src, final Element elem) throws Exception {
        final XmlValueInstantiator<? extends Action> inst = getActionConverter(elem);
        final Action a = inst.fromXml(elem);
        if (a != null) src.add(a);
        return a;
    }

    @Override
    public M fromXmlChild(final M src, final Element elem) throws Exception {
        final String tagName = elem.getTagName();
        if (isSubMenuElement(elem, tagName)) {
            createSubMenu(src, elem);
            return src;
        } else if (isSubItemElement(elem, tagName)) {
            createSubItem(src, elem);
            return src;
        } else if (isActionElement(elem, tagName)) {
            addAction(src, elem);
            return src;
        } else if (isSeparatorElement(elem, tagName)) return createMenuSeparator(src, elem);
        return super.fromXmlChild(src, elem);
    }

    public static final JPopupMenuReflectiveProxy<JPopupMenu> POPUPMENU = new JPopupMenuReflectiveProxy<JPopupMenu>(JPopupMenu.class, true);
}
