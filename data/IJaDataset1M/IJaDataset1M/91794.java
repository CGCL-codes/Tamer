package gov.sns.apps.knobs;

import javax.swing.*;

/** manages the display of the knob editor */
public class KnobEditDisplayer {

    /** split pane which contains the knob view and the knob editor */
    protected final JSplitPane CONTAINER_PANE;

    /** knob currently being edited */
    protected Knob _knob;

    /** Constructor */
    public KnobEditDisplayer(final JSplitPane containerPane) {
        CONTAINER_PANE = containerPane;
    }

    /** get the knob */
    public Knob getKnob() {
        return _knob;
    }

    /** show the specified editor */
    public void display(final KnobEditor editor) {
        _knob = editor.getKnob();
        CONTAINER_PANE.setBottomComponent(editor);
        CONTAINER_PANE.setDividerLocation(0.6);
    }

    /** hide the specified editor */
    public void hide(final KnobEditor editor) {
        close();
    }

    /** hide the editor */
    public void close() {
        _knob = null;
        CONTAINER_PANE.setDividerLocation(1.0);
        CONTAINER_PANE.setBottomComponent(null);
    }
}
