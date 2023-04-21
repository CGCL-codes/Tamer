package doodler.parts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import doodler.bounds.Bounds;

public class Fragment extends Leaf {

    private TextLayout textLayout = null;

    public TextLayout getTextLayout() {
        return this.textLayout;
    }

    public void setTextLayout(TextLayout textLayout) {
        if (this.textLayout == textLayout) {
            return;
        }
        this.textLayout = textLayout;
        invalidateBounds();
    }

    protected Bounds getNewRawBounds() {
        final Rectangle2D textBounds = this.textLayout.getBounds();
        return new Bounds(0, -this.textLayout.getAscent(), textBounds.getWidth(), this.textLayout.getDescent());
    }

    public void paintThis(Graphics2D g, Bounds clipping) {
        float drawPosX;
        if (this.textLayout.isLeftToRight()) {
            drawPosX = 0;
        } else {
            drawPosX = (float) getRawBounds().getWidth();
        }
        g.setColor(Color.BLACK);
        this.textLayout.draw(g, drawPosX, 0);
    }
}
