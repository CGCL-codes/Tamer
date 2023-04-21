package be.lassi.ui.widgets;

import javax.swing.JTextField;
import be.lassi.cues.Timing;
import be.lassi.ui.sheet.parse.DocumentTiming;
import be.lassi.ui.sheet.parse.ParserTiming;

/**
 *
 *
 */
public class TimingField extends JTextField {

    private Timing timing = new Timing();

    public TimingField() {
        this(0);
    }

    public TimingField(final int columns) {
        super(columns);
        setDocument(new DocumentTiming());
    }

    public Timing getTiming() {
        ParserTiming.parse(getText(), timing);
        return timing;
    }

    public void setTiming(final Timing timing) {
        this.timing.set(timing);
        setText(this.timing.displayString());
    }
}
