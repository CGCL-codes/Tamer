package org.thechiselgroup.choosel.core.client.visualization.behaviors;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.resources.command.SwitchSelectionCommand;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemBehavior;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemContainerChangeEvent;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemInteraction;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.SelectionModel;

/**
 * Manages {@link VisualItem} highlighting in a single view.
 * 
 * @author Lars Grammel
 * @author Del Myers
 */
public class SwitchSelectionOnClickVisualItemBehavior implements VisualItemBehavior {

    private SelectionModel selectionModel;

    private CommandManager commandManager;

    public SwitchSelectionOnClickVisualItemBehavior(SelectionModel selectionModel, CommandManager commandManager) {
        assert selectionModel != null;
        assert commandManager != null : "CommandManager must not be null";
        this.selectionModel = selectionModel;
        this.commandManager = commandManager;
    }

    protected SelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void onInteraction(VisualItem visualItem, VisualItemInteraction interaction) {
        assert visualItem != null;
        assert interaction != null;
        switch(interaction.getEventType()) {
            case CLICK:
                switchSelection(visualItem);
                break;
        }
    }

    @Override
    public void onVisualItemContainerChanged(VisualItemContainerChangeEvent event) {
    }

    protected void switchSelection(VisualItem visualItem) {
        commandManager.execute(new SwitchSelectionCommand(visualItem.getResources(), getSelectionModel()));
    }
}
