package org.jgistools.gui.factory.impl;

import org.jgistools.gui.factory.IGUIFactory;
import org.jgistools.gui.mapcomponent.IMapComponent;
import org.jgistools.gui.mapcomponent.impl.swing.JMapComponent;
import org.jgistools.gui.mappane.impl.swing.IMapPane;
import org.jgistools.gui.mappane.impl.swing.JMapPane;
import org.jgistools.gui.mappane.impl.swing.loaders.IMapToolsLoader;
import org.jgistools.gui.mappane.impl.swing.loaders.IMapUserActionsLoader;
import org.jgistools.map.model.core.IMapModel;

/**
 * An implementation of the {@link IGUIFactory} interface that creates
 * GUI components that can be used with the Swing framework.
 * Note: the aditional parameter present in the factory methods is not used
 * and should be null.
 * @author Teodor Baciu
 *
 */
public class SwingGUIFactory implements IGUIFactory {

    /**
	 * Creates a new instance of this class.
	 */
    SwingGUIFactory() {
    }

    @Override
    public IMapComponent createMapComponent(IMapModel mapModel, Object aditionalParam) throws Exception {
        return new JMapComponent(mapModel);
    }

    @Override
    public IMapPane createMapPane(IMapComponent mapComponent, Object aditionalParam) throws Exception {
        return new JMapPane(mapComponent);
    }

    @Override
    public IMapPane createMapPane(IMapComponent mapComponent, IMapToolsLoader mapToolsLoader, IMapUserActionsLoader userActionLoader, Object aditionalParam) throws Exception {
        throw new RuntimeException("Not implemeted !");
    }

    @Override
    public IMapComponent createMapComponent(Object aditionalParam) throws Exception {
        return new JMapComponent();
    }
}
