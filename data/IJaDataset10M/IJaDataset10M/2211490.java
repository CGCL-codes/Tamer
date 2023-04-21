package percussiongenerator.commands;

import org.jfonia.connect5.intervals.Interval;
import org.jfonia.connect5.intervals.LinkedDoubleIntervalList;
import org.jfonia.connect5.intervals.LinkedIntIntervalList;
import org.jfonia.connect5.intervals.LinkedLongIntervalList;

/**
 *
 * @author Jannes Plyson
 */
public class RemoveInterval implements ICommand {

    private Interval interval;

    private Object list;

    private int index;

    public RemoveInterval(Interval interval, LinkedIntIntervalList list) {
        this.interval = interval;
        this.list = list;
    }

    public RemoveInterval(Interval interval, LinkedLongIntervalList list) {
        this.interval = interval;
        this.list = list;
    }

    public RemoveInterval(Interval interval, LinkedDoubleIntervalList list) {
        this.interval = interval;
        this.list = list;
    }

    public void execute() {
        if (list instanceof LinkedIntIntervalList) {
            index = ((LinkedIntIntervalList) list).indexOf(interval);
            ((LinkedIntIntervalList) list).remove(index);
            if (index != 0) {
                Interval<Integer> val = ((LinkedIntIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Integer) (val.getEnd()) + (Integer) (interval.getDifference()));
            }
        } else if (list instanceof LinkedLongIntervalList) {
            index = ((LinkedLongIntervalList) list).indexOf(interval);
            ((LinkedLongIntervalList) list).remove(index);
            if (index != 0) {
                Interval<Long> val = ((LinkedLongIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Long) (val.getEnd()) + (Long) (interval.getDifference()));
            }
        } else {
            index = ((LinkedDoubleIntervalList) list).indexOf(interval);
            ((LinkedDoubleIntervalList) list).remove(index);
            if (index != 0) {
                Interval<Double> val = ((LinkedDoubleIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Double) (val.getEnd()) + (Double) (interval.getDifference()));
            }
        }
    }

    public void undo() {
        if (list instanceof LinkedIntIntervalList) {
            ((LinkedIntIntervalList) list).insert(index, interval);
            if (index != 0) {
                Interval<Integer> val = ((LinkedIntIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Integer) (val.getEnd()) - (Integer) (interval.getDifference()));
            }
        } else if (list instanceof LinkedLongIntervalList) {
            ((LinkedLongIntervalList) list).insert(index, interval);
            if (index != 0) {
                Interval<Long> val = ((LinkedLongIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Long) (val.getEnd()) - (Long) (interval.getDifference()));
            }
        } else {
            ((LinkedDoubleIntervalList) list).insert(index, interval);
            if (index != 0) {
                Interval<Double> val = ((LinkedDoubleIntervalList) list).getIntervalFromIndex(index - 1);
                val.getEndNode().setValue((Double) (val.getEnd()) - (Double) (interval.getDifference()));
            }
        }
    }
}
