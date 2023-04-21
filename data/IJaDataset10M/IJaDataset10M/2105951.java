package net.sf.doolin.app.sc.shared.factory;

import net.sf.doolin.app.sc.engine.ClientResponse;
import net.sf.doolin.app.sc.engine.ClientState;
import net.sf.doolin.app.sc.engine.EngineController;
import net.sf.doolin.app.sc.engine.InstanceFactory;
import net.sf.doolin.app.sc.engine.InstanceGenerator;
import net.sf.doolin.app.sc.engine.InstanceStore;
import net.sf.doolin.app.sc.engine.factory.LocalEngineFactory;
import net.sf.doolin.app.sc.shared.SharedLocalEngine;
import net.sf.doolin.app.sc.shared.SharingPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Factory for a shared engine.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <G>
 *            Instance generator type
 * @param <S>
 *            Instance type
 * @param <T>
 *            Instance state type
 * @param <P>
 *            Client response type
 */
public class SharedEngineFactory<G extends InstanceGenerator, S, T extends ClientState, P extends ClientResponse> extends LocalEngineFactory<G, S, T, P> {

    @Autowired
    private ApplicationContext applicationContext;

    private final SharingPreferences sharingPreferences;

    /**
	 * Constructor
	 * 
	 * @param instanceFactory
	 *            Instance factory
	 * @param instanceStore
	 *            Instance store
	 */
    public SharedEngineFactory(InstanceFactory<G, S, T, P> instanceFactory, InstanceStore<S> instanceStore, SharingPreferences sharingPreferences) {
        super(instanceFactory, instanceStore);
        this.sharingPreferences = sharingPreferences;
    }

    @Override
    public EngineController<G, T, P> createEngine(String name) {
        EngineController<G, T, P> localEngine = super.createEngine(name);
        return new SharedLocalEngine<G, S, T, P>(localEngine, this.sharingPreferences, this.applicationContext);
    }
}
