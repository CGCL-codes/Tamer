package de.morknet.mrw.rcc.state;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import de.morknet.mrw.base.Signal;
import de.morknet.mrw.comm.SignalCode;

/**
 * Diese Klasse zeichnet ein Gleissperrsignal.
 * @author sm
 *
 */
final class GleissperrsignalDrawer extends SignalStateDrawer {

    private Color c1 = RED;

    private Color c2 = WHITE;

    private static final int gx = 2 - (SCALE_X >> 1);

    /**
	 * Dieser Konstruktur initialisiert diese Zeichenklasse mit dem
	 * zu zeichnenden Signal.
	 * @param signal Das zu zeichnende Signal
	 */
    GleissperrsignalDrawer(Signal signal) {
        super(signal);
    }

    @Override
    int getGX() {
        return gx;
    }

    @Override
    void prepare() {
        SignalCode state = signal.getSignalState();
        switch(state) {
            case SIGNAL_OFF:
                c1 = c2 = BLACK;
                break;
            case SIGNAL_TST:
                c1 = RED;
                c2 = WHITE;
                break;
            case SIGNAL_SH0:
                c1 = RED;
                c2 = BLACK;
                break;
            case SIGNAL_SH1:
                c1 = BLACK;
                c2 = WHITE;
                break;
            default:
                throw new IllegalStateException("Unusable state " + state);
        }
    }

    @Override
    void draw(GC gc) {
        gc.setBackground(c1);
        gc.setForeground(c1);
        gc.fillOval(gx + 4, 5, 2, 2);
        gc.fillOval(gx + 4, 10, 2, 2);
        gc.setBackground(c2);
        gc.setForeground(c2);
        gc.fillOval(gx + 1, 5, 2, 2);
        gc.fillOval(gx + 4, 8, 2, 2);
    }
}
