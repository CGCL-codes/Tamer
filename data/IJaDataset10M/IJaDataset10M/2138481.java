package org.thechiselgroup.choosel.visualization_component.graph.client;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceManager;
import org.thechiselgroup.choosel.core.client.visualization.model.ViewContentDisplay;
import org.thechiselgroup.choosel.core.client.visualization.model.initialization.ViewContentDisplayFactory;
import com.google.inject.Inject;

public class GraphViewContentDisplayFactory implements ViewContentDisplayFactory {

    @Inject
    private ArcTypeProvider arcStyleProvider;

    @Inject
    private CommandManager commandManager;

    @Inject
    private GraphExpansionRegistry registry;

    @Inject
    private ResourceCategorizer resourceCategorizer;

    @Inject
    private ResourceManager resourceManager;

    @Inject
    public GraphViewContentDisplayFactory() {
    }

    @Override
    public ViewContentDisplay createViewContentDisplay() {
        return new Graph(new Graph.DefaultDisplay(), commandManager, resourceManager, resourceCategorizer, arcStyleProvider, registry);
    }

    @Override
    public String getViewContentTypeID() {
        return Graph.ID;
    }
}
