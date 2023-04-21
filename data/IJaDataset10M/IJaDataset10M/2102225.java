package org.jfge.api.collision;

import org.jfge.spi.collision.CollisionDetectionStrategy;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.multibindings.Multibinder;

public final class CollisionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CollisionHandlerParser.class).to(CollisionHandlerParserImpl.class);
        bind(CollisionDetector.class).to(CollisionDetectorImpl.class);
        bind(CollisionHandlerFactory.class).toProvider(FactoryProvider.newFactory(CollisionHandlerFactory.class, CollisionHandlerImpl.class));
        Multibinder<CollisionHandler> collisionHandlerBinder = Multibinder.newSetBinder(binder(), CollisionHandler.class);
        Multibinder<CollisionDetectionStrategy> collisionStrategyBinder = Multibinder.newSetBinder(binder(), CollisionDetectionStrategy.class);
        collisionStrategyBinder.addBinding().to(RectangleCollisionStrategy.class);
    }
}
