package org.simbrain.world.odorworld.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.simbrain.util.SimbrainMath;
import org.simbrain.util.environment.SmellSource;
import org.simbrain.world.odorworld.OdorWorldPanel;
import org.simbrain.world.odorworld.entities.BasicEntity;

/**
 * Add entity action.
 */
public final class AddEntityAction extends AbstractAction {

    /**
     * Reference to Panel; the action refers to the panel because it needs
     * information on mouse clicks, etc.
     */
    private final OdorWorldPanel worldPanel;

    /**
     * Create a new add entity action.
     *
     * @param worldPanel parent panel.
     */
    public AddEntityAction(final OdorWorldPanel worldPanel) {
        super("Add Entity");
        this.worldPanel = worldPanel;
    }

    /** {@inheritDoc} */
    public void actionPerformed(final ActionEvent event) {
        BasicEntity entity = new BasicEntity(worldPanel.getWorld());
        entity.setLocation(worldPanel.getSelectedPoint().x, worldPanel.getSelectedPoint().y);
        entity.setSmellSource(new SmellSource(SimbrainMath.multVector(new double[] { 0.0, 0.0, 0.3, 0.7, 0.0, 0.0 }, 100), SmellSource.DecayFunction.GAUSSIAN, entity.getLocation()));
        worldPanel.getWorld().addEntity(entity);
    }
}
