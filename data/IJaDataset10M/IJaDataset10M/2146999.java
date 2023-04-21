package org.tzi.use.gui.views;

import java.awt.*;
import org.tzi.use.uml.sys.MSystem;
import org.tzi.use.uml.sys.MSystemState;
import org.tzi.use.uml.sys.StateChangeEvent;

/** 
 * A LineChartView showing the evolution of objects and links over time.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author      Mark Richters 
 */
public class StateEvolutionView extends LineChartView implements View {

    private MSystem fSystem;

    public StateEvolutionView(MSystem system) {
        super(50, 2, new Color[] { Color.blue, Color.red });
        fSystem = system;
        fSystem.addChangeListener(this);
        update();
    }

    private void update() {
        MSystemState systemState = fSystem.state();
        int[] values = { systemState.allObjects().size(), systemState.allLinks().size() };
        addValues(values);
    }

    public void stateChanged(StateChangeEvent e) {
        update();
    }

    /**
     * Detaches the view from its model.
     */
    public void detachModel() {
        fSystem.removeChangeListener(this);
    }
}
