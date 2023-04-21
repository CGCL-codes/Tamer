package org.jfge.j2se.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jfge.api.collision.RectangleCollisionStrategy;
import org.jfge.api.engine.EngineModule;
import org.jfge.j2se.controller.J2SeKeyboardController1;
import org.jfge.j2se.controller.J2SeKeyboardController2;
import org.jfge.spi.collision.CollisionDetectionStrategy;
import org.jfge.spi.controller.Controller;
import org.jfge.spi.graphics.GraphicsFactory;
import org.jfge.spi.graphics.GraphicsProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

/**
 * The Class GraphicsModule.
 */
public final class J2SeGraphicsModule extends AbstractModule {

    @Override
    protected void configure() {
        loadProperties(binder());
        bind(GraphicsProvider.class).to(J2SeGraphicsProvider.class);
        bind(GraphicsFactory.class).to(J2SeGraphicsFactory.class);
        bind(Controller.class).annotatedWith(Names.named("keyboard.controller1")).to(J2SeKeyboardController1.class);
        bind(Controller.class).annotatedWith(Names.named("keyboard.controller2")).to(J2SeKeyboardController2.class);
        MapBinder<String, Controller> controllerBinderr = MapBinder.newMapBinder(binder(), String.class, Controller.class);
        controllerBinderr.addBinding("keyboard.controller1").to(J2SeKeyboardController1.class);
        controllerBinderr.addBinding("keyboard.controller2").to(J2SeKeyboardController2.class);
    }

    private void loadProperties(Binder binder) {
        InputStream stream = EngineModule.class.getResourceAsStream("/org/jfge/config/controller/keyboard.properties");
        Properties engineProperties = new Properties();
        try {
            engineProperties.load(stream);
            Names.bindProperties(binder, engineProperties);
        } catch (IOException e) {
            binder.addError(e);
        }
    }
}
