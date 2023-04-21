package net.sf.karatasi.databaseoperations.edit;

import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.karatasi.database.Category;
import net.sf.karatasi.database.DBValueException;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.database.DatabaseChangeEvent;
import net.sf.karatasi.database.DatabaseChangeListener;
import net.sf.karatasi.database.IndexIterator;
import net.sf.karatasi.viewmodels.CategoryListEntryModel;
import net.sf.karatasi.viewmodels.CategoryListModel;
import net.sf.karatasi.viewmodels.SelectedListModel;
import net.sf.karatasi.views.CategoryListCellRenderer;
import net.sf.karatasi.views.OrderedListCellRenderer;
import org.jetbrains.annotations.NotNull;

/** The container for the list of categories of a database.
 * This container ignores the list modes.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class CategoriesOfDatabaseListContainer extends ListContainer implements ChangeListener, DatabaseChangeListener {

    /** The database providing the categories. */
    @NotNull
    private final Database database;

    /** The data model for the list. */
    protected CategoryListModel categoryListModel;

    /** Constructor. */
    public CategoriesOfDatabaseListContainer(@NotNull final Database database) {
        super();
        this.database = database;
        this.categoryListModel = new CategoryListModel();
        this.database.addDatabaseChangeListener(this, Database.DATABASE_CHANGED_CATEGORY_COUNT);
        doUpdateCategoryList();
    }

    /** Close the container.
     * This method frees resources, and de-registers any notifications.
     */
    @Override
    public void close() {
        database.removeDatabaseChangeListener(this);
        for (int n = 0; n < categoryListModel.size(); n++) {
            ((CategoryListEntryModel) categoryListModel.get(n)).getCategory().removeDatabaseChangeListener(this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CategoriesOfDatabaseListContainer;
    }

    /** This category list container stays on the top of the stack.
     * @return true if it stays on the top of the stack.
     */
    @Override
    public boolean isSticky() {
        return true;
    }

    /**  {@inheritDoc} */
    @Override
    public boolean supportsAdding() {
        return true;
    }

    /**  {@inheritDoc} */
    @Override
    public boolean supportsDeleting() {
        return true;
    }

    /**  {@inheritDoc} */
    @Override
    public boolean supportsOrdering() {
        return true;
    }

    /**  {@inheritDoc} */
    @Override
    public SelectedListModel getDataModel() {
        return categoryListModel;
    }

    /**  {@inheritDoc} */
    @Override
    public String getHeadline() {
        return "categories";
    }

    /**  {@inheritDoc} */
    @Override
    public String getDescription() {
        return "list of categories";
    }

    /**  {@inheritDoc} */
    @Override
    protected CategoryDetailController getControllerForListElement(@NotNull final DatabaseEditController databaseEditController, final int index) {
        categoryListModel.setSelection(index);
        final CategoryListEntryModel listEntry = (CategoryListEntryModel) categoryListModel.get(index);
        assert listEntry != null;
        CategoryDetailController newController = null;
        try {
            newController = new CategoryDetailController(databaseEditController, database, listEntry.getId());
        } catch (final Exception e) {
            return null;
        }
        return newController;
    }

    /**  {@inheritDoc} */
    @Override
    protected CategoryDetailController getControllerWithNewListElement(@NotNull final DatabaseEditController databaseEditController) {
        try {
            database.makeNewCategory();
        } catch (final DBValueException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("creating a new category failed: " + e.toString());
            return null;
        } catch (final SQLException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("creating a new category failed: " + e.toString());
            return null;
        }
        final int indexOfNewCategory = categoryListModel.size() - 1;
        categoryListModel.setSelection(indexOfNewCategory);
        return getControllerForListElement(databaseEditController, indexOfNewCategory);
    }

    /**  {@inheritDoc} */
    @Override
    protected DetailController deleteListElementAndGetAnotherController(@NotNull final DatabaseEditController databaseEditController) {
        if (categoryListModel.getSelectedCategoryId() == 0) {
            return null;
        }
        try {
            final Category deletedCategory = database.getCategoryById(categoryListModel.getSelectedCategoryId());
            if (deletedCategory.getCardCount() == 0) {
                deletedCategory.delete();
            } else {
                final Category defaultCategory = database.makeDefaultCategory();
                if (defaultCategory.getCategoryId() != categoryListModel.getSelectedCategoryId()) {
                    deletedCategory.mergeTo(defaultCategory);
                } else {
                    defaultCategory.delete();
                }
            }
        } catch (final DBValueException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("deleting category " + categoryListModel.getSelectedCategoryId() + " failed: " + e.toString());
            return null;
        } catch (final SQLException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("deleting category " + categoryListModel.getSelectedCategoryId() + " failed: " + e.toString());
            return null;
        }
        doUpdateCategoryList();
        int newSelection = categoryListModel.getSelection() - 1;
        if (newSelection < 0 && categoryListModel.size() > 0) {
            newSelection = 0;
        }
        categoryListModel.setSelection(newSelection);
        if (categoryListModel.getSelection() >= 0) {
            return getControllerForListElement(databaseEditController, categoryListModel.getSelection());
        }
        return null;
    }

    /**  {@inheritDoc} */
    @Override
    boolean moveRow(final int index, final int offset) {
        if (offset == 0) {
            return false;
        }
        final int newIndex = index + offset;
        if (newIndex < 0 || newIndex >= categoryListModel.size()) {
            return false;
        }
        categoryListModel.add(newIndex, categoryListModel.remove(index));
        for (int n = 0; n < categoryListModel.size(); n++) {
            try {
                ((CategoryListEntryModel) categoryListModel.get(n)).getCategory().setOrderingNumber(n);
            } catch (final SQLException e) {
                Logger.getLogger("net.sf.japi.net.rest").warning("setting of category order failed: " + e.toString());
            }
        }
        return true;
    }

    /**  {@inheritDoc} */
    @Override
    public ListCellRenderer getSideboardListCellRenderer() {
        return new OrderedListCellRenderer(new CategoryListCellRenderer());
    }

    /** Update the data model of the category list from the database. */
    private void doUpdateCategoryList() {
        try {
            final IndexIterator categoryIterator = database.getAllCategoryIndices();
            int listElementCount = 0;
            while (categoryIterator.hasNext() || listElementCount < categoryListModel.size()) {
                CategoryListEntryModel categoryEntry = null;
                if (categoryIterator.hasNext()) {
                    final int categoryId = categoryIterator.next().getIdxValue();
                    categoryEntry = new CategoryListEntryModel(database.getCategoryById(categoryId));
                }
                if (listElementCount < categoryListModel.size()) {
                    if (categoryEntry != null) {
                        ((CategoryListEntryModel) categoryListModel.get(listElementCount)).getCategory().removeDatabaseChangeListener(this);
                        categoryListModel.set(listElementCount, categoryEntry);
                        categoryEntry.getCategory().addDatabaseChangeListener(this);
                    } else {
                        ((CategoryListEntryModel) categoryListModel.get(listElementCount)).getCategory().removeDatabaseChangeListener(this);
                        categoryListModel.remove(listElementCount);
                    }
                } else {
                    categoryListModel.addElement(categoryEntry);
                    categoryEntry.getCategory().addDatabaseChangeListener(this);
                }
                listElementCount++;
            }
        } catch (final SQLException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("update of category list failed: " + e.toString());
        } catch (final DBValueException e) {
            Logger.getLogger("net.sf.japi.net.rest").warning("update of category list failed: " + e.toString());
        }
        if (categoryListModel.getSelectedCategoryId() > 0) {
            for (int n = 0; n < categoryListModel.size(); n++) {
                final int categoryId = ((CategoryListEntryModel) categoryListModel.get(n)).getId();
                if (categoryId == categoryListModel.getSelectedCategoryId()) {
                    categoryListModel.setSelection(n);
                }
            }
        }
    }

    /** This is the notification that the category list has changed.
     * It is invoked when the target of the listener has changed its state.
     * This event may only be called from inside the SWING thread!
     *
     * @param event a ChangeEvent object
     */
    public void stateChanged(final ChangeEvent event) {
        doUpdateCategoryList();
    }

    /** This notification is rised by the database if the category list has changed.
     * @param event a ChangeEvent object
     */
    public void databaseStateChanged(final DatabaseChangeEvent event) {
        if (event.getSource() == database) {
            if ((event.getChangeMask() & Database.DATABASE_CHANGED_CATEGORY_COUNT) != 0) {
                doUpdateCategoryList();
            }
        } else {
            for (int n = 0; n < categoryListModel.size(); n++) {
                if (((CategoryListEntryModel) categoryListModel.get(n)).getCategory() == event.getSource()) {
                    categoryListModel.set(n, categoryListModel.get(n));
                }
            }
        }
    }
}
