package mipt.gui.graph.actions;

import java.awt.event.ActionEvent;

/**
 * Swithes between {all auto limits = true} and {some auto limit = false (on some axis
 *   and some min/max which are got from initial plot state)}. The second case is default.
 * Attach this only in case if autoMax!=autoMin for at least one axis
 *   (also will work if autoMax==autoMin but autoX!=autoY.. however it is almost senseless).
 * @author Evdokimov
 */
public class PlotMixedLimitsSwitcher extends AbstractPlotAction {

    protected boolean[] autolimits;

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        boolean[] al = plot.getAutoLimits();
        if (al[0] && al[1] && al[2] && al[3]) {
            plot.setAutolimits(autolimits[0], autolimits[1], autolimits[2], autolimits[3]);
        } else {
            autolimits = al;
            plot.setAutolimits(true, true);
        }
        plot.update();
        plot.repaint();
    }
}
