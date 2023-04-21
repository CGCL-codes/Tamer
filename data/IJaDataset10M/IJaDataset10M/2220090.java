package abbot.editor;

import abbot.script.*;
import abbot.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Template for recording AWTEvents and converting them into an appropriate
 * semantic event.  The EventRecorder class decides which SemanticRecorder to
 * use and handles cancel/termination.  Implementations should be named
 * AbstractButtonRecorder, JTableRecorder, etc.  The semantic recorders will
 * be dynamically loaded based on the component receiving a given event
 * (compare to ComponentTester).<p>
 * See EventRecorder for implementation details.
 */
public abstract class SemanticRecorder {

    private ActionListener al;

    private Resolver resolver;

    private ComponentFinder finder;

    protected ArrayList events = new ArrayList();

    private Step step = null;

    /** Create a SemanticRecorder for use in capturing the semantics of a GUI
     * action.
     */
    public SemanticRecorder(Resolver resolver, ComponentFinder finder) {
        this.resolver = resolver;
        this.finder = finder;
        this.al = null;
    }

    /** Create a SemanticRecorder for use in capturing the semantics of a GUI
     * action.
     */
    public SemanticRecorder(Resolver resolver, ComponentFinder finder, ActionListener l) {
        this.resolver = resolver;
        this.finder = finder;
        this.al = l;
    }

    public final void clear() {
        events.clear();
        step = null;
    }

    /** Returns whether this SemanticRecorder wishes to accept the given event
     * and subsequent events.
     */
    public abstract boolean accept(AWTEvent event);

    /**
     * Handle an event.  Maintain a list of all events captured, for future
     * reference, before passing on to the derived class for parsing.
     */
    public final boolean record(java.awt.AWTEvent event) {
        boolean recorded = parse(event);
        if (recorded) events.add(event);
        return recorded;
    }

    /** Handle an event.  Return true if the event has been consumed.
     * Returning false usually means that isFinished() will return true.
     */
    public abstract boolean parse(AWTEvent event);

    /** Return the ComponentFinder to be used by this recorder. */
    protected ComponentFinder getFinder() {
        return finder;
    }

    /** Return the Resolver to be used by this recorder. */
    protected Resolver getResolver() {
        return resolver;
    }

    /** Returns the script step generated from the events recorded so far.  If
     * no real action resulted, may return null (for example, a mouse press on
     * a button which releases outside the button).
     */
    public Step getStep() {
        if (step == null) {
            step = createStep();
        }
        return step;
    }

    /** Create a step based on the events received thus far.  Returns null if
     * no semantic event or an imcomplete event has been detected. */
    protected abstract Step createStep();

    /**
     * Add the given step.  Should be used whenever the recorder detects a
     * complete semantic event sequence.
     */
    protected void addStep(Step newStep) {
        events.clear();
        if (step == null) {
            step = newStep;
        } else if (step instanceof Sequence) {
            ((Sequence) step).addStep(newStep);
        } else {
            Sequence seq = new Sequence(getResolver(), getFinder(), (String) null, null);
            seq.addStep(step);
            seq.addStep(newStep);
            step = seq;
        }
    }

    /** Return whether this recorder has finished recording the current
     * semantic event.
     */
    public abstract boolean isFinished();

    /** Indicate the current recording state, so that the status may be
     * displayed elsewhere.
     */
    protected void setStatus(String msg) {
        if (al != null) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, msg);
            al.actionPerformed(event);
        }
    }

    public String toString() {
        return getClass().toString();
    }
}
