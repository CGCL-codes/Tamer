package petcatalog.model.impl;

import petcatalog.model.Item;

/**
 * The model implementation for the Item service. Represents a row in the &quot;PETS_ITEM&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link petcatalog.model.Item} interface.
 * </p>
 *
 * <p>
 * Never reference this class directly. All methods that expect a item model instance should use the {@link Item} interface instead.
 * </p>
 */
public class ItemImpl extends ItemModelImpl implements Item {

    public ItemImpl() {
    }
}
