package com.cosylab.vdct.application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Date;
import javax.swing.SwingUtilities;
import com.cosylab.vdct.utils.PrintfFormat;
import com.cosylab.vdct.visual.scene.ModelScene;

public class ScenePrintable implements Printable {

    private ModelScene scene;

    private String name;

    public ScenePrintable(ModelScene scene, String name) {
        this.scene = scene;
        this.name = name;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        Rectangle rect = scene.getBounds();
        double height = pageFormat.getImageableHeight() - 50;
        double widht = pageFormat.getImageableWidth();
        double x = pageFormat.getImageableX();
        double y = pageFormat.getImageableY();
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.translate(x, y);
        g2D.setFont(Font.decode("Arial-12"));
        g2D.setStroke(new BasicStroke(1));
        g2D.setPaint(Color.black);
        g2D.drawString("VDCT Model: " + name, (float) x, (float) y);
        double scale = Math.min(height / rect.height, widht / rect.width);
        if (scale > 1) scale = 1;
        g2D.translate(0, 25);
        g2D.scale(scale, scale);
        scene.paint(g2D);
        g2D.scale(1. / scale, 1. / scale);
        g2D.translate(0, height - 25);
        g2D.setFont(Font.decode("Arial-12"));
        g2D.setStroke(new BasicStroke(1));
        g2D.setPaint(Color.black);
        g2D.drawString("Scale: " + format.sprintf(scale * 100) + "% Timestamp: " + new Date(), (float) x, (float) y + 25);
        return PAGE_EXISTS;
    }

    private static final PrintfFormat format = new PrintfFormat("%3.1f");
}
