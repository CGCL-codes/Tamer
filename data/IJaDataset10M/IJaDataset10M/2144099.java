package far1.display;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import far1.simulation.Field;

/**
 * Provide the capability to display multiple views of the simulation.
 * 
 * @author David J. Barnes, Michael Kolling and Poul Henricksen
 * @version 2008.03.30
 */
public class MultiView implements SimulatorView {

    private Set<SimulatorView> views;

    public MultiView(int height, int depth) {
        views = new HashSet<SimulatorView>();
        views.add(new AnimatedView(height, depth));
        views.add(new TextView());
    }

    /**
     * Associate a class with a color.
     * @param cl An actor class.
     * @param color The visualization color.
     */
    public void setColor(Class cl, Color color) {
        for (SimulatorView view : views) {
            view.setColor(cl, color);
        }
    }

    /**
     * Determine whether the field is viable or not.
     * @param field The field to be examined.
     * @return true if viable, false otherwise.
     */
    @Override
    public boolean isViable(Field field) {
        for (SimulatorView view : views) {
            if (view.isViable(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Show the status of the field at the given step.
     * @param step The simulation step.
     * @param field The field whose status is to be shown.
     */
    @Override
    public void showStatus(int step, Field field) {
        for (SimulatorView view : views) {
            view.showStatus(step, field);
        }
    }
}
