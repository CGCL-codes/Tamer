package net.sf.gham.core.entity.player.skill;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author fabio
 *
 */
public class TrainedWeeksRemainingCellRenderer extends DefaultTableCellRenderer {

    private static TrainedWeeksRemainingCellRenderer singleton;

    public static TrainedWeeksRemainingCellRenderer singleton() {
        if (singleton == null) {
            singleton = new TrainedWeeksRemainingCellRenderer();
        }
        return singleton;
    }

    private TrainedWeeksRemainingCellRenderer() {
        setHorizontalAlignment(CENTER);
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof TrainedWeeksRemaining) {
            float f = ((TrainedWeeksRemaining) value).getWeeks();
            if (f > 0) {
                if (((TrainedWeeksRemaining) value).isRealValue()) {
                    setText(FORMAT.format(f));
                } else {
                    setText(new StringBuffer(6).append("(").append(FORMAT.format(f)).append(")").toString());
                }
            } else {
                setText("0.0");
            }
        } else {
            setText("");
        }
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("##0.0");
}
