package org.databene.webdecs.util;

import org.databene.commons.Filter;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;

/**
 * {@link DataIterator} proxy which applies a {@link Filter} to the data provides by its source.<br/><br/>
 * Created: 24.07.2011 10:19:41
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class FilteringDataIterator<E> extends DataIteratorProxy<E> {

    protected Filter<E> filter;

    public FilteringDataIterator(DataIterator<E> source, Filter<E> filter) {
        super(source);
        this.filter = filter;
    }

    @Override
    public DataContainer<E> next(DataContainer<E> wrapper) {
        DataContainer<E> result;
        do {
            result = source.next(wrapper);
        } while (result != null && !filter.accept(result.getData()));
        return result;
    }
}
