package com.jpmorrsn.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class IIPBlock extends Block {

    IIPBlock(Diagram ctlr) {
        super(ctlr);
        type = Block.Types.IIP_BLOCK;
        width = diag.driver.fontWidth * 20 + 4;
        height = diag.driver.fontHeight + 4;
    }

    @Override
    void draw(Graphics2D g) {
        if (!visible && this != diag.driver.selBlockP) {
            showZones(g);
            return;
        }
        Font fontsave = g.getFont();
        g.setFont(diag.driver.fontf);
        g.setColor(Color.GRAY);
        if (description != null) {
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            width = 4;
            for (int j = 0; j < description.length(); j++) {
                width += metrics.charWidth(description.charAt(j));
            }
        }
        g.drawRoundRect(cx - width / 2, cy - height / 2, width, height, 6, 6);
        if (this == diag.driver.selBlockP) g.setColor(new Color(255, 255, 200)); else g.setColor(new Color(200, 255, 255));
        g.fillRoundRect(cx - width / 2 + 1, cy - height / 2 + 1, width - 1, height - 1, 6, 6);
        g.setColor(Color.GRAY);
        if (description != null) {
            g.setColor(Color.GRAY);
            g.drawString(description, cx - width / 2 + 4, cy + 4);
        }
        calcDiagMaxAndMin(cx - width / 2, cx + width / 2, cy - height / 2, cy + height / 2);
        g.setFont(fontsave);
    }
}
