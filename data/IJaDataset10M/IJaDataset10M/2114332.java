package net.sf.bacchus.spring.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.util.ExecutionContextUserSupport;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.ClassUtils;

/**
 * Support class for {@link ItemStream}s that may be beans. Provides empty
 * implementations of the {@code ItemStream} methods, intelligent strategies to
 * name the item stream, an {@link ExecutionContextUserSupport} for the stream,
 * and a {@link Log} with the same name as the item stream.
 */
public abstract class AbstractLoggingBeanNameAwareItemStream implements ItemStream, BeanNameAware {

    /** manage execution context keys. */
    private ExecutionContextUserSupport executionContextUserSupport;

    /** a log with the same name as the item stream. */
    private Log streamLog;

    /** Creates an unnamed {@link ItemStream}. */
    public AbstractLoggingBeanNameAwareItemStream() {
    }

    /**
     * Creates a named {@link ItemStream}.
     * @param name the name of the item stream.
     */
    public AbstractLoggingBeanNameAwareItemStream(final String name) {
        this.executionContextUserSupport = new ExecutionContextUserSupport(name);
    }

    /**
     * Sets the name of this item stream.
     * @param name the name of this item stream.
     */
    public void setName(final String name) {
        if (this.executionContextUserSupport == null) this.executionContextUserSupport = new ExecutionContextUserSupport(name); else this.executionContextUserSupport.setName(name);
        this.streamLog = null;
    }

    /**
     * Sets the name of this bean. If the bean name is set and is not generated,
     * it is used as the default name of this item stream.
     * @param name {@inheritDoc}
     * @see #setName(String)
     * @see BeanFactoryUtils#isGeneratedBeanName(String)
     */
    @Override
    public void setBeanName(final String name) {
        if (this.executionContextUserSupport == null && !BeanFactoryUtils.isGeneratedBeanName(name)) setName(name);
    }

    /**
     * Empty implementation of {@link ItemStream#open(ExecutionContext)}.
     * <p>
     * Overriding subclasses should consider using {@link #getStreamKey(String)}
     * to generate keys for values in the execution context.
     * </p>
     * @param executionContext {@inheritDoc}
     * @throws ItemStreamException Never.
     */
    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
    }

    /**
     * Empty implementation of {@link ItemStream#update(ExecutionContext)}.
     * <p>
     * Overriding subclasses should consider using {@link #getStreamKey(String)}
     * to generate keys for values in the execution context.
     * </p>
     * @param executionContext {@inheritDoc}
     * @throws ItemStreamException Never.
     */
    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {
    }

    /**
     * Empty implementation of {@link ItemStream#close()} .
     * @throws ItemStreamException Never.
     */
    @Override
    public void close() throws ItemStreamException {
    }

    /**
     * Generates a name for an object prefixed with the name of this
     * {@link ItemStream}. Use this method to create keys for elements in an
     * {@link ExecutionContext}. The name is generated as follows:
     * <ol>
     * <li>An explicit name set by {@link #setName(String)}</li>
     * <li>An explicit name passed to
     * {@link #AbstractLoggingBeanNameAwareItemStream(String) the constructor}</li>
     * <li>The bean name passed to {@link #setBeanName(String)}.</li>
     * <li>The short name of this class.</li>
     * </ol>
     * @param name the key name for the item.
     * @return the key name prefixed with the name of the item stream.
     */
    protected String getStreamKey(final String name) {
        if (this.executionContextUserSupport == null) this.executionContextUserSupport = new ExecutionContextUserSupport(ClassUtils.getShortName(getClass()));
        return this.executionContextUserSupport.getKey(name);
    }

    /**
     * Gets a {@link Log} with the same name as the item stream.
     * @return a log with the same name as the item stream.
     */
    protected Log getStreamLog() {
        if (this.streamLog == null) if (this.executionContextUserSupport == null) this.streamLog = LogFactory.getLog(getClass()); else {
            final String name = this.executionContextUserSupport.getKey("");
            this.streamLog = LogFactory.getLog(name.substring(0, name.length() - 1));
        }
        return this.streamLog;
    }
}
