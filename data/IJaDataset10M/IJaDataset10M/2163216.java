package uk.co.ordnancesurvey.confluence.ui.itemlist;

import java.util.logging.Logger;
import uk.co.ordnancesurvey.confluence.IConfluenceEditorKit;
import uk.co.ordnancesurvey.confluence.ui.filter.DefaultFilterList;
import uk.co.ordnancesurvey.confluence.ui.filter.IFilter;
import uk.co.ordnancesurvey.confluence.ui.filter.IFilterList;
import uk.co.ordnancesurvey.confluence.ui.itemlist.item.ICflItemController;
import uk.co.ordnancesurvey.confluence.ui.itemlist.item.ICflItemModel;

/**
 * Extends {@link CflItemListController} by providing filtering methods based on
 * the item model. Subclasses activate the filter by calling method
 * {@link #isAllowedByFilter(ICflItemModel)}, if this method is not used, no
 * filter is created.
 * 
 * Subclasses can customize the behaviour of the filtering by overriding methods
 * {@link #createItemModelFilter()} and {@link #initItemModelFilter()}. For the
 * latter method subclasses can use method {@link #addItemModelFilter(IFilter)}
 * to add a new filter to the {@link #itemModelFilter}.
 * 
 * @author rdenaux
 * 
 * @param <ModelType>
 * @param <ViewType>
 * @param <ItemControllerType>
 * @param <ItemModelType>
 */
public abstract class CflItemListControllerWithFilter<ModelType extends ICflItemListModel<ItemModelType>, ViewType extends ICflItemListView<? extends ICflItemView<?>, ? extends ICflHeaderView<?>>, ItemControllerType extends ICflItemController<ItemModelType, ? extends ICflItemView<?>>, ItemModelType extends ICflItemModel<?>> extends CflItemListController<ModelType, ViewType, ItemControllerType, ItemModelType> {

    private static final Logger log = Logger.getLogger(CflItemListControllerWithFilter.class.getName());

    private IFilterList<ItemModelType> itemModelFilter;

    public CflItemListControllerWithFilter(IConfluenceEditorKit editorKit) {
        super(editorKit);
    }

    /**
	 * Returns whether the {@link #itemModelFilter} for this item list
	 * controller allows aItemModel.
	 * 
	 * @param aItemModel
	 * @return
	 */
    protected final boolean isAllowedByFilter(ItemModelType aItemModel) {
        boolean result = getItemModelFilter().isAllowed(aItemModel);
        log.fine("is allowed by filter " + getItemModelFilter().getFilterListSize() + " : " + result);
        return result;
    }

    private IFilterList<ItemModelType> getItemModelFilter() {
        if (itemModelFilter == null) {
            itemModelFilter = createItemModelFilter();
            initItemModelFilter();
        }
        return itemModelFilter;
    }

    /**
	 * Subclasses can override this method to initialise the
	 * {@link #itemModelFilter} for this item list controller. Use method
	 */
    protected void initItemModelFilter() {
    }

    /**
	 * Use this method to add aItemModelFilter to the {@link #itemModelFilter}
	 * 
	 * @param aItemModelFilter
	 */
    protected final void addItemModelFilter(IFilter<ItemModelType> aItemModelFilter) {
        log.fine("Add item model filter " + aItemModelFilter);
        assert itemModelFilter != null;
        itemModelFilter.addFilter(aItemModelFilter);
    }

    /**
	 * Creates the {@link #itemModelFilter}
	 * 
	 * @return
	 */
    protected IFilterList<ItemModelType> createItemModelFilter() {
        return new DefaultFilterList<ItemModelType>();
    }
}
