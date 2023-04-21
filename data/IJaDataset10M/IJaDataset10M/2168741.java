package org.rapla.entities.configuration.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.rapla.components.util.iterator.ArrayIterator;
import org.rapla.components.util.iterator.NestedIterator;
import org.rapla.entities.EntityNotFoundException;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.internal.ClassificationFilterImpl;
import org.rapla.entities.storage.CannotExistWithoutTypeException;
import org.rapla.entities.storage.DynamicTypeDependant;
import org.rapla.entities.storage.EntityReferencer;
import org.rapla.entities.storage.EntityResolver;
import org.rapla.entities.storage.RefEntity;

public abstract class AbstractClassifiableFilter implements EntityReferencer, DynamicTypeDependant, Serializable {

    private static final long serialVersionUID = 1L;

    ClassificationFilter[] classificationFilters;

    AbstractClassifiableFilter() {
        classificationFilters = new ClassificationFilter[0];
    }

    public void resolveEntities(EntityResolver resolver) throws EntityNotFoundException {
        for (int i = 0; i < classificationFilters.length; i++) {
            ((EntityReferencer) classificationFilters[i]).resolveEntities(resolver);
        }
    }

    public boolean isRefering(RefEntity<?> object) {
        for (int i = 0; i < classificationFilters.length; i++) if (((ClassificationFilterImpl) classificationFilters[i]).isRefering(object)) return true;
        return false;
    }

    public Iterator getReferences() {
        Iterator classificatonFilterIterator = new ArrayIterator(classificationFilters);
        return new NestedIterator(classificatonFilterIterator) {

            public Iterator getNestedIterator(Object obj) {
                return ((ClassificationFilterImpl) obj).getReferences();
            }
        };
    }

    public void setClassificationFilter(ClassificationFilter[] classificationFilters) {
        if (classificationFilters != null) this.classificationFilters = classificationFilters; else this.classificationFilters = ClassificationFilter.CLASSIFICATIONFILTER_ARRAY;
    }

    public boolean needsChange(DynamicType type) {
        ClassificationFilter[] filters = getFilter();
        for (int i = 0; i < filters.length; i++) {
            ClassificationFilterImpl filter = (ClassificationFilterImpl) filters[i];
            if (filter.needsChange(type)) return true;
        }
        return false;
    }

    public void commitChange(DynamicType type) {
        ClassificationFilter[] filters = getFilter();
        for (int i = 0; i < filters.length; i++) {
            ClassificationFilterImpl filter = (ClassificationFilterImpl) filters[i];
            if (filter.getType().equals(type)) filter.commitChange(type);
        }
    }

    public void commitRemove(DynamicType type) throws CannotExistWithoutTypeException {
        ClassificationFilter[] filters = getFilter();
        List<ClassificationFilter> newFilter = new ArrayList(Arrays.asList(filters));
        for (Iterator<ClassificationFilter> f = newFilter.iterator(); f.hasNext(); ) {
            ClassificationFilter filter = f.next();
            if (filter.getType().equals(type)) {
                f.remove();
                break;
            }
        }
        classificationFilters = newFilter.toArray(ClassificationFilter.CLASSIFICATIONFILTER_ARRAY);
    }

    public ClassificationFilter[] getFilter() {
        return classificationFilters;
    }
}
