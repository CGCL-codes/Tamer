package com.peterhi.player.shape;

import java.util.UUID;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 *
 * @author YUN TAO
 */
public class Text extends Shape {

    public int width;

    public int height;

    public String text;

    public Text(boolean assignName) {
        if (assignName) {
            name = UUID.randomUUID().toString();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        Dimension dim = new Dimension(width, height);
        float formatWidth = dim.width;
        float drawPosY = 0;
        String[] array = text.split("\n");
        for (String item : array) {
            if (item == null || item.length() <= 0) {
                item = " ";
            }
            AttributedString at = new AttributedString(item);
            at.addAttribute(TextAttribute.FONT, new Font("Sans", Font.PLAIN, 16));
            AttributedCharacterIterator itor = at.getIterator();
            FontRenderContext frc = g.getFontRenderContext();
            LineBreakMeasurer lbm = new LineBreakMeasurer(itor, frc);
            int start = itor.getBeginIndex();
            int end = itor.getEndIndex();
            lbm.setPosition(start);
            while (lbm.getPosition() < end) {
                TextLayout lay = lbm.nextLayout(formatWidth);
                drawPosY += lay.getAscent();
                float drawPosX;
                if (lay.isLeftToRight()) {
                    drawPosX = 0;
                } else {
                    drawPosX = formatWidth - lay.getAdvance();
                }
                lay.draw(g, x + drawPosX, y + drawPosY);
                drawPosY += lay.getDescent() + lay.getLeading();
            }
        }
    }

    public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
}
