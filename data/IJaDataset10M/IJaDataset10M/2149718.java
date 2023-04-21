package shag.list.controller;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Convenience class for implementing a CreatingListController.
 * 
 * @author kflanagan
 */
public abstract class AbstractCreatingListController implements CreatingListController {

    /**
     * @see shag.list.controller.UsefulListController#isListEditable()
     */
    public boolean isListEditable() {
        return true;
    }

    /**
     * @see shag.list.controller.UsefulListController#isListDeletable()
     */
    public boolean isListDeletable() {
        return true;
    }

    /**
     * @see shag.list.controller.UsefulListController#isAdditionAllowed()
     */
    public boolean isAdditionAllowed() {
        return true;
    }

    /**
     * @see shag.list.controller.UsefulListController#isListReorderable()
     */
    public boolean isListReorderable() {
        return false;
    }

    /**
     * @see shag.list.controller.UsefulListController#move(java.lang.Object, int)
     */
    public boolean move(Object obj, int position) {
        return false;
    }

    /**
     * @see shag.list.controller.UsefulListController#getListTitle()
     */
    public String getListTitle() {
        return "";
    }

    /**
     * @see shag.list.controller.UsefulListController#getListColumnWidths()
     */
    public int[] getListColumnWidths() {
        return null;
    }

    /**
     * @see shag.list.controller.UsefulListController#isListMultiSelectable()
     */
    public boolean isListMultiSelectable() {
        return true;
    }

    /**
     * @see shag.list.controller.UsefulListController#getLastTouched()
     */
    public Object getLastTouched() {
        return null;
    }

    /**
     * @see shag.list.controller.UsefulListController#getListCellEditors()
     */
    public TableCellEditor[] getListCellEditors() {
        return null;
    }

    /**
     * @see shag.list.controller.UsefulListController#getListCellRenderers()
     */
    public TableCellRenderer[] getListCellRenderers() {
        return null;
    }

    /**
     * @see shag.list.controller.UsefulListController#getMenuActions()
     */
    public AbstractListAction[] getMenuActions() {
        return null;
    }
}
