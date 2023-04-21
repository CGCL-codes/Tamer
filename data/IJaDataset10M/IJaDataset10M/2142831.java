package org.metastopheles;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A MetaDataObject contains data about a particular aspect of a bean class.
 * @author James Carman
 * @since 1.0
 */
public abstract class MetaDataObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<FacetKey<?>, Object> facetMap = new HashMap<FacetKey<?>, Object>();

    /**
     * Returns the default source for annotation data for this MetaDataObject.
     * @return the default source for annotation data for this MetaDataObject
     */
    protected abstract AnnotatedElement getDefaultAnnotationSource();

    /**
     * Retrieves a facet value associated with this MetaDataObject.
     * @param key the facet key
     * @return the facet value
     */
    @SuppressWarnings("unchecked")
    public <T> T getFacet(FacetKey<T> key) {
        return (T) facetMap.get(key);
    }

    /**
     * Returns the annotation associated with this MetaDataObject's default annotation source.
     *
     * @param annotationType the annotation type
     * @return the annotation
     * @see #getDefaultAnnotationSource()
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return getDefaultAnnotationSource().getAnnotation(annotationType);
    }

    /**
     * Returns a snapshot of the keys of the facets currently associated with this MetaDataObject.
     * @return a snapshot of the keys of the facets currently associated with this MetaDataObject
     */
    public Set<FacetKey<?>> getFacetKeys() {
        return new HashSet<FacetKey<?>>(facetMap.keySet());
    }

    /**
     * Associates a facet with this MetaDataObject.
     * @param key the facet key
     * @param value the facet value
     */
    public <T> void setFacet(FacetKey<T> key, T value) {
        facetMap.put(key, value);
    }
}
