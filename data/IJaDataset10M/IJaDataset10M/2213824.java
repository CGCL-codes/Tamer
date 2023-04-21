package net.sf.extcos.internal;

import static net.sf.extcos.util.StringUtils.append;
import java.util.Set;
import net.sf.extcos.classgeneration.ClassGenerationListener;
import net.sf.extcos.filter.ImmediateConnector;
import net.sf.extcos.resource.Resource;
import net.sf.extcos.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmediateConnectorImpl implements ImmediateConnector {

    private static Logger logger = LoggerFactory.getLogger(ImmediateConnectorImpl.class);

    private Set<Class<?>> receivingSet;

    private Set<Resource> filtered;

    @Override
    public void setReceivingSet(final Set<Class<?>> receivingSet) {
        try {
            Assert.notNull(receivingSet, IllegalArgumentException.class, "receivingSet must not be null");
            this.receivingSet = receivingSet;
            if (logger.isTraceEnabled()) {
                logger.trace("successfully set receivingSet");
            }
        } catch (IllegalArgumentException e) {
            logger.debug("couldn't set receivingSet", e);
        }
    }

    @Override
    public void setFilteredRegistry(final Set<Resource> filtered) {
        try {
            Assert.notNull(filtered, IllegalArgumentException.class, "filtered must not be null");
            this.filtered = filtered;
            if (logger.isTraceEnabled()) {
                logger.trace("successfully set filtered registry");
            }
        } catch (IllegalArgumentException e) {
            logger.debug("couldn't set filtered registry", e);
        }
    }

    @Override
    public void connect(final Resource resource) {
        if (receivingSet == null) {
            logger.debug("can't connect: receivingSet is not set");
            return;
        }
        resource.addClassGenerationListener(new ClassGenerationListener() {

            @SuppressWarnings("hiding")
            private final Logger logger = LoggerFactory.getLogger("ClassGenerationListener");

            @Override
            public <T> void classGenerated(final Class<? extends T> clazz) {
                if (clazz == null) {
                    return;
                }
                receivingSet.add(clazz);
                if (logger.isTraceEnabled()) {
                    logger.trace(append("successfully added generated ", clazz));
                }
            }
        });
        filtered.add(resource);
        if (logger.isTraceEnabled()) {
            logger.trace(append("successfully connected resource ", resource));
        }
    }

    @Override
    public Set<Class<?>> getReceivingSet() {
        return receivingSet;
    }
}
