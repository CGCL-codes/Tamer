package com.g2d.studio.gameedit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import com.cell.rpg.template.TAvatar;
import com.cell.rpg.template.TEffect;
import com.cell.rpg.template.TItem;
import com.cell.rpg.template.TItemList;
import com.cell.rpg.template.TShopItemList;
import com.cell.rpg.template.TSkill;
import com.cell.rpg.template.TUnit;
import com.cell.rpg.template.TemplateNode;
import com.cell.xls.XLSColumns;
import com.cell.xls.XLSFullRow;
import com.g2d.studio.Studio;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.cpj.entity.CPJFile;
import com.g2d.studio.gameedit.dynamic.DAvatar;
import com.g2d.studio.gameedit.dynamic.DEffect;
import com.g2d.studio.gameedit.dynamic.DItemList;
import com.g2d.studio.gameedit.dynamic.DShopItemList;
import com.g2d.studio.gameedit.entity.IProgress;
import com.g2d.studio.gameedit.entity.ObjectNode;
import com.g2d.studio.gameedit.template.XLSItem;
import com.g2d.studio.gameedit.template.XLSShopItem;
import com.g2d.studio.gameedit.template.XLSSkill;
import com.g2d.studio.gameedit.template.XLSTemplateNode;
import com.g2d.studio.gameedit.template.XLSUnit;
import com.g2d.studio.io.File;
import com.g2d.studio.res.Res;

public class ObjectManager {

    public final com.g2d.studio.io.File objects_dir;

    private XLSColumns player_xls_columns;

    private XLSColumns unit_xls_columns;

    private XLSColumns pet_xls_columns;

    private LinkedHashMap<Class<?>, ObjectManagerTree<?, ?>> managers = new LinkedHashMap<Class<?>, ObjectManagerTree<?, ?>>();

    public ObjectManager(Studio studio) {
        this.objects_dir = Studio.getInstance().project_save_path.getChildFile("objects");
        this.player_xls_columns = XLSColumns.getXLSColumns(studio.xls_tplayer.getInputStream());
        this.pet_xls_columns = XLSColumns.getXLSColumns(studio.xls_tpet.getInputStream());
    }

    /**
	 * 初始化管理器
	 * @param studio
	 * @param progress
	 */
    public void loadAll(Studio studio, ProgressForm progress) {
        {
            ObjectTreeViewTemplateXLS<XLSUnit, TUnit> view = new ObjectTreeViewTemplateXLS<XLSUnit, TUnit>("单位模板", XLSUnit.class, TUnit.class, objects_dir, studio.xls_tunit, progress);
            XLSUnitManagerTree form = new XLSUnitManagerTree(studio, progress, Res.icon_hd, view);
            managers.put(view.node_type, form);
            this.unit_xls_columns = view.xls_columns;
        }
        {
            ObjectTreeViewTemplateXLS<XLSItem, TItem> view = new ObjectTreeViewTemplateXLS<XLSItem, TItem>("道具模板", XLSItem.class, TItem.class, objects_dir, studio.xls_titem, progress);
            XLSItemManagerTree form = new XLSItemManagerTree(studio, progress, Res.icon_res_4, view);
            managers.put(view.node_type, form);
        }
        {
            ObjectTreeViewTemplateXLS<XLSSkill, TSkill> tree_skills_view = new SkillTreeView("技能模板", objects_dir, studio.xls_tskill, progress);
            XLSSkillManagerTree form_skills_view = new XLSSkillManagerTree(studio, progress, Res.icon_res_3, tree_skills_view);
            managers.put(tree_skills_view.node_type, form_skills_view);
        }
        {
            ShopItemTreeView tree_shopitems_view = new ShopItemTreeView("商品模板", objects_dir, studio.xls_tshopitem, progress);
            XLSShopItemManagerTree form_shopitems_view = new XLSShopItemManagerTree(studio, progress, Res.icon_res_7, tree_shopitems_view);
            managers.put(tree_shopitems_view.node_type, form_shopitems_view);
        }
        {
        }
        {
            ObjectTreeViewTemplateDynamic<DAvatar, TAvatar> tree_avatars_view = new AvatarTreeView("AVATAR", objects_dir, progress);
            ObjectManagerTree<DAvatar, TAvatar> form_avatars_view = new ObjectManagerTree<DAvatar, TAvatar>(studio, progress, Res.icon_res_2, tree_avatars_view);
            managers.put(tree_avatars_view.node_type, form_avatars_view);
        }
        {
            ObjectTreeViewTemplateDynamic<DEffect, TEffect> tree_effects_view = new EffectTreeView("魔法效果/特效", objects_dir, progress);
            ObjectManagerTree<DEffect, TEffect> form_effects_view = new ObjectManagerTree<DEffect, TEffect>(studio, progress, Res.icon_res_8, tree_effects_view);
            managers.put(tree_effects_view.node_type, form_effects_view);
        }
        {
            ObjectTreeViewTemplateDynamic<DItemList, TItemList> tree_item_list_view = new ItemListTreeView("掉落道具列表", objects_dir, progress);
            ObjectManagerTree<DItemList, TItemList> form_item_list_view = new ObjectManagerTree<DItemList, TItemList>(studio, progress, Res.icon_res_9, tree_item_list_view);
            managers.put(tree_item_list_view.node_type, form_item_list_view);
        }
        {
            ObjectTreeViewTemplateDynamic<DShopItemList, TShopItemList> tree = new ShopItemListTreeView("售卖商品列表", objects_dir, progress);
            ObjectManagerTree<DShopItemList, TShopItemList> form = new ObjectManagerTree<DShopItemList, TShopItemList>(studio, progress, Res.icon_res_9, tree);
            managers.put(tree.node_type, form);
        }
        for (ObjectManagerTree<?, ?> page : managers.values()) {
            page.initToolbars(this);
        }
    }

