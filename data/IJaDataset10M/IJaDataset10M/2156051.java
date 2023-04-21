package org.jtools.tef;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack </a>
 */
public class DelegatedTEFModelFactory<I> implements TEFModelFactory<I> {

    private final TEFModelFactory<I> delegator;

    public DelegatedTEFModelFactory(TEFModelFactory<I> delegator) {
        if (delegator == null) throw new NullPointerException("delegator");
        this.delegator = delegator;
    }

    public DelegatedTEFModelFactory(Class modelClass) {
        this(modelClass, null);
    }

    public DelegatedTEFModelFactory(Class modelClass, Class instrumentatorClass) {
        this(new SimpleTEFModelFactory<I>(modelClass, instrumentatorClass));
    }

    public TEFModel createTEFModel(TEFGenerator generator, Object application, I instrumentator) {
        return delegator.createTEFModel(generator, application, instrumentator);
    }

    public I getTEFModelConfiguration() {
        return delegator.getTEFModelConfiguration();
    }
}
