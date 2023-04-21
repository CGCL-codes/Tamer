package org.kumenya.operators;

import org.kumenya.api.operators.AbstractOperator;
import org.kumenya.api.operators.Publisher;
import org.kumenya.openapi.TemporalEvent;
import org.kumenya.openapi.TimeInterval;
import org.kumenya.functions.Function;
import org.kumenya.functions.Functions;
import java.util.*;

/**
 * Reduces incoming events with the merge function.
 *
 * @author Jean Morissette
 */
public class Aggregator extends AbstractOperator {

    protected Function.Unary grouped;

    /**
     * A binary function that accumulates the incoming values.
     */
    protected Function.Binary merge;

    /**
     * A parameterless function that returns the zero of the merge function.
     */
    protected Function.Nullary zero;

    protected Publisher pub;

    /**
     * The aggregates.
     */
    protected List<TemporalEvent> aggs;

    private Function.Unary mapping;

    public Aggregator(Publisher pub, Function.Unary grouped, Function.Binary merge, Function.Nullary zero) {
        this(pub, grouped, merge, zero, Functions.identity());
    }

    public Aggregator(Publisher pub, Function.Unary grouped, Function.Binary merge, Function.Nullary zero, Function.Unary mapping) {
        this.pub = pub;
        this.grouped = grouped;
        this.merge = merge;
        this.zero = zero;
        this.mapping = mapping;
        pub.subscribe(this);
    }

    /**
     * TODO: some tests that should be performed
     * 1)
     * |---|
     * |---|
     * |---|
     * |----------------------|
     * <p/>
     * 2)
     * |---|
     * |---|
     * |---|
     * <p/>
     * 3)
     * empty
     * |---|
     * <p/>
     * 4)
     * |---|
     * |---|
     * |-------|
     * <p/>
     * <p/>
     * TODO: refactor all operators such that they are not dependent on the Interval implementation.
     * Instead, add and use methods like Interval#create(T start, T end) or even
     * Interval<T>#create(Interval<T> start, Interval<T> end)
     */
    public synchronized void notify(Object x) {
        final TemporalEvent te = (TemporalEvent) x;
        List<TemporalEvent> group = aggs;
        Object teImage = grouped.eval(te.getObject());
        if (group == null) {
            this.aggs = group = new LinkedList<TemporalEvent>();
            group.add(new TemporalEvent(merge.eval(zero.eval(), teImage), te.getInterval()));
            flush(te.getStart());
            return;
        }
        long teStart = te.getStart();
        final long teEnd = te.getEnd();
        TemporalEvent te1;
        ListIterator<TemporalEvent> it = group.listIterator();
        boolean completed = false;
        while (it.hasNext()) {
            te1 = it.next();
            if (teEnd <= te1.getStart()) {
                continue;
            }
            if (teStart >= te1.getEnd()) {
                continue;
            }
            if (teStart < te1.getStart()) {
                it.previous();
                it.add(new TemporalEvent(merge.eval(zero.eval(), teImage), new TimeInterval(teStart, te1.getStart())));
                teStart = te1.getStart();
            } else if (teStart > te1.getStart()) {
                it.set(new TemporalEvent(te1.getObject(), new TimeInterval(te1.getStart(), teStart)));
                te1 = new TemporalEvent(merge.eval(te1.getObject(), teImage), new TimeInterval(teStart, te1.getEnd()));
                it.add(te1);
                teStart = te1.getEnd();
                if (it.hasNext()) {
                    te1 = it.next();
                } else {
                    break;
                }
            }
            assert teStart == te1.getStart();
            if (teEnd < te1.getEnd()) {
                it.set(new TemporalEvent(merge.eval(te1.getObject(), teImage), new TimeInterval(teStart, teEnd)));
                it.add(new TemporalEvent(te1.getObject(), new TimeInterval(teEnd, te1.getEnd())));
                completed = true;
                break;
            } else {
                it.set(new TemporalEvent(merge.eval(te1.getObject(), teImage), new TimeInterval(teStart, te1.getEnd())));
                teStart = te1.getEnd();
                if (teEnd == te1.getEnd()) {
                    completed = true;
                    break;
                }
            }
        }
        if (!completed) {
            group.add(new TemporalEvent(merge.eval(zero.eval(), teImage), new TimeInterval(teStart, teEnd)));
        }
        flush(te.getStart());
    }

    /**
     * Flush the expired events.
     */
    private void flush(long t) {
        PriorityQueue<TemporalEvent> expired = new PriorityQueue<TemporalEvent>(11, TemporalEvent.START_TIMESTAMP_COMPARATOR);
        Iterator<TemporalEvent> group = aggs.iterator();
        while (group.hasNext()) {
            TemporalEvent te = group.next();
            if (te.getEnd() <= t) {
                group.remove();
                expired.add(new TemporalEvent(mapping.eval(te.getObject()), te.getInterval()));
            } else {
                break;
            }
        }
        while (expired.peek() != null) {
            publish(expired.poll());
        }
    }
}
