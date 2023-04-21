package org.wicketrad;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Convenience abstract class that implements IFilterDataProvider and
 * ISortableDataProvider
 * 
 */
public abstract class AbstractFilterDataProvider extends SortableDataProvider implements IFilterDataProvider {

    private FilterCriteria filterCriteria;

    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(FilterCriteria criteria) {
        filterCriteria = criteria;
    }

    public IModel model(Object modelObject) {
        return new BeanDetachableModel(modelObject);
    }

    public void detach() {
    }
}
