package org.databene.webdecs.util;

import java.io.Closeable;
import org.databene.commons.IOUtil;
import org.databene.webdecs.DataIterator;
import org.databene.webdecs.DataSource;

/**
 * Provides {@link DataSource}-style access to a Java-SDK-{@link Iterable}.<br/><br/>
 * Created: 24.07.2011 11:07:04
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class DataSourceFromIterable<E> extends AbstractDataSource<E> {

    protected Iterable<E> source;

    public DataSourceFromIterable(Iterable<E> source, Class<E> type) {
        super(type);
        this.source = source;
    }

    public DataIterator<E> iterator() {
        return new DataIteratorFromJavaIterator<E>(source.iterator(), type);
    }

    @Override
    public void close() {
        if (source instanceof Closeable) IOUtil.close((Closeable) source);
        super.close();
    }
}
