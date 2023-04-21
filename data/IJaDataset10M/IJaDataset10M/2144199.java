package v3.simulation;

import java.awt.Color;

public class TextView implements SimulatorView {

    private FieldStats stats;

    public TextView() {
        stats = new FieldStats();
    }

    public void setColor(Class cl, Color color) {
    }

    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    public void showStatus(int step, Field field) {
        stats.reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object actor = field.getObjectAt(row, col);
                if (actor != null) {
                    stats.incrementCount(actor.getClass());
                }
            }
        }
        stats.countFinished();
        System.out.println(stats.getPopulationDetails(field));
    }

    public void reset() {
    }
}
