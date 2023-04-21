package de.jaret.examples.timebars.touren;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import de.jaret.util.date.Interval;
import de.jaret.util.swing.GraphicsHelper;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.TimeBarGapRenderer;

/**
 * @author Peter Kliem
 * @version $Id: TourenGapRenderer.java 234 2007-02-10 00:22:45Z olk $
 */
public class TourenGapRenderer implements TimeBarGapRenderer {

    RendererComponent _component;

    public TourenGapRenderer() {
        _component = new RendererComponent();
    }

    public JComponent getTimeBarGapRendererComponent(TimeBarViewer tbv, TimeBarRow row, Interval interval1, Interval interval2) {
        _component.setTourElements((TourElement) interval1, (TourElement) interval2);
        return _component;
    }

    public int getMinimumWidth() {
        return 30;
    }

    class RendererComponent extends JComponent {

        TourElement _te1;

        TourElement _te2;

        public RendererComponent() {
            setLayout(null);
            setOpaque(false);
        }

        public void setTourElements(TourElement te1, TourElement te2) {
            _te1 = te1;
            _te2 = te2;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int height = getHeight();
            int width = getWidth();
            int y = height / 3;
            int bheight = height / 3;
            int yEnd = y + bheight;
            int xcenter = width / 2;
            g.setColor(Color.BLACK);
            int minutes = _te1.getEnd().getMinutes();
            GraphicsHelper.drawStringCentered(g, Integer.toString(minutes), xcenter, y - 2);
            GraphicsHelper.drawStringCentered(g, _te1.getEndeOrt(), xcenter, yEnd + 12);
        }
    }
}
