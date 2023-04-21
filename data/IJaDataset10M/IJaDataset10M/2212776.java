package org.radeox.example;

import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseInitialRenderContext;
import org.radeox.engine.context.BaseRenderContext;
import java.util.Locale;

public class PicoExample {

    public static void main(String[] args) {
        String test = "==SnipSnap== {link:Radeox|http://radeox.org}";
        DefaultPicoContainer c = new org.picocontainer.defaults.DefaultPicoContainer();
        try {
            InitialRenderContext initialContext = new BaseInitialRenderContext();
            initialContext.set(RenderContext.INPUT_LOCALE, new Locale("otherwiki", ""));
            c.registerComponentInstance(InitialRenderContext.class, initialContext);
            c.registerComponentImplementation(RenderEngine.class, BaseRenderEngine.class);
            c.getComponentInstances();
        } catch (Exception e) {
            System.err.println("Could not register component: " + e);
        }
        PicoContainer container = c;
        RenderEngine engine = (RenderEngine) container.getComponentInstance(RenderEngine.class);
        RenderContext context = new BaseRenderContext();
        System.out.println(engine.render(test, context));
    }
}
