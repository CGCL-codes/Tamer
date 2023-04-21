package com.google.gdt.eclipse.designer.gxt.model.widgets.menu;

import com.google.gdt.eclipse.designer.gxt.model.widgets.ComponentInfo;
import org.eclipse.wb.draw2d.geometry.Rectangle;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.description.ComponentDescription;
import org.eclipse.wb.internal.core.model.menu.AbstractMenuObject;
import org.eclipse.wb.internal.core.model.menu.IMenuInfo;
import org.eclipse.wb.internal.core.model.menu.IMenuItemInfo;
import org.eclipse.wb.internal.core.model.menu.IMenuPolicy;
import org.eclipse.wb.internal.core.model.menu.MenuObjectInfoUtils;
import org.eclipse.wb.internal.core.utils.IAdaptable;
import org.eclipse.wb.internal.core.utils.ast.AstEditor;
import org.eclipse.swt.graphics.Image;

/**
 * Model for <code>com.extjs.gxt.ui.client.widget.menu.Item</code>.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.model
 */
public class ItemInfo extends ComponentInfo implements IAdaptable {

    public ItemInfo(AstEditor editor, ComponentDescription description, CreationSupport creationSupport) throws Exception {
        super(editor, description, creationSupport);
    }

    /**
   * @return optional sub menu.
   */
    protected MenuInfo getSubMenu() {
        return null;
    }

    private final IMenuItemInfo m_itemImpl = new MenuItemImpl();

    public <T> T getAdapter(Class<T> adapter) {
        if (adapter.isAssignableFrom(IMenuItemInfo.class)) {
            return adapter.cast(m_itemImpl);
        }
        return null;
    }

    /**
   * Implementation of {@link IMenuItemInfo}.
   * 
   * @author scheglov_ke
   */
    private final class MenuItemImpl extends AbstractMenuObject implements IMenuItemInfo {

        public MenuItemImpl() {
            super(ItemInfo.this);
        }

        public Object getModel() {
            return ItemInfo.this;
        }

        public Image getImage() {
            return null;
        }

        public Rectangle getBounds() {
            return ItemInfo.this.getBounds();
        }

        public IMenuInfo getMenu() {
            MenuInfo menu = getSubMenu();
            if (menu != null) {
                return MenuObjectInfoUtils.getMenuInfo(menu);
            }
            return null;
        }

        public IMenuPolicy getPolicy() {
            return IMenuPolicy.NOOP;
        }
    }
}
