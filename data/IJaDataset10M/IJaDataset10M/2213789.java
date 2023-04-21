package siouxsie.mvc.impl;

import java.util.ArrayList;
import java.util.Collection;
import siouxsie.mvc.IInitializerContainer;
import siouxsie.mvc.IInitializer;
import siouxsie.mvc.IScreen;

/**
 * Basic implementation of {@link IInitializerContainer}.
 * @author Arnaud Cogoluegnes
 * @version $Id$
 */
public class InitializerContainer implements IInitializerContainer {

    private Collection<IInitializer> initializers = new ArrayList<IInitializer>();

    public void addInitializer(IInitializer initializer) {
        initializers.add(initializer);
    }

    public void initializeAction(Object action) {
        for (IInitializer init : initializers) {
            init.initializeAction(action);
        }
    }

    public void initializeScreen(IScreen screen) {
        for (IInitializer init : initializers) {
            init.initializeScreen(screen);
        }
    }
}
