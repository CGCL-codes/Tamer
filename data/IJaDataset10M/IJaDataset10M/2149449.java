package net.sf.gham.core.entity.player.category;

import javax.swing.JOptionPane;
import net.sf.gham.core.dao.CategoryItem;
import net.sf.gham.swing.MainApp;
import net.sf.gham.swing.editlist.AbstractMutableListModel;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class CategoryItemListModel extends AbstractMutableListModel<CategoryItemWrapper> {

    private ICategoryBO categoryBO;

    public void addObject() {
        final String name = JOptionPane.showInputDialog(MainApp.main, Messages.getString("Insert_name"));
        if (name != null && name.length() > 0) {
            if (containsNamedObject(name)) {
                String s = Messages.getString("Item_already_exists");
                JOptionPane.showMessageDialog(MainApp.main, s, s, JOptionPane.ERROR_MESSAGE);
            } else {
                CategoryItem item = new CategoryItem();
                item.setName(name);
                CategoryWrapper cat = (CategoryWrapper) list;
                item.setViewOrder(getSize());
                categoryBO.saveCategoryItem(cat.getCategory(), item);
                add(new CategoryItemWrapper(item));
            }
        }
    }

    private boolean containsNamedObject(String name) {
        for (int i = 0; i < getSize(); i++) {
            CategoryItemWrapper c = getElementAt(i);
            if (c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public ICategoryBO getCategoryBO() {
        return categoryBO;
    }

    public void setCategoryBO(ICategoryBO categoryBO) {
        this.categoryBO = categoryBO;
    }

    @Override
    protected void beforeRemoveListener(int selectedRow, CategoryItemWrapper removed) {
        categoryBO.deleteCategoryItem(removed.getCategoryItem());
    }

    @Override
    protected void beforeSwapListener(CategoryItemWrapper obj1, CategoryItemWrapper obj2, int index1, int index2) {
        categoryBO.swapItems(obj1.getCategoryItem(), obj2.getCategoryItem());
        super.beforeSwapListener(obj1, obj2, index1, index2);
    }
}
