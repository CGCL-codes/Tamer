package org.fest.swing.driver;

import javax.swing.JSlider;
import org.fest.swing.edt.GuiQuery;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands an action, executed in the event dispatch thread, that returns the maximum value supported by a
 * <code>{@link JSlider}</code>.
 * @see JSlider#getMaximum()
 *
 * @author Yvonne Wang
 */
final class JSliderMaximumQuery {

    static int maximumOf(final JSlider slider) {
        return execute(new GuiQuery<Integer>() {

            protected Integer executeInEDT() {
                return slider.getMaximum();
            }
        });
    }

    private JSliderMaximumQuery() {
    }
}
