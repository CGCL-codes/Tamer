package com.googlecode.beanfiles;

import java.util.Collection;
import java.util.Map;
import com.googlecode.beanfiles.translators.PropertyTranslator;

/**
 * Implementations of this interface are expected to produce translator maps for mapping bean properties to property translators.
 * 
 * Implementations are expected to set column indexes while constructing default translators, or cloning custom translators.
 * 
 */
public interface TranslatorsMapFactory {

    /**
     * Build an ordered map based on the heading values provided by the input. Input is likely Strings
     * 
     * @param <T>
     *            The type of object being produced by an Iterator.
     * @param input
     * @param targetFactory
     * @return
     */
    public <T> Map<String, ? extends PropertyTranslator> buildMap(Collection<Object> input, TargetFactory<T> targetFactory);

    /**
     * Overrides default translators. 
     */
    public void setCustomTranslators(Map<String, ? extends PropertyTranslator> translators);
}
