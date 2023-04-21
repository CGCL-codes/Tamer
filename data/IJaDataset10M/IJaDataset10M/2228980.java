package base.gui.clock.hand;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collection;
import base.gui.clock.ClockPane;

public interface ClockHand {

    final Collection<ClockHand> CLOCK_HANDS = Arrays.<ClockHand>asList(ModernClockHand.INSTANCE, DotClockHand.INSTANCE, DigitalClockHand.INSTANCE);

    /** Anzeigetext, etc */
    String getRessourceKey();

    /** Zeiger zeichnen */
    void paint(Graphics2D gr, ClockPane cp);
}
