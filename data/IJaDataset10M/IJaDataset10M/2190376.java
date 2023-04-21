package percussiongenerator.commands;

import org.jfonia.connect5.intervals.Interval;

/**
 *
 * @author Jannes Plyson
 */
public class ChangeEnd implements ICommand {

    private Interval interval;

    private Object value;

    public ChangeEnd(Interval interval, Integer value) {
        this.value = value;
        this.interval = interval;
    }

    public ChangeEnd(Interval interval, Long value) {
        this.value = value;
        this.interval = interval;
    }

    public ChangeEnd(Interval interval, Double value) {
        this.value = value;
        this.interval = interval;
    }

    public void execute() {
        if (value instanceof Integer) {
            interval.getEndNode().setValue((Integer) interval.getEnd() + (Integer) value);
        } else if (value instanceof Long) {
            interval.getEndNode().setValue((Long) interval.getEnd() + (Long) value);
        } else {
            interval.getEndNode().setValue((Double) interval.getEnd() + (Double) value);
        }
    }

    public void undo() {
        if (value instanceof Integer) {
            interval.getEndNode().setValue((Integer) interval.getEnd() - (Integer) value);
        } else if (value instanceof Long) {
            interval.getEndNode().setValue((Long) interval.getEnd() - (Long) value);
        } else {
            interval.getEndNode().setValue((Double) interval.getEnd() - (Double) value);
        }
    }
}
