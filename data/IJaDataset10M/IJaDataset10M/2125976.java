package org.josef.web.jsf.beans.demo.crud.jpa;

import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.josef.demo.jpa.Car;
import org.josef.demo.jpa.CarSearchCriteria;
import org.josef.util.CColor;
import org.josef.web.jsf.bean.crud.AbstractCrudSingleEntityUsingEntityManagerBean;
import org.josef.web.jsf.util.JsfExpressionUtil;
import org.josef.web.jsf.util.JsfSelectItemsFromCodeDescriptionsUtil;

/**
 * Car Bean, demonstrating maintenance of a 1 to 1 relationship, directly
 * using an EntityManager.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3407 $
 */
@ManagedBean
@SessionScoped
public class CarBean extends AbstractCrudSingleEntityUsingEntityManagerBean<Car> {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = -2201079970244106025L;

    /**
     * The search criteria.
     */
    private final CarSearchCriteria searchCriteria = new CarSearchCriteria();

    /**
     * Public default constructor to make this a bean.
     */
    public CarBean() {
    }

    /**
     * Fetches all Cars and sorts them on object ID.
     */
    @PostConstruct
    public void init() {
        getSearchDelegate().setSortColumn("objectId");
        searchItemsListener(null);
    }

    /**
     * Gets the type of the Entity to maintain.
     * @return The type of the Entity to maintain.
     */
    @Override
    public Class<Car> getItemType() {
        return Car.class;
    }

    /**
     * Gets the Car.
     * @return The Car.
     */
    public Car getCar() {
        return getDetailDelegate().getItem();
    }

    /**
     * Gets the search criteria.
     * @return The search criteria.
     */
    @Override
    public CarSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Gets all {@link CColor CColors} as {@link SelectItem SelectItems}.
     * @return All {@link CColor CColors} as {@link SelectItem SelectItems}.
     */
    public List<SelectItem> getColorsAsSelectItems() {
        final Locale locale = JsfExpressionUtil.evaluateExpressionGet("#{userSettingsBean.locale}", Locale.class);
        return JsfSelectItemsFromCodeDescriptionsUtil.codeDescriptionsToSelectItems(CColor.getCodeDescriptions(locale));
    }
}
