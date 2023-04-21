package it.tukano.jps.testmodules;

import it.tukano.jps.core.AbstractModule;
import it.tukano.jps.core.Engine;
import it.tukano.jps.event.DispatcherRule;
import it.tukano.jps.event.engine.EngineEvent;
import it.tukano.jps.event.engine.EngineEventListener;
import it.tukano.jps.event.engine.EngineListenerRegistry;
import it.tukano.jps.event.engine.SceneElementAdd;

/**
 *
 */
public class TestModule001 extends AbstractModule {

    /**
     * Default no arg constructor
     */
    public TestModule001() {
    }

    @Override
    protected void initialize() {
    }

    public void start() {
        EngineListenerRegistry registry = new EngineListenerRegistry();
        registry.add(DispatcherRule.SAME_TYPE, SceneElementAdd.class, new EngineEventListener() {

            public void engineEventPerformed(EngineEvent event) {
            }
        });
        Engine engine = getApplication().findFirstModule(Engine.class);
    }

    public void stop() {
    }

    public void destroy() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
