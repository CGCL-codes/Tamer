package dmeduc.weblink.url;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entities;
import org.modelibra.IDomainModel;
import org.modelibra.ISelector;
import org.modelibra.Oid;
import org.modelibra.PropertySelector;
import dmeduc.weblink.category.Category;

/**
 * Url generated entities. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-07
 */
public abstract class GenUrls extends Entities<Url> {

    private static final long serialVersionUID = 1171894920491L;

    private static Log log = LogFactory.getLog(GenUrls.class);

    private Category category;

    /**
	 * Constructs urls within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenUrls(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs urls for the category parent.
	 * 
	 * @param category
	 *            category
	 */
    public GenUrls(Category category) {
        this(category.getModel());
        setCategory(category);
    }

    /**
	 * Retrieves the url with a given oid. Null if not found.
	 * 
	 * @param oid
	 *            oid
	 * @return url
	 */
    public Url getUrl(Oid oid) {
        return retrieveByOid(oid);
    }

    /**
	 * Retrieves the url with a given oid unique number. Null if not found.
	 * 
	 * @param oidUniqueNumber
	 *            oid unique number
	 * @return url
	 */
    public Url getUrl(Long oidUniqueNumber) {
        return getUrl(new Oid(oidUniqueNumber));
    }

    /**
	 * Retrieves the first url whose property with a property code is equal to a
	 * property object. Null if not found.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return url
	 */
    public Url getUrl(String propertyCode, Object property) {
        return retrieveByProperty(propertyCode, property);
    }

    /**
	 * Selects urls whose property with a property code is equal to a property
	 * object. Returns empty entities if no selection.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return urls
	 */
    public Urls getUrls(String propertyCode, Object property) {
        return (Urls) selectByProperty(propertyCode, property);
    }

    /**
	 * Gets urls ordered by a property.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered urls
	 */
    public Urls getUrls(String propertyCode, boolean ascending) {
        return (Urls) orderByProperty(propertyCode, ascending);
    }

    /**
	 * Gets urls selected by a selector. Returns empty urls if there are no urls
	 * that satisfy the selector.
	 * 
	 * @param selector
	 *            selector
	 * @return selected urls
	 */
    public Urls getUrls(ISelector selector) {
        return (Urls) selectBySelector(selector);
    }

    /**
	 * Gets urls ordered by a composite comparator.
	 * 
	 * @param comparator
	 *            comparator
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered urls
	 */
    public Urls getUrls(Comparator comparator, boolean ascending) {
        return (Urls) orderByComparator(comparator, ascending);
    }

    /**
	 * Gets name url.
	 * 
	 * @param name
	 *            name
	 * @return name url
	 */
    public Url getNameUrl(String name) {
        PropertySelector propertySelector = new PropertySelector("name");
        propertySelector.defineEqual(name);
        List<Url> list = getUrls(propertySelector).getList();
        if (list.size() > 0) return list.iterator().next(); else return null;
    }

    /**
	 * Gets urls ordered by name.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered urls
	 */
    public Urls getUrlsOrderedByName(boolean ascending) {
        return getUrls("name", ascending);
    }

    /**
	 * Gets urls ordered by link.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered urls
	 */
    public Urls getUrlsOrderedByLink(boolean ascending) {
        return getUrls("link", ascending);
    }

    /**
	 * Sets category.
	 * 
	 * @param category
	 *            category
	 */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
	 * Gets category.
	 * 
	 * @return category
	 */
    public Category getCategory() {
        return category;
    }

    /**
	 * Creates url.
	 * 
	 * @param categoryParent
	 *            category parent
	 * @param name
	 *            name
	 * @param link
	 *            link
	 * @return url
	 */
    public Url createUrl(Category categoryParent, String name, String link) {
        Url url = new Url(getModel());
        url.setCategory(categoryParent);
        url.setName(name);
        url.setLink(link);
        if (!add(url)) {
            url = null;
        }
        return url;
    }
}
