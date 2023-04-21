package org.illico.common.model.sort;

import org.illico.common.bean.BeanUtils;
import org.illico.common.model.bean.BeanModel;

public class BeanSortModel<T, P> extends AbstractMultipleSortModel<T, P, PropertySortInformation<P>> implements BeanModel {

    public BeanSortModel() {
    }

    public BeanSortModel(PropertySortInformation<P>... sortInformations) {
        super(sortInformations);
    }

    public BeanSortModel(BeanModel beanModel) {
        for (int i = 0; i < beanModel.getPropertyCount(); i++) {
            String property = beanModel.getProperty(i);
            addSortInformation(property).setSorted(false);
        }
    }

    public BeanSortModel(String... properties) {
        for (String property : properties) {
            addSortInformation(property);
        }
    }

    protected PropertySortInformation<P> createSortInformation() {
        return new PropertySortInformation<P>();
    }

    protected P getSortElement(T object, int index) {
        return (P) BeanUtils.getPropertyValue(object, getSortInformation(index).getProperty());
    }

    public PropertySortInformation<P> addSortInformation(String property) {
        PropertySortInformation<P> sortInformation = new PropertySortInformation<P>(property);
        addSortInformation(sortInformation);
        return sortInformation;
    }

    public PropertySortInformation<P> setSortInformation(String property) {
        clearSortInformations();
        return addSortInformation(property);
    }

    public String getProperty(int index) {
        return getSortInformation(index).getProperty();
    }

    public int getPropertyCount() {
        return getSortInformationsCount();
    }
}