    static File toListFile(File objects_dir, Class<?> data_type) {
        String name = data_type.getSimpleName().toLowerCase();
        return Studio.getInstance().getIO().createFile(objects_dir, name + ".obj/" + name + ".list");
    }

    /**
	 * 得到显示在StudioToolbar上的控件
	 * @return
	 */
    public ArrayList<ObjectManagerTree<?, ?>> getIconPages() {
        ArrayList<ObjectManagerTree<?, ?>> pages = new ArrayList<ObjectManagerTree<?, ?>>(managers.values());
        pages.remove(managers.get(DItemList.class));
        pages.remove(managers.get(DShopItemList.class));
        return pages;
    }

    public Collection<ObjectManagerTree<?, ?>> getPages() {
        return managers.values();
    }

    public <T extends ObjectNode<?>> void refresh(Class<T> type) {
        for (ObjectManagerTree<?, ?> page : managers.values()) {
            if (page.tree_view.node_type.equals(type)) {
                page.tree_view.reload();
            }
        }
    }

    public XLSColumns getPlayerXLSColumns() {
        return player_xls_columns;
    }

    public XLSColumns getUnitXLSColumns() {
        return unit_xls_columns;
    }

    public XLSColumns getPetXLSColumns() {
        return pet_xls_columns;
    }

    public ObjectTreeView<?, ?> getPage(Class<?> type) {
        if (managers.containsKey(type)) {
            return managers.get(type).tree_view;
        }
        return null;
    }

    public ObjectManagerTree<?, ?> getPageForm(Class<?> type) {
        if (managers.containsKey(type)) {
            return managers.get(type);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends ObjectNode<?>> Vector<T> getObjects(Class<T> type) {
        ObjectTreeView<?, ?> page = getPage(type);
        if (page.node_type.equals(type)) {
            return (Vector<T>) page.getAllObject();
        }
        return null;
    }

    public <T extends ObjectNode<?>> T getObject(Class<T> type, int id) {
        ObjectTreeView<?, ?> page = getPage(type);
        if (page.node_type.equals(type)) {
            return type.cast(page.getNode(id));
        }
        return null;
    }

    public <T extends ObjectNode<?>> T getObject(Class<T> type, String id) {
        return getObject(type, Integer.parseInt(id));
    }

    public void resetAllResources(CPJFile cpj) {
        for (XLSUnit unit : getObjects(XLSUnit.class)) {
            unit.resetResource(cpj);
        }
    }

    public void saveAll(IProgress progress) throws Throwable {
        for (ObjectManagerTree<?, ?> page : managers.values()) {
            page.saveAll(progress);
        }
        System.out.println(getClass().getSimpleName() + " : save all");
    }

    public static <T extends XLSTemplateNode<?>> T createXLSObjectFromRow(Class<T> node_type, Map<String, XLSFullRow> xls_row_map, String row_key, TemplateNode data) {
        T node = null;
        try {
            XLSFullRow row = xls_row_map.get(row_key);
            if (row != null) {
                if (node_type.equals(XLSUnit.class)) {
                    node = (node_type.cast(new XLSUnit(row.xls_file, row, data)));
                } else if (node_type.equals(XLSItem.class)) {
                    node = (node_type.cast(new XLSItem(row.xls_file, row, data)));
                } else if (node_type.equals(XLSShopItem.class)) {
                    node = (node_type.cast(new XLSShopItem(row.xls_file, row, data)));
                } else if (node_type.equals(XLSSkill.class)) {
                    node = (node_type.cast(new XLSSkill(row.xls_file, row, data)));
                }
            } else {
                System.err.println(node_type + " : XML 行不存在 : " + row_key);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return node;
    }
}
