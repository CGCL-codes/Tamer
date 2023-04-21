package org.jtell.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jtell.EventChannel;
import org.jtell.JTellContext;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * <p>
 * <code>EventChannelFactoryBean</code> is ...
 * </p>
 */
public class EventChannelFactoryBean extends AbstractFactoryBean {

    private static Log LOG = LogFactory.getLog(EventChannelFactoryBean.class);

    /**
     * <p>
     * The JTell context from which to create the channel. 
     * </p>
     */
    private JTellContext m_context;

    /**
     * <p>
     * The parent channel from which to create the channel. 
     * </p>
     */
    private EventChannel m_parentChannel;

    /**
     * <p>
     * Construct a {@link EventChannelFactoryBean} instance.
     * </p>
     */
    public EventChannelFactoryBean() {
        super();
    }

    public void afterPropertiesSet() throws Exception {
        if (null != m_context && null != m_parentChannel) {
            throw new IllegalArgumentException("The [context] and [parentChannel] properties cannot be used together.");
        }
        super.afterPropertiesSet();
    }

    public Class getObjectType() {
        return EventChannel.class;
    }

    /**
     * <p>
     * Set the context through which a new top level channel will be created.
     * </p>
     *
     * @param context the context.
     */
    public void setContext(final JTellContext context) {
        m_context = context;
    }

    /**
     * <p>
     * Set the parent channel for which a new child channel will be created.
     * </p>
     *
     * @param parentChannel the parent channel.
     */
    public void setParentChannel(final EventChannel parentChannel) {
        m_parentChannel = parentChannel;
    }

    protected Object createInstance() throws Exception {
        final EventChannel result;
        if (null != m_parentChannel) {
            result = m_parentChannel.createChildChannel();
        } else {
            if (null == m_context) {
                m_context = ContextUtils.createDefaultContext();
            }
            result = m_context.createChannel();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Returning channel [%s].", result));
        }
        return result;
    }
}
