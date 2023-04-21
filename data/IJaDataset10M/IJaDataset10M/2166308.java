package org.zeroexchange.web.navigation.target.key;

import org.zeroexchange.common.processors.AbstractDataProcessorsRegistry;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.web.navigation.target.Target;

/**
 * THe default implementation of the TargetKeyFactory.
 * 
 * @author black
 */
public class DefaultTargetKeyFactory extends AbstractDataProcessorsRegistry<TargetKeyProvider> implements TargetKeyFactory {

    /**
     * Constructor.
     */
    public DefaultTargetKeyFactory() {
        super(TargetKeyProvider.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTargetKey(Target target) {
        TargetKeyProvider provider = getProcessor(target.getClass());
        if (provider == null) {
            throw new BusinessLogicException("Cannot find key provider for the target of class '" + target.getClass().getName() + "'");
        }
        return provider.getTargetKey(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMenuLeafKey(Target target) {
        TargetKeyProvider provider = getProcessor(target.getClass());
        if (provider == null) {
            throw new BusinessLogicException("Cannot find key provider for the target of class '" + target.getClass().getName() + "'");
        }
        return provider.getMenuLeafKey(target);
    }
}
