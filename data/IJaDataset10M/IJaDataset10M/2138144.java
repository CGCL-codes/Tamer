package base.gui.clock;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import base.gui.clock.hand.ClockHand;
import base.gui.clock.hand.ModernClockHand;

public class AnalogClockPane extends ClockPane {

    public AnalogClockPane() {
        selected = ModernClockHand.INSTANCE;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(30, 30);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        selected.paint((Graphics2D) g, this);
    }

    public void setSelectedClockHand(final ClockHand ch) {
        selected = ch;
    }

    private ClockHand selected;
}
