package com.google.gdt.eclipse.designer.uibinder.gef;

import com.google.common.collect.ImmutableList;
import com.google.gdt.eclipse.designer.uibinder.model.widgets.menu.MenuBarInfo;
import com.google.gdt.eclipse.designer.uibinder.model.widgets.menu.MenuItemInfo;
import com.google.gdt.eclipse.designer.uibinder.model.widgets.menu.MenuItemSeparatorInfo;
import org.eclipse.wb.core.gef.MatchingEditPartFactory;
import org.eclipse.wb.core.gef.part.menu.MenuEditPartFactory;
import org.eclipse.wb.gef.core.EditPart;
import org.eclipse.wb.gef.core.IEditPartFactory;
import org.eclipse.wb.internal.core.model.menu.IMenuInfo;
import org.eclipse.wb.internal.core.model.menu.IMenuItemInfo;
import org.eclipse.wb.internal.core.model.menu.MenuObjectInfoUtils;

/**
 * {@link IEditPartFactory} for GWT UiBinder.
 * 
 * @author scheglov_ke
 * @coverage GWT.UiBinder.gef
 */
public final class EditPartFactory implements IEditPartFactory {

    private final IEditPartFactory[] FACTORIES = { MENU_FACTORY, MATCHING_FACTORY };

    private static final IEditPartFactory MATCHING_FACTORY = new MatchingEditPartFactory(ImmutableList.of("com.google.gdt.eclipse.designer.uibinder.model.widgets", "com.google.gdt.eclipse.designer.uibinder.model.widgets"), ImmutableList.of("com.google.gdt.eclipse.designer.uibinder.gef.part", "com.google.gdt.eclipse.designer.gef.part.panels"));

    public EditPart createEditPart(EditPart context, Object model) {
        for (IEditPartFactory factory : FACTORIES) {
            EditPart editPart = factory.createEditPart(null, model);
            if (editPart != null) {
                return editPart;
            }
        }
        return null;
    }

    private static final IEditPartFactory MENU_FACTORY = new IEditPartFactory() {

        public EditPart createEditPart(EditPart context, Object model) {
            if (model instanceof MenuBarInfo) {
                MenuBarInfo menu = (MenuBarInfo) model;
                IMenuInfo menuObject = MenuObjectInfoUtils.getMenuInfo(menu);
                return MenuEditPartFactory.createMenu(menu, menuObject);
            }
            if (model instanceof MenuItemInfo) {
                MenuItemInfo item = (MenuItemInfo) model;
                IMenuItemInfo itemObject = MenuObjectInfoUtils.getMenuItemInfo(item);
                return MenuEditPartFactory.createMenuItem(item, itemObject);
            }
            if (model instanceof MenuItemSeparatorInfo) {
                MenuItemSeparatorInfo item = (MenuItemSeparatorInfo) model;
                IMenuItemInfo itemObject = MenuObjectInfoUtils.getMenuItemInfo(item);
                return MenuEditPartFactory.createMenuItem(item, itemObject);
            }
            return null;
        }
    };
}
