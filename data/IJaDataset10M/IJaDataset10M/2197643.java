package wesodi.logic.manage.interfaces;

import java.util.List;
import wesodi.entities.persi.AccessPoint;
import wesodi.entities.persi.AvailableProduct;

/**
 * @author Maria Krieg
 * @date 22.03.2009
 * 
 */
public interface AvailableProductManagement {

    /**
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_NEW_PRODUCT}
	 * <i>(required)</i></li>
	 * </ul>
	 */
    public static final String METH_CREATE_AVAILABLE_RPODUCT = "createAvailableProduct";

    /**
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_PRODUCT_ID}
	 * <i>(required)</i></li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_PATH_TO_PRODUCT}</li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_NEW_REPOSITORY}</li>
	 ** 
	 * <li>{@link AvailableProductManagement#REQ_KEY_NEW_PRODUCT_NAME}</li>
	 * </ul>
	 */
    public static final String METH_EDIT_AVAILABLE_PRODUCT = "editAvailableProduct";

    /**
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_PRODUCT_ID}
	 * <i>(required)</i></li>
	 * </ul>
	 */
    public static final String METH_DELETE_AVAILABLE_PRODUCT = "deleteAvailableProduct";

    /**
	 * use DistributionPointManagement.editDistributedProduct for this by
	 * editing the state of a distributed product
	 * 
	 * 
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_PRODUCT_ID}
	 * <i>(required)</i></li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_SHOP_ID} <i>(required)</i></li>
	 * </ul>
	 */
    @Deprecated
    public static final String METH_AUTHORIZE_SHOP = "authorizeShop";

    /**
	 * * use DistributionPointManagement.editDistributedProduct for this by
	 * editing the state of a distributed product
	 * 
	 * 
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_PRODUCT_ID}
	 * <i>(required)</i></li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_SHOP_ID} <i>(required)</i></li>
	 * </ul>
	 */
    @Deprecated
    public static final String METH_REMOVE_SHOP_AUTHORISATION = "removeShopAuthorization";

    /**
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_ALL}</i></li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_MY}</i></li>
	 * </ul>
	 * 
	 * Return values:
	 * <ul>
	 * <li>{@link AvailableProductManagement#RESP_KEY_PRODUCT_LIST}</li>
	 * </ul>
	 * 
	 * Beschreibung: Liefert eine Liste mit "AvailableProduct"-Objekten. Wenn
	 * der Parameter REQ_KEY_ALL oder der Parameter REQ_KEY_MY angegeben wurde,
	 * werden die Produkte, die dem angemeldeten Benutzer geh�ren bzw. alle
	 * verf�gbaren Produkte zur�ckgegeben. Wenn keiner der beiden Parameter
	 * angeben wurde, wirft die Methode eine {@link IllegalArgumentException}.
	 * 
	 * Description:
	 */
    public static final String METH_GET_AVAILABLE_PRODUCTS = "getAvailableProducts";

    /**
	 * Parameters:
	 * <ul>
	 * <li>{@link AvailableProductManagement#REQ_KEY_ALL}</i></li>
	 * <li>{@link AvailableProductManagement#REQ_KEY_MY}</i></li>
	 * </ul>
	 * 
	 * Return values:
	 * <ul>
	 * <li>{@link AvailableProductManagement#RESP_KEY_ARCHIVES}</li>
	 * </ul>
	 * 
	 * TODO f�r Sarah: hab die Parameter ge�ndert (mkg)
	 */
    public static final String METH_GET_ARCHIVES = "getArchives";

    /**
	 * Value is instance of {@link AvailableProduct}
	 */
    public static final String REQ_KEY_NEW_PRODUCT = "newProduct";

    /**
	 * Value is instance of {@link Long}
	 */
    public static final String REQ_KEY_PRODUCT_ID = "productId";

    /**
	 * Value is instance of {@link Long}
	 */
    public static final String REQ_KEY_NEW_PRODUCT_NAME = "newProductName";

    /**
	 * Value is instance of {@link String}
	 */
    public static final String REQ_KEY_PATH_TO_PRODUCT = "pathToProduct";

    /**
	 * Value is instance of {@link Long} which is the id of the selected access
	 * point
	 */
    public static final String REQ_KEY_NEW_REPOSITORY = "newRepository";

    /**
	 * Value is instance of {@link Long}
	 */
    public static final String REQ_KEY_SHOP_ID = "shopId";

    /**
	 * Value has to be not null <br /><br /> This parameter can be used with all
	 * methods, that return a collection. It indicated that the returned
	 * collection has to include all available entities.
	 */
    public static final String REQ_KEY_ALL = "all";

    /**
	 * Value has to be not null <br /><br /> This parameter can be used with all
	 * methods, that return a collection. It indicated, that the returned
	 * collection has to include just entities, that belong to the current user.
	 */
    public static final String REQ_KEY_MY = "my";

    /**
	 * Value is instance of {@link List}<{@link AccessPoint}>
	 */
    public static final String RESP_KEY_ARCHIVES = "archives";

    /**
	 * Value is instance of {@link AvailableProduct}
	 */
    public static final String RESP_KEY_PRODUCT = "product";

    /**
	 * Value is instance of {@link List}<{@link AvailableProduct}>
	 */
    public static final String RESP_KEY_PRODUCT_LIST = "priductList";
}
