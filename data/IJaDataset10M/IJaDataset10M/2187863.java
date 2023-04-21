package br.com.wepa.webapps.orca.logica.persistencia.search;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import br.com.wepa.webapps.search.Order;
import br.com.wepa.webapps.search.Search;
import br.com.wepa.webapps.search.SearchResult;

public class BeanSearch<T> extends DefaultSearch<T> implements Search {

    private static String[] EMPTY_PARAM = {};

    private T beanInstance;

    private Example example;

    private String[] excludeProperty;

    private List<Order> orderPropertyList = new LinkedList<Order>();

    /**
	 * Creates an empty search
	 * @return
	 */
    public static BeanSearch createEmpty() {
        return new BeanSearch();
    }

    public BeanSearch() {
        this(null, EMPTY_PARAM);
    }

    public BeanSearch(T bean, String... excludeProperty) {
        setBeanInstance(bean);
        setExcludeProperty(excludeProperty);
    }

    @SuppressWarnings("unchecked")
    public SearchResult<T> find() throws PersistenceException {
        if (isFindEnabled()) {
            setSearchResult(getPersistence().searchBean(this));
        }
        return getSearchResult();
    }

    public Object getBeanInstance() {
        return beanInstance;
    }

    public void setBeanInstance(T bean) {
        this.beanInstance = bean;
    }

    public String[] getExcludeProperty() {
        return excludeProperty;
    }

    public void setExcludeProperty(String[] excludeProperty) {
        this.excludeProperty = excludeProperty;
    }

    /**
	 * Excludes null values
	 *
	 */
    public void excludeNone() {
        getExample().excludeNone();
    }

    /**
	 * Exclude zero values
	 *
	 */
    public void excludeZeroes() {
        getExample().excludeZeroes();
    }

    /**
	 *  Ignores the case sensitive in strings
	 *
	 */
    public void ignoreCase() {
        getExample().ignoreCase();
    }

    /**
	 *  In string fields, tests if the value to search match with the end of the string
	 */
    public void enableLikeAny() {
        getExample().enableLike(MatchMode.ANYWHERE);
    }

    /**
	 * In string fields, tests if the value to search matchs with the start of the string
	 */
    public void enableLikeStart() {
        getExample().enableLike(MatchMode.START);
    }

    /**
	 *  In string fields, tests if the value to search matchs with the end of the string
	 */
    public void enableLikeEnd() {
        getExample().enableLike(MatchMode.END);
    }

    /**
	 * In string fields, tests if the value to search matchs exactly with the string
	 *
	 */
    public void enableLikeExact() {
        getExample().enableLike(MatchMode.EXACT);
    }

    /**
	 * Adds an order property
	 * @param property
	 * @param ascending, if false decreasing 
	 */
    public void addOrder(String property, boolean ascending) {
        orderPropertyList.add(new Order(property, ascending));
    }

    public Order getOrder(int i) {
        return orderPropertyList.get(i);
    }

    public List<Order> getOrderPropertyList() {
        return orderPropertyList;
    }

    public void setOrderPropertyList(List<Order> list) {
        orderPropertyList = list;
    }

    public Example getExample() {
        if (example == null) {
            setExample(Example.create(beanInstance));
        }
        return example;
    }

    private void setExample(Example example) {
        this.example = example;
    }
}
