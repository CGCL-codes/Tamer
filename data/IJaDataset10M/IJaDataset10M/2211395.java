package no.ugland.utransprod.gui.model;

import java.util.List;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.Deviation;
import no.ugland.utransprod.model.Order;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * Interface for klasser som har kostnad
 * 
 * @author atle.brekka
 * 
 * @param <T>
 * @param <E>
 */
public interface ICostableModel<T, E> extends ModelInterface<E> {

    /**
	 * 
	 */
    public static final String PROPERTY_COSTS = "costList";

    /**
	 * 
	 */
    public static final String PROPERTY_ORDER = "order";

    /**
	 * 
	 */
    public static final String PROPERTY_DEVIATION = "deviation";

    /**
	 * 
	 */
    public static final String PROPERTY_ORDER_LINE_ARRAY_LIST_MODEL = "orderLineArrayListModel";

    /**
	 * 
	 */
    public static final String PROPERTY_ARTICLES = "articles";

    /**
	 * @return kostnader
	 */
    ArrayListModel getCostList();

    /**
	 * @return ordre
	 */
    Order getOrder();

    /**
	 * @return avvik
	 */
    Deviation getDeviation();

    /**
	 * @return ordrelinjer
	 */
    ArrayListModel getOrderLineArrayListModel();

    /**
	 * @return artikler
	 */
    List<ArticleType> getArticles();

    /**
	 * @return fornavn til kunde
	 */
    String getCustomerFirstName();

    /**
	 * @return etternavn til kunde
	 */
    String getCustomerLastName();

    /**
	 * @return leveringsadresse
	 */
    String getDeliveryAddress();

    /**
	 * @return postnummer
	 */
    String getPostalCode();

    /**
	 * @return poststed
	 */
    String getPostOffice();
}
