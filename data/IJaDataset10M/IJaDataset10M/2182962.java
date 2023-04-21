package org.makagiga.commons.validator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import org.makagiga.commons.MGraphics2D;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.UI;
import org.makagiga.commons.painters.GlassPainter;
import org.makagiga.commons.swing.MMessageLabel;
import org.makagiga.commons.swing.border.MBorder;
import org.makagiga.commons.swing.event.MMouseAdapter;

/**
 * @since 3.4
 */
public class ValidatorMessage extends MMessageLabel {

    private static StaticHandler staticHandler;

    static final int TAIL_SIZE = 10;

    Validator<?> validatedBy;

    public ValidatorMessage() {
        setBorder(BorderFactory.createCompoundBorder(new TailBorder(), UI.createEmptyBorder(2)));
        GlassPainter painter = (GlassPainter) getPainter();
        painter.setPainterInsets(new Insets(TAIL_SIZE, 0, 0, 0));
        painter.setUsePainterInsets(true);
        setRoundType(GlassPainter.RoundType.BOTTOM);
        setVisible(false);
        if (staticHandler == null) staticHandler = new StaticHandler();
        addMouseListener(staticHandler);
    }

    @Override
    public void setMessage(final String text, final Icon icon, final Color color) {
        String iconName = MIcon.getName(icon);
        super.setMessage(text, (iconName != null) ? MIcon.small(iconName) : icon, color);
    }

    private static final class StaticHandler extends MMouseAdapter {

        public StaticHandler() {
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            ValidatorMessage vm = (ValidatorMessage) e.getSource();
            vm.setVisible(false);
        }
    }

    private static final class TailBorder extends MBorder {

        @Override
        public void paintBorder(final Component c, final Graphics graphics, final int x, final int y, final int width, final int height) {
            Graphics2D g = (Graphics2D) graphics;
            Color bg = UI.getBackground(c);
            g.setColor(GlassPainter.getBrighterColor(bg));
            Shape triangle = MGraphics2D.createTriangle(5, ValidatorMessage.TAIL_SIZE, 5, 0, 5 + (int) (ValidatorMessage.TAIL_SIZE * 1.5f), ValidatorMessage.TAIL_SIZE);
            UI.setAntialiasing(g, true);
            g.fill(triangle);
        }

        private TailBorder() {
            super(false, ValidatorMessage.TAIL_SIZE, 0, 0, 0);
        }
    }
}
